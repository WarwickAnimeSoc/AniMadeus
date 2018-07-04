package net.mrporky.anisoc.util;

/*
    TODO: Redo functionality to reduce the amount of things that need to go through here and re-route into Config
    This class is mainly used as a middleman for accessing the config at this point.
    Will be used in later point for other loading functions, but for now, it is pretty empty.
 */
public class BotLoader {
    private boolean isLoaded = false;
    private String configPath;

    public static Config config;

    public BotLoader(String config){
        this.configPath = config;
        loadBot();
    }

    private boolean loadBot(){
        if(!isLoaded){
            // Load into memory the specific file names that are expected:
            config = new Config(configPath);
            return true;
        }

        return false;
    }

    public Config getConfig(){
        return config;
    }
}
