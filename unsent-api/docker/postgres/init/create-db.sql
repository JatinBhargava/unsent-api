-- Create role if it doesn't exist
DO
$$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'unsent_user') THEN
    CREATE ROLE unsent_user WITH LOGIN PASSWORD 'unsent_pass';
  END IF;
END
$$;

-- Create database if it doesn't exist (CREATE DATABASE must run outside a transaction)
SELECT 'CREATE DATABASE unsent_db OWNER unsent_user'
  WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'unsent_db');
\gexec

-- Optional: ensure privileges on the DB (connect and set grants)
\connect unsent_db
GRANT ALL PRIVILEGES ON DATABASE unsent_db TO unsent_user;