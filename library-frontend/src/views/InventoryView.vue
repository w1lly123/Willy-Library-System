<script setup>
import { ref, onMounted } from 'vue';
import { useUserStore } from '../stores/userStore';
import { apiService } from '../services/api';

const userStore = useUserStore();
const inventory = ref([]);
const message = ref('');

const statusMap = { 1: '在庫', 2: '出借中', 3: '整理中', 4: '遺失', 5: '損毀', 6: '廢棄' };
const getStatusText = (id) => statusMap[id] || '未知';

const getStatusClass = (id) => {
    const status = Number(id);
    if (status === 1) return 'text-green-600 font-bold';
    if (status === 2) return 'text-red-600';
    return 'text-gray-500';
};

const showMessage = (msg) => {
    message.value = msg;
    setTimeout(() => message.value = '', 3000);
};

const fetchInventory = async () => {
    try {
        const data = await apiService.getInventory();
        inventory.value = data;
    } catch (err) {
        console.error(err);
    }
};

const borrowBook = async (invId) => {
    try {
        await apiService.borrowBook(userStore.currentUser.userId, invId);
        showMessage('借閱成功');
        fetchInventory();
    } catch (err) { showMessage(err.response?.data?.error || '借閱失敗'); }
};

const returnBook = async (invId) => {
    try {
        await apiService.returnBook(userStore.currentUser.userId, invId);
        showMessage('還書成功');
        fetchInventory();
    } catch (err) { showMessage(err.response?.data?.error || '還書失敗'); }
};

onMounted(() => {
    fetchInventory();
});
</script>

<template>
    <div class="bg-white p-6 rounded shadow">
        <h2 class="text-xl font-bold mb-4">書籍列表</h2>
        <div class="overflow-x-auto">
            <table class="w-full text-left border-collapse">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="p-3 border">庫存ID</th>
                        <th class="p-3 border">書名</th>
                        <th class="p-3 border">作者</th>
                        <th class="p-3 border">狀態</th>
                        <th class="p-3 border">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item in inventory" :key="item.inventoryId" class="hover:bg-gray-50">
                        <td class="p-3 border">{{ item.inventoryId }}</td>
                        <td class="p-3 border">{{ item.bookName }}</td>
                        <td class="p-3 border">{{ item.author }}</td>
                        <td class="p-3 border">
                            <span :class="getStatusClass(item.status)">
                                {{ getStatusText(item.status) }}
                            </span>
                        </td>
                        <td class="p-3 border">
                            <!-- 還原簡單的 v-if 判斷與按鈕樣式 -->
                            <button v-if="item.status == 1" @click="borrowBook(item.inventoryId)" class="bg-blue-500 text-white px-3 py-1 rounded text-sm hover:bg-blue-600 mr-2">借閱</button>
                            <button v-if="item.status == 2" @click="returnBook(item.inventoryId)" class="bg-orange-500 text-white px-3 py-1 rounded text-sm hover:bg-orange-600">還書</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- 提示訊息維持原樣 -->
        <div v-if="message" class="fixed bottom-4 right-4 bg-gray-800 text-white px-6 py-3 rounded shadow-lg">
            {{ message }}
        </div>
    </div>
</template>