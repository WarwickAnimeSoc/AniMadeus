package net.mrporky.anisoc.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.mrporky.anisoc.Command;
import net.mrporky.anisoc.util.RestReturn;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;

public class Events implements Command {

    /**
     * Returns the status from if the command was called correctly
     * @param args
     * @param event
     * @return
     */
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    /**
     * Displays the events of the society onto the server
     * <p>Will access the animesoc site via a REST call to the endpoint animesoc.co.uk/api/events
     * Returns as a JSONArray. This method will convert the array into a list that is ordered from the
     * current date onwards up to a max of 4 events as not to overload the discord server.
     * This will then be formatted and printed out in a MessageBuilder format</p>
     * @param args
     * @param event
     */
    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        RestReturn restReturn = new RestReturn();
        JSONArray events = new JSONArray();
        try {
            events = restReturn.getRest("https://animesoc.co.uk/api/events/?format=json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinkedList<JSONObject> objectsToShow = new LinkedList<>();

        for (Object eventItem : events) {
            JSONObject obj = (JSONObject) eventItem;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = null;
            try {
                parsedDate = dateFormat.parse(String.valueOf(obj.get("when")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert parsedDate != null;
            if (((parsedDate.getTime() + (86400 * 1000)) > (System.currentTimeMillis()))) {
                objectsToShow.addFirst(obj);
            }
        }

        if (objectsToShow.size() > 0) {
            EmbedBuilder status_builder = new EmbedBuilder();
            status_builder.setColor(event.getMember().getColor());

            status_builder.setTimestamp(Instant.now());
            status_builder.setTitle("Events", "https://animesoc.co.uk/events/1/");
            status_builder.setFooter("A full event history can be found at" +
                    " https://animesoc.co.uk/events/1/", null);

            if(objectsToShow.size() > 4){
                status_builder.setDescription("Found " + objectsToShow.size() + " events, but due to space, " +
                        "only showing the top 4! Check the website for the full list!");
            }

            for (int i = 0; i < (Math.min(objectsToShow.size(), 4)); i++) {
                JSONObject item = objectsToShow.get(i);
                String details = String.valueOf(item.get("details")).replaceAll("\\<[^>]*>","");
                if(details.length() > 1000){
                    details = details.substring(0, 800);
                    details += "...";
                }
                status_builder.addField(String.valueOf(item.get("title")),
                        "https://animesoc.co.uk/events/event_detail/"
                                + item.get("id") +"\n" + details, true);
            }


            MessageEmbed embed = status_builder.build();
            MessageBuilder mbuilder = new MessageBuilder();
            mbuilder.setEmbed(embed);

            Message message = mbuilder.build();
            event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ", I found the following events:").queue();
            event.getChannel().sendMessage(message).queue();
        }else{
            event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ", Sorry, I do not see any events coming up soon. You can see all events at https://animesoc.co.uk/events/1/").queue();
        }
    }

    @Override
    public void help(MessageReceivedEvent event) {

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
