package data.user;

public class Customer {
    // This class use the Customer table
    // Only use for authentication
    private final String firstname;

    private final String lastname;

    private final String email;

    public Customer(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getUsername() {
        return firstname;
    }

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }
}
