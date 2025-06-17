// vite.config.js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

const API_BASE_URL = process.env.VITE_API_BASE_URL;

export default defineConfig({
  plugins: [react()],
  server: {
    allowedHosts: ['localhost', '127.0.0.1', 'lacheln.p-e.kr', '52.79.195.13'],
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
