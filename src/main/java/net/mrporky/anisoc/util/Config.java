package net.mrporky.anisoc.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    This class loads the config into memory and allows for us to access the keys
     / secret values without having them written into the source code.
 */
public class Config {
    private final static Logger LOGGER = Logger.getLogger(Config.class.getName());

    // String / Hashmap to store keys / values
    private String filename;
    private HashMap<String, String> values = new HashMap<>();


    // Basic constructor
    public Config(String filename){
        this.filename = filename;
        loadConfig();
    }

    public HashMap<String, String> getConfigRaw(){
        return values;
    }

    private void loadConfig(){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            // While we have another command that can be parsed
            while(line != null){
                // Split on spaces (will split on multiple)
                String[] splitPair = line.split("\\s+");
                values.put(splitPair[0], splitPair[1]);
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Config file not found! Any settings will not be applied!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "Successfully loading the configuration settings");
    }

    // Get the values from the hash-map
    public String getValue(String key){
        return values.get(key);
    }
}
