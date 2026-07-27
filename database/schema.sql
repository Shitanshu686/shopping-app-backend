CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    brand VARCHAR(255),
    description TEXT,
    price DOUBLE,
    old_price DOUBLE,
    rating DOUBLE,
    image VARCHAR(255),
    category VARCHAR(100),
    stock INT
);