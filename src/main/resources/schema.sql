DROP TABLE IF EXISTS post_tags CASCADE;
DROP TABLE IF EXISTS tags CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS posts CASCADE;

CREATE TABLE IF NOT EXISTS posts (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(128) NOT NULL,
    content TEXT NOT NULL,
    image_url TEXT,
    likes_count integer,
    comments_count integer
);

CREATE TABLE IF NOT EXISTS tags (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(128) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS post_tags (
    post_id BIGINT references posts(id) on delete cascade ,
    tag_id BIGINT references tags(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS comments(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    content TEXT not null,
    post_id BIGINT references posts(id) on delete cascade not null
);