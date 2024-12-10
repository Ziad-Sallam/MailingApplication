package com.example.demo.Model;

import java.util.List;
import java.util.stream.Collectors;

public class FilterBySender implements IMailFilter{
    private String sender;

    public FilterBySender(String sender) {
        this.sender = sender;
    }

    @Override
    public List<Mail> applyFilter(List<Mail> mails) {
        return mails.stream()
                .filter(mail -> mail.getSender().equalsIgnoreCase(sender))
                .collect(Collectors.toList());
    }
}
