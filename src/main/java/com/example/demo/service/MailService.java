package com.example.demo.Service;

import com.example.demo.Model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class MailService {
    static SystemData systemData = new SystemData();

    public MailService() {
        getData();
    }

    private static void getData() {
        if (systemData != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                systemData = mapper.readValue(new File("data/data.json"), SystemData.class);
                System.out.println("Users loaded: " + systemData.getUsers());
            } catch (IOException e) {
                System.out.println("Error loading system data: " + e.getMessage());
            }
        }
    }

    private static void writeData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("data/data.json"), systemData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String email, String password, String name) {
        ObjectMapper mapper = new ObjectMapper();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        try {
            ArrayList<String> temp = systemData.getUsers();
            if (temp.contains(email)) {
                System.out.println("User already exists");
                return;
            }

            createDirectoriesIfNeeded("data/users/" + email + ".json");
            mapper.writeValue(new File("data/users/" + email + ".json"), user);

            temp.add(email);
            systemData.setUsers(temp);
            writeData();

            System.out.println("User created: " + email);
        } catch (IOException e) {
            System.out.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            createDirectoriesIfNeeded("data/users/" + user.getEmail() + ".json");
            mapper.writeValue(new File("data/users/" + user.getEmail() + ".json"), user);
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    public User getUser(String email) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("data/users/" + email + ".json"), User.class);
        } catch (IOException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }

    public void createEmail(String from, ArrayList<String> to, String subject, String body, int priority) {
        MailBuilder builder = new MailBuilder();
        builder.setSender(from);
        builder.setReceivers(to);
        builder.setSubject(subject);
        builder.setBody(body);
        builder.setId(systemData.getNumberOfMails());
        builder.setPriority(priority);
        Mail mail = builder.build();

        ObjectMapper mapper = new ObjectMapper();
        try {
            createDirectoriesIfNeeded("data/mails/" + systemData.getNumberOfMails() + ".json");
            mapper.writeValue(new File("data/mails/" + systemData.getNumberOfMails() + ".json"), mail);

            User sender = getUser(from);
            if (sender != null) {
                Folder sentFolder = getOrCreateFolder(sender, "Sent");
                sentFolder.addMail(systemData.getNumberOfMails());
                sender.getSent().add(systemData.getNumberOfMails());
                setUser(sender);
            }


            for (String receiverEmail : to) {
                User receiver = getUser(receiverEmail);
                if (receiver != null) {
                    Folder inbox = getOrCreateFolder(receiver, "Inbox");
                    inbox.addMail(systemData.getNumberOfMails());
                    receiver.addReceivedMail(systemData.getNumberOfMails());
                    setUser(receiver);
                }
            }

            systemData.setNumberOfMails(systemData.getNumberOfMails() + 1);
            writeData();
        } catch (IOException e) {
            System.out.println("Error creating email: " + e.getMessage());
        }
    }

    public void moveToTrash(String email, int mailId) {
        User user = getUser(email);
        if (user == null) {
            System.out.println("User not found: " + email);
            return;
        }

        Folder trash = getOrCreateFolder(user, "Trash");
        Folder inbox = getOrCreateFolder(user, "Inbox");

        if (inbox.getFolderMailIds().remove((Integer) mailId)) {
            trash.addMail(mailId);
            setUser(user);
            System.out.println("Mail ID " + mailId + " moved to Trash for user: " + email);
        } else {
            System.out.println("Mail ID " + mailId + " not found in Inbox for user: " + email);
        }
    }

    public Mail getEmail(int id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("data/mails/" + id + ".json"), Mail.class);
        } catch (IOException e) {
            System.out.println("Error fetching email: " + e.getMessage());
        }
        return null;
    }

    private Folder getOrCreateFolder(User user, String folderName) {
        return user.getUserFolders().stream()
                .filter(folder -> folder.getName().equalsIgnoreCase(folderName))
                .findFirst()
                .orElseGet(() -> {
                    Folder newFolder = new Folder(folderName);
                    user.getUserFolders().add(newFolder);
                    return newFolder;
                });
    }

    private void createDirectoriesIfNeeded(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
}

class TestMailService {
    public static void main(String[] args) {
        MailService mailService = new MailService();


        mailService.createUser("user10@example.com", "password123", "User One");
        mailService.createUser("user20@example.com", "password456", "User Two");


        ArrayList<String> recipients = new ArrayList<>();
        recipients.add("user20@example.com");


        mailService.createEmail("user10@example.com", recipients, "Meeting Reminder", "Don't forget our meeting tomorrow!", 3);


        System.out.println("\nInbox for user20@example.com:");
        User user2 = mailService.getUser("user20@example.com");
        if (user2 != null) {
            Folder inbox = user2.getUserFolders().stream()
                    .filter(folder -> folder.getName().equalsIgnoreCase("Inbox"))
                    .findFirst()
                    .orElse(null);

            if (inbox != null) {
                inbox.getFolderMailIds().forEach(mailId -> {
                    Mail mail = mailService.getEmail(mailId);
                    if (mail != null) {
                        System.out.println("Mail ID: " + mail.getId() + ", Subject: " + mail.getSubject());
                    }
                });
            } else {
                System.out.println("Inbox not found.");
            }
        }


        System.out.println("\nMoving mail to Trash for user2@example.com...");
        mailService.moveToTrash("user20@example.com", 0);

        // Step 6: Display user2's Trash folder
        System.out.println("\nTrash for user20@example.com:");
        if (user2 != null) {
            Folder trash = user2.getUserFolders().stream()
                    .filter(folder -> folder.getName().equalsIgnoreCase("Trash"))
                    .findFirst()
                    .orElse(null);

            if (trash != null) {
                trash.getFolderMailIds().forEach(mailId -> {
                    Mail mail = mailService.getEmail(mailId);
                    if (mail != null) {
                        System.out.println("Mail ID: " + mail.getId() + ", Subject: " + mail.getSubject());
                    }
                });
            } else {
                System.out.println("Trash folder not found.");
            }
        }


        System.out.println("\nSent folder for user10@example.com:");
        User user1 = mailService.getUser("user10@example.com");
        if (user1 != null) {
            Folder sent = user1.getUserFolders().stream()
                    .filter(folder -> folder.getName().equalsIgnoreCase("Sent"))
                    .findFirst()
                    .orElse(null);

            if (sent != null) {
                sent.getFolderMailIds().forEach(mailId -> {
                    Mail mail = mailService.getEmail(mailId);
                    if (mail != null) {
                        System.out.println("Mail ID: " + mail.getId() + ", Subject: " + mail.getSubject());
                    }
                });
            } else {
                System.out.println("Sent folder not found.");
            }
        }
    }
}
