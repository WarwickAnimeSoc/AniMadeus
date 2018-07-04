package net.mrporky.anisoc.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.mrporky.anisoc.Command;
import net.mrporky.anisoc.util.BotLoader;
import net.mrporky.anisoc.util.SQLQuery;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.LinkedList;

/*
    List of events that are currently going to occur
    INCOMPLETE - Replacing with RestAPI from the site
    TODO: Do above
 */
public class Events implements Command {

    private String hostname = BotLoader.config.getValue("DBHOSTNAME");
    private String database = BotLoader.config.getValue("DBNAME");
    private String username = BotLoader.config.getValue("DBUSERNAME");
    private String password = BotLoader.config.getValue("DBPASSWORD");

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    /*
     TODO: Replace this with API accessor rather than direct database manipulation
     */

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        // Load the selection of all the possible events
        SQLQuery query = new SQLQuery(hostname, database, username, password, 3306);
        ResultSet rs = query.query("SELECT * FROM events_event", new LinkedList<>());

        //System.out.println(rs.getArray("title").toString());


        EmbedBuilder status_builder = new EmbedBuilder();
        status_builder.setColor(event.getMember().getColor());

        status_builder.setTimestamp(Instant.now());
        status_builder.setTitle("Events", "https://animesoc.co.uk/events/1/");
        status_builder.setFooter("GITHUBLINK", null);
        for(int i = 0; i < 4; i++) {
            status_builder.addField("Name:", event.getJDA().getSelfUser().getName() + " (ID: " + event.getJDA().getSelfUser().getId() + ")", true);
        }

        MessageEmbed embed = status_builder.build();
        MessageBuilder mbuilder = new MessageBuilder();
        mbuilder.setEmbed(embed);

        Message message = mbuilder.build();
        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ", I found the following events:").queue();
        event.getChannel().sendMessage(message).queue();

    }

    @Override
    public void help(MessageReceivedEvent event) {

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
