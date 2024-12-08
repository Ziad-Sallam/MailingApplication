package com.example.demo.Model;

import java.util.ArrayList;

public class SystemData {
    int numberOfMails;
    int numberOfAttachments;
    ArrayList<String> users = new ArrayList<>();

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
