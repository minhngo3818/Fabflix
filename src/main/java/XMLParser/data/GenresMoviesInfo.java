package XMLParser.data;

public class GenresMoviesInfo {
    private String name;

    private String movieId;

    public GenresMoviesInfo() {
        name = "";
        movieId = "";
    };

    public GenresMoviesInfo(String name, String movieId) {
        this.name = name;
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }


    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + movieId.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof GenresMoviesInfo)) return false;
        GenresMoviesInfo other = (GenresMoviesInfo) obj;
        return this.name.equals(other.name) && this.movieId.equals(other.movieId);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Genre In Movies Details - ");
        sb.append("Genre:" + getName());
        sb.append("; ");
        sb.append("movieId:" + getMovieId());
        sb.append(".");

        return sb.toString();
    }
}
