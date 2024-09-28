package data.star;

import java.util.Objects;

public class Star {
    private String id;
    private String name;
    private Integer birthYear;

    public Star() {
        this.id = null;
        this.name = null;
        this.birthYear = null;
    }

    public Star(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Star(String id, String name, int birthYear) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Star)) return false;
        Star other = (Star) obj;
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Star Details - ");
        sb.append("Id:" + getId());
        sb.append(", ");
        sb.append("Name:" + getName());
        sb.append(", ");
        sb.append("Birth Year:" + getBirthYear());
        sb.append(".");

        return sb.toString();
    }
}
