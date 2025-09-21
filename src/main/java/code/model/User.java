package code.model;

import com.opencsv.bean.CsvBindByName;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
public abstract class User {
    @CsvBindByName
    private int userId;
    @CsvBindByName
    private String firstName;
    @CsvBindByName
    private String lastName;
    @CsvBindByName
    private String username;
    @CsvBindByName
    private String password;
    @CsvBindByName
    private boolean isActive;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
