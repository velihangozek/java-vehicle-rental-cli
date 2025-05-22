-- Users table (ADMIN vs. CUSTOMER)
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password_hash CHAR(64)            NOT NULL,
    role          VARCHAR(20)         NOT NULL,
    birthdate     DATE,
    is_corporate  BOOLEAN DEFAULT FALSE
);

-- Vehicles table
CREATE TYPE vehicle_type AS ENUM ('CAR', 'HELICOPTER', 'MOTORCYCLE');
CREATE TABLE vehicles
(
    id             SERIAL PRIMARY KEY,
    type           vehicle_type   NOT NULL,
    brand          VARCHAR(50)    NOT NULL,
    model          VARCHAR(50)    NOT NULL,
    purchase_price BIGINT         NOT NULL,
    rate_hourly    NUMERIC(10, 2) NOT NULL,
    rate_daily     NUMERIC(10, 2) NOT NULL,
    rate_weekly    NUMERIC(10, 2) NOT NULL,
    rate_monthly   NUMERIC(10, 2) NOT NULL
);

-- Rentals table
CREATE TABLE rentals
(
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER REFERENCES users (id),
    vehicle_id  INTEGER REFERENCES vehicles (id),
    start_time  TIMESTAMP      NOT NULL,
    end_time    TIMESTAMP      NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    deposit     NUMERIC(12, 2)
);