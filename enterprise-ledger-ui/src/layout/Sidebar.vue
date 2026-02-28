<template>
  <div class="side-bar">
    <div class="side-bar-items">
      <div class="logo-brand">
        <div>
          <img width="24" height="24" src="../assets/blockchain.png" alt="" />
        </div>
        <div class="brand-name">E L S</div>
      </div>

      <div class="items">
        <RouterLink to="/" class="side-bar-item" active-class="active-link">
          <img width="24" height="24" src="../assets/dashboard-icon.png" />
          <span>Dashboard</span>
        </RouterLink>

        <RouterLink to="/accounts" class="side-bar-item" active-class="active-link">
          <img width="24" height="24" src="../assets/banking.png" />
          <span>Accounts</span>
        </RouterLink>

        <RouterLink to="/journals" class="side-bar-item" active-class="active-link">
          <img width="24" height="24" src="../assets/book-journal.png" />
          <span>Journals</span>
        </RouterLink>
      </div>
    </div>
    <div class="user-profile">
      <div class="profile-pic">
        <img width="28" height="28" src="../assets/user.png" alt="" />
      </div>
      <div class="profile-details">
        <div class="userNmn">John Doe</div>
        <div class="userTyp">Admin</div>
      </div>
    </div>
  </div>
</template>
<script setup></script>
<style scoped>
.logo-brand {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  margin-top: 15px;
  gap: 5px;
  border-bottom: 1px solid #4f8ef7;
  padding-bottom: 10px;

  .brand-name {
    font-size: 16px;
    font-weight: 700;
  }
}
.side-bar {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: auto;
  border: 1px solid #4f8ef7;
  padding-right: 40px;
  padding-left: 20px;
}

.items {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
  gap: 20px;
}

.side-bar-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 5px 16px;
  border-radius: 10px;
  cursor: pointer;
  color: #cbd5e1;
  text-decoration: none;
  transition: all 0.2s ease;
}

.side-bar-item:hover {
  background-color: #1e3353;
  color: #ffffff;
}

.active-link {
  background: #2a4f84;
  color: #ffffff;
  box-shadow: inset 3px 0 0 #4f8ef7;
}

.user-profile {
  display: flex;
  flex-direction: row;
  gap: 10px;
  border-top: 1px solid #4f8ef7;
  padding-top: 10px;
  padding-bottom: 20px;

  .profile-pic {
  }

  .userNmn {
    font-size: 12px;
    font-weight: 600;
    padding-top: 2px;
  }

  .userTyp {
    font-size: 8px;
    font-weight: 400;
    text-align: left;
  }
}
</style>
