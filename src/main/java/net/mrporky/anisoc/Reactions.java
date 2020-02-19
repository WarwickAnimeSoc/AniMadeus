package net.mrporky.anisoc;

import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public interface Reactions {
    public void onReaction(MessageReactionAddEvent event);
    public void onReactionRemove(MessageReactionRemoveEvent event);
}
