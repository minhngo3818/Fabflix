package data.movie;

import data.genre.Genre;
import data.star.Star;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieListItem extends Movie {

    private final List<Genre> genres;
    private final List<Star> stars;

    public MovieListItem(
            String id,
            String title,
            String director,
            Integer year,
            Double rating) {
        super(id, title, director, year, rating);
        this.genres = new ArrayList<Genre>();
        this.stars = new ArrayList<Star>();
    }

    public MovieListItem(String id, String title, String director, Integer year, Double rating, List<Genre> genres, List<Star> stars) {
        super(id, title, director, year, rating);
        this.genres = genres;
        this.stars = stars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieListItem)) return false;
        if (!super.equals(o)) return false;
        MovieListItem that = (MovieListItem) o;
        return Objects.equals(getGenres(), that.getGenres()) && Objects.equals(getStars(), that.getStars());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }

    public List<Genre> getGenres() {
        return this.genres;
    }

    public List<Star> getStars() {
        return this.stars;
    }
}
