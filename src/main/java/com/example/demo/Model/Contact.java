package com.example.demo.Model;

import java.util.ArrayList;

public class Contact {
    private String name;
    private ArrayList<String> userEmails;

    public Contact(ArrayList<String> userEmails, String name) {
        this.userEmails = userEmails;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getUserEmails() {
        return userEmails;
    }

    public void setUserEmails(ArrayList<String> userEmails) {
        this.userEmails = userEmails;
    }
}