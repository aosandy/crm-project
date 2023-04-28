CREATE TABLE call_types (
	id INT PRIMARY KEY,
	name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE tariffs (
	id INT PRIMARY KEY,
	name VARCHAR(50) NOT NULL
);

CREATE TABLE periods (
	id SERIAL PRIMARY KEY,
	minute_limit INT,
	fix_cost INT NOT NULL,
	price_per_minute INT NOT NULL,
	next_period_id INT,
	FOREIGN KEY(next_period_id) REFERENCES periods(id)
);

CREATE TABLE start_period_choice (
	id SERIAL PRIMARY KEY,
	tariff_id INT NOT NULL,
	call_type_id INT NOT NULL,
	is_intranet BOOLEAN NOT NULL,
	start_period_id INT NOT NULL,
	FOREIGN KEY(tariff_id) REFERENCES tariffs(id),
	FOREIGN KEY(call_type_id) REFERENCES call_types(id),
	FOREIGN KEY(start_period_id) REFERENCES periods(id)
);

INSERT INTO call_types (id, name)
VALUES 
	(1, 'Исходящий'),
	(2, 'Входящий');

INSERT INTO tariffs (id, name)
VALUES 
	(6, 'Безлимит 300'),
	(3, 'Поминутный'),
	(11, 'Обычный'),
	(82, 'Тариф X');

INSERT INTO periods (minute_limit, fix_cost, price_per_minute, next_period_id)
VALUES 
	(NULL, 0, 0, NULL),
	(NULL, 0, 100, NULL),
	(NULL, 0, 150, NULL),
	(300, 10000, 0, 2),
	(100, 0, 50, 3);
	
INSERT INTO start_period_choice (tariff_id, call_type_id, is_intranet, start_period_id)
VALUES 
	(6, 1, false, 4),
	(6, 1, true, 4),
	(6, 2, false, 4),
	(6, 2, true, 4),
	
	(3, 1, false, 3),
	(3, 1, true, 3),
	(3, 2, false, 3),
	(3, 2, true, 3),
	
	(11, 1, false, 5),
	(11, 1, true, 5),
	(11, 2, false, 1),
	(11, 2, true, 1),
	
	(82, 1, false, 3),
	(82, 1, true, 1),
	(82, 2, false, 3),
	(82, 2, true, 1);