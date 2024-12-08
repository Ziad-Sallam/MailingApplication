package com.example.demo.Model;

import java.util.ArrayList;
import java.util.List;

public class Folder {
    private String name;
    private List<Integer> folderMailIds; // Store mail IDs

    public Folder(String name) {
        this.name = name;
        this.folderMailIds = new ArrayList<>();
    }

    public Folder() {
        this.folderMailIds = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getFolderMailIds() {
        return folderMailIds;
    }

    public void addMail(int mailId) {
        folderMailIds.add(mailId);
    }

    public void removeMail(int mailId) {
        folderMailIds.remove((Integer) mailId);
    }
}
