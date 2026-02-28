import { createRouter, createWebHistory } from 'vue-router';
import Dashboard from '../views/Dashboard.vue';
import Accounts from '../views/Accounts.vue';
import Journals from '../views/Journals.vue';

const routes = [
  {
    path: '/',
    name: 'dashboard',
    component: Dashboard,
    meta: { title: 'Dashboard' },
  },

  {
    path: '/accounts',
    name: 'accounts',
    component: Accounts,
    meta: { title: 'Accounts' },
  },
  {
    path: '/journals',
    name: 'journals',
    component: Journals,
    meta: { title: 'Journals' },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});
export default router;
