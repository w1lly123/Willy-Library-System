<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/userStore';
import { apiService } from '../services/api';

const router = useRouter();
const userStore = useUserStore();

const currentTab = ref('login'); // 控制 Tab 切換
const loginForm = ref({ phoneNumber: '', password: '' });
const registerForm = ref({ phoneNumber: '', userName: '', password: '' });
const message = ref('');

const showMessage = (msg) => {
    message.value = msg;
    setTimeout(() => message.value = '', 3000);
};

const handleLogin = async () => {
    try {
        const data = await apiService.login(loginForm.value.phoneNumber, loginForm.value.password);
        userStore.login({ userId: data.userId, phone: loginForm.value.phoneNumber, userName: data.userName });
        showMessage(data.message);
        router.push('/inventory'); // 登入成功跳轉
    } catch (err) {
        showMessage(err.response?.data?.error || '登入失敗');
    }
};

const handleRegister = async () => {
    try {
        await apiService.register(registerForm.value);
        showMessage('註冊成功，請登入');
        currentTab.value = 'login';
        loginForm.value.phoneNumber = registerForm.value.phoneNumber;
    } catch (err) {
        showMessage(err.response?.data?.error || '註冊失敗');
    }
};
</script>

<template>
    <!-- 對應 max-w-md mx-auto bg-white p-6 rounded shadow -->
    <div class="max-w-md mx-auto bg-white p-6 rounded shadow">
        
        <!-- Tabs -->
        <div class="flex mb-4 border-b">
            <button 
                @click="currentTab = 'login'" 
                :class="['flex-1 py-2', currentTab === 'login' ? 'border-b-2 border-green-500 font-bold' : '']"
            >
                登入
            </button>
            <button 
                @click="currentTab = 'register'" 
                :class="['flex-1 py-2', currentTab === 'register' ? 'border-b-2 border-green-500 font-bold' : '']"
            >
                註冊
            </button>
        </div>

        <!-- Login Form -->
        <div v-if="currentTab === 'login'">
            <input v-model="loginForm.phoneNumber" type="text" placeholder="手機號碼" class="w-full p-2 mb-3 border rounded">
            <input v-model="loginForm.password" type="password" placeholder="密碼" class="w-full p-2 mb-3 border rounded">
            <button @click="handleLogin" class="w-full bg-green-600 text-white p-2 rounded hover:bg-green-700">登入</button>
        </div>

        <!-- Register Form -->
        <div v-if="currentTab === 'register'">
            <input v-model="registerForm.phoneNumber" type="text" placeholder="手機號碼" class="w-full p-2 mb-3 border rounded">
            <input v-model="registerForm.userName" type="text" placeholder="使用者名稱" class="w-full p-2 mb-3 border rounded">
            <input v-model="registerForm.password" type="password" placeholder="密碼" class="w-full p-2 mb-3 border rounded">
            <button @click="handleRegister" class="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700">註冊</button>
        </div>

        <!-- 錯誤訊息顯示 -->
        <div v-if="message" class="mt-4 text-center text-sm text-red-500 font-bold">{{ message }}</div>
    </div>
</template>