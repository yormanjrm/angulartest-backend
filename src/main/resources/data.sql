CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    image VARCHAR(255),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (name, email, password, role, image)
VALUES ('John Doe', 'john.doe@example.com', 'password123', 'ADMIN', 'default.png');

INSERT INTO users (name, email, password, role, image)
VALUES ('Jane Doe', 'jane.doe@example.com', 'password123', 'USER', 'default.png');