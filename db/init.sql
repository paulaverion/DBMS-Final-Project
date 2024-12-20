CREATE DATABASE IF NOT EXISTS tourist_db;

USE tourist_db;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS footprint_entries (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount DOUBLE NOT NULL,
    carbon_footprint DOUBLE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS carbon_footprint (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    totalCarbonFootprint DOUBLE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO users (username, password) VALUES 
('paul', '123456'),
('marlou', 'password1'),
('jasiel', 'itstime');

INSERT INTO footprint_entries (user_id, type, amount, carbon_footprint) VALUES
(1, 'Electricity', 150.0, 150.0 * 0.233),
(1, 'Vehicle', 50.0, 50.0 * 2.31),
(2, 'Air Travel', 200.0, 200.0 * 0.09),
(2, 'Waste', 30.0, 30.0 * 1.9),
(3, 'Electricity', 300.0, 300.0 * 0.233),
(3, 'Vehicle', 75.0, 75.0 * 2.31);

INSERT INTO carbon_footprint (user_id, totalCarbonFootprint) VALUES
(1, 
    (SELECT SUM(carbon_footprint) FROM footprint_entries WHERE user_id = 1)
),
(2, 
    (SELECT SUM(carbon_footprint) FROM footprint_entries WHERE user_id = 2)
),
(3, 
    (SELECT SUM(carbon_footprint) FROM footprint_entries WHERE user_id = 3)
);