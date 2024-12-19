package com.example.demo.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MailBuilder {
    private String subject;
    private String sender;
    private ArrayList<String> receivers;
    private String body;
    private String dateSent;
    private int id;
    private int priority = 2; // Default priority
    private List<Attachment> attachments = new ArrayList<>();

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
        if (priority >= 1 && priority <= 5) {
            this.priority = priority;
        } else {
            throw new IllegalArgumentException("Priority must be between 1 and 5");
        }
        return this;
    }

    public MailBuilder setDateSent() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateSent = now.format(formatter);
        return this;
    }

    public MailBuilder addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        return this;
    }

    public MailBuilder addAttachments(List<Attachment> attachments) {
        this.attachments.addAll(attachments);
        return this;
    }

    public MailBuilder clearAttachments() {
        this.attachments.clear();
        return this;
    }

    public Mail build() {
        return new Mail(subject, sender, receivers, body, dateSent, id, priority, attachments);
    }
}