DELETE FROM test_spring_repair_agency.users;

INSERT INTO test_spring_repair_agency.users (first_name, last_name, email, password, role) VALUES
('Testing', 'Admin', 'testing_admin@mail.com', '$2y$10$XE9GLIsRTUZOS.aESLN7QerNyKhJ4cRzjd8Qo8dQKe2KF9HaF70Dq', 'ADMIN'),
('Testing', 'Customer', 'testing_customer@mail.com', '$2y$10$d.H9YgTVqz4sjYQ6DFcjTe8tOUjg82FOnq6XFmX1BA5EXKLoyGfU.', 'CUSTOMER'),
('Testing', 'Master', 'testing_master@mail.com', '$2y$10$shOhAj3XBaRbUl/ozpiQrun6y.hmTT5TqR9bzcA5oi6vCTdAs/qqy', 'MASTER'),
('Testing', 'Manager', 'testing_manager@mail.com', '$2y$10$LQaofA1Lz9b.lOR5HzrCAuk9wDYy5gvdjeng5rQfp8rsVYFIyvvI2', 'MANAGER');