import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],

  // 打包基础路径
  base: './',

  server: {
    host: 'localhost',
    port: 3001,

    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        ws: true,
        rewrite: (pathStr) => pathStr.replace(/^\/api/, '')
      }
    }
  },

  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
      views: path.resolve(__dirname, './src/views'),
      components: path.resolve(__dirname, './src/components'),
      utils: path.resolve(__dirname, './src/utils'),
      less: path.resolve(__dirname, './src/less'),
      assets: path.resolve(__dirname, './src/assets'),
      com: path.resolve(__dirname, './src/components'),
      store: path.resolve(__dirname, './src/store'),
      mixins: path.resolve(__dirname, './src/mixins')
    }
  }
})