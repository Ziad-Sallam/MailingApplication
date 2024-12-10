package com.example.demo.Model;

public class MailSorter {

    private SortStrategy sortStrategy;

    public MailSorter() {
        this.sortStrategy = new SortByDate();
    }

    public MailSorter(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public void setSortStrategy(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public SortStrategy getSortStrategy() {
        return sortStrategy;
    }

    public void sortFolder(Folder folder) {
        sortStrategy.sort(folder);
    }
}
