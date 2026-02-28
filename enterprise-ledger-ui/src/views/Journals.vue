<template>
  <AppLayout>
    <template #main-content>
      <div class="create-journal">
        <div class="page-desc">Institutional Double-Entry Posting</div>

        <div class="post-journal-div">
          <div class="pj-desc">DESCRIPTION</div>
          <div class="pj-desc-input">
            <input type="text" name="description" id="" placeholder="Enter journal description" />
          </div>
          <div class="pj-entry">
            <div class="pj-entry-header">
              <div>Account</div>
              <div>Entry Type</div>
              <div>Amount</div>
            </div>
            <div class="selection-sec">
              <div v-for="(line, index) in journal.lines" class="sec-item" :key="index">
                <div>
                  <select v-model="line.accountPublicId">
                    <option value="">Select Account</option>
                    <option v-for="acc in accounts" :key="acc.id" :value="acc.id">{{ acc.name }}</option>
                  </select>
                </div>

                <div>
                  <select v-model="line.entryType">
                    <option value="DEBIT">DEBIT</option>
                    <option value="CREDIT">CREDIT</option>
                  </select>
                </div>
                <div>
                  <input type="number" min="0" step="0.01" v-model="line.amount" />
                </div>

                <div @click="removeLine(index)">
                  <img width="28" height="28" src="../assets/delete.png" alt="" />
                </div>
              </div>
              <div>
                <button @click="addLine()">
                  + Add Line
                </button>
              </div>

              <div>
                <div>
                  Total Debits:<span>{{ totalDebits.toFixed(2) }}</span>
                </div>

                <div>
                  Total Credits:<span>{{ totalCredits.toFixed(2) }}</span>
                </div>
              </div>

              <div>
                <button :disabled="!isBalanced" @click="submitJournal()">
                  Post Journal
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </AppLayout>
</template>
<script setup>
import AppLayout from '../layout/AppLayout.vue';
import { computed, onMounted, ref } from 'vue';
import AccountApi from '../services/api';

const accounts = ref([]);

const journal = ref({
  description: '',
  lines: [{ accountPublicId: '', entryType: 'DEBIT', amount: 0 }],
});

const totalDebits = computed(() =>
  journal.value.lines.filter(l => l.entryType === 'DEBIT').reduce((sum, l) => sum + Number(l.amount || 0), 0)
);

const totalCredits = computed(() =>
  journal.value.lines.filter(l => l.entryType === 'CREDIT').reduce((sum, l) => sum + Number(l.amount || 0), 0)
);

const isBalanced = computed(() => totalDebits.value === totalCredits.value && totalDebits.value > 0);

const addLine = () => {
  journal.value.lines.push({
    accountPublicId: '',
    entryType: 'DEBIT',
    amount: 0,
  });
};

const removeLine = index => {
  if (journal.value.lines.length > 1) {
    journal.value.lines.splice(index, 1);
  }
};

const submitJournal = async () => {
  if (!isBalanced.value) return;

  const idempotencyKey = crypto.randomUUID();

  const response = await AccountApi.createJournal(
    {
      description: journal.value.description,
      lines: journal.value.lines,
    },
    {
      header: {
        'Idempotency-Key': idempotencyKey,
      },
    }
  );

  console.log('create journal response', response);
};

const fetchAccounts = async () => {
  try {
    const response = await AccountApi.getAccounts();
    console.log('get Accounts response', response);
    if (response && response.responseCode === '00' && response.accounts.length > 0) {
      accounts.value = response?.accounts;
    } else {
      accounts.value = [
        [
          { accountPublicId: '10001', name: 'Cash Reserve' },
          { accountPublicId: '10002', name: 'Treasury Holdings' },
          { accountPublicId: '3001', name: 'Capital Account' },
        ],
      ];
    }
  } catch (error) {
    console.log('caught an error ', error);
    setMockAccounts();
  }
};

const setMockAccounts = () => {
  accounts.value = [
    { accountPublicId: '10001', name: 'Cash Reserve' },
    { accountPublicId: '10002', name: 'Treasury Holdings' },
    { accountPublicId: '3001', name: 'Capital Account' },
  ];
};

onMounted(async () => {
  await fetchAccounts();
});
</script>
<style scoped>
.create-journal {
  display: flex;
  flex-direction: column;
  /* margin-top: 20px; */
  padding: 20px;
  padding-top: 0;
}

.page-desc {
  display: flex;
  justify-content: flex-start;
  font-weight: 600;
  border-bottom: 1px solid white;
  padding-bottom: 10px;
}

.post-journal-div {
  background: #111c2d;
  border-radius: 12px;
  margin-top: 20px;
  padding: 0 20px;

  .pj-desc {
    margin-top: 10px;
    text-align: start;
    border-bottom: 1px solid white;
    padding-bottom: 10px;
    font-size: 12px;
    color: #94a3b8;
  }

  .pj-desc-input {
    display: flex;
    justify-content: flex-start;
    width: 100%;
    margin-top: 10px;

    input {
      width: 100%;
      height: 48px;
      padding: 10px;
      border: 1px solid #1e293b;
    }
  }
}

.pj-entry {
  margin-top: 20px;
  border: 1px solid whitesmoke;
}

.pj-entry-header {
  display: flex;
  flex-direction: row;
  padding: 10px;
  gap: 250px;
}

.sec-item {
  display: flex;
  flex-direction: row;
  gap: 200px;
  border: 1px solid white;
  padding-top: 10px;
}
</style>
