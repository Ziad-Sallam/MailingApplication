package com.example.demo.Model;

import java.util.ArrayList;

public class User {

    String name;
    String email;
    String password ;
    ArrayList<Integer> sent = new ArrayList<>();
    ArrayList<Integer> received = new ArrayList<>();
    ArrayList<Attachment> attachments;

    public String getName() {
        return name;
    }

    public void setReceived(ArrayList<Integer> received) {
        this.received = received;
    }

    public void setSent(ArrayList<Integer> sent) {
        this.sent = sent;
    }

    public ArrayList<Integer> getReceived() {
        return received;
    }
    public ArrayList<Integer> getSent() {
        return sent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }
}
