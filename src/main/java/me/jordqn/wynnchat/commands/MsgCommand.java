package me.jordqn.wynnchat.commands;

import com.google.common.base.Strings;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.jordqn.wynnchat.Channel;
import me.jordqn.wynnchat.events.EventHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class MsgCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> getBuilder() {
        return Commands.literal("msg")
                .then(Commands.argument("username", StringArgumentType.word())
                        .executes(MsgCommand::setRecipient));
    }

    private static int setRecipient(CommandContext<CommandSourceStack> context) {
        if (!EventHandler.onWynnCraft) {
            Component syntax = Component.literal("This command is disabled outside of WynnCraft.")
                    .withStyle(ChatFormatting.RED);

            context.getSource().sendFailure(syntax);
            return 0;
        }

        String username = StringArgumentType.getString(context, "username");

        if (EventHandler.channel == Channel.MSG) {
            if (Strings.nullToEmpty(EventHandler.recipient).equalsIgnoreCase(username)) {
                Component inChannel = Component.literal("You're already in this channel!")
                        .withStyle(ChatFormatting.RED);

                context.getSource().sendSuccess(inChannel, false);
                return 1;
            }
        }

        EventHandler.channel = Channel.MSG;
        EventHandler.recipient = username;

        Component channelSwitched = Component.literal("Opened a chat conversation with ")
                .append(Component.literal(username).withStyle(ChatFormatting.AQUA))
                .append(". Use ")
                .append(Component.literal("/chat a").withStyle(ChatFormatting.AQUA))
                .append(" to leave")
                .withStyle(ChatFormatting.GREEN);

        context.getSource().sendSuccess(channelSwitched, false);
        return 1;
    }
}
