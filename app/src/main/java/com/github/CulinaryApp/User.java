package com.github.CulinaryApp;

import java.util.ArrayList;

public class User {

    private String email;
    private String firstName;
    private String lastName;
    private ArrayList<String> lifestyles;

    public User(){

    }

    public User(String email){
        this.email = email;
    }

    public User(String firstName, String lastName, ArrayList<String> lifestyles, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.lifestyles = lifestyles;
        this.email = email;
    }

    public ArrayList<String> getLifestyles(){
        return this.lifestyles;
    }

}
