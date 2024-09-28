USE moviedb;

-- SHOW INDEX
# show index from moviedb.stars_in_movies;
# show index from stars;
#
# DROP INDEX IF EXISTS idx_movies_title ON movies;
# DROP INDEX idx_movies_year ON movies;
# DROP INDEX idx_movies_director ON movies;
# DROP INDEX idx_genre_name ON genres;
# DROP INDEX idx_stars_name ON stars;
#
# DROP INDEX idx_starId_stars_movies ON stars_in_movies;


-- SINGLE INDEX
CREATE INDEX idx_movies_title ON movies (title);
CREATE INDEX idx_movies_year ON movies (year);
CREATE INDEX idx_movies_director ON movies (director);
CREATE INDEX idx_genre_name ON genres (name);
CREATE INDEX idx_stars_name ON stars (name);

-- RELATION INDEX
CREATE INDEX idx_starId_stars_movies ON stars_in_movies(starId);