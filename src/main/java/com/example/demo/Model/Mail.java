package com.example.demo.Model;


import java.util.ArrayList;

public class Mail {
    private String subject;
    private String sender;
    private ArrayList<String> recievers;
    private String body;
    private String datasent;

    public Mail(String subject, String sender, ArrayList<String> recievers,
         String body, String datasent){
        this.subject=subject;
        this.sender=sender;
        this.recievers=recievers;
        this.datasent=datasent;
        this.body=body;
    }






}
