
CREATE TABLE IF NOT EXISTS posts (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title varchar NOT NULL,
    content varchar NOT NULL,
    image_url varchar,
    likes_count integer,
    comments_count integer
);

CREATE TABLE IF NOT EXISTS tags (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title varchar(128) UNIQUE
);