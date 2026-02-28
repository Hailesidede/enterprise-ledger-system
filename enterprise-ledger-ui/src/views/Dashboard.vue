<template>
  <AppLayout>
    <template #main-content>
      <div class="dashboard">
        <div class="tallys-one">
          <div class="account-tally">
            <div class="accounts-item">
              <div class="tab-label">Total Accounts</div>
              <div class="tab-label">12</div>
            </div>
            <div class="icon">
              <img width="30" height="30" src="../assets/folder-icon.png" alt="" />
            </div>
          </div>
          <div class="account-tally">
            <div class="accounts-item">
              <div class="tab-label">Total Balance</div>
              <div class="tab-label">$24,200.00</div>
            </div>
            <div class="icon">
              <img width="30" height="30" src="../assets/dollar-sign.png" alt="" />
            </div>
          </div>
        </div>
        <div class="tallys-one">
          <div class="account-tally">
            <div class="accounts-item">
              <div class="tab-label">Income</div>
              <div class="tab-label">$42,200.00</div>
            </div>
            <div class="icon">
              <img width="30" height="30" src="../assets/arrow-trend.png" alt="" />
            </div>
          </div>
          <div class="account-tally">
            <div class="accounts-item">
              <div class="tab-label">Expenses</div>
              <div class="tab-label">$1,800.00</div>
            </div>
            <div class="icon">
              <img width="30" height="30" src="../assets/decrease.png" alt="" />
            </div>
          </div>
        </div>
        <div class="chart-sec">
          <DashboardChart />
        </div>
      </div>
    </template>
  </AppLayout>
</template>
<script setup>
import AppLayout from '../layout/AppLayout.vue';
import DashboardChart from '../components/DashboardChart.vue';
</script>
<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  margin-left: 20px;
}

.tallys-one {
  display: flex;
  flex-direction: row;
  gap: 20px;
  margin-top: 10px;
  margin-right: 30px;
}

.account-tally {
  width: 50%;
  border: 1px solid #4f8ef7;
  border-radius: 5px;
  height: 100px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.accounts-item {
  display: flex;
  flex-direction: column;
  text-align: left;
  padding-left: 20px;
}

.icon {
  margin-right: 10px;
  padding: 5px;
  background-color: #1e3353;
  display: flex;
  align-items: center;
}

.tab-label {
  font-weight: 400;
  font-size: 18px;
}

.chart-sec {
  margin-top: 20px;
  margin-bottom: 20px;
  width: 98%;
}
</style>
