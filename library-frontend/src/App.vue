<script setup>
import { useRouter } from 'vue-router';
import { useUserStore } from './stores/userStore';

const router = useRouter();
const userStore = useUserStore();

const handleLogout = () => {
  userStore.logout();
  router.push('/');
};
</script>

<template>
  <!-- 對應 body class="bg-gray-100 min-h-screen" -->
  <div class="min-h-screen bg-gray-100">
    
    <!-- 對應 div id="app" class="container mx-auto p-4" -->
    <div class="container mx-auto p-4">
      
      <!-- Header 區塊 -->
      <header class="bg-green-600 text-white p-4 rounded-lg shadow mb-6 flex justify-between items-center">
        <h1 class="text-2xl font-bold">玉山銀行圖書借閱系統</h1>
        <div v-if="userStore.isLoggedIn()">
          <span class="mr-4">你好, {{ userStore.currentUser.userName }} (ID: {{ userStore.currentUser.userId }})</span>
          <button 
            @click="handleLogout" 
            class="bg-white text-green-600 px-3 py-1 rounded hover:bg-gray-100"
          >
            登出
          </button>
        </div>
      </header>

      <!-- 這裡會顯示 LoginView 或 InventoryView -->
      <RouterView />
    
    </div>
  </div>
</template>