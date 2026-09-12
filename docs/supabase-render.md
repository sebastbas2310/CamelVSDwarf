# Supabase and Render

Supabase uses PostgreSQL. The project must use a PostgreSQL JDBC URL; the Supabase project URL (`https://...supabase.co`) is not a database URL.

## Supabase values

In Supabase, open **Project Settings > Database > Connect** and copy the URI for the **Session pooler**. Convert it to JDBC by adding `jdbc:`:

```text
jdbc:postgresql://<pooler-host>:6543/postgres?sslmode=require&prepareThreshold=0
```

Use the username shown by Supabase, commonly:

```text
postgres.<project-ref>
```

Use the database password configured in Supabase. This is not the publishable/anon API key.

## Render environment variables

Create these variables in the Render service. Do not commit them:

```text
DB_URL=jdbc:postgresql://<pooler-host>:6543/postgres?sslmode=require&prepareThreshold=0
DB_USERNAME=postgres.<project-ref>
DB_PASSWORD=<supabase-database-password>
JPA_DDL_AUTO=update
JWT_SECRET=<long-random-secret>
JWT_EXPIRATION=3600000
```

Do not set `DB_HOST=localhost` on Render. `localhost` refers to the Render container itself.

## Local Docker

The existing `compose.yml` continues to use the local PostgreSQL container. Copy `.env.example` to `.env` and use the local values when developing with Docker.

## Credential rotation

If a database password or JWT secret has ever been committed or shared, rotate it in Supabase and Render before deploying again.
