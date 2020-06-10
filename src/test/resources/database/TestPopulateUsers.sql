DELETE FROM test_spring_repair_agency.users;

INSERT INTO test_spring_repair_agency.users (id, first_name, last_name, email, password, role) VALUES
(1, 'Testing', 'Admin', 'testing_admin@mail.com', '$2y$10$XE9GLIsRTUZOS.aESLN7QerNyKhJ4cRzjd8Qo8dQKe2KF9HaF70Dq', 'ADMIN'),
(2, 'Testing', 'Customer', 'testing_customer@mail.com', '$2y$10$kqFCqt9ap1ylLCQclPbMWeaTF3emnV1bd.l/37ogBGtFD9ml67Cbq', 'CUSTOMER'),
(3, 'Testing', 'Master', 'testing_master@mail.com', '$2y$10$shOhAj3XBaRbUl/ozpiQrun6y.hmTT5TqR9bzcA5oi6vCTdAs/qqy', 'MASTER'),
(4, 'Testing', 'Manager', 'testing_manager@mail.com', '$2y$10$LQaofA1Lz9b.lOR5HzrCAuk9wDYy5gvdjeng5rQfp8rsVYFIyvvI2', 'MANAGER');