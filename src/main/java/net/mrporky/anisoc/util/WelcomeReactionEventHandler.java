package net.mrporky.anisoc.util;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.managers.GuildController;
import net.mrporky.anisoc.Reactions;

import java.util.List;

public class WelcomeReactionEventHandler implements Reactions {

    private final BotLoader loader = new BotLoader("config.json");
    private final ConfigData configData = loader.getConfigData();

    @Override
    public void onReaction(MessageReactionAddEvent event) {
        // Get event info initially for neatness
        Long eventChannel = event.getChannel().getIdLong();
        String eventReact = event.getReactionEmote().getName();

        // Uncomment this for figuring out exact internal representation of reactions
        // System.out.println(eventReact);

        // Search through channels to find roleReactPairs for the channel the react occurred in
        for (Channel channel : configData.getChannels()) {
            if (channel.getChannelId().equals(eventChannel)) {
                List<RoleReactPair> roleReactPairs = channel.getRoleReactPairs();
                // Do the same as above to find the role associated with the reaction
                for (RoleReactPair rrp : roleReactPairs) {
                    if (rrp.getReact().equals(eventReact)) {
                        System.out.println("Adding role");
                        // Gets correct role based on name and assigns it to the user who reacted
                        try {
                            Role role = event.getGuild().getRolesByName(rrp.getRole(), true).get(0);
                            GuildController controller = new GuildController(event.getGuild());
                            controller.addRolesToMember(event.getMember(), role).queue();
                        } catch (IndexOutOfBoundsException e) {
                            // In case role does not exist
                            System.out.println("Specified role does not exist in server");
                            e.printStackTrace();
                        } catch (HierarchyException e) {
                            // Flags role hierarchy problems to console
                            System.out.println("Specified role must be lower in role list than bot role");
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onReactionRemove(MessageReactionRemoveEvent event) {
        // Get event info initially for neatness
        Long eventChannel = event.getChannel().getIdLong();
        String eventReact = event.getReactionEmote().getName();
        // Search through channels to find roleReactPairs for the channel the react occurred in
        for (Channel channel : configData.getChannels()) {
            if (channel.getChannelId().equals(eventChannel)) {
                List<RoleReactPair> roleReactPairs = channel.getRoleReactPairs();
                // Do the same as above to find the role associated with the reaction
                for (RoleReactPair rrp : roleReactPairs) {
                    if (rrp.getReact().equals(eventReact)) {
                        System.out.println("Removing role");
                        // Gets correct role based on name and removes it from the user who reacted
                        try {
                            Role role = event.getGuild().getRolesByName(rrp.getRole(), true).get(0);
                            GuildController controller = new GuildController(event.getGuild());
                            controller.removeRolesFromMember(event.getMember(), role).queue();
                        } catch (IndexOutOfBoundsException e) {
                            // In case role does not exist
                            System.out.println("Specified role does not exist in server");
                            e.printStackTrace();
                        } catch (HierarchyException e) {
                            // Flags role hierarchy problems to console
                            System.out.println("Specified role must be lower in role list than bot role");
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }
}
