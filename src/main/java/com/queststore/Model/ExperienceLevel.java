package com.queststore.Model;

public class ExperienceLevel {

    private int id;
    private String name;
    private int levelStart;

    public ExperienceLevel(int id, String name, int levelStart) {
        this.id = id;
        this.name = name;
        this.levelStart = levelStart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevelStart() {
        return levelStart;
    }

    public void setLevelStart(int levelStart) {
        this.levelStart = levelStart;
    }
}
