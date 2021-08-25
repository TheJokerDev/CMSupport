package net.coralmc.cmsupport.storage;

import net.coralmc.cmsupport.languages.LBase;
import xyz.theprogramsrc.supercoreapi.SuperPlugin;
import xyz.theprogramsrc.supercoreapi.global.storage.DataBase;
import xyz.theprogramsrc.supercoreapi.global.storage.DataBaseStorage;
import xyz.theprogramsrc.supercoreapi.global.utils.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class UserStorage extends DataBaseStorage {
    public static HashMap<String, User> cache;
    private final String table;

    public UserStorage(SuperPlugin<?> plugin, DataBase dataBase) {
        super(plugin, dataBase);

        cache = new HashMap<>();
        table = getTablePrefix()+"users";
        preloadTables();
    }

    public SuperPlugin<?> getPlugin() {
        return plugin;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public User saveAndGet(User user){
        this.save(user, false);
        return this.get(user.getUsername());
    }

    public void save(User user){
        this.save(user, true);
    }

    public void save(final User user, boolean async){
        Runnable runnable = () -> {
            cache.remove(user.getUsername());
            this.dataBase.connect(c->{
                try{
                    saveUser(user, c);
                }catch (SQLException ex){
                    this.plugin.addError(ex);
                    this.plugin.log("&c" + LBase.ERROR_WHILE_SAVING_USER_DATA.options().vars(user.getUsername()).placeholder("{UserName}", user.getUsername()).toString());
                    ex.printStackTrace();
                }
            });
        };
        if(async){
            new Thread(runnable).start();
        }else{
            runnable.run();
        }
    }

    public void resetVotesOfPartner(Partner partner){
        Runnable runnable = () -> {
            User[] users = requestUsers(true);
            for (User u : users){
                if (u.getVotes() == null){
                    continue;
                }
                if (!u.getVotes().contains(partner.getUsername())){
                    continue;
                }
                u.removePartnerVote(partner);
            }
        };

        new Thread(runnable).start();
    }

    public void saveUser(User user, Connection c) throws SQLException{
        String username = user.getUsername();
        String votes = user.getVotes();
        Statement s = c.createStatement();
        if(!this.exists(username)){
            s.executeUpdate("INSERT INTO " + this.table + " (user_name, votes) VALUES ('"+username+"', '"+votes+"');");
        }else{
            s.executeUpdate("UPDATE " + this.table + " SET votes='"+votes+"';");
        }
        s.close();
    }

    public User get(String username){
        return get(username, false);
    }

    public User get(final String username, boolean override_cache){
        if(!override_cache){
            if(cache.containsKey(username)){
                return cache.get(username);
            }
        }

        final AtomicReference<User> result = new AtomicReference<>(null);
        this.dataBase.connect(c->{
            try{
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM " + this.table + " WHERE user_name='"+username+"';");
                if(rs.next()){
                    String votes = rs.getString("votes");
                    User user = new User(username)
                            .loadVotes(votes);
                    result.set(user);
                }
                rs.close();
                s.close();
            }catch (SQLException ex){
                this.plugin.addError(ex);
                this.plugin.log("&c" + LBase.ERROR_ON_DATA_REQUEST);
                ex.printStackTrace();
            }
        });
        if(result.get() != null) cache.put(username, result.get());
        return result.get();
    }

    public User[] requestUsers(){
        return requestUsers(false);
    }

    public User[] requestUsers(boolean overrideCache){
        if(!overrideCache){
            return cache.values().stream().filter(Utils::nonNull).toArray(User[]::new);
        }else{
            List<User> users = new ArrayList<>();
            this.dataBase.connect(c->{
                try{
                    Statement s = c.createStatement();
                    ResultSet rs = s.executeQuery("SELECT * FROM " + this.table + ";");
                    while(rs.next()){
                        String username = rs.getString("user_name");
                        cache.remove(username);
                        String votes = rs.getString("votes");
                        User user = new User(username)
                                .loadVotes(votes);
                        cache.put(username, user);
                        users.add(user);
                    }
                    rs.close();
                    s.close();
                }catch (Exception ex){
                    this.plugin.addError(ex);
                    this.plugin.log("&c" + LBase.ERROR_ON_DATA_REQUEST);
                    ex.printStackTrace();
                }
            });
            if(!users.isEmpty()){
                User[] array = new User[users.size()];
                array = users.toArray(array);
                return array;
            }
        }
        return new User[0];
    }

    public boolean exists(String username){
        AtomicBoolean exists = new AtomicBoolean(false);
        this.dataBase.connect(c->{
            try{
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM " + this.table + " WHERE user_name='"+username+"';");
                exists.set(rs.next());
                rs.close();
                s.close();
            }catch (Exception ex){
                this.plugin.addError(ex);
                this.plugin.log("&c" + LBase.ERROR_ON_DATA_REQUEST);
                ex.printStackTrace();
            }
        });
        return exists.get();
    }

    private void preloadTables(){
        new Thread(() -> this.dataBase.connect(c->{
            try{
                Statement s = c.createStatement();
                s.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.table + " (user_name VARCHAR(100), votes MEDIUMTEXT);");
                s.close();
            }catch (SQLException ex){
                this.plugin.addError(ex);
                this.plugin.log("&c" + LBase.ERROR_WHILE_CREATING_TABLES);
                ex.printStackTrace();
            }
        })).start();
    }

    public User getRandomUser(){
        User[] users = this.requestUsers();
        int rand = Utils.random(0, users.length);
        return users[rand];
    }

    public void remove(User user) {
        new Thread(()-> this.dataBase.connect(c->{
            try{
                Statement s = c.createStatement();
                s.executeUpdate("DELETE FROM " + this.table + " WHERE user_name='"+user.getUsername()+"'");
                s.close();
                cache.remove(user.getUsername());
            }catch (SQLException ex){
                this.plugin.addError(ex);
                this.plugin.log("&c" + LBase.ERROR_WHILE_DELETING_USER);
                ex.printStackTrace();
            }
        })).start();
    }

    public void removeCache(String username) {
        cache.remove(username);
    }
}
