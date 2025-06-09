# Gold Commodity Exchange - Frontend

This is the frontend for the Gold Commodity Exchange project, built using React with Vite.

## 🧱 Stack

- React + Vite
- Docker
- Nginx (for production deployment)

## 🚀 Development

To run locally:

```bash
npm install
npm run dev
```

The app will be available at `http://localhost:5173` (default Vite port).

## 🐳 Docker Deployment

1. Build the Docker image:

   ```bash
   docker build -t gold-exchange-frontend .
   ```

2. Run the container:

   ```bash
   docker run -d -p 8080:80 gold-exchange-frontend
   ```

3. The app will be accessible at `http://localhost:8080`.

## 🔧 Notes

- Ensure `nginx.conf` is present in the project root to route SPA properly.
- Use in combination with backend services hosted on Render or other cloud platforms.
