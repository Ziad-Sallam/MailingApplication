package com.example.demo.Model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Folder {
    private String name;
    private HashMap<Integer,String> folderMailIds;


    public Folder(String name) {
        this.name = name;
        this.folderMailIds = new HashMap<>();
    }

    public  Folder() {
        this.folderMailIds = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer,String> getFolderMailIds() {
        return folderMailIds;
    }

    public void addMail(int mailId) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String d = now.format(formatter);
        folderMailIds.put(mailId,d);
    }

    public void removeMail(int mailId) {
        folderMailIds.remove((Integer) mailId);
    }

}
