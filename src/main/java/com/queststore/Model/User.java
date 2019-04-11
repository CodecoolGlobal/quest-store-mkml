package com.queststore.Model;

import java.sql.Blob;

public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Class userClass;
    private Blob avatar;
    private UserType userType;

    public User(Integer id, String firstName, String lastName, String email, Class userClass, Blob avatar, UserType userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userClass = userClass;
        this.avatar = avatar;
        this.userType = userType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Class getUserClass() {
        return userClass;
    }

    public void setUserClass(Class userClass) {
        this.userClass = userClass;
    }

    public Blob getAvatar() {
        return avatar;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public static class UserBuilder {
        private Integer nestedId;
        private String nestedFirstName;
        private String nestedLastName;
        private String nestedEmail;
        private Class nestedUserClass;
        private Blob nestedAvatar;
        private UserType nestedUserType;

        public UserBuilder id(Integer id) {
            this.nestedId = id;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.nestedFirstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.nestedLastName = lastName;
            return this;
        }

        public UserBuilder email(String email) {
            this.nestedEmail = email;
            return this;
        }

        public UserBuilder userClass(Class userClass) {
            this.nestedUserClass = userClass;
            return this;
        }

        public UserBuilder avatar(Blob avatar) {
            this.nestedAvatar = avatar;
            return this;
        }

        public UserBuilder userType(UserType userType) {
            this.nestedUserType = userType;
            return this;
        }

        public User createUser() {
            return new User(
                    nestedId, nestedFirstName, nestedLastName, nestedEmail,
                    nestedUserClass, nestedAvatar, nestedUserType);
        }

    }
}
