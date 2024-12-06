package com.example.demo.Model;


import java.util.ArrayList;

public class Mail {
    private String subject;
    private String sender;
    private ArrayList<String> recievers;
    private String body;
    private ArrayList<Attachment> attachments=new ArrayList<>() ;//intialiaze it empty
    private String datasent;

    public Mail(String subject, String sender, ArrayList<String> recievers,
         String body, ArrayList<Attachment>attachments,String datasent){
        this.subject=subject;
        this.sender=sender;
        this.recievers=recievers;
        this.datasent=datasent;
        this.body=body;
        this.attachments=attachments;
    }






}
