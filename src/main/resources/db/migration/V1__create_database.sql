CREATE TABLE producer (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    description TEXT
);
CREATE TABLE producer_tag (
    tag VARCHAR(255) PRIMARY KEY
);
CREATE TABLE producer_tag_link (
    id REFERENCES producer.id,
    tag REFERENCES producer_tag.tag
);