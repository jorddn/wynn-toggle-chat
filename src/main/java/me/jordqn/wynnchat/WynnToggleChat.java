package me.jordqn.wynnchat;

import me.jordqn.wynnchat.commands.OtherChatCommand;
import me.jordqn.wynnchat.commands.SetChannelCommand;
import me.jordqn.wynnchat.events.ChatEventHandler;
import me.jordqn.wynnchat.events.ServerJoinHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.Arrays;

@Mod(
        name = WynnToggleChat.NAME,
        modid = WynnToggleChat.MOD_ID,
        clientSideOnly = true
)
public class WynnToggleChat
{
    public static final String MOD_ID = "wynn-toggle-chat";
    public static final String NAME = "Wynn Toggle Chat";

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Register chat preprocessor
        MinecraftForge.EVENT_BUS.register(new ChatEventHandler());

        // Register server join handler
        MinecraftForge.EVENT_BUS.register(new ServerJoinHandler());

        // Register command to toggle channels
        ClientCommandHandler.instance.registerCommand(new SetChannelCommand());

        // Register a command to quickly use any channel without toggling between them
        Arrays.asList("a", "p", "g").forEach((prefix) -> {
            ClientCommandHandler.instance.registerCommand(new OtherChatCommand(prefix));
        });
    }
}
