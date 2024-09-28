CREATE DATABASE IF NOT EXISTS moviedb;
USE moviedb;

DROP TABLE IF EXISTS stars_in_movies;
DROP TABLE IF EXISTS genres_in_movies;
DROP TABLE IF EXISTS sales_info;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS items_in_cart;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS creditcards;
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS stars;


CREATE TABLE IF NOT EXISTS movies (
    id VARCHAR(10) NOT NULL,
    title VARCHAR(100) NOT NULL DEFAULT '',
    year INT NOT NULL,
    director VARCHAR(100) NOT NULL DEFAULT '',

    PRIMARY KEY (id),
    FULLTEXT (title),
    FULLTEXT (director),
    FULLTEXT (title, director)
);

CREATE TABLE IF NOT EXISTS stars (
    id VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL DEFAULT '',
    birthYear INT,

    PRIMARY KEY (id),
    FULLTEXT (name)

);

CREATE TABLE IF NOT EXISTS stars_in_movies (
    starId VARCHAR(10) NOT NULL,
    movieId VARCHAR(10) NOT NULL,

    PRIMARY KEY (starId, movieId),
    Foreign Key (starId) REFERENCES stars(id) ON DELETE CASCADE,
    Foreign Key (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL DEFAULT '',

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS genres_in_movies (
    genreId INT NOT NULL,
    movieId VARCHAR(10) NOT NULL,

    PRIMARY KEY (genreId, movieId),
    Foreign Key (genreId) REFERENCES genres(id) ON DELETE CASCADE,
    Foreign Key (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS creditcards (
    id VARCHAR(20) NOT NULL,
    firstName VARCHAR(50) NOT NULL DEFAULT '',
    lastName VARCHAR(50) NOT NULL DEFAULT '',
    expirationDate DATE NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS customers (
    id INT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL DEFAULT '',
    lastName VARCHAR(50) NOT NULL DEFAULT '',
    ccId VARCHAR(20) NOT NULL DEFAULT '',
    address VARCHAR(200) NOT NULL DEFAULT '',
    email VARCHAR(50) UNIQUE,
    password VARCHAR(50) UNIQUE,

    PRIMARY KEY (id),
    Foreign Key (ccId) REFERENCES creditcards(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS employees (
    email VARCHAR(50) UNIQUE,
    password VARCHAR(20) UNIQUE,
    fullname VARCHAR(100),

	PRIMARY KEY (email)
);

CREATE TABLE IF NOT EXISTS sales (
    id INT NOT NULL AUTO_INCREMENT,
    customerId INT NOT NULL,
    movieId VARCHAR(10) NOT NULL,
    saleDate DATE NOT NULL,

    PRIMARY KEY (id),
    Foreign Key (customerId) REFERENCES customers(id) ON DELETE CASCADE,
    Foreign Key (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sales_info (
	salesId INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unitPrice DOUBLE NOT NULL DEFAULT 1.0,
    
    PRIMARY KEY (salesId),
    Foreign Key (salesId) REFERENCES sales(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ratings (
    movieId VARCHAR(10) NOT NULL,
    rating FLOAT NOT NULL,
    numVotes INT NOT NULL,

    PRIMARY KEY (movieId),
    Foreign Key (movieId) REFERENCES movies(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS items (
	id INT NOT NULL AUTO_INCREMENT,
	movieId VARCHAR(10) NOT NULL DEFAULT '',
    movieTitle VARCHAR(100) NOT NULL DEFAULT '',
    quantity INT DEFAULT 1,
    unitPrice DOUBLE DEFAULT 0.0, 
    
    PRIMARY KEY (id),
    FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS items_in_cart (
	customerId INT NOT NULL DEFAULT 1,
    itemId INT NOT NULL DEFAULT 0,
    
    PRIMARY KEY (itemId, customerId),
    FOREIGN KEY (itemId) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY (customerId) REFERENCES customers(id) ON DELETE CASCADE
);