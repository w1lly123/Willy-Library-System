import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router' // 確保這裡是相對路徑 './router' 或 './router/index.js'
import './style.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')