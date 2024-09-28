package XMLParser.data;

import java.util.Objects;

public class StarInfo {
    private String id;
    private String name;

    private Integer birthYear;

    public StarInfo() {
        name = "";
    }

    public StarInfo(String name, Integer birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StarInfo)) return false;
        StarInfo other = (StarInfo) obj;

//        if (Objects.equals(this.name, other.name) && (this.birthYear != null || other.birthYear != null))
//            return true;

        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("StarInfo Details - ");
        sb.append("Id:" + getName());
        sb.append(", ");
        sb.append("Name:" + getName());
        sb.append(", ");
        sb.append("Year:" + getBirthYear());
        sb.append(".");

        return sb.toString();
    }
}
