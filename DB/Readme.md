# 🐘 Running PostgreSQL via Docker Compose

This project uses a PostgreSQL database running in Docker for local development.

---

## 🐳 Run PostgreSQL Container

From the directory containing docker-compose.yml, run:

```shell
docker-compose up -d
```

for postgres sql cmd

```shell
docker exec -it postgres_db psql -U postgres
```

✅ This will:

- Pull the postgres:15 image if not already available
- Run PostgreSQL on port 5432
- Create the goldexchange database with user postgres
