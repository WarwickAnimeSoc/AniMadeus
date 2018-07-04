package net.mrporky.anisoc.command;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.mrporky.anisoc.Command;
import net.mrporky.anisoc.members.Member;
import net.mrporky.anisoc.members.MemberNotFoundException;
import net.mrporky.anisoc.members.Members;
import net.mrporky.anisoc.util.BotLoader;
import net.mrporky.anisoc.util.RestReturn;
import net.mrporky.anisoc.util.SQLQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberCreate implements Command {

    private String hostname = BotLoader.config.getValue("DBHOSTNAME");
    private String database = BotLoader.config.getValue("DBNAME");
    private String username = BotLoader.config.getValue("DBUSERNAME");
    private String password = BotLoader.config.getValue("DBPASSWORD");

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        // Access the SU API
        RestReturn rest = new RestReturn();
        Members members = rest.getRest(BotLoader.config.getValue("SU-API"));
        try {
            // Create objects required for this scope
            Member member = members.getMember(args[0]);
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
                    discordTag = rs.getString("discordTag");
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
                        GuildController controller = new GuildController(event.getGuild());
                        controller.addRolesToMember(event.getMember(), role).queue();

                        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " - Successfully added member role!").queue();
                    }
                } catch (IndexOutOfBoundsException e) {
                    event.getTextChannel().sendMessage(event.getGuild().getOwner().getAsMention() + " - Could not add member to "
                            + event.getAuthor().getName() + "! Role does not exist!").queue();
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

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
