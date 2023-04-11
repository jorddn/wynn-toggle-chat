package me.jordqn.wynnchat.events;

import me.jordqn.wynnchat.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatEventHandler {
    public static Channel channel = Channel.ALL;
    private String recipient;

    @SubscribeEvent
    public void onMessageSend(ClientChatEvent event) {
        if (!ServerJoinHandler.onWynnCraft) return;

        String message = event.getMessage();

        if (isMessageCommand(message)) {
            event.setCanceled(true);
            return;
        }

        if (message.startsWith("/")) return;

        event.setMessage(channel.getCommandPrefix().replaceFirst("\\{username}", recipient) + message);
    }

    private boolean isMessageCommand(String message) {
        System.out.println(message.trim().split(" ").length);
        if (message.startsWith("/msg") && message.trim().split(" ").length == 2) {
            if (channel == Channel.MSG && recipient.equalsIgnoreCase(message.trim().split(" ")[1])) {
                TextComponentString inUse = new TextComponentString("You're already in this channel!");
                inUse.getStyle().setColor(TextFormatting.RED);

                Minecraft.getMinecraft().player.sendMessage(inUse);
                return true;
            }

            recipient = message.trim().split(" ")[1];
            channel = Channel.MSG;

            TextComponentString username = new TextComponentString(recipient);
            TextComponentString command = new TextComponentString("/chat a");

            username.getStyle().setColor(TextFormatting.AQUA);
            command.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString channelSwitched = (TextComponentString) new TextComponentString("Opened a chat conversation with ")
                    .appendSibling(username)
                    .appendText(". Use ")
                    .appendSibling(command)
                    .appendText(" to leave");

            channelSwitched.getStyle().setColor(TextFormatting.GREEN);

            Minecraft.getMinecraft().player.sendMessage(channelSwitched);
            return true;
        }

        return false;
    }
}
