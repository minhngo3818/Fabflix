USE moviedb;

DROP FUNCTION IF EXISTS movie_exists;
DROP FUNCTION IF EXISTS star_exists;
DROP FUNCTION IF EXISTS genre_exists;
DROP FUNCTION IF EXISTS generate_id;
DROP PROCEDURE IF EXISTS add_movie;
DROP PROCEDURE IF EXISTS add_star;
DROP PROCEDURE IF EXISTS add_genre;
DROP FUNCTION IF EXISTS is_movie_inconsistent;
DROP PROCEDURE IF EXISTS insert_movie;


DELIMITER $$
CREATE FUNCTION movie_exists(movieTitle VARCHAR(100), movieDirector VARCHAR(100), movieYear INT)
    RETURNS BOOLEAN
    DETERMINISTIC
BEGIN
    DECLARE recordCount INT;
    SELECT COUNT(*) INTO recordCount FROM movies WHERE title = movieTitle AND director = movieDirector AND year = movieYear;
    IF recordCount > 0 THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END $$
DELIMITER ;


DELIMITER $$
CREATE FUNCTION star_exists(starName VARCHAR(100))
    RETURNS BOOLEAN
    DETERMINISTIC
BEGIN
    DECLARE recordCount INT;
    SELECT COUNT(*) INTO recordCount FROM stars WHERE name = starName;

    IF recordCount > 0 THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END $$
DELIMITER ;


DELIMITER $$
CREATE FUNCTION genre_exists(genreName VARCHAR(100))
    RETURNS BOOLEAN
    DETERMINISTIC
BEGIN
    DECLARE recordCount INT;
    SELECT COUNT(*) INTO recordCount FROM genres WHERE name = genreName;

    IF recordCount > 0 THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END $$
DELIMITER ;


DELIMITER $$
CREATE FUNCTION generate_id(tableName VARCHAR(100))
    RETURNS VARCHAR(10)
    DETERMINISTIC
BEGIN
    -- max movie id tt0499469
    -- max star id nm9423080
    DECLARE prefix VARCHAR(2);

    DECLARE numericPart INT;
    DECLARE newId VARCHAR(10);
    DECLARE maxId VARCHAR(10);

    IF tableName = 'movies' THEN
        SELECT max(id) INTO maxId FROM movies;
    END IF;

    IF tableName = 'stars' THEN
        SELECT max(id) INTO maxId FROM stars;
    END IF;

    SET prefix := LEFT(maxId, 2);
    SET numericPart := CAST(REGEXP_REPLACE(SUBSTRING(maxId, 3), '[^0-9]', '') AS UNSIGNED);

    IF numericPart IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot generate ID.';
    ELSE
        SET numericPart := numericPart + 1;
    END IF;

    SET newId := CONCAT(prefix, numericPart);

    RETURN newId;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE add_movie(
    IN movieTitle VARCHAR(100),
    IN movieDirector VARCHAR(100),
    IN movieYear INT,
    IN genre VARCHAR(100),
    IN starName VARCHAR(100),
    IN starBirthYear INT,

    OUT movieId VARCHAR(10),
    OUT starId VARCHAR(10),
    OUT genreId INT
)
BEGIN
    DECLARE newMovieId VARCHAR(10);
    DECLARE newStarId VARCHAR(10);
    DECLARE newGenreId INT;

    IF movie_exists(movieTitle, movieDirector, movieYear) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Duplicated Movie!';
    ELSE
        SET newMovieId := generate_id('movies');

        START TRANSACTION;

            INSERT INTO movies (id, title, director, year) VALUES (newMovieId, movietitle, movieDirector, movieYear);
            SET movieId := newMovieId;

            -- Handle insertion for associated star based its existence
            IF star_exists(starName) THEN
                SELECT id INTO newStarId FROM stars WHERE name = starName;
            ELSE
                SET newStarId := generate_id('stars');
                INSERT INTO stars (id, name, birthYear) VALUES (newStarId, starName, starBirthYear);
            END IF;
            INSERT INTO stars_in_movies (starId, movieId) VALUES (newStarId, newMovieId);
            SET starId := newStarId;

            -- Handle insertion for associated genre based its existence
            IF genre_exists(genre) THEN
                SELECT id INTO newGenreId FROM genres WHERE name = genre;
            ELSE
                INSERT INTO genres (name) VALUES (genre);
                SET newGenreId := LAST_INSERT_ID();
            END IF;
            INSERT INTO genres_in_movies (genreId, movieId) VALUES (newGenreId, newMovieId);
            SET genreId := newGenreId;

        COMMIT;
    END IF;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE add_star(
    IN starName VARCHAR(100),
    IN starBirthYear INT,

    OUT starId VARCHAR(10)
)
BEGIN
    DECLARE newStarId VARCHAR(10);
    SET newStarId = generate_id('stars');
    INSERT INTO stars (id, name, birthYear) VALUES (newStarId, starName, starBirthYear);

    IF ROW_COUNT() = 1 THEN
        SELECT 'Insert a star successful' AS message;
        SET starId = newStarId;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insert failed';
    END IF;

END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE add_genre(
    IN genre VARCHAR(100),

    OUT genreId INT
)
BEGIN
    IF genre_exists(genre) = FALSE THEN
        INSERT INTO genres (name) VALUES (genre);
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Genre already existed';
    END IF;

    IF ROW_COUNT() = 1 THEN
        SELECT 'Insert a genre successful' AS message;
        SET genreId = LAST_INSERT_ID();
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insert failed or genre already existed';
    END IF;
END $$
DELIMITER ;


DELIMITER $$
CREATE FUNCTION is_movie_inconsistent(movieId VARCHAR(10), movieTitle VARCHAR(100), movieDirector VARCHAR(100), movieYear INT)
    RETURNS BOOLEAN
    DETERMINISTIC
BEGIN
    DECLARE titleVar VARCHAR(100);
    DECLARE directorVar VARCHAR(100);
    DECLARE yearVar INT;
    DECLARE recordCount INT;
    SELECT title, director, year INTO titleVar, directorVar, yearVar  FROM movies WHERE id = movieId;

    IF titleVar != movieTitle or directorVar != movieDirector or yearVar != movieYear THEN
        RETURN TRUE;
    END IF;

    SELECT COUNT(*) INTO recordCount FROM movies WHERE title = movieTitle and director = movieDirector;

    IF recordCount >= 1 THEN
        RETURN TRUE;
    END IF;

    RETURN FALSE;

END $$
DELIMITER ;


DELIMITER $$

CREATE PROCEDURE insert_movie(
    IN movie_id VARCHAR(10),
    IN movie_title VARCHAR(100),
    IN movie_director VARCHAR(100),
    IN movie_year INT)
BEGIN
    DECLARE movie_exists_result BOOLEAN;

    -- If the movie already exists, raise an error
    IF movie_exists(movie_title, movie_director, movie_year) THEN
        SIGNAL SQLSTATE '23100' SET MESSAGE_TEXT = 'Duplicate record: A movie with the same title, director, and year already exists.';
    END IF;

    IF is_movie_inconsistent(movie_id, movie_title, movie_director, movie_year) THEN
        SIGNAL SQLSTATE '23200' SET MESSAGE_TEXT = 'Inconsistent record: An existed movie has the same id, but some different field values';
    END IF;

    -- If the movie doesn't exist, insert it
    INSERT INTO movies (id, title, director, year) VALUES (movie_id, movie_title, movie_director, movie_year);
END $$

DELIMITER ;