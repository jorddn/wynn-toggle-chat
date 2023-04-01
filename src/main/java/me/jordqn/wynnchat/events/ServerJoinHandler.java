package me.jordqn.wynnchat.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ServerJoinHandler {
    public static boolean onWynnCraft;

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        ServerData currentServer = Minecraft.getMinecraft().getCurrentServerData();
        onWynnCraft = currentServer != null && !currentServer.isOnLAN() && currentServer.serverIP.contains("wynncraft");
    }

    @SubscribeEvent
    public void onServerLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        onWynnCraft = false;
    }
}
