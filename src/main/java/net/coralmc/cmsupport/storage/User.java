package net.coralmc.cmsupport.storage;

import net.coralmc.cmsupport.Main;

import java.util.*;

public class User {
    private String username;
    private HashMap<Partner, Date> votes;

    public User(String username){
        this.username = username;
        votes = new HashMap<>();
    }

    public User loadVotes(String string){
        for (String s : string.split(",")){
            String[] var1 = s.split(";");
            Partner partner = Main.plugin.getPartnerStorage().getPartner(var1[0]);
            if (partner == null){
                continue;
            }
            Date date = new Date(var1[1]);
            votes.put(partner, date);
        }
        return this;
    }

    public String getVotes(){
        StringBuilder sb = new StringBuilder();
        if (votes.isEmpty()){
            return null;
        }
        for (Map.Entry<Partner, Date> entry : votes.entrySet()){
            sb.append(entry.getKey().getUsername()).append(";").append(entry.getValue().toString()).append(",");
        }
        return sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public List<Partner> getVotedPartners(){

        return new ArrayList<>(votes.keySet());
    }

    public Date getVotedTime(String name){
        Partner partner = Main.plugin.getPartnerStorage().getPartner(name);
        if (partner == null){
            return null;
        }
        if (!votes.containsKey(partner)){
            return null;
        }
        return votes.get(partner);
    }

    public boolean checkVotedTime(Partner partner, Date date){
        return date.before(getVotedTime(partner.getUsername()));
    }

    public void addPartnerVote(Partner partner, Date date){
        votes.put(partner, date);
        Main.plugin.getUserStorage().save(this, true);
    }
}
