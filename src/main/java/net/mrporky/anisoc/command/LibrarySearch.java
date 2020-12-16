package net.mrporky.anisoc.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.mrporky.anisoc.Command;
import net.mrporky.anisoc.util.RestReturn;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;

public class LibrarySearch implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        RestReturn restReturn = new RestReturn();
        JSONArray jsonArray = null;
        try {
            jsonArray = restReturn.getRest("https://animesoc.co.uk/api/library/?format=json&search="
                    + URLEncoder.encode(String.join(" ", args), "UTF-8"));
            assert jsonArray != null;
            if (jsonArray.length() > 0) {
                EmbedBuilder status_builder = new EmbedBuilder();
                status_builder.setColor(event.getMember().getColor());

                status_builder.setTimestamp(Instant.now());
                status_builder.setTitle("Library results", "https://animesoc.co.uk/library/1/?category=all&query="
                        + URLEncoder.encode(String.join(" ", args), "UTF-8"));
                status_builder.setFooter("A full library can be viewed at https://animesoc.co.uk/library",
                        null);

                if (jsonArray.length() > 4) {
                    status_builder.setDescription("Found " + jsonArray.length() + " entries, but due to space, " +
                            "only showing the top 4! Check the website for the full list!");
                }

                for (int i = 0; i < (Math.min(jsonArray.length(), 4)); i++) {
                    JSONObject item = (JSONObject) jsonArray.get(i);
                    String synopsis = String.valueOf(item.get("synopsis")).replaceAll("\\<[^>]*>", "");
                    if (synopsis.length() > 1000) {
                        synopsis = synopsis.substring(0, 800);
                        synopsis += "...";
                    }
                    status_builder.addField(String.valueOf(item.get("title_english")),
                            "https://animesoc.co.uk/library/details/" + item.get("id") + "\n"
                                    + synopsis, true);
                }


                MessageEmbed embed = status_builder.build();
                MessageBuilder mbuilder = new MessageBuilder();
                mbuilder.setEmbed(embed);

                Message message = mbuilder.build();
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ", I found the following " +
                        "entries in the library:").queue();
                event.getChannel().sendMessage(message).queue();
            } else {
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ", Sorry, I could not find" +
                        " that series. You can double-check on https://animesoc.co.uk/library or ask an exec to put in" +
                        " a request").queue();
            }
        } catch (
                JSONException e) {
            event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ", Sorry, there's been a problem with my ghost. I could not find an internal value!").queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void help(MessageReceivedEvent event) {

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
