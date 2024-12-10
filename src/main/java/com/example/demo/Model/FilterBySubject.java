package com.example.demo.Model;

import java.util.List;
import java.util.stream.Collectors;

public class FilterBySubject implements IMailFilter {
    private String subject;
    public FilterBySubject(String subject) {
        this.subject = subject;
    }

    @Override
    public List<Mail> applyFilter(List<Mail> mails) {
        return mails.stream()
                .filter(mail -> mail.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }
}
