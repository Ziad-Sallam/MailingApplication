package com.example.demo.Model;

import com.example.demo.service.MailService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class SortBySender implements SortStrategy, Comparator<Mail> {
    MailService mailService = new MailService();

    @Override
    public int compare(Mail mail1, Mail mail2) {
        return mail1.getSender().compareTo(mail2.getSender());
    }

    @Override
    public void sort(Folder folder) {
        ArrayList<Integer> list = new ArrayList<>();
        for(Map.Entry<Integer,String> s : folder.getFolderMailIds().entrySet()){
            list.add(s.getKey());
        }

        list.sort((id1, id2) -> {
            Mail mail1 = mailService.getEmail((Integer) id1);
            Mail mail2 = mailService.getEmail((Integer) id2);
            return mail1.getSender().compareTo(mail2.getSender());
        });
    }
}
