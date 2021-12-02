CREATE TABLE IF NOT EXISTS rooms
(
    id   SERIAL PRIMARY KEY  NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS persons
(
    id       SERIAL PRIMARY KEY  NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(2000) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles
(
    id   SERIAL PRIMARY KEY  NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS messages
(
    id        SERIAL PRIMARY KEY NOT NULL,
    created   TIMESTAMP          NOT NULL DEFAULT now(),
    text      TEXT               NOT NULL,
    room_id   INT                NOT NULL,
    person_id INT                NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms (id),
    FOREIGN KEY (person_id) REFERENCES persons (id)
);

CREATE TABLE IF NOT EXISTS persons_in_rooms
(
    person_id INT NOT NULL,
    room_id   INT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES persons (id),
    FOREIGN KEY (room_id) REFERENCES rooms (id)
);

CREATE TABLE role_of_persons
(
    person_id INT NOT NULL UNIQUE,
    role_id   INT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES persons (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);