package XMLParser.data;

import java.util.ArrayList;
import java.util.List;

public class MovieInfo {
    private String id;

    private String title;

    private String director;

    private Integer year;

    private List<String> genres;

    public MovieInfo() {
        id = "";
        title = "";
        director = "";
        genres = new ArrayList<>();
    }

    public MovieInfo(String id, String title, String director, Integer year) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
        genres = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<String> getGenres() {
        return genres;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MovieInfo)) return false;
        MovieInfo other = (MovieInfo) obj;
        if (this.id.equals(other.id)) return true;
        if (this.title.equals(other.title) && this.director.equals(other.director)) return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("MovieInfo Details - ");
        sb.append("Id:" + getId());
        sb.append(", ");
        sb.append("Title:" + getTitle());
        sb.append(", ");
        sb.append("Director:" + getDirector());
        sb.append(", ");
        sb.append("Year:" + getYear());
        sb.append(", ");
        sb.append("Genres:" + getGenres().toString());
        sb.append(".");

        return sb.toString();
    }
}
