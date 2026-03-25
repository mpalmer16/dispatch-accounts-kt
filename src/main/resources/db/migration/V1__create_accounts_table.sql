CREATE TABLE accounts (
                          id UUID PRIMARY KEY,
                          email TEXT NOT NULL UNIQUE,
                          full_name TEXT NOT NULL,
                          password_hash TEXT NOT NULL,
                          loyalty_tier TEXT NOT NULL DEFAULT 'bronze',
                          preferred_location TEXT,
                          encrypted_note TEXT,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_accounts_email ON accounts(email);
CREATE INDEX idx_accounts_loyalty_tier ON accounts(loyalty_tier);