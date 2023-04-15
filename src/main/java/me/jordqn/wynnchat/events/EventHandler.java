package me.jordqn.wynnchat.events;

import com.google.common.base.Strings;
import me.jordqn.wynnchat.Channel;
import me.jordqn.wynnchat.mixins.ChatScreenInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    public static Channel channel = Channel.ALL;
    public static String recipient;

    public static boolean onWynnCraft;

    @SubscribeEvent
    public void onOpenChat(ScreenEvent.Init.Post event) {
        if (!onWynnCraft) return;

        if (!(event.getScreen() instanceof ChatScreen chatScreen)) return;
        if (channel.getCommandPrefix().isEmpty()) return;

        // Don't put prefix if opened using / because a command is probably being typed
        if (((ChatScreenInvoker) chatScreen).getInput().getValue().equals("/")) return;

        ((ChatScreenInvoker) chatScreen).invokeInsertText(channel.getCommandPrefix().replaceFirst("\\{username}", Strings.nullToEmpty(recipient)), true);
    }

    @SubscribeEvent
    public void onServerJoin(ClientPlayerNetworkEvent.LoggingIn ignoredEvent) {
        ServerData currentServer = Minecraft.getInstance().getCurrentServer();
        onWynnCraft = currentServer != null && !currentServer.isLan() && currentServer.ip.contains("wynncraft");
    }

    @SubscribeEvent
    public void onServerLeave(ClientPlayerNetworkEvent.LoggingOut ignoredEvent) {
        onWynnCraft = false;
    }
}
