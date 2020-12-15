package net.mrporky.anisoc.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.*;
import java.util.*;

public class CommandPermission {

    public static boolean hasPermission(String requiredRole, MessageReceivedEvent event) throws NoRoleFoundException {
        Member caller = event.getGuild().getMember(event.getAuthor());
        List roles = caller.getRoles();
        List<Role> role = event.getGuild().getRolesByName(requiredRole, true);

        return roles.contains(role.get(0)) && event.getGuild() != null;

    }

    public static boolean isAdmin(MessageReceivedEvent event) {
        return event.getMember().getPermissions().contains(Permission.ADMINISTRATOR);
    }

    public static boolean isOwner(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(event.getGuild().getOwner().getUser().getId())) {
            return true;
        } else {
            event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " - You do not have permission to change this! Required permission: PERMISSION_OWNER").queue();
            return false;
        }

    }
}

