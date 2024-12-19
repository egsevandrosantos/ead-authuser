CREATE TABLE users
(
    id UUID DEFAULT GEN_RANDOM_UUID() NOT NULL,
    username CHARACTER VARYING(50) NOT NULL UNIQUE,
    email CHARACTER VARYING(50) NOT NULL UNIQUE,
    password CHARACTER VARYING(255) NOT NULL,
    name CHARACTER VARYING(150) NOT NULL,
    status CHARACTER(1) NOT NULL,
    type CHARACTER(1) NOT NULL,
    phone CHARACTER VARYING(20),
    cpf CHARACTER(11),
    image_url TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);