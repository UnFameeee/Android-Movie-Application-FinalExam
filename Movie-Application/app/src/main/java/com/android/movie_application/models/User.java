package com.android.movie_application.models;

public class User
{
    public String email, userName, firstName, lastName, password;
    public User(String email, String userName, String firstName, String lastName, String password)
    {
        this.email = email;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public User()
    {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
