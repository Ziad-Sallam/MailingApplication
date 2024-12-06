package com.example.demo.Model;

import java.util.ArrayList;

public class MailBuilder {
    private String subject;
    private String sender;
    private ArrayList<String> recievers;
    private String body;
    private ArrayList<Attachment> attachments;
    private String datasent;

    public MailBuilder setsubject (String subject){
        this.subject=subject;
        return this;

    }
    public MailBuilder setsender (String sender){
        this.sender=sender;
        return this;

    }

    public MailBuilder setrecivers (ArrayList<String> recievers){
        this.recievers=recievers;
        return this;

    }

    public MailBuilder setbody (String body){
        this.body=subject;
        return this;

    }
    public MailBuilder setattachment (ArrayList<Attachment> attachments){
        this.attachments=attachments;
        return this;

    }

    public MailBuilder setdatesent(String datasent){
        this.datasent=datasent;
        return this;

    }

    public Mail Build(){
        return new Mail(subject,sender,recievers,body,attachments,datasent);
    }






}
