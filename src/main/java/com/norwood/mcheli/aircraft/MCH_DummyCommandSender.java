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
      ICommandManager icommandmanager = MCH_Utils.getServer().func_71187_D();
      icommandmanager.func_71556_a(instance, s);
   }

   public String func_70005_c_() {
      return "";
   }

   public ITextComponent func_145748_c_() {
      return null;
   }

   public void func_145747_a(ITextComponent component) {
   }

   public boolean func_70003_b(int permLevel, String commandName) {
      return true;
   }

   public World func_130014_f_() {
      return null;
   }

   public MinecraftServer func_184102_h() {
      return MCH_Utils.getServer();
   }
}
