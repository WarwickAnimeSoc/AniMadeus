/*
                ======================================================================================================================================================================
                ======================================================================================================================================================================
                ====                                                                                                                                                              ====
                ====                                       University of Warwick Students Union Anime and Manga Society Discord Bot                                               ====
                ====                                                                                                                                                              ====
                ====                                       This bot is designed by MrPorky, aided by Sorc278's and jai_'s prior wo-                                               ====
                ====                                       rks to modernise the societies website and internet based applications.                                                ====
                ====                                                                                                                                                              ====
                ====                                       This bot is licenced under MIT so feel free to make any modifications t-                                               ====
                ====                                       hat you see fit providing that the conditions of the MIT licence are met                                               ====
                ====                                                                                                                                                              ====
                ======================================================================================================================================================================
                ======================================================================================================================================================================


                The purpose of this class is to manage the subclasses and control the various different services
                that the bot runs to allow for it work. It should be noted that all configuration of the bot is
                stored within the root directory in the config files, and any changes to API keys as well as to
                direct database accesses should be done through this file rather than via direct code access.
 */

package net.mrporky.anisoc;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.mrporky.anisoc.command.*;
import net.mrporky.anisoc.util.BotLoader;
import net.mrporky.anisoc.util.SchedulerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

    // Define the variables that will be used throughout this main scope
    private static String key = "";
    public static JDA jda;
    static final CommandParser parser = new CommandParser();

    // Hash-map containing all of the possible commands and their corresponding interface
    private static HashMap<String, Command> commands = new HashMap<>();

    public Main(){
        // Define the keys and load the bot
        BotLoader loader = new BotLoader("config.txt");
        key = BotLoader.config.getValue("DISCORD-KEY");
        // Begin to load in JDA and connect to the Discord API
        try{
            jda = new JDABuilder(AccountType.BOT).setToken(key).buildBlocking();
            jda.addEventListener(new BotListener());
            jda.setAutoReconnect(true);

            // Begin the Scheduler thread running the Service class instance
            new Thread(SchedulerService::new).start();
        }catch(Exception e){
            e.printStackTrace();
        }

        // General Commands
        commands.put("member", new MemberCreate());
        commands.put("events", new Events());
        commands.put("library", new LibrarySearch());

    }

    /**
     * Parses a command to the relevant handler of each of the command classes
     * <p>Will handle the command that is parsed to it via the BotListener, and will verify that the inputted command is
     * safe.</p>
     * @return void
     * @param cmd
     */
    public static void parseCommand(CommandParser.CommandContainer cmd){
        if(commands.containsKey(cmd.invoke)){
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
            if(new ArrayList(Arrays.asList(cmd.args)).contains("--help")){
                commands.get(cmd.invoke).help(cmd.event);
            }else if(safe && !new ArrayList(Arrays.asList(cmd.args)).contains("--help")){
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(true, cmd.event);
            } else{
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        }
    }

}