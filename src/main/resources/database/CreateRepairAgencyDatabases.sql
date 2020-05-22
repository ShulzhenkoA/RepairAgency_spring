DROP DATABASE IF EXISTS spring_repair_agency;
CREATE DATABASE spring_repair_agency CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;

CREATE TABLE spring_repair_agency.users
(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
first_name VARCHAR(64) NOT NULL,
last_name VARCHAR(64) NOT NULL,
email VARCHAR(64) NOT NULL,
password VARCHAR(256) NOT NULL,
role VARCHAR(32) NOT NULL
);

CREATE TABLE spring_repair_agency.cars
(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
brand VARCHAR(32) NOT NULL,
model VARCHAR(32) NOT NULL,
year VARCHAR(4) NOT NULL
);

CREATE TABLE spring_repair_agency.orders
(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
customer_id INT NOT NULL,
creation_date TIMESTAMP NOT NULL,
car_id int NOT NULL,
repair_type VARCHAR(32) NOT NULL,
repair_description VARCHAR(4096) NOT NULL,
price DOUBLE,
master_id INT,
repair_completion_date TIMESTAMP,
status VARCHAR(32) NOT NULL,
manager_comment VARCHAR(4096),
FOREIGN KEY (customer_id) REFERENCES spring_repair_agency.users(id) ON DELETE CASCADE ON UPDATE NO ACTION,
FOREIGN KEY (car_id) REFERENCES spring_repair_agency.cars(id)
);

CREATE TABLE spring_repair_agency.reviews
(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
customer_id INT NOT NULL,
review_date TIMESTAMP NOT NULL,
review_content VARCHAR(4096) NOT NULL,
FOREIGN KEY (customer_id) REFERENCES spring_repair_agency.users(id) ON DELETE CASCADE ON UPDATE NO ACTION
);



DROP DATABASE IF EXISTS test_spring_repair_agency;
CREATE DATABASE test_spring_repair_agency CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;

CREATE TABLE test_spring_repair_agency.users
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL,
    password VARCHAR(256) NOT NULL,
    role VARCHAR(32) NOT NULL
);

CREATE TABLE test_spring_repair_agency.cars
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(32) NOT NULL,
    model VARCHAR(32) NOT NULL,
    year VARCHAR(4) NOT NULL
);

CREATE TABLE test_spring_repair_agency.orders
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    car_id int NOT NULL,
    repair_type VARCHAR(32) NOT NULL,
    repair_description VARCHAR(4096) NOT NULL,
    price DOUBLE,
    master_id INT,
    repair_completion_date TIMESTAMP,
    status VARCHAR(32) NOT NULL,
    manager_comment VARCHAR(4096),
    FOREIGN KEY (customer_id) REFERENCES test_spring_repair_agency.users(id) ON DELETE CASCADE ON UPDATE NO ACTION,
    FOREIGN KEY (car_id) REFERENCES test_spring_repair_agency.cars(id)
);

CREATE TABLE test_spring_repair_agency.reviews
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    review_date TIMESTAMP NOT NULL,
    review_content VARCHAR(4096) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES test_spring_repair_agency.users(id) ON DELETE CASCADE ON UPDATE NO ACTION
);