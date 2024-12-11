package com.example.demo.Model;

import com.example.demo.service.MailService;

import java.util.Comparator;


public class SortByDate implements SortStrategy, Comparator<Mail> {
    MailService mailService = new MailService();

    @Override
    public int compare(Mail mail1, Mail mail2) {
        return mail1.getDateSent().compareTo(mail2.getDateSent());
    }

    @Override
    public void sort(Folder folder) {
        folder.getFolderMailIds().sort((id1, id2) -> {
            Mail mail1 = mailService.getEmail(id1);
            Mail mail2 = mailService.getEmail(id2);
            return compare(mail1, mail2);
        });
    }
}
