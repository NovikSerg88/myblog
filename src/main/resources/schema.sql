
CREATE TABLE IF NOT EXISTS posts (
    post_id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title varchar NOT NULL,
    content varchar NOT NULL,
    image_url varchar,
    likes_count integer,
    comments_count integer
);