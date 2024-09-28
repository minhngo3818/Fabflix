package data.user;

public class Employee {

    private final String email;

    private final String fullname;

    public Employee(String email, String fullname) {
        this.email = email;
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }
}
