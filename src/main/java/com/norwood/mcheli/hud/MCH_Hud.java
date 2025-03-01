package com.norwood.mcheli.hud;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.norwood.mcheli.MCH_BaseInfo;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;
import com.norwood.mcheli.__helper.info.ContentParseException;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.wrapper.W_ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_Hud extends MCH_BaseInfo {
   public static final MCH_Hud NoDisp = new MCH_Hud(MCH_Utils.buildinAddon("none"), "none");
   public final String name;
   public final String fileName;
   private List<MCH_HudItem> list;
   public boolean isWaitEndif;
   private boolean isDrawing;
   public boolean isIfFalse;
   public boolean exit;

   public MCH_Hud(AddonResourceLocation location, String filePath) {
      super(location, filePath);
      this.name = location.func_110623_a();
      this.fileName = filePath;
      this.list = new ArrayList();
      this.isDrawing = false;
      this.isIfFalse = false;
      this.exit = false;
   }

   public boolean validate() throws Exception {
      MCH_HudItem hud;
      for(Iterator var1 = this.list.iterator(); var1.hasNext(); hud.parent = this) {
         hud = (MCH_HudItem)var1.next();
      }

      if (this.isWaitEndif) {
         throw new RuntimeException("Endif not found!");
      } else {
         return true;
      }
   }

   public void loadItemData(int fileLine, String item, String data) {
      String[] prm = data.split("\\s*,\\s*");
      if (prm != null && prm.length != 0) {
         if (item.equalsIgnoreCase("If")) {
            if (this.isWaitEndif) {
               throw new RuntimeException("Endif not found!");
            }

            this.list.add(new MCH_HudItemConditional(fileLine, false, prm[0]));
            this.isWaitEndif = true;
         } else if (item.equalsIgnoreCase("Endif")) {
            if (!this.isWaitEndif) {
               throw new RuntimeException("IF in a pair can not be found!");
            }

            this.list.add(new MCH_HudItemConditional(fileLine, true, ""));
            this.isWaitEndif = false;
         } else {
            String rot;
            if (!item.equalsIgnoreCase("DrawString") && !item.equalsIgnoreCase("DrawCenteredString")) {
               if (item.equalsIgnoreCase("Exit")) {
                  this.list.add(new MCH_HudItemExit(fileLine));
               } else if (item.equalsIgnoreCase("Color")) {
                  if (prm.length == 1) {
                     MCH_HudItemColor c = MCH_HudItemColor.createByParams(fileLine, new String[]{prm[0]});
                     if (c != null) {
                        this.list.add(c);
                     }
                  } else if (prm.length == 4) {
                     String[] s = new String[]{prm[0], prm[1], prm[2], prm[3]};
                     MCH_HudItemColor c = MCH_HudItemColor.createByParams(fileLine, s);
                     if (c != null) {
                        this.list.add(c);
                     }
                  }
               } else if (item.equalsIgnoreCase("DrawTexture")) {
                  if (prm.length >= 9 && prm.length <= 10) {
                     rot = prm.length == 10 ? prm[9] : "0";
                     this.list.add(new MCH_HudItemTexture(fileLine, prm[0], prm[1], prm[2], prm[3], prm[4], prm[5], prm[6], prm[7], prm[8], rot));
                  }
               } else if (item.equalsIgnoreCase("DrawRect")) {
                  if (prm.length == 4) {
                     this.list.add(new MCH_HudItemRect(fileLine, prm[0], prm[1], prm[2], prm[3]));
                  }
               } else {
                  int len;
                  if (item.equalsIgnoreCase("DrawLine")) {
                     len = prm.length;
                     if (len >= 4 && len % 2 == 0) {
                        this.list.add(new MCH_HudItemLine(fileLine, prm));
                     }
                  } else if (item.equalsIgnoreCase("DrawLineStipple")) {
                     len = prm.length;
                     if (len >= 6 && len % 2 == 0) {
                        this.list.add(new MCH_HudItemLineStipple(fileLine, prm));
                     }
                  } else if (item.equalsIgnoreCase("Call")) {
                     len = prm.length;
                     if (len == 1) {
                        this.list.add(new MCH_HudItemCall(fileLine, prm[0]));
                     }
                  } else if (!item.equalsIgnoreCase("DrawEntityRadar") && !item.equalsIgnoreCase("DrawEnemyRadar")) {
                     if (!item.equalsIgnoreCase("DrawGraduationYaw") && !item.equalsIgnoreCase("DrawGraduationPitch1") && !item.equalsIgnoreCase("DrawGraduationPitch2") && !item.equalsIgnoreCase("DrawGraduationPitch3")) {
                        if (item.equalsIgnoreCase("DrawCameraRot") && prm.length == 2) {
                           this.list.add(new MCH_HudItemCameraRot(fileLine, prm[0], prm[1]));
                        }
                     } else if (prm.length == 4) {
                        int type = -1;
                        if (item.equalsIgnoreCase("DrawGraduationYaw")) {
                           type = 0;
                        }

                        if (item.equalsIgnoreCase("DrawGraduationPitch1")) {
                           type = 1;
                        }

                        if (item.equalsIgnoreCase("DrawGraduationPitch2")) {
                           type = 2;
                        }

                        if (item.equalsIgnoreCase("DrawGraduationPitch3")) {
                           type = 3;
                        }

                        this.list.add(new MCH_HudItemGraduation(fileLine, type, prm[0], prm[1], prm[2], prm[3]));
                     }
                  } else if (prm.length == 5) {
                     this.list.add(new MCH_HudItemRadar(fileLine, item.equalsIgnoreCase("DrawEntityRadar"), prm[0], prm[1], prm[2], prm[3], prm[4]));
                  }
               }
            } else if (prm.length >= 3) {
               rot = prm[2];
               if (rot.charAt(0) == '"' && rot.charAt(rot.length() - 1) == '"') {
                  rot = rot.substring(1, rot.length() - 1);
                  this.list.add(new MCH_HudItemString(fileLine, prm[0], prm[1], rot, prm, item.equalsIgnoreCase("DrawCenteredString")));
               }
            }
         }

      }
   }

   public void draw(MCH_EntityAircraft ac, EntityPlayer player, float partialTicks) {
      if (MCH_HudItem.mc == null) {
         MCH_HudItem.mc = Minecraft.func_71410_x();
      }

      MCH_HudItem.ac = ac;
      MCH_HudItem.player = player;
      MCH_HudItem.partialTicks = partialTicks;
      ScaledResolution scaledresolution = new W_ScaledResolution(MCH_HudItem.mc, MCH_HudItem.mc.field_71443_c, MCH_HudItem.mc.field_71440_d);
      MCH_HudItem.scaleFactor = scaledresolution.func_78325_e();
      if (MCH_HudItem.scaleFactor <= 0) {
         MCH_HudItem.scaleFactor = 1;
      }

      MCH_HudItem.width = (double)(MCH_HudItem.mc.field_71443_c / MCH_HudItem.scaleFactor);
      MCH_HudItem.height = (double)(MCH_HudItem.mc.field_71440_d / MCH_HudItem.scaleFactor);
      MCH_HudItem.centerX = MCH_HudItem.width / 2.0D;
      MCH_HudItem.centerY = MCH_HudItem.height / 2.0D;
      this.isIfFalse = false;
      this.isDrawing = false;
      this.exit = false;
      if (ac != null && ac.getAcInfo() != null && player != null) {
         MCH_HudItem.update();
         this.drawItems();
         MCH_HudItem.drawVarMap();
      }

   }

   protected void drawItems() {
      if (!this.isDrawing) {
         this.isDrawing = true;
         Iterator var1 = this.list.iterator();

         while(var1.hasNext()) {
            MCH_HudItem hud = (MCH_HudItem)var1.next();
            byte line = -1;

            try {
               int line = hud.fileLine;
               if (hud.canExecute()) {
                  hud.execute();
                  if (this.exit) {
                     break;
                  }
               }
            } catch (Exception var5) {
               MCH_Lib.Log("#### Draw HUD Error!!!: line=%d, file=%s", Integer.valueOf(line), this.fileName);
               var5.printStackTrace();
               throw new RuntimeException(var5);
            }
         }

         this.exit = false;
         this.isIfFalse = false;
         this.isDrawing = false;
      }

   }

   public void parse(List<String> lines, String fileExtension, boolean reload) throws Exception {
      if ("txt".equals(fileExtension)) {
         int line = 0;

         try {
            Iterator var5 = lines.iterator();

            while(var5.hasNext()) {
               String str = (String)var5.next();
               ++line;
               str = str.trim();
               if (str.equalsIgnoreCase("endif")) {
                  str = "endif=0";
               }

               if (str.equalsIgnoreCase("exit")) {
                  str = "exit=0";
               }

               int eqIdx = str.indexOf(61);
               if (eqIdx >= 0 && str.length() > eqIdx + 1) {
                  this.loadItemData(line, str.substring(0, eqIdx).trim().toLowerCase(), str.substring(eqIdx + 1).trim());
               }
            }
         } catch (Exception var8) {
            throw new ContentParseException(var8, line);
         }
      }

   }
}
