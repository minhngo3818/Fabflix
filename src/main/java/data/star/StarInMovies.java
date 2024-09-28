package data.star;

import data.movie.Movie;

import java.util.ArrayList;
import java.util.List;

public class StarInMovies extends Star {

    private final List<Movie> movies;

    public StarInMovies(String id, String name, int birthYear) {
        super(id, name, birthYear);
        this.movies = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return this.movies;
    }
}
