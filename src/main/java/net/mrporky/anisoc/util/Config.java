package net.mrporky.anisoc.util;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
    This class loads the config into memory and allows for us to access the keys
     / secret values without having them written into the source code.
     Note: This has been updated so the data is held in the ConfigData object, for use with JSON config files
     Note: This has again been updated to follow the Singleton pattern, so that configs are only loaded once
        Access the config through the getInstance() method
 */
public class Config {
    private final static Logger LOGGER = Logger.getLogger(Config.class.getName());

    // instance is part of the Singleton pattern
    private static Config instance = null;
    private final String filename;
    private ConfigData configData;

    // Constructor is private to avoid any other code creating Config objects
    private Config(String filename){
        this.filename = filename;
        loadConfig();
    }

    // Loads config if not already loaded
    public static Config getInstance(String configPath) {
        if (instance == null) {
            instance = new Config(configPath);
        }
        return instance;
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config("config.json");
        }
        return instance;
    }

    private void loadConfig(){
        try {
            // Jackson will read the JSON file using the ObjectMapper, and fills in the class structure in ConfigData
            File file = new File(this.filename);
            ObjectMapper objectMapper = new ObjectMapper();
            this.configData = objectMapper.readValue(file, ConfigData.class);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Config file not found! Any settings will not be applied!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "Successfully loading the configuration settings");
    }

    // Get the config data for further interrogation
    public ConfigData getConfigData() {
        return configData;
    }
}
