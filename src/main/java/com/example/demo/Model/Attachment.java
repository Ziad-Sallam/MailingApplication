package com.example.demo.Model;

public class Attachment {
    public int id;
    private String fileName;
    private String fileType; // Example: "application/pdf" or "image/jpeg"
    private byte[] fileContent;

    public Attachment(String fileName, String fileType, byte[] fileContent) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileContent = fileContent;
    }
    public Attachment() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}