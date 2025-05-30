# 🐘 Running PostgreSQL via Docker Compose

This project uses a PostgreSQL database running in Docker for local development.

---

## 🧾 Step 1: Set Database Environment Variables

Create a `db.env` file in the same directory as `docker-compose.yml`:

```env
POSTGRES_DB=goldexchange
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```

## 🐳 Step 2: Run PostgreSQL Container

From the directory containing docker-compose.yml, run:

```shell
docker-compose up -d
```

✅ This will:

- Pull the postgres:15 image if not already available
- Run PostgreSQL on port 5432
- Create the goldexchange database with user postgres
