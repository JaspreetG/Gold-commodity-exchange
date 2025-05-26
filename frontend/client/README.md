React + Vite + Tailwind CSS Starter
This is a simple starter project using:

⚛️ React (with JSX)

⚡ Vite (for fast build and dev server)

🎨 Tailwind CSS (utility-first CSS framework)

🔧 Project Setup
1. Create Project with Vite
        npm create vite@latest my-app -- --client
        cd client

2. Install Dependencies
        npm install

3. Set Up Tailwind CSS
        npm install tailwindcss @tailwindcss/vite

Update vite.config.js:
    import { defineConfig } from 'vite'
    import react from '@vitejs/plugin-react'
    import tailwindcss from '@tailwindcss/vite'

    // https://vite.dev/config/
    export default defineConfig({
    plugins: [react(),tailwindcss()],
    })



🚀 Development Server
Start the Vite development server with:
npm run dev
This will run the app locally at: http://localhost:5173

