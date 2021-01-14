package net.mrporky.anisoc.command;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.mrporky.anisoc.Command;
import net.mrporky.anisoc.members.Member;
import net.mrporky.anisoc.members.MemberNotFoundException;
import net.mrporky.anisoc.members.Members;
import net.mrporky.anisoc.util.Config;
import net.mrporky.anisoc.util.ConfigData;
import net.mrporky.anisoc.util.RestReturn;
import net.mrporky.anisoc.util.SQLQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberCreate implements Command {

    // Grab values from config instance directly
    private final ConfigData configData = Config.getInstance().getConfigData();
    private final String hostname = configData.getDbHostName();
    private final String database = configData.getDbName();
    private final String username = configData.getDbUsername();
    private final String password = configData.getDbPassword();

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        // Access the SU API
        RestReturn rest = new RestReturn();
        Members members = rest.getXML(configData.getSuAPI());
        try {
            // Create objects required for this scope
            Member member;
            try {
                member = members.getMember(args[0]);
            }catch (ArrayIndexOutOfBoundsException e){
                throw new MemberNotFoundException();
            }
            String idUser = "", discordTag = "";

            // Create a query object that can be used to access the Anisoc database
            SQLQuery query = new SQLQuery(hostname, database, username, password, 3306);

            // Create a string array that will be inputted to the Prep statement
            String[] values = {member.getUniqueID()};
            ResultSet rs = query.query("SELECT * FROM auth_user WHERE username=(?)", values);

            // Check if set is valid, and get this value out
            try {
                if(rs.next()){
                    idUser = rs.getString("id");
                }else{
                    throw new MemberNotFoundException();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Get the user id from the database based on their website ID
            values[0] = idUser;
            rs = query.query("SELECT * FROM members_member WHERE user_id=(?)", values);

            try {
                if(rs.next()){
                    discordTag = rs.getString("discord_tag");
                }else{
                    throw new MemberNotFoundException();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // If the user's discriminator tag equals that of the inputted tag, we have the right user
            if(discordTag.equals(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator())) {
                Role role;
                try {
                    // Make sure that the role exists, if not tell the server owner that the role does not exist and should be created
                    if ((role = event.getGuild().getRolesByName("Member", true).get(0)) != null) {
                        // Access the guild controller allowing for the assignment of the roles to the user
                        event.getGuild().addRoleToMember(event.getMember(), role).queue();

                        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " - Successfully added member role!").queue();
                    }
                } catch (IndexOutOfBoundsException e) {
                    event.getTextChannel().sendMessage(event.getGuild().getOwner().getAsMention() + " - Could not add member to "
                            + event.getAuthor().getName() + "! Role does not exist!").queue();
                } catch (HierarchyException e){
                    event.getTextChannel().sendMessage(event.getGuild().getOwner().getAsMention() + ": Cannot add role to " + event.getAuthor().getName() + "! Cannot apply a role of equal or higher permission " +
                            "than own to another user! Please give the bot elevated permissions!").queue();
                }
            }else{
                throw new MemberNotFoundException();
            }

        } catch (MemberNotFoundException e) {
            event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " - Could not find member with that unique ID. " +
                    "Make sure you entered the ID correctly and then contact an exec if the problem persists!").queue();
        }

    }

    @Override
    public void help(MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ": To gain access to member role, Add your Discord Name and Discriminator [Username#UserID] to your profile at " +
                "https://animesoc.co.uk/members/profile/ and run the command again with the format: !member <Your University ID>!").queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
