DROP TABLE coffee;

// mysql
CREATE TABLE coffee  (
    coffee_id int NOT NULL AUTO_INCREMENT,
    brand VARCHAR(20),
    origin VARCHAR(20),
    characteristics VARCHAR(30),
    PRIMARY KEY (coffee_id)
);

// postgresql
CREATE TABLE public.coffee  (
    coffee_id SERIAL PRIMARY KEY ,
    brand VARCHAR(20),
    origin VARCHAR(20),
    characteristics VARCHAR(30),
    address varchar(300)
);