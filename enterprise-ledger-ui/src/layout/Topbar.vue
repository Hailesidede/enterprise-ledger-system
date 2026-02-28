<template>
  <div class="top-bar">
    <div class="page-title">{{ pageTitle }}</div>

    <div class="top-bar-icons">
      <div>
        <img width="24" height="24" src="../assets/search.png" alt="" />
      </div>
      <div>
        <img width="24" height="24" src="../assets/bell.png" alt="" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { useRoute } from 'vue-router';
import { computed } from 'vue';

const route = useRoute();

const pageTitle = computed(() => route.meta.title || 'ELS');
</script>
<style scoped>
.top-bar {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 40px;
  height: 60px;
  width: 100%;
  align-items: center;
  padding-left: 20px;
  margin-top: 20px;
  margin-bottom: 10px;
}

.page-title {
  font-size: 20px;
  font-weight: 700;
}

.top-bar-icons {
  display: flex;
  flex-direction: row;
  gap: 25px;
  margin-right: 50px;
}
</style>
