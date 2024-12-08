package com.example.demo.Model;

import java.util.ArrayList;

public class SystemData {
    private int numberOfMails;
    private int numberOfAttachments;
    private ArrayList<String> users = new ArrayList<>();

    // Getters and Setters
    public int getNumberOfMails() {
        return numberOfMails;
    }

    public void setNumberOfMails(int numberOfMails) {
        this.numberOfMails = numberOfMails;
    }

    public int getNumberOfAttachments() {
        return numberOfAttachments;
    }

    public void setNumberOfAttachments(int numberOfAttachments) {
        this.numberOfAttachments = numberOfAttachments;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}
