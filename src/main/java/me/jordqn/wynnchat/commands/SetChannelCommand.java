package me.jordqn.wynnchat.commands;
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
