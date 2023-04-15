package me.jordqn.wynnchat;

import me.jordqn.wynnchat.commands.MsgCommand;
import me.jordqn.wynnchat.commands.SetChannelCommand;
import me.jordqn.wynnchat.events.EventHandler;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(WynnToggleChat.MOD_ID)
public class WynnToggleChat {
    public static final String MOD_ID = "wynnchat";

    public WynnToggleChat() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @SubscribeEvent
    public void registerCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(SetChannelCommand.getBuilder());
        event.getDispatcher().register(MsgCommand.getBuilder());
    }
}
