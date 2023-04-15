package me.jordqn.wynnchat.mixins;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatScreen.class)
public interface ChatScreenInvoker {
    @Accessor
    EditBox getInput();

    @Invoker("insertText")
    void invokeInsertText(String p_95606_, boolean p_95607_);
}
