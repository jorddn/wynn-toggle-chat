package me.jordqn.wynnchat.events;

import me.jordqn.wynnchat.Channel;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatEventHandler {
    public static Channel channel = Channel.ALL;

    @SubscribeEvent
    public void onMessageSend(ClientChatEvent event) {
        String message = event.getMessage();

        if (message.startsWith("/") || !ServerJoinHandler.onWynnCraft) {
            return;
        }

        event.setMessage(channel.getCommandPrefix() + message);
    }
}
