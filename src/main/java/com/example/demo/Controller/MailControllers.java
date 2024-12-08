package com.example.demo.Controller;

import com.example.demo.Service.MailManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mails")
public class MailControllers {
    private final MailManagement mailManagement;
    @Autowired
    public MailControllers() {
        this.mailManagement = MailManagement.getInstance();
    }

    //all api comes from front

}
