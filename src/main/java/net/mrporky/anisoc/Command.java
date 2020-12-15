package net.mrporky.anisoc;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/*
    Interface defining the standard layout for each of the classes that manages a individual command
 */
public interface Command {
    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event);
    void help(MessageReceivedEvent event);
    void executed(boolean success, MessageReceivedEvent event);
}
