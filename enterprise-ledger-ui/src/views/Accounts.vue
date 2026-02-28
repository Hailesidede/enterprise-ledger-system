<template>
  <AppLayout>
    <template #main-content>
      <div class="accounts">
        <div class="desc">List of your financial accounts.</div>

        <div v-if="loading" class="state-indicator">Loading accounts...</div>

        <div v-else-if="error" class="state-indicator error">Failed to load accounts: {{ error }}</div>

        <div v-else class="table">
          <div class="table-scroll">
            <table>
              <thead>
                <tr>
                  <th>Account Name</th>
                  <th>Account Type</th>
                  <th class="balance-header">Balance</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="account in accounts" :key="account.id">
                  <td>{{ account?.accountName }}</td>
                  <td>{{ account?.accountType }}</td>
                  <td class="balance">{{ formatCurrency(account?.balance) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="pagination-info">Page{{ pagination.currentPage }} of {{ pagination.totalPages }}</div>
      </div>
    </template>
  </AppLayout>
</template>
<script setup>
import AppLayout from '../layout/AppLayout.vue';
import { ref, onMounted } from 'vue';
import AccountApi from '../services/api';

const accounts = ref([]);
const loading = ref(true);
const error = ref(null);
const pagination = ref({
  currentPage: 1,
  totalPages: 5,
  totalItems: 30,
});

const fetchAccounts = async () => {
  const response = await AccountApi.getAccounts();
  console.log('get Accounts response', response);
  if (response.responseCode === '00' && response.accounts.length > 0) {
    accounts.value = response?.accounts;
  } else {
    accounts.value = [];
  }
};

const formatCurrency = value => {
  const dollars = value / 100;
  return new Intl.NumberFormat('en-Us', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(dollars);
};

onMounted(async () => {
  try {
    loading.value = true;
    error.value = null;
    await fetchAccounts();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Unknown error';
    console.log('Failed to fetch accounts', err);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.accounts {
  display: flex;
  flex-direction: column;
  padding-left: 20px;
  padding-right: 20px;
}
.desc {
  text-align: left;
  font-size: 1rem;
  color: #4a5568;
  margin-bottom: 1.5rem;
}

.state-indicator {
  padding: 2rem;
  text-align: center;
  color: #718096;
  background: #f7f9fc;
  border-radius: 12px;
  width: 100%;
  max-width: 900px;
}

.state-indicator.error {
  color: #c53030;
  background: #fff5f5;
}

.table {
  width: 100%;
  margin-right: 100px;
  background: #1e3353;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0, 0.05);
  overflow: auto;
  margin-bottom: 20px;
  height: 350px;
}

.table-scroll {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
  font-size: 0.95rem;
  border-radius: 12px;
}

th {
  background-color: #1e3353;
  color: #ffffff;
  font-weight: 600;
  padding: 1rem 1.5rem;
  border-bottom: 2px solid #e2e8f0;
  border-radius: 12px;
}

td {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #edf2f7;
  color: #ffffff;
}

tr:last-child td {
  border-bottom: none;
}

.balance-header,
.balance {
  text-align: right;
}

tbody tr:hover {
  background-color: #3e577d;
}

.pagination-info {
  /* padding: 1rem 1.5rem; */
  /* background-color: #1e3353; */
  /* border-top: 1px solid #e2e8f0; */
  font-size: 0.9rem;
  color: #ffffff;
  text-align: center;
}
</style>
