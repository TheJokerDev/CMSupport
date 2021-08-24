package net.coralmc.cmsupport.storage;

import java.util.Date;

public class Partner {
    private String name;
    private int votes;
    private Date lastUpdate;

    public Partner(String name){
        this.name = name;
        votes = 0;
        lastUpdate = new Date();
    }

    public String getUsername() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public Partner setVotes(int votes) {
        this.votes = votes;
        return this;
    }

    public Partner setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }
}