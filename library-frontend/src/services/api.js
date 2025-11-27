import axios from 'axios';
import { LoginRequest, BorrowRequest } from './models.js';

const apiClient = axios.create({
    baseURL: 'http://localhost:8081/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

export const apiService = {
    register: (data) => apiClient.post('/users/register', data),
    
    login: async (phoneNumber, password) => {
        const payload = new LoginRequest(phoneNumber, password);
        const response = await apiClient.post('/users/login', payload);
        return response.data;
    },

    getInventory: async () => {
        const response = await apiClient.get('/books/inventory');
        return response.data;
    },

    borrowBook: async (userId, inventoryId) => {
        const payload = new BorrowRequest(userId, inventoryId);
        return await apiClient.post('/books/borrow', payload);
    },

    returnBook: async (userId, inventoryId) => {
        const payload = new BorrowRequest(userId, inventoryId);
        return await apiClient.post('/books/return', payload);
    }
};