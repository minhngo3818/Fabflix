package XMLParser.data;

import java.util.Objects;

public class CastInfo {
    private String starName;
    private String movieId;

    public CastInfo() {}

    public CastInfo(String starName, String movieId) {
        this.starName = starName;
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CastInfo)) return false;
        CastInfo other = (CastInfo) obj;
        return Objects.equals(this.starName, other.starName) && Objects.equals(this.movieId, other.movieId);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CastInfo Details - ");
        sb.append("Star Name:" + getStarName());
        sb.append(", ");
        sb.append("Movie Id:" + getMovieId());
        sb.append(".");

        return sb.toString();
    }
}
