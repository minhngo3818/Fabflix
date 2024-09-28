package data.movie;

public class MovieSuggestion {

    private String id;

    private String title;

    private Integer year;

    private String genreName;

    public MovieSuggestion() {
    }

    public MovieSuggestion(String id, String title, Integer year, String genreName) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genreName = genreName;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MovieSuggestion)) return false;
        MovieSuggestion other = (MovieSuggestion) obj;
        return this.id.equals(other.id);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Movie Suggestion Details - ");
        sb.append("Id:" + getId());
        sb.append(", ");
        sb.append("Title:" + getTitle());
        sb.append(", ");
        sb.append("Year:" + getYear());
        sb.append(", ");
        sb.append("Genre:" + getGenreName());
        sb.append(".");

        return sb.toString();
    }
}
