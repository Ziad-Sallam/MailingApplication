package com.example.demo.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mail {
    private int id;
    private String subject;
    private String sender;
    private List<String> receivers;
    private String body;

    private String dateSent;
    private int priority = 2;

    public Mail(String subject, String sender, ArrayList<String> receivers, String body, String dateSent, int id, int priority) {
        this.subject = subject;
        this.sender = sender;
        this.receivers = new ArrayList<>(receivers);
        this.body = body;
        this.dateSent = dateSent;
        this.id = id;
        setPriority(priority);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceivers() {
        return Collections.unmodifiableList(receivers);
    }

    public void setReceivers(ArrayList<String> receivers) {
        this.receivers = new ArrayList<>(receivers);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority >= 1 && priority <= 5) {
            this.priority = priority;
        } else {
            throw new IllegalArgumentException("Priority must be between 1 and 5");
        }
    }

    public Mail() {
        // Default constructor
    }

}
