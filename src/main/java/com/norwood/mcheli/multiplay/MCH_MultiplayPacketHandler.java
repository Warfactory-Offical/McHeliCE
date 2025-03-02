package com.norwood.mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_PacketNotifyServerSettings;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.__helper.network.HandleSide;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MCH_MultiplayPacketHandler {
   private static final Logger logger = LogManager.getLogger();
   private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
   private static byte[] imageData = null;
   private static String lastPlayerName = "";
   private static double lastDataPercent = 0.0D;
   public static EntityPlayer modListRequestPlayer = null;
   private static int playerInfoId = 0;

   @HandleSide({Side.SERVER})
   public static void onPacket_Command(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (!player.world.isRemote) {
         MCH_PacketIndMultiplayCommand pc = new MCH_PacketIndMultiplayCommand();
         pc.readData(data);
         scheduler.func_152344_a(() -> {
            MinecraftServer minecraftServer = MCH_Utils.getServer();
            MCH_Lib.DbgLog(false, "MCH_MultiplayPacketHandler.onPacket_Command cmd:%d:%s", pc.CmdID, pc.CmdStr);
            switch(pc.CmdID) {
            case 256:
               MCH_Multiplay.shuffleTeam(player);
               break;
            case 512:
               MCH_Multiplay.jumpSpawnPoint(player);
               break;
            case 768:
               ICommandManager icommandmanager = minecraftServer.func_71187_D();
               icommandmanager.func_71556_a(player, pc.CmdStr);
               break;
            case 1024:
               if ((new CommandScoreboard()).func_184882_a(minecraftServer, player)) {
                  minecraftServer.func_71188_g(!minecraftServer.func_71219_W());
                  MCH_PacketNotifyServerSettings.send((EntityPlayerMP)null);
               }
               break;
            case 1280:
               destoryAllAircraft(player);
               break;
            default:
               MCH_Lib.DbgLog(false, "MCH_MultiplayPacketHandler.onPacket_Command unknown cmd:%d:%s", pc.CmdID, pc.CmdStr);
            }

         });
      }
   }

   private static void destoryAllAircraft(EntityPlayer player) {
      CommandSummon cmd = new CommandSummon();
      if (cmd.func_184882_a(MCH_Utils.getServer(), player)) {
         Iterator var2 = player.world.field_72996_f.iterator();

         while(var2.hasNext()) {
            Entity e = (Entity)var2.next();
            if (e instanceof MCH_EntityAircraft) {
               ((MCH_EntityAircraft)e).func_70106_y();
            }
         }
      }

   }

   @HandleSide({Side.CLIENT})
   public static void onPacket_NotifySpotedEntity(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player.world.isRemote) {
         MCH_PacketNotifySpotedEntity pc = new MCH_PacketNotifySpotedEntity();
         pc.readData(data);
         scheduler.func_152344_a(() -> {
            if (pc.count > 0) {
               for(int i = 0; i < pc.num; ++i) {
                  MCH_GuiTargetMarker.addSpotedEntity(pc.entityId[i], pc.count);
               }
            }

         });
      }
   }

   @HandleSide({Side.CLIENT})
   public static void onPacket_NotifyMarkPoint(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player.world.isRemote) {
         MCH_PacketNotifyMarkPoint pc = new MCH_PacketNotifyMarkPoint();
         pc.readData(data);
         scheduler.func_152344_a(() -> {
            MCH_GuiTargetMarker.markPoint(pc.px, pc.py, pc.pz);
         });
      }
   }

   @HandleSide({Side.SERVER})
   public static void onPacket_LargeData(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (!player.world.isRemote) {
         MCH_PacketLargeData pc = new MCH_PacketLargeData();
         pc.readData(data);
         scheduler.func_152344_a(() -> {
            try {
               if (pc.imageDataIndex < 0 || pc.imageDataTotalSize <= 0) {
                  return;
               }

               if (pc.imageDataIndex == 0) {
                  if (imageData != null && !lastPlayerName.isEmpty()) {
                     LogError("[mcheli]Err1:Saving the %s screen shot to server FAILED!!!", lastPlayerName);
                  }

                  imageData = new byte[pc.imageDataTotalSize];
                  lastPlayerName = player.func_145748_c_().func_150254_d();
                  lastDataPercent = 0.0D;
               }

               double dataPercent = (double)((pc.imageDataIndex + pc.imageDataSize) / pc.imageDataTotalSize) * 100.0D;
               if (dataPercent - lastDataPercent >= 10.0D || lastDataPercent == 0.0D) {
                  LogInfo("[mcheli]Saving the %s screen shot to server. %.0f%% : %dbyte / %dbyte", player.func_145748_c_(), dataPercent, pc.imageDataIndex, pc.imageDataTotalSize);
                  lastDataPercent = dataPercent;
               }

               if (imageData == null) {
                  if (imageData != null && lastPlayerName.isEmpty()) {
                     LogError("[mcheli]Err2:Saving the %s screen shot to server FAILED!!!", player.func_145748_c_());
                  }

                  imageData = null;
                  lastPlayerName = "";
                  lastDataPercent = 0.0D;
                  return;
               }

               for(int i = 0; i < pc.imageDataSize; ++i) {
                  imageData[pc.imageDataIndex + i] = pc.buf[i];
               }

               if (pc.imageDataIndex + pc.imageDataSize >= pc.imageDataTotalSize) {
                  DataOutputStream dos = null;
                  String dt = dateFormat.format(new Date()).toString();
                  File file = new File("screenshots_op");
                  file.mkdir();
                  file = new File(file, player.func_145748_c_() + "_" + dt + ".png");
                  String s = file.getAbsolutePath();
                  LogInfo("[mcheli]Save Screenshot has been completed: %s", s);
                  FileOutputStream fos = new FileOutputStream(s);
                  dos = new DataOutputStream(fos);
                  dos.write(imageData);
                  dos.flush();
                  dos.close();
                  imageData = null;
                  lastPlayerName = "";
                  lastDataPercent = 0.0D;
               }
            } catch (Exception var9) {
               var9.printStackTrace();
            }

         });
      }
   }

   public static void LogInfo(String format, Object... args) {
      logger.info(String.format(format, args));
   }

   public static void LogError(String format, Object... args) {
      logger.error(String.format(format, args));
   }

   @HandleSide({Side.CLIENT})
   public static void onPacket_IndClient(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player.world.isRemote) {
         MCH_PacketIndClient pc = new MCH_PacketIndClient();
         pc.readData(data);
         scheduler.func_152344_a(() -> {
            if (pc.CmdID == 1) {
               MCH_MultiplayClient.startSendImageData();
            } else if (pc.CmdID == 2) {
               MCH_MultiplayClient.sendModsInfo(player.func_145748_c_().func_150254_d(), player.func_145748_c_().func_150260_c(), Integer.parseInt(pc.CmdStr));
            }

         });
      }
   }

   public static int getPlayerInfoId(EntityPlayer player) {
      modListRequestPlayer = player;
      ++playerInfoId;
      if (playerInfoId > 1000000) {
         playerInfoId = 1;
      }

      return playerInfoId;
   }

   @HandleSide({Side.CLIENT, Side.SERVER})
   public static void onPacket_ModList(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      MCH_PacketModList pc = new MCH_PacketModList();
      pc.readData(data);
      if (player.world.isRemote) {
         scheduler.func_152344_a(() -> {
            MCH_Lib.DbgLog(player.world, "MCH_MultiplayPacketHandler.onPacket_ModList : ID=%d, Num=%d", pc.id, pc.num);
            if (pc.firstData) {
               MCH_Lib.Log(TextFormatting.RED + "###### " + player.func_145748_c_() + " ######");
            }

            Iterator var2 = pc.list.iterator();

            while(var2.hasNext()) {
               String s = (String)var2.next();
               MCH_Lib.Log(s);
               player.func_145747_a(new TextComponentString(s));
            }

         });
      } else if (pc.id == playerInfoId) {
         scheduler.func_152344_a(() -> {
            if (modListRequestPlayer != null) {
               MCH_PacketModList.send(modListRequestPlayer, pc);
            } else {
               if (pc.firstData) {
                  LogInfo("###### " + player.func_145748_c_() + " ######");
               }

               Iterator var2 = pc.list.iterator();

               while(var2.hasNext()) {
                  String s = (String)var2.next();
                  LogInfo(s);
               }
            }

         });
      }

   }
}
