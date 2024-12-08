package com.example.demo.Service;

import com.example.demo.Model.Mail;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MailManagement {

    private static MailManagement mailManagement;
    private ArrayList<Mail> mails;

    public static synchronized MailManagement getInstance() {
        if (mailManagement == null) {
            mailManagement = new MailManagement();
        }
        return mailManagement;
    }

    public void addmail(Mail mail){
        mails.add(mail);
    }

    //all functions deals with mails

}
