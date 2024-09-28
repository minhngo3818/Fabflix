package data.payment;

import java.util.Date;
import java.util.Objects;

public class CreditCard {

    private final String id;

    private final String firstName;

    private final String lastName;

    private final Date expDate;

    public CreditCard(String id, String firstName, String lastName, Date expDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expDate = expDate;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getExpDate() {
        return expDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getExpDate(), that.getExpDate());
    }
}
