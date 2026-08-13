import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '/auth': {
        target: 'http://localhost:8000',
        changeOrigin: true,
      },
      '/system': {
        target: 'http://localhost:8000',
        changeOrigin: true,
      },
      '/open-platform': {
        target: 'http://localhost:8000',
        changeOrigin: true,
      },
      '/open-api': {
        target: 'http://localhost:8000',
        changeOrigin: true,
      },
    },
  },
})