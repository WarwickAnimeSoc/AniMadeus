package net.mrporky.anisoc.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

/*
    Parses the command to the command handler in a format such that it can be reused
 */
public class CommandParser {
    public CommandContainer parse(String msg, MessageReceivedEvent event){
        ArrayList<String> split = new ArrayList<>();
        String beheaded = msg.replaceFirst("!", "");
        String[] splitBeheaded = beheaded.split(" ");
        split.addAll(Arrays.asList(splitBeheaded));
        String invoke = split.get(0);
        String [] args = new String[split.size() -1];
        split.subList(1, split.size()).toArray(args);

        return new CommandContainer(msg, beheaded, splitBeheaded, invoke, args, event);
    }

    public class CommandContainer{
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;

        public CommandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[]args, MessageReceivedEvent e){
            this.raw = rw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = e;
        }
    }
}
