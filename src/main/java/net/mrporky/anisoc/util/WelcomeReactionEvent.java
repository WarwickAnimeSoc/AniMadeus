package net.mrporky.anisoc.util;

import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.mrporky.anisoc.Main;
import net.mrporky.anisoc.Reactions;

import javax.management.relation.RoleNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WelcomeReactionEvent implements Reactions {
    private static Config config = new Config("config.txt");
    @Override
    public void onReaction(MessageReactionAddEvent event) {
        Iterator it = config.getConfigRaw().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String[] channel = pair.getKey().toString().split(":");
            String[] reactions = pair.getValue().toString().split(":");
            if (channel[0].equals("CHANNEL") && reactions[1].equals(event.getChannel().getName())){
                if (reactions[0].equals(event.getReactionEmote().getName())){
                    System.out.println("Adding role");
                    Role role = event.getGuild().getRolesByName(channel[1], true).get(0);
                    GuildController controller = new GuildController(event.getGuild());
                    controller.addRolesToMember(event.getMember(), role).queue();
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        // TODO Remove this is tmp fix
        config = new Config("config.txt");
    }

    @Override
    public void onReactionRemove(MessageReactionRemoveEvent event) {
        Iterator it = config.getConfigRaw().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String[] channel = pair.getKey().toString().split(":");
            String[] reactions = pair.getValue().toString().split(":");
            if (channel[0].equals("CHANNEL") && reactions[1].equals(event.getChannel().getName())){
                if (reactions[0].equals(event.getReactionEmote().getName())){
                    System.out.println("Adding role");
                    Role role = event.getGuild().getRolesByName(channel[1], true).get(0);
                    GuildController controller = new GuildController(event.getGuild());
                    controller.removeRolesFromMember(event.getMember(), role).queue();
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        // TODO Remove this is tmp fix
        config = new Config("config.txt");
    }
}
