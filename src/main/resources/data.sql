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
VALUES ('John Doe', 'john.doe@example.com', '$2a$10$/iOt2Bx.vd4PHhWWJttE3.mNlX/jriVf4dASqOwbijXR.0goXmyRe', 'ADMIN', 'http://localhost:8080/images/default.png');

INSERT INTO users (name, email, password, role, image)
VALUES ('Jane Doe', 'jane.doe@example.com', '$2a$10$/iOt2Bx.vd4PHhWWJttE3.mNlX/jriVf4dASqOwbijXR.0goXmyRe', 'RECEP', 'http://localhost:8080/images/woman1.jpg');