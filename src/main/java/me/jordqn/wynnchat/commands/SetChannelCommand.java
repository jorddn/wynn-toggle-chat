package me.jordqn.wynnchat.commands;//package me.jordqn.wynnchat.commands;
//
//import me.jordqn.wynnchat.Channel;
//import me.jordqn.wynnchat.events.ChatEventHandler;
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.command.WrongUsageException;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.TextComponentString;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraftforge.client.IClientCommand;
//
//import javax.annotation.Nullable;
//import java.util.Collections;
//import java.util.List;
//
//public class SetChannelCommand extends CommandBase implements IClientCommand {
//    private final String USAGE = "/chat <channel>\n" + "Valid channels: all, party, guild";
//
//    @Override
//    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
//        return false;
//    }
//
//    @Override
//    public String getName() {
//        return "chat";
//    }
//
//    @Override
//    public String getUsage(ICommandSender sender) {
//        return USAGE;
//    }
//
//    @Override
//    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
//        if (args.length == 1) {
//            return getListOfStringsMatchingLastWord(args, "all", "party", "guild");
//        }
//        return Collections.emptyList();
//    }
//
//    @Override
//    public int getRequiredPermissionLevel() {
//        return 0;
//    }
//
//    @Override
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
//        if (args.length != 1) throw new WrongUsageException(USAGE);
//
//        Channel channel;
//
//        switch (args[0].toLowerCase()) {
//            case "p":
//            case "party":
//                channel = Channel.PARTY;
//                break;
//            case "g":
//            case "guild":
//                channel = Channel.GUILD;
//                break;
//            case "a":
//            case "all":
//                channel = Channel.ALL;
//                break;
//            default:
//                throw new WrongUsageException(USAGE);
//        }
//
//        if (ChatEventHandler.channel == channel) {
//            TextComponentString inUse = new TextComponentString("You're already in this channel!");
//            inUse.getStyle().setColor(TextFormatting.RED);
//
//            sender.sendMessage(inUse);
//            return;
//        }
//
//        ChatEventHandler.channel = channel;
//
//        TextComponentString channelString = new TextComponentString(channel.name());
//        channelString.getStyle().setColor(TextFormatting.GOLD);
//
//        TextComponentString channelSwitched = (TextComponentString) new TextComponentString("You are now in the ").appendSibling(channelString).appendText(" channel");
//        channelSwitched.getStyle().setColor(TextFormatting.GREEN);
//
//        sender.sendMessage(channelSwitched);
//    }
//}

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.jordqn.wynnchat.Channel;
import me.jordqn.wynnchat.events.EventHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class SetChannelCommand {
    private static final SuggestionProvider<CommandSourceStack> CHANNEL_SUGGESTION_PROVIDER =
            (context, builder) -> SharedSuggestionProvider.suggest(Arrays.asList("all", "party", "guild"), builder);

    public static LiteralArgumentBuilder<CommandSourceStack> getBuilder() {
        return Commands.literal("chat")
                .then(Commands.argument("channel", StringArgumentType.word())
                        .suggests(CHANNEL_SUGGESTION_PROVIDER)
                        .executes(SetChannelCommand::setChannel))
                .executes(SetChannelCommand::syntaxError);
    }

    private static int syntaxError(CommandContext<CommandSourceStack> context) {
        Component syntax = Component.literal("/chat <channel>\n" + "Valid channels: all, party, guild")
                .withStyle(ChatFormatting.RED);

        context.getSource().sendFailure(syntax);
        return 0;
    }

    private static int setChannel(CommandContext<CommandSourceStack> context) {
        if (!EventHandler.onWynnCraft) {
            Component syntax = Component.literal("This command is disabled outside of WynnCraft.")
                    .withStyle(ChatFormatting.RED);

            context.getSource().sendFailure(syntax);
            return 0;
        }

        Channel channel;

        String channelArg = StringArgumentType.getString(context, "channel");

        channel = switch (channelArg) {
            case "p", "party" -> Channel.PARTY;
            case "g", "guild" -> Channel.GUILD;
            case "a", "all" -> Channel.ALL;
            default -> null;
        };

        if (channel == null) return SetChannelCommand.syntaxError(context);

        if (EventHandler.channel == channel) {
            Component inChannel = Component.literal("You're already in this channel!")
                    .withStyle(ChatFormatting.RED);

            context.getSource().sendSuccess(inChannel, false);
            return 1;
        }

        EventHandler.channel = channel;

        Component channelSwitched = Component.literal("You are now in the ")
                .append(Component.literal(channel.name()).withStyle(ChatFormatting.GOLD))
                .append(" channel")
                .withStyle(ChatFormatting.GREEN);

        context.getSource().sendSuccess(channelSwitched, false);
        return 1;
    }
}
