CREATE TABLE "menu" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"price"	NUMERIC NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);

CREATE TABLE "user_types" (
	"id"	INTEGER NOT NULL,
	"type_name"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);

CREATE TABLE "users" (
	"id"	INTEGER NOT NULL,
	"username"	TEXT NOT NULL,
	"password"	TEXT NOT NULL,
	"user_type"	INTEGER,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("user_type") REFERENCES "user_types"("id")
);

CREATE TABLE "order_items" (
	"id"	INTEGER NOT NULL,
	"item_id"	INTEGER NOT NULL,
	"amount"	INTEGER NOT NULL,
	"total_price"	INTEGER NOT NULL,
	"paid"	INTEGER NOT NULL,
	"completed" INTEGER NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("item_id") REFERENCES "menu"("id")
);

CREATE TABLE "sold_items" (
	"id"	INTEGER NOT NULL,
	"sold_item"	INTEGER NOT NULL,
	"amount"	INTEGER NOT NULL,
	"total_price"	NUMERIC NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("sold_item") REFERENCES "menu"("id")
);

-- menu_seed.sql
INSERT INTO menu (name, price) VALUES ('Americano', 3.50);
INSERT INTO menu (name, price) VALUES ('Latte', 4.00);
INSERT INTO menu (name, price) VALUES ('Cappuccino', 4.50);
INSERT INTO menu (name, price) VALUES ('Espresso', 3.00);
-- Add more menu items as needed

-- user_types_seed.sql
INSERT INTO user_types (type_name) VALUES ('Administrator');
INSERT INTO user_types (type_name) VALUES ('Employee');
INSERT INTO user_types (type_name) VALUES ('Customer');
-- Add more user types as needed

-- users_seed.sql
INSERT INTO users (username, password, user_type) VALUES ('sophal', '1234', 1);
INSERT INTO users (username, password, user_type) VALUES ('kimheak', '1234', 2);
INSERT INTO users (username, password, user_type) VALUES ('nidate', '1234', 3);
-- Add more user data as needed

-- order_items_seed.sql
INSERT INTO order_items (item_id, amount, total_price, paid) VALUES (1, 2, 7.00, 1);
INSERT INTO order_items (item_id, amount, total_price, paid) VALUES (2, 1, 4.00, 1);
-- Add more order items as needed

-- sold_items_seed.sql
INSERT INTO sold_items (sold_item, amount, total_price) VALUES (1, 5, 17.50);
INSERT INTO sold_items (sold_item, amount, total_price) VALUES (3, 3, 13.50);
-- Add more sold items as needed
