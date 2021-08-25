package net.coralmc.cmsupport.storage;

import net.coralmc.cmsupport.Main;

import java.util.Date;

public class Partner {
    private String name;
    private int votes;
    private Date resetDate;

    public Partner(String name){
        this.name = name;
        votes = 0;
        resetDate = null;
    }

    public String getUsername() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    public Date getResetDate() {
        return resetDate;
    }

    public void addVote(){
        votes +=1;
        Main.plugin.getPartnerStorage().save(this, true);
    }

    public Partner setVotes(int votes) {
        this.votes = votes;
        Main.plugin.getPartnerStorage().save(this, true);
        return this;
    }

    public Partner setResetDate(Date date) {
        this.resetDate = date;
        Main.plugin.getPartnerStorage().save(this, true);
        return this;
    }
}
