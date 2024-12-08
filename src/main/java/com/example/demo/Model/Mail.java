package com.example.demo.Model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


public class Mail {
    int id;
    String subject;
    String sender;
    ArrayList<String> recievers;
    String body;
    String datasent;

    public Mail(String subject, String sender, ArrayList<String> recievers,
         String body, String datasent,int id){
        this.subject=subject;
        this.sender=sender;
        this.recievers=recievers;
        this.datasent=datasent;
        this.body=body;
        this.id=id;
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

    public ArrayList<String> getRecievers() {
        return recievers;
    }

    public void setRecievers(ArrayList<String> recievers) {
        this.recievers = recievers;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDatasent() {
        return datasent;
    }

    public void setDatasent(String datasent) {
        this.datasent = datasent;
    }

    public Mail(){}

}
