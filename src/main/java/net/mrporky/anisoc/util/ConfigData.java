package net.mrporky.anisoc.util;

import java.util.List;

// Data class for JSON deserialisation of config file
// Setters unused, but needed for Jackson JSON mapping

public class ConfigData {
    private String discordKey;
    private String suAPI;
    private String dbHostName;
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    private List<Channel> channels;

    public String getDiscordKey() {
        return discordKey;
    }

    public void setDiscordKey(String discordKey) {
        this.discordKey = discordKey;
    }

    public String getSuAPI() {
        return suAPI;
    }

    public void setSuAPI(String suAPI) {
        this.suAPI = suAPI;
    }

    public String getDbHostName() {
        return dbHostName;
    }

    public void setDbHostName(String dbHostName) {
        this.dbHostName = dbHostName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
}
