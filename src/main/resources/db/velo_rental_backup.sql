--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: vehicle_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.vehicle_type AS ENUM (
    'CAR',
    'HELICOPTER',
    'MOTORCYCLE'
);


ALTER TYPE public.vehicle_type OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: rentals; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rentals (
    id integer NOT NULL,
    user_id integer,
    vehicle_id integer,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    total_price numeric(12,2) NOT NULL,
    deposit numeric(12,2)
);


ALTER TABLE public.rentals OWNER TO postgres;

--
-- Name: rentals_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rentals_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rentals_id_seq OWNER TO postgres;

--
-- Name: rentals_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rentals_id_seq OWNED BY public.rentals.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    email character varying(100) NOT NULL,
    password_hash character(64) NOT NULL,
    role character varying(20) NOT NULL,
    birthdate date,
    is_corporate boolean DEFAULT false
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: vehicles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vehicles (
    id integer NOT NULL,
    type public.vehicle_type NOT NULL,
    brand character varying(50) NOT NULL,
    model character varying(50) NOT NULL,
    purchase_price bigint NOT NULL,
    rate_hourly numeric(10,2) NOT NULL,
    rate_daily numeric(10,2) NOT NULL,
    rate_weekly numeric(10,2) NOT NULL,
    rate_monthly numeric(10,2) NOT NULL
);


ALTER TABLE public.vehicles OWNER TO postgres;

--
-- Name: vehicles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.vehicles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.vehicles_id_seq OWNER TO postgres;

--
-- Name: vehicles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.vehicles_id_seq OWNED BY public.vehicles.id;


--
-- Name: rentals id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rentals ALTER COLUMN id SET DEFAULT nextval('public.rentals_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: vehicles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicles ALTER COLUMN id SET DEFAULT nextval('public.vehicles_id_seq'::regclass);


--
-- Data for Name: rentals; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (1, 10, 1, '2025-05-23 02:22:47.736084', '2025-05-25 02:22:47.736084', 1200.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (2, 12, 1, '2025-05-23 04:15:07.440022', '2025-05-23 05:15:07.440022', 100.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (3, 13, 5, '2025-05-23 05:33:02.458224', '2025-06-23 05:33:02.458224', 6000.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (4, 13, 8, '2025-05-25 00:00:13.42271', '2025-06-29 00:00:13.42271', 6000.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (5, 14, 10, '2025-05-25 00:15:37.287165', '2025-06-25 00:15:37.287165', 1488000.00, 2500000.00);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (6, 12, 12, '2025-05-25 00:18:41.220931', '2025-06-03 00:18:41.220931', 3240.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (7, 12, 14, '2025-05-25 00:27:38.011578', '2025-08-25 00:27:38.011578', 18000.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (8, 12, 14, '2025-05-25 18:02:57.785883', '2025-05-25 20:02:57.785883', 110.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (9, 12, 14, '2025-05-25 18:03:34.6888', '2025-05-25 21:03:34.6888', 165.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (10, 14, 13, '2025-05-25 18:17:03.204665', '2025-07-25 18:17:03.204665', 6000.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (11, 14, 3, '2025-05-25 18:21:15.985568', '2025-07-02 18:21:15.985568', 570000.00, 500000.00);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (12, 15, 2, '2025-05-25 18:23:57.396206', '2025-05-30 18:23:57.396206', 6000.00, 250000.00);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (13, 15, 8, '2025-05-25 19:23:01.415225', '2025-06-15 19:23:01.415225', 3600.00, NULL);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (14, 15, 3, '2025-05-25 19:24:29.395561', '2025-05-28 19:24:29.395561', 45000.00, 500000.00);
INSERT INTO public.rentals (id, user_id, vehicle_id, start_time, end_time, total_price, deposit) VALUES (15, 13, 5, '2025-05-25 21:51:49.675401', '2025-06-29 21:51:49.675401', 9000.00, NULL);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (id, email, password_hash, role, birthdate, is_corporate) VALUES (10, 'smoke@example.com', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'CUSTOMER', '1990-05-23', false);
INSERT INTO public.users (id, email, password_hash, role, birthdate, is_corporate) VALUES (13, 'velocorporate@mail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'CUSTOMER', '1998-03-08', true);
INSERT INTO public.users (id, email, password_hash, role, birthdate, is_corporate) VALUES (12, 'velocustomer@mail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'CUSTOMER', '1998-03-08', false);
INSERT INTO public.users (id, email, password_hash, role, birthdate, is_corporate) VALUES (11, 'veloadmin@mail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'ADMIN', '1998-03-08', false);
INSERT INTO public.users (id, email, password_hash, role, birthdate, is_corporate) VALUES (14, 'velocorporateolderthan30@mail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'CUSTOMER', '1980-03-08', true);
INSERT INTO public.users (id, email, password_hash, role, birthdate, is_corporate) VALUES (15, 'velocustomerolderthan30@mail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'CUSTOMER', '1950-03-08', false);
INSERT INTO public.users (id, email, password_hash, role, birthdate, is_corporate) VALUES (16, 'corp-test@example.com', '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'CUSTOMER', '1985-05-25', true);


--
-- Data for Name: vehicles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (1, 'CAR', 'Toyota', 'Corolla', 1500000, 100.00, 600.00, 3000.00, 10000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (2, 'CAR', 'BMW', 'M5', 2500000, 200.00, 1200.00, 6000.00, 20000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (3, 'CAR', 'Mercedes-Benz', 'S-Class', 5000000, 2500.00, 15000.00, 90000.00, 300000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (4, 'HELICOPTER', 'Robinson', 'R44 Raven II', 30000000, 10000.00, 60000.00, 350000.00, 1000000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (5, 'MOTORCYCLE', 'Yamaha', 'MT-07', 250000, 50.00, 300.00, 1800.00, 6000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (6, 'CAR', 'Audi', 'A6', 1800000, 120.00, 720.00, 3600.00, 12000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (7, 'CAR', 'Tesla', 'Model S', 3500000, 300.00, 1800.00, 10800.00, 36000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (8, 'CAR', 'Ford', 'Fiesta', 400000, 40.00, 240.00, 1200.00, 4000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (9, 'HELICOPTER', 'Bell', '206 JetRanger', 15000000, 5000.00, 30000.00, 180000.00, 600000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (10, 'HELICOPTER', 'Airbus', 'H125', 25000000, 8000.00, 48000.00, 280000.00, 900000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (11, 'HELICOPTER', 'Robinson', 'R66 Turbine', 35000000, 12000.00, 72000.00, 450000.00, 1400000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (12, 'MOTORCYCLE', 'Ducati', 'Monster 821', 280000, 60.00, 360.00, 2000.00, 7000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (13, 'MOTORCYCLE', 'Kawasaki', 'Ninja 400', 120000, 30.00, 180.00, 800.00, 3000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (14, 'MOTORCYCLE', 'Honda', 'CB650R', 220000, 55.00, 330.00, 1800.00, 6000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (15, 'CAR', 'BMW', '3 Series', 2200000, 150.00, 900.00, 5000.00, 15000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (16, 'HELICOPTER', 'Bell', '407', 30000000, 12000.00, 70000.00, 400000.00, 1200000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (17, 'MOTORCYCLE', 'Kawasaki', 'Z650', 200000, 50.00, 300.00, 1800.00, 6000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (18, 'CAR', 'TestBrand', 'Expensive', 2500000, 200.00, 1200.00, 6000.00, 20000.00);
INSERT INTO public.vehicles (id, type, brand, model, purchase_price, rate_hourly, rate_daily, rate_weekly, rate_monthly) VALUES (19, 'CAR', 'testbrand', 'testmodel', 1000, 10.00, 20.00, 30.00, 40.00);


--
-- Name: rentals_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rentals_id_seq', 15, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 16, true);


--
-- Name: vehicles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.vehicles_id_seq', 19, true);


--
-- Name: rentals rentals_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rentals
    ADD CONSTRAINT rentals_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: vehicles vehicles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicles
    ADD CONSTRAINT vehicles_pkey PRIMARY KEY (id);


--
-- Name: rentals rentals_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rentals
    ADD CONSTRAINT rentals_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: rentals rentals_vehicle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rentals
    ADD CONSTRAINT rentals_vehicle_id_fkey FOREIGN KEY (vehicle_id) REFERENCES public.vehicles(id);


--
-- PostgreSQL database dump complete
--

