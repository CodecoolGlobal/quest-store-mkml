package com.queststore.Model;

import java.sql.Blob;

public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer classId;
    private Blob avatar;
    private String userType;

    private User(Integer id, String firstName, String lastName, String email, Integer classId, Blob avatar, String userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.classId = classId;
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

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Blob getAvatar() {
        return avatar;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public static class UserBuilder {
        private Integer nestedId;
        private String nestedFirstName;
        private String nestedLastName;
        private String nestedEmail;
        private Integer nestedClassId;
        private Blob nestedAvatar;
        private String nestedUserType;

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

        public UserBuilder classId(Integer classId) {
            this.nestedClassId = classId;
            return this;
        }

        public UserBuilder avatar(Blob avatar) {
            this.nestedAvatar = avatar;
            return this;
        }

        public UserBuilder userType(String userType) {
            this.nestedUserType = userType;
            return this;
        }

        public User createUser() {
            return new User(
                    nestedId, nestedFirstName, nestedLastName, nestedEmail,
                    nestedClassId, nestedAvatar, nestedUserType);
        }

    }
}
