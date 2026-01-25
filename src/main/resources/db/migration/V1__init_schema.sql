-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Diary entries table
CREATE TABLE diary_entries (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   user_id UUID NOT NULL,
   content TEXT NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT NOW(),

   CONSTRAINT fk_diary_user
       FOREIGN KEY (user_id)
           REFERENCES users (id)
           ON DELETE CASCADE
);