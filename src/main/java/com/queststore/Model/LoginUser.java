package com.queststore.Model;

public class LoginUser {

    private Integer id;
    private String email;
    private String password;

    public LoginUser(Integer id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
