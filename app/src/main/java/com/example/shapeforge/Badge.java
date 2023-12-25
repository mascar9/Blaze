package com.example.shapeforge;

public class Badge {

    private int iconResource;
    private String title, description;

    private boolean unlocked;

    public Badge(String title, int iconResource, String description){
        this.title = title;
        this.iconResource = iconResource;
        this.description = description;
        this.unlocked = false;
    }

    public Badge(){
    }

    public int getIconResource() {
        return iconResource;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public boolean isUnlocked(){
        return unlocked;
    }

    public void unlockBadge(){
        unlocked = true;
    }

    public void lockBadge() {
        unlocked = false;
    }
}
