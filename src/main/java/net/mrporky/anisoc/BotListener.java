package net.mrporky.anisoc;

/*
    Listens for input from the client and validates some basic functionality, i.e. making
    sure that the client is an acceptable user (not a bot), and that the client is accessing
    the bot from within a public channel (i.e. a guild channel)
 */

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class BotListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && event.getMessage().getContentRaw().charAt(0) == '!') {
            if (event.getChannelType().isGuild()) {
                Main.parseCommand(Main.parser.parse(event.getMessage().getContentRaw().toLowerCase(), event));
            }
        }

        if (!event.getAuthor().isBot() && event.getChannelType().isGuild()) {
            // This is only for non-commands that are written without the !
            Main.parseString(Main.parser.parse(event.getMessage().getContentRaw().toLowerCase(), event));
        }

    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        User user = event.retrieveUser().complete();
        if (!user.isBot()) {
            Main.parseReactionEvent(event);
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        User user = event.retrieveUser().complete();
        if (!user.isBot()) {
            Main.parseReactionRemoveEvent(event);
        }
    }
}