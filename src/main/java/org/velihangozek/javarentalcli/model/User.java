package org.velihangozek.javarentalcli.model;

import java.time.LocalDate;
import java.util.Objects;

public class User {
    private Integer id;
    private String email;
    private String passwordHash;
    private String role;           // e.g. "ADMIN" or "CUSTOMER"
    private LocalDate birthdate;
    private boolean isCorporate;

    public User() { }

    public User(Integer id, String email, String passwordHash, String role,
                LocalDate birthdate, boolean isCorporate) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.birthdate = birthdate;
        this.isCorporate = isCorporate;
    }

    // Getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getBirthdate() { return birthdate; }
    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public boolean isCorporate() { return isCorporate; }
    public void setCorporate(boolean corporate) { isCorporate = corporate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", birthdate=" + birthdate +
                ", isCorporate=" + isCorporate +
                '}';
    }
}
