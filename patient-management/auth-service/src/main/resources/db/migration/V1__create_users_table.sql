CREATE TABLE users (

    user_id UUID PRIMARY KEY,

    user_email VARCHAR(255) NOT NULL,

    password VARCHAR(255) NOT NULL,

    role VARCHAR(20) NOT NULL,

    CONSTRAINT uk_users_user_email UNIQUE(user_email)
);

CREATE INDEX idx_users_user_email
    ON users(user_email);