<template>
  <div class="layout">
    <Sidebar></Sidebar>

    <div class="right-section">
      <Topbar></Topbar>
      <div class="main-content">
        <slot name="main-content"></slot>
      </div>
    </div>
  </div>
</template>

<script setup>
import Sidebar from './Sidebar.vue';
import Topbar from './Topbar.vue';
</script>

<style scoped>
.layout {
  display: flex;
  background-color: #0f1e35;
  height: 100vh;
}

.right-section {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.main-content {
  flex: 1;
}
</style>
