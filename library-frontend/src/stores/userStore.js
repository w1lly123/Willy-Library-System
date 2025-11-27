import { defineStore } from 'pinia';
import { ref } from 'vue';
import { User } from '../services/models';

export const useUserStore = defineStore('user', () => {
    // 狀態
    const currentUser = ref(new User(null, '', ''));
    
    // 動作
    const login = (data) => {
        currentUser.value = new User(data.userId, data.phone, data.userName);
    };

    const logout = () => {
        currentUser.value = new User(null, '', '');
    };

    // Getters (檢查是否登入)
    const isLoggedIn = () => !!currentUser.value.userId;

    return { currentUser, login, logout, isLoggedIn };
});