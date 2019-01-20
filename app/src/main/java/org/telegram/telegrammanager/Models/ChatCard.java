package org.telegram.telegrammanager.Models;

public class ChatCard {

    public String name;
    public Integer subs;
    public Integer photoId;
    public Integer id;

    public ChatCard(String name, Integer subs, int photoId) {
        this.name = name;
        this.subs = subs;
        this.photoId = photoId;
        this.id = id;
    }
}
