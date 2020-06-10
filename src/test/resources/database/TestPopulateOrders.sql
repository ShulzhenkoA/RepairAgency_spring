DELETE FROM test_spring_repair_agency.orders;
DELETE FROM test_spring_repair_agency.cars;

INSERT INTO test_spring_repair_agency.cars VALUES (1, 'OPEL', 'ASTRA', 2005);

INSERT INTO test_spring_repair_agency.orders
    (id, customer_id, creation_date, car_id, repair_type, repair_description, price, status)
VALUES(1, 2, '2020-06-01', 1, 'ENGINE_REPAIR', 'Some repairDescription', 0, 'PENDING');