package net.mrporky.anisoc.util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.mrporky.anisoc.Reactions;

import java.util.List;
import java.util.Objects;

public class WelcomeReactionEventHandler implements Reactions {

    private final ConfigData configData = Config.getInstance().getConfigData();

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
                        System.out.println("Adding role " + rrp.getRole() + " to " + event.getUser().getName());
                        // Gets correct role based on name and assigns it to the user who reacted
                        try {
                            Member member = event.retrieveMember().complete();
                            Role role = event.getGuild().getRolesByName(rrp.getRole(), true).get(0);
                            event.getGuild().addRoleToMember(member, role).queue();
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
                            Member member = event.retrieveMember().complete();
                            Role role = event.getGuild().getRolesByName(rrp.getRole(), true).get(0);
                            event.getGuild().removeRoleFromMember(member, role).queue();
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
