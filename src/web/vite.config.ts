import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

const apiServer = "http://127.0.0.1:8147";

export default defineConfig({
    build: {
        rollupOptions: {
            input: {
                main: 'index.html'
            }
        },
        outDir: '../main/resources/static',
        emptyOutDir: true
    },
    plugins: [
        react()
    ],
    resolve: {
        // 路径别名
        alias: {
            '~': path.resolve(__dirname, './') // 根路径
        }
    },
    server: {
        host: '0.0.0.0', // 服务器主机名，如果允许外部访问，可设置为"0.0.0.0"
        port: 8150,
        open: true,
        cors: true,
        // https: false,
        // 代理跨域（mock 不需要配置，这里只是个事列）
        proxy: {
            '/api': apiServer
        }
    }
})
