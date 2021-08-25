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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PartnerStorage extends DataBaseStorage {
    public static HashMap<String, Partner> cache;
    private final String table;

    public Partner[] getPartners(){
        return cache.values().toArray(new Partner[0]);
    }

    public Partner getPartner(String name){
        return this.get(name);
    }

    public PartnerStorage(SuperPlugin<?> plugin, DataBase dataBase) {
        super(plugin, dataBase);

        cache = new HashMap<>();
        table = getTablePrefix()+"partners";
        preloadTables();
    }


    public SuperPlugin<?> getPlugin() {
        return plugin;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public Partner saveAndGet(Partner user){
        this.save(user, false);
        return this.get(user.getUsername());
    }

    public void save(Partner user){
        this.save(user, true);
    }

    public void save(final Partner user, boolean async){
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

    public void createUser(Partner user){
        Runnable runnable = () -> {
            cache.remove(user.getUsername());
            this.dataBase.connect(c->{
                try{
                    saveUser(user, c);
                    cache.put(user.getUsername(), user);
                }catch (SQLException ex){
                    this.plugin.addError(ex);
                    this.plugin.log("&c" + LBase.ERROR_WHILE_SAVING_USER_DATA.options().vars(user.getUsername()).placeholder("{UserName}", user.getUsername()).toString());
                    ex.printStackTrace();
                }
            });
        };
        new Thread(runnable).start();
    }

    public void saveUser(Partner user, Connection c) throws SQLException{
        String username = user.getUsername();
        int votes = user.getVotes();
        Statement s = c.createStatement();
        if(!this.exists(username)){
            s.executeUpdate("INSERT INTO " + this.table + " (user_name, votes) VALUES ('"+username+"', '"+votes+"');");
        }else{
            s.executeUpdate("UPDATE " + this.table + " SET votes='"+votes+"';");
        }
        s.close();
    }

    public Partner get(String username){
        return get(username, false);
    }

    public Partner get(final String username, boolean override_cache){
        if(!override_cache){
            if(cache.containsKey(username)){
                return cache.get(username);
            }
        }

        final AtomicReference<Partner> result = new AtomicReference<>(null);
        this.dataBase.connect(c->{
            try{
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM " + this.table + " WHERE user_name='"+username+"';");
                if(rs.next()){
                    int votes = rs.getInt("votes");
                    Partner user = new Partner(username)
                            .setVotes(votes)
                            .setLastUpdate(new Date());
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

    public Partner[] requestPartners(){
        return requestPartners(false);
    }

    public Partner[] requestPartners(boolean overrideCache){
        if(!overrideCache){
            return cache.values().stream().filter(Utils::nonNull).toArray(Partner[]::new);
        }else{
            List<Partner> users = new ArrayList<>();
            this.dataBase.connect(c->{
                try{
                    Statement s = c.createStatement();
                    ResultSet rs = s.executeQuery("SELECT * FROM " + this.table + ";");
                    while(rs.next()){
                        String username = rs.getString("user_name");
                        cache.remove(username);
                        int votes = rs.getInt("votes");
                        Partner user = new Partner(username)
                                .setVotes(votes)
                                .setLastUpdate(new Date());
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
                Partner[] array = new Partner[users.size()];
                array = users.toArray(array);
                return array;
            }
        }
        return new Partner[0];
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
                s.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.table + " (user_name VARCHAR(100), votes INT, last_update MEDIUMTEXT);");
                s.close();
            }catch (SQLException ex){
                this.plugin.addError(ex);
                this.plugin.log("&c" + LBase.ERROR_WHILE_CREATING_TABLES);
                ex.printStackTrace();
            }
        })).start();
    }

    public Partner getRandomPartner(){
        Partner[] users = this.requestPartners();
        int rand = Utils.random(0, users.length);
        return users[rand];
    }

    public void remove(Partner user) {
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
