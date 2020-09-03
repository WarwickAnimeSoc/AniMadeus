package net.mrporky.anisoc.util;

/*
    TODO: Redo functionality to reduce the amount of things that need to go through here and re-route into Config
    This class is mainly used as a middleman for accessing the config at this point.
    Will be used in later point for other loading functions, but for now, it is pretty empty.
    Note: Config can be accessed either through the BotLoader or directly from the singleton Config class instance,
        routing through the bot is now optional (but functionally the same)
 */
public class BotLoader {
    private final boolean isLoaded = false;
    private final String configPath;

    public static Config config;

    public BotLoader(String config){
        this.configPath = config;
        loadBot();
    }

    private boolean loadBot(){
        if(!isLoaded){
            // Gets the config instance, or creates it if not present already
            config = Config.getInstance(configPath);
            return true;
        }

        return false;
    }

    public ConfigData getConfigData(){
        return config.getConfigData();
    }
}
