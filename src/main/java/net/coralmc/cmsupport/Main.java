package net.coralmc.cmsupport;

import net.coralmc.cmsupport.commands.SupportCMD;
import net.coralmc.cmsupport.hooks.PlaceholderAPIHook;
import net.coralmc.cmsupport.languages.LBase;
import net.coralmc.cmsupport.storage.PartnerStorage;
import net.coralmc.cmsupport.storage.UserStorage;
import xyz.theprogramsrc.supercoreapi.global.files.yml.YMLConfig;
import xyz.theprogramsrc.supercoreapi.global.storage.DataBase;
import xyz.theprogramsrc.supercoreapi.global.storage.DataBaseSettings;
import xyz.theprogramsrc.supercoreapi.global.storage.MySQLDataBase;
import xyz.theprogramsrc.supercoreapi.global.storage.SQLiteDataBase;
import xyz.theprogramsrc.supercoreapi.global.utils.ServerUtils;
import xyz.theprogramsrc.supercoreapi.global.utils.Utils;
import xyz.theprogramsrc.supercoreapi.spigot.SpigotPlugin;

import java.io.File;

public final class Main extends SpigotPlugin {
    private DataBase dataBase;
    private YMLConfig menuYML;
    public static Main plugin;
    public ServerUtils serverUtils;
    public UserStorage userStorage;
    public PartnerStorage partnerStorage;

    @Override
    public void onPluginLoad() {
        plugin = this;
    }

    @Override
    public void onPluginEnable() {
        try {
            registerTranslation(LBase.class);
            serverUtils = new ServerUtils();
            log("Server Utils loaded");
            setupSettings();
            if (isEmergencyStop()){
                return;
            }

            userStorage = new UserStorage(this, dataBase);
            partnerStorage = new PartnerStorage(this, dataBase);
            this.log("Loaded User and Partner Storage");

            this.debug("Registering commands...");
            new SupportCMD();
            this.log("Registered '/" + getSettingsStorage().getConfig().getString("Settings.SupportCommand").toLowerCase() + "' command");

            boolean papi = this.getSuperUtils().isPlugin("PlaceholderAPI");

            if(papi){
                new PlaceholderAPIHook().register();
                this.log("&aPlaceholderAPI Hook Registered.");
            }

        } catch (Exception e) {
            addError(e);
            e.printStackTrace();
        }
    }

    public boolean isSQLite(){
        return this.dataBase instanceof SQLiteDataBase;
    }

    public ServerUtils getServerUtils() {
        return serverUtils;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public PartnerStorage getPartnerStorage() {
        return partnerStorage;
    }

    public YMLConfig getMenuYML() {
        return menuYML;
    }

    private void setupSettings(){

        File file = new File(getDataFolder(), "menu.yml");
        if (!file.exists()) {
            saveResource("menu.yml", false);
        }
        menuYML = new YMLConfig(file);

        final YMLConfig cfg = this.getSettingsStorage().getConfig();
        if(!cfg.contains("MySQL.Enabled") || !cfg.contains("MySQL.Host") || !cfg.contains("MySQL.Port") || !cfg.contains("MySQL.DataBase") || !cfg.contains("MySQL.UserName") || !cfg.contains("MySQL.Password") || !cfg.contains("MySQL.UseSSL")){
            if(!cfg.contains("MySQL.Enabled")) cfg.add("MySQL.Enabled", false);
            if(!cfg.contains("MySQL.Host")) cfg.add("MySQL.Host", "sql.example.com");
            if(!cfg.contains("MySQL.Port")) cfg.add("MySQL.Port", "3306");
            if(!cfg.contains("MySQL.DataBase")) cfg.add("MySQL.DataBase", "cmsupport");
            if(!cfg.contains("MySQL.UserName")) cfg.add("MySQL.UserName", "cmsupport");
            if(!cfg.contains("MySQL.Password")) cfg.add("MySQL.Password", Utils.randomPassword(16));
            if(!cfg.contains("MySQL.UseSSL")) cfg.add("MySQL.UseSSL", false);
            if(!cfg.contains("Settings.SupportCommand")) cfg.add("Settings.SupportCommand", "support");
            if(this.isFirstStart()){
                this.log("&cPlease fill in the MySQL Settings. If you're going to use SQLite you can ignore this message.");
                this.log("&9Information: If the path 'MySQL.Password' doesn't exists, the plugin will generate a random password with 16 characters length");
            }
        }


        if(cfg.getBoolean("MySQL.Enabled")){
            if(cfg.getString("MySQL.Host").equals("sql.example.com")){
                this.log("&cPlease fill in the MySQL Host!");
                this.emergencyStop();
                return;
            }
        }

        if(this.isEmergencyStop()) return;

        if(cfg.getBoolean("MySQL.Enabled")){
            this.dataBase = new MySQLDataBase(this) {
                @Override
                public DataBaseSettings getDataBaseSettings() {
                    return new DataBaseSettings() {
                        @Override
                        public String host() {
                            return cfg.getString("MySQL.Host");
                        }

                        @Override
                        public String port() {
                            return cfg.getString("MySQL.Port");
                        }

                        @Override
                        public String database() {
                            return cfg.getString("MySQL.DataBase");
                        }

                        @Override
                        public String username() {
                            return cfg.getString("MySQL.UserName");
                        }

                        @Override
                        public String password() {
                            return cfg.getString("MySQL.Password");
                        }

                        @Override
                        public boolean useSSL() {
                            return cfg.getBoolean("MySQL.UseSSL");
                        }
                    };
                }
            };
        }else{
            this.dataBase = new SQLiteDataBase(this) {
                @Override
                public DataBaseSettings getDataBaseSettings() {
                    return null;
                }
            };
        }
    }

    @Override
    public void onPluginDisable() {
        dataBase = null;
    }
}
