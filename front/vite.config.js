// vite.config.js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port:3000,
    proxy: {
      '/ws': {
        target: 'http://localhost:5050', // ✅ 정확한 포트 설정
        changeOrigin: true,
        ws: true, // ✅ WebSocket 프록시 활성화
      }
    }
  },
  define: {
    global: {}, // stomp.js 호환용
  }
});
