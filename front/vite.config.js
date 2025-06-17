// vite.config.js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

const API_BASE_URL = process.env.VITE_API_BASE_URL;

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      '/ws': {
        target: API_BASE_URL,
        changeOrigin: true,
        ws: true,
      }
    }
  },
  define: {
    global: {}, // stomp.js 호환용
  }
});
