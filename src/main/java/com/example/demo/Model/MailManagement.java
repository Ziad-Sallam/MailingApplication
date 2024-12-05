package com.example.demo.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

   // all functions deals with mails

}
