package com.norwood.mcheli.aircraft;

import com.norwood.mcheli.__helper.MCH_Utils;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class MCH_DummyCommandSender implements ICommandSender {
    public static MCH_DummyCommandSender instance = new MCH_DummyCommandSender();

    public static void execCommand(String s) {
        ICommandManager icommandmanager = MCH_Utils.getServer().getCommandManager();
        icommandmanager.executeCommand(instance, s);
    }

    public String getName() {
        return "";
    }

    public ITextComponent getDisplayName() {
        return null;
    }

    public void sendMessage(ITextComponent component) {
    }

    public boolean canUseCommand(int permLevel, String commandName) {
        return true;
    }

    public World getEntityWorld() {
        return null;
    }

    public MinecraftServer getServer() {
        return MCH_Utils.getServer();
    }
}
