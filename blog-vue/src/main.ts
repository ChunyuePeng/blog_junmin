import { createApp } from 'vue'
import App from './App.vue'

import router from './router'
import { store, key } from './store'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import service from './utils/https'
import urls from './utils/urls'

const app = createApp(App)

// Vuex
app.use(store, key)

// Router
app.use(router)

// Element Plus（推荐一次性引入）
app.use(ElementPlus)

// 全局属性
app.config.globalProperties.$https = service
app.config.globalProperties.$urls = urls

app.mount('#app')