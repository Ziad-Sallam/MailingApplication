package com.example.demo.Service;

import com.example.demo.Model.*;

import java.util.ArrayList;
import java.util.List;

public interface IMailServiceProxy {
    public void createUser(String email, String password, String name);
    public User getUser(String email);
    public Mail createEmail(String from, ArrayList<String> to, String subject, String body, int priority, List<Attachment> attachments);
    public void moveEmail(String email, int mailId,String fromFolderName,String toFolderName);
    public void setUser(User user);
    public Mail getEmail(int id);
    public Folder getFolder(User user, String folderName);
    public void addFolder(String folderName, String userName);
    public void renamefolder(User user, String oldname, String newname);
    public void deletefolder(User user, String foldername);
    public void createDirectoriesIfNeeded(String filePath);
    //    public List<Mail> getMailsFromFolder(Folder folder);
    public List<Mail> getMailsFromFolder(String email, String folder);
    public List<Mail> filterFolderMails(User user, String folderName, String filterType, String filterValue);
    public ArrayList<String> getUserFolders(String email);
    public Folder sortFolder(User user, String folderName, String strategy);
    public DraftedMail createDrafted(String id, Mail mail);
    public DraftedMail getDrafted(String id);
    public List<DraftedMail> getUserDrafts(String email);
    public boolean deleteDraft(String id, String email);
    //    public void cleanOldMails();
    public void writeData();
    public void addContacts(Contact contact, String email);
}