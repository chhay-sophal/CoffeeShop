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
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"item_id"	INTEGER NOT NULL,
	"amount"	INTEGER NOT NULL,
	"total_price"	INTEGER NOT NULL,
	"paid"	INTEGER NOT NULL,
	"completed" INTEGER NOT NULL,
	FOREIGN KEY("item_id") REFERENCES "menu"("id")
);

CREATE TABLE "sold_items" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "order_item_id" INTEGER NOT NULL,
    FOREIGN KEY("order_item_id") REFERENCES "order_items"("id")
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
INSERT INTO order_items (item_id, amount, total_price, paid, completed) VALUES (1, 2, 7.00, 1, 0);
INSERT INTO order_items (item_id, amount, total_price, paid, completed) VALUES (3, 1, 9.00, 1, 0);
INSERT INTO order_items (item_id, amount, total_price, paid, completed) VALUES (4, 3, 9.00, 0, 1);
INSERT INTO order_items (item_id, amount, total_price, paid, completed) VALUES (2, 1, 4.00, 1, 1);
-- Add more order items as needed

-- sold_items_seed.sql
INSERT INTO sold_items (order_item_id) VALUES (1);
INSERT INTO sold_items (order_item_id) VALUES (3);
-- Add more sold items as needed
