package com.bdzjn.xml.model;

import com.bdzjn.xml.model.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String username;

    @JsonIgnore
    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private String token;

    @PrePersist
    public void generateToken() {
        this.token = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final User user = (User) o;

        if (!username.equals(user.username)) return false;
        if (!token.equals(user.token)) return false;
        if (!password.equals(user.password)) return false;
        if (!name.equals(user.name)) return false;
        return role == user.role;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + token.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
