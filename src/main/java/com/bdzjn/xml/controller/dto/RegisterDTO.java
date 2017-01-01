package com.bdzjn.xml.controller.dto;

import com.bdzjn.xml.model.enumeration.Role;

public class RegisterDTO {

    private String username;

    private String password;

    private String name;

    private Role role;

    @Override
    public String toString() {
        return "RegisterDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
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
}
