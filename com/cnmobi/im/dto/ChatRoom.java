package com.cnmobi.im.dto;

public class ChatRoom {
    private String createrJid;
    private String description;
    private String jid;
    private String name;
    private int occupants;
    private String ownerJid;
    private String subject;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public int getOccupants() {
        return this.occupants;
    }

    public void setOccupants(int occupants) {
        this.occupants = occupants;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOwnerJid() {
        return this.ownerJid;
    }

    public void setOwnerJid(String ownerJid) {
        this.ownerJid = ownerJid;
    }

    public String getCreaterJid() {
        return this.createrJid;
    }

    public void setCreaterJid(String createrJid) {
        this.createrJid = createrJid;
    }
}
