import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import InventoryView from '../views/InventoryView.vue';
import { useUserStore } from '../stores/userStore';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView
    },
    {
      path: '/inventory',
      name: 'inventory',
      component: InventoryView,
      meta: { requiresAuth: true } // 需要登入才能看
    }
  ]
});

// 路由守衛：沒登入不能看庫存頁
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  if (to.meta.requiresAuth && !userStore.isLoggedIn()) {
    next('/');
  } else {
    next();
  }
});

export default router;