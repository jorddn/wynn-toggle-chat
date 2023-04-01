package me.jordqn.wynnchat.commands;

import me.jordqn.wynnchat.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import java.util.Collections;
import java.util.List;

public class OtherChatCommand extends CommandBase implements IClientCommand {
    private final String PREFIX;
    private final String USAGE;
    private final Channel CHANNEL;

    public OtherChatCommand(String prefix) {
        this.PREFIX = prefix;
        this.USAGE =  "/" + prefix + "chat <message>";

        switch (PREFIX) {
            case "p":
                this.CHANNEL = Channel.PARTY;
                break;
            case "g":
                this.CHANNEL = Channel.GUILD;
                break;
            case "a":
                this.CHANNEL = Channel.ALL;
                break;
            default:
                throw new RuntimeException("Unknown channel prefix");
        }
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    @Override
    public String getName() {
        return PREFIX + "chat";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(PREFIX + "c");
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return USAGE;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) throw new WrongUsageException(USAGE);
        Minecraft.getMinecraft().player.sendChatMessage(CHANNEL.getCommandPrefix() + String.join(" ", args));
    }
}
