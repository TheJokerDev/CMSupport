package net.coralmc.cmsupport.storage;

import net.coralmc.cmsupport.Main;

import java.util.*;

public class User {
    private final String username;
    private final HashMap<String, Date> votes;

    public User(String username){
        this.username = username;
        votes = new HashMap<>();
    }

    public User loadVotes(String string){
        for (String s : string.split(",")){
            if (!s.contains(";")){
                continue;
            }
            String[] var1 = s.split(";");
            Partner partner = Main.plugin.getPartnerStorage().getPartner(var1[0]);
            if (partner == null){
                continue;
            }
            Date date = new Date(var1[1]);
            votes.put(partner.getUsername(), date);
        }
        return this;
    }

    public HashMap<String, Date> getVotesHashMap(){
        return votes;
    }

    public String getVotes(){
        StringBuilder sb = new StringBuilder();
        if (votes.isEmpty()){
            return null;
        }
        for (Map.Entry<String, Date> entry : votes.entrySet()){
            sb.append(entry.getKey()).append(";").append(entry.getValue().toString()).append(",");
        }
        return sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public List<String> getVotedPartners(){

        return new ArrayList<>(votes.keySet());
    }

    public Date getVotedTime(String name){
        if (name == null){
            return null;
        }
        if (!votes.containsKey(name)){
            return null;
        }
        return votes.get(name);
    }

    public Date getVotedTime(Partner partner){
        if (!getVotes().contains(partner.getUsername())){
            return null;
        }
        return votes.get(partner.getUsername());
    }

    public boolean checkVotedTime(Partner partner, Date date){
        return date.before(getVotedTime(partner.getUsername()));
    }

    public void addPartnerVote(Partner partner, Date date){
        votes.put(partner.getUsername(), date);
        Main.plugin.getUserStorage().save(this, true);
    }

    public void removePartnerVote(Partner partner){
        votes.remove(partner.getUsername());
        Main.plugin.getUserStorage().save(this, true);
    }
}
