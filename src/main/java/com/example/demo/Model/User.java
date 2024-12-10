package com.example.demo.Model;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String password;
    private ArrayList<Folder> userFolders = new ArrayList<>();
    private ArrayList<Integer> sent = new ArrayList<>();
    private ArrayList<Integer> received = new ArrayList<>();
    private ArrayList<Contact> usercontact;

    public User() {
        this.usercontact = new ArrayList<>();
        addDefaultFolders();
    }

    public ArrayList<Contact> getUsercontact() {
        return new ArrayList<>(usercontact);
    }

    public void setUsercontact(ArrayList<Contact> usercontact) {
        this.usercontact = new ArrayList<>(usercontact);
    }

    private void addDefaultFolders() {
        if (userFolders.isEmpty()) {
            userFolders.add(new Folder("Inbox"));
            userFolders.add(new Folder("Trash"));
            userFolders.add(new Folder("Sent"));
        }
    }

    public void addFolder(String folderName) {
        if (userFolders.stream().noneMatch(f -> f.getName().equalsIgnoreCase(folderName))) {
            userFolders.add(new Folder(folderName));
        } else {
            throw new IllegalArgumentException("Folder already exists");
        }
    }

    public void renameFolder(String oldName, String newName) {
        Folder folder = userFolders.stream()
                .filter(f -> f.getName().equalsIgnoreCase(oldName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Folder not found"));
        folder.setName(newName);
    }

    public void deleteFolder(String folderName) {
        Folder folder = userFolders.stream()
                .filter(f -> f.getName().equalsIgnoreCase(folderName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Folder not found"));
        userFolders.remove(folder);
    }

    public void addReceivedMail(int mailId) {
        this.received.add(mailId);
    }

    public ArrayList<Folder> getUserFolders() {
        return new ArrayList<>(userFolders);
    }

    public void setUserFolders(ArrayList<Folder> userFolders) {
        this.userFolders = new ArrayList<>(userFolders);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Integer> getSent() {
        return new ArrayList<>(sent);
    }

    public void setSent(ArrayList<Integer> sent) {
        this.sent = new ArrayList<>(sent);
    }

    public ArrayList<Integer> getReceived() {
        return new ArrayList<>(received);
    }

    public void setReceived(ArrayList<Integer> received) {
        this.received = new ArrayList<>(received);
    }
}