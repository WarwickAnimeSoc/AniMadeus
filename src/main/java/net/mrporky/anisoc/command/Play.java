//package net.mrporky.anisoc.command;
//
//import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
//import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
//import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
//import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
//import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
//import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
//import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
//import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.entities.TextChannel;
//import net.dv8tion.jda.api.entities.VoiceChannel;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import net.dv8tion.jda.api.managers.AudioManager;
//import net.mrporky.anisoc.Command;
//import net.mrporky.anisoc.command.musicsystem.GuildMusicManager;
//import net.mrporky.anisoc.util.CommandPermission;
//import net.mrporky.anisoc.util.NoRoleFoundException;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.sql.*;
//import java.util.*;
//
//public class Play extends ListenerAdapter implements Command {
//    private final String HELP = "Usage: !ping";
//    private boolean search = false;
//    private MessageReceivedEvent location;
//    private LinkedHashMap<String, Integer> channelsSkip = new LinkedHashMap<>();
//
//    public static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
//    public static Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
//
//    private Connection connect = null;
//    private Statement statement = null;
//
//    public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
//        long guildId = Long.parseLong(guild.getId());
//        GuildMusicManager musicManager = musicManagers.get(guildId);
//
//        if (musicManager == null) {
//            musicManager = new GuildMusicManager(playerManager);
//            musicManagers.put(guildId, musicManager);
//        }
//
//        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
//
//        return musicManager;
//    }
//
//    @Override
//    public boolean called(String[] args, MessageReceivedEvent event) {
//        AudioSourceManagers.registerRemoteSources(playerManager);
//        AudioSourceManagers.registerLocalSource(playerManager);
//        if (event.getMessage().getContentDisplay().equals("!alexastop")) {
//            skipTrack(event.getTextChannel(), event, event.getGuild().getAudioManager());
//        } else {
//            String[] command = event.getMessage().getContentDisplay().split(" ", 2);
//            Guild guild = event.getGuild();
//            if (guild != null) {
//                loadAndPlay(event.getTextChannel(), "https://www.youtube.com/watch?v=OD7AdmG9QfM", event);
//            }
//        }
//
//        super.onMessageReceived(event);
//        return true;
//    }
//
//    private void loadAndPlay(final TextChannel channel, final String trackUrl, MessageReceivedEvent event) {
//        final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
//        //musicManager.player.setVolume(50);
//        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
//            @Override
//            public void trackLoaded(AudioTrack track) {
//                play(channel.getGuild(), musicManager, track, event);
//            }
//
//            @Override
//            public void playlistLoaded(AudioPlaylist playlist) {
//                AudioTrack firstTrack = playlist.getSelectedTrack();
//                if (firstTrack == null) {
//                    firstTrack = playlist.getTracks().get(0);
//                }
//                channel.sendMessage(event.getAuthor().getAsMention() + " - Added " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
//
//                play(channel.getGuild(), musicManager, firstTrack, event);
//            }
//
//            @Override
//            public void noMatches() {
//                channel.sendMessage(event.getAuthor().getAsMention() + " - No songs were found  with the URL: " + trackUrl).queue();
//            }
//
//            @Override
//            public void loadFailed(FriendlyException exception) {
//                if (!search) {
//                    channel.sendMessage("Could not play: " + exception.getMessage()).queue();
//                } else {
//                    channel.sendMessage(event.getAuthor().getAsMention() + " - Retried the most popular song from the search query and added it to the queue!").queue();
//                }
//            }
//        });
//    }
//
//    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, MessageReceivedEvent event) {
//        try {
//            AudioManager audioManager = guild.getAudioManager();
//            boolean connected = false;
//            for (int i = 0; i < audioManager.getGuild().getVoiceChannels().size(); i++) {
//                VoiceChannel voiceChannel = audioManager.getGuild().getVoiceChannels().get(i);
//                long songlength = track.getDuration() / 1000;
//                if (voiceChannel.getMembers().contains(event.getMember())) {
//                    connected = true;
//                    if (songlength < 3600 && !track.getInfo().isStream) {
//                        connectToFirstVoiceChannel(guild, guild.getAudioManager(), event, voiceChannel);
//
//                        musicManager.scheduler.queue(track);
//                        musicManager.player.setVolume(15);
//
//                    } else if (track.getInfo().isStream) {
//                        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " - Livestreams" +
//                                " are currently disabled!\n How you broke this is beyond me...").queue();
//                    } else {
//                        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " - The song" +
//                                " length must not exceed one hour!\n How you broke this is beyond me...").queue();
//                    }
//                }
//            }
//            if (!connected) {
//                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " - This is so sad," +
//                        " can you join a voice channel?").queue();
//            }
//
//        } catch (Exception e) {
//            location.getTextChannel().sendMessage("Failed to Play " + track.getInfo() + ": Song URL Invalid").queue();
//        }
//
//    }
//
//    private static void connectToFirstVoiceChannel(Guild guild, AudioManager audioManager, MessageReceivedEvent event, VoiceChannel voiceChannel) {
//        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
//            audioManager.openAudioConnection(voiceChannel);
//        }
//    }
//
//    private void skipTrack(TextChannel channel, MessageReceivedEvent event, AudioManager audioManager) {
//        Guild guild = event.getGuild();
//        String[] command = event.getMessage().getContentDisplay().split(" ", 2);
//        try {
//            if ((CommandPermission.hasPermission("Exec", event) || CommandPermission.isAdmin(event)) && guild != null) {
//                if ("!alexastop".equals(command[0])) {
//                    GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
//                    musicManager.scheduler.nextTrack();
//                    musicManager.scheduler.getQueue();
//                    audioManager.closeAudioConnection();
//                }
//            }
//        } catch (NoRoleFoundException e) {
//            // No class found
//        }
//
//    }
//
//    @Override
//    public void action(String[] args, MessageReceivedEvent event) {
//
//    }
//
//    @Override
//    public void help(MessageReceivedEvent event) {
//        event.getTextChannel().sendMessage("This is so sad, Alexa, play trailroll on UWCS-Djikstra").queue();
//    }
//
//    @Override
//    public void executed(boolean success, MessageReceivedEvent event) {
//        return;
//    }
//}