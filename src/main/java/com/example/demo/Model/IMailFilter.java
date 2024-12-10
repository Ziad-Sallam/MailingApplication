package com.example.demo.Model;

import java.util.List;

public interface IMailFilter {
    List<Mail> applyFilter(List<Mail> mails);
}
