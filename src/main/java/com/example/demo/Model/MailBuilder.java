package com.example.demo.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MailBuilder {
    private String subject;
    private String sender;
    private ArrayList<String> receivers;
    private String body;

    private String dateSent;
    private int id;
    private int priority = 2;

    public MailBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public MailBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public MailBuilder setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public MailBuilder setReceivers(ArrayList<String> receivers) {
        this.receivers = new ArrayList<>(receivers);
        return this;
    }

    public MailBuilder setBody(String body) {
        this.body = body;
        return this;
    }


    public MailBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }
    public MailBuilder setDateSent() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateSent = now.format(formatter);
        return this;
    }

    public Mail build() {
        return new Mail(subject, sender, receivers, body, dateSent, id, priority);
    }
}
