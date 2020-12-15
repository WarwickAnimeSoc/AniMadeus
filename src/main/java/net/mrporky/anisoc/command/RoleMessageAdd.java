package net.mrporky.anisoc.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.mrporky.anisoc.Command;

public class RoleMessageAdd implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

    }

    @Override
    public void help(MessageReceivedEvent event) {

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
