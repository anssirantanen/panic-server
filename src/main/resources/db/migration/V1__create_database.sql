CREATE TABLE producer (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    description TEXT
);
CREATE TABLE producer_tag (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tag VARCHAR(255)
);
CREATE TABLE producer_tag_link (
    producer UUID REFERENCES producer(id),
    tag  UUID REFERENCES producer_tag(id)
);