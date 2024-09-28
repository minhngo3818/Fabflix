package data.movie;

public class Movie {
    private String id;
    private String title;
    private String director;
    private Integer year;
    private Double rating;

    public Movie() {
        this.id = "";
        this.title = "";
        this.director = "";
    }

    public Movie(String title, String director, Integer year) {
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public Movie(String id, String title, String director, Integer year) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public Movie(String id, String title, String director, Integer year, Double rating) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public Integer getYear() {
        return year;
    }

    public Double getRating() { return rating; }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setRating(Double rating) { this.rating = rating; }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Movie)) return false;
        Movie other = (Movie) obj;
        return this.id.equals(other.id);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Movie Details - ");
        sb.append("Id:" + getId());
        sb.append(", ");
        sb.append("Title:" + getTitle());
        sb.append(", ");
        sb.append("Director:" + getDirector());
        sb.append(", ");
        sb.append("Year:" + getYear());
        sb.append(".");

        return sb.toString();
    }
}
