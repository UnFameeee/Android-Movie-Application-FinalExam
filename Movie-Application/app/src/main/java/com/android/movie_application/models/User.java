package com.android.movie_application.models;

public class User
{
    public String firstName, lastName, email, password;
    public User(String firstName, String lastName, String email, String password)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
