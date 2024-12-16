package com.example.demo.Model;

import com.example.demo.Service.MailService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class SortByBody implements SortStrategy, Comparator<Mail> {
    MailService mailService = new MailService();

    @Override
    public int compare(Mail mail1, Mail mail2) {
        return mail1.getBody().compareTo(mail2.getBody());
    }

    @Override
    public void sort(Folder folder) {
        ArrayList<Integer> list = new ArrayList<>();
        for(Map.Entry<Integer,String> s : folder.getFolderMailIds().entrySet()){
            list.add(s.getKey());
        }
        list.sort((id1, id2) -> {
            Mail mail1 = mailService.getEmail(id1);
            Mail mail2 = mailService.getEmail(id2);
            return compare(mail1, mail2);
        });
    }
}
