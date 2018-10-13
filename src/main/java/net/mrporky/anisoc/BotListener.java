package net.mrporky.anisoc;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/*
    Listens for input from the client and validates some basic functionality, i.e. making
    sure that the client is an acceptable user (not a bot), and that the client is accessing
    the bot from within a public channel (i.e. a guild channel)
 */

public class BotListener extends ListenerAdapter{

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(!event.getAuthor().isBot() && event.getMessage().getContentRaw().charAt(0) == '!'){
            if(event.getChannelType().isGuild()) {
                Main.parseCommand(Main.parser.parse(event.getMessage().getContentRaw().toLowerCase(), event));
            }
        }
    }
}