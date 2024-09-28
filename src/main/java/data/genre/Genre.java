package data.genre;

import java.util.Objects;

public class Genre {
    private String id;
    private String name;

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Genre)) return false;
        Genre other = (Genre) obj;
        return this.id.equals(other.id);
    }
}
