package com.example.demo.Model;

import com.example.demo.Service.MailService;

import java.util.*;

public class SortByDate implements SortStrategy {
    MailService mailService = new MailService();

    @Override
    public void sort(Folder folder) {
        List<Integer> mailIds = new ArrayList<>(folder.getFolderMailIds().keySet());
        mailIds.sort((id1, id2) -> {
            Mail mail1 = mailService.getEmail(id1);
            Mail mail2 = mailService.getEmail(id2);
            if (mail1 == null || mail2 == null) {
                return 0;
            }
            return mail2.getDateSent().compareTo(mail1.getDateSent());
        });
        LinkedHashMap<Integer, String> sortedFolderMailIds = new LinkedHashMap<>();
        for (Integer mailId : mailIds) {
            sortedFolderMailIds.put(mailId, folder.getFolderMailIds().get(mailId));
        }
        folder.setFolderMailIds(sortedFolderMailIds);
    }
}