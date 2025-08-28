package com.norwood.mcheli.hud;

import com.norwood.mcheli.MCH_BaseInfo;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.helper.MCH_Utils;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import com.norwood.mcheli.helper.info.ContentParseException;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.wrapper.W_ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class MCH_Hud extends MCH_BaseInfo {
    public static final MCH_Hud NoDisp = new MCH_Hud(MCH_Utils.buildinAddon("none"), "none");
    public final String name;
    public final String fileName;
    public boolean isWaitEndif;
    public boolean isIfFalse;
    public boolean exit;
    private final List<MCH_HudItem> list;
    private boolean isDrawing;

    public MCH_Hud(AddonResourceLocation location, String filePath) {
        super(location, filePath);
        this.name = location.getPath();
        this.fileName = filePath;
        this.list = new ArrayList<>();
        this.isDrawing = false;
        this.isIfFalse = false;
        this.exit = false;
    }

    @Override
    public boolean validate() {
        for (MCH_HudItem hud : this.list) {
            hud.parent = this;
        }

        if (this.isWaitEndif) {
            throw new RuntimeException("Endif not found!");
        } else {
            return true;
        }
    }

    @Override
    public void onPostReload() {

    }

    public void loadItemData(int fileLine, String item, String data) {
        String[] prm = data.split("\\s*,\\s*");
        if (prm.length != 0) {
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
            } else if (item.equalsIgnoreCase("DrawString") || item.equalsIgnoreCase("DrawCenteredString")) {
                if (prm.length >= 3) {
                    String s = prm[2];
                    if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
                        s = s.substring(1, s.length() - 1);
                        this.list.add(new MCH_HudItemString(fileLine, prm[0], prm[1], s, prm, item.equalsIgnoreCase("DrawCenteredString")));
                    }
                }
            } else if (item.equalsIgnoreCase("Exit")) {
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
                    String rot = prm.length == 10 ? prm[9] : "0";
                    this.list.add(new MCH_HudItemTexture(fileLine, prm[0], prm[1], prm[2], prm[3], prm[4], prm[5], prm[6], prm[7], prm[8], rot));
                }
            } else if (item.equalsIgnoreCase("DrawRect")) {
                if (prm.length == 4) {
                    this.list.add(new MCH_HudItemRect(fileLine, prm[0], prm[1], prm[2], prm[3]));
                }
            } else if (item.equalsIgnoreCase("DrawLine")) {
                int len = prm.length;
                if (len >= 4 && len % 2 == 0) {
                    this.list.add(new MCH_HudItemLine(fileLine, prm));
                }
            } else if (item.equalsIgnoreCase("DrawLineStipple")) {
                int len = prm.length;
                if (len >= 6 && len % 2 == 0) {
                    this.list.add(new MCH_HudItemLineStipple(fileLine, prm));
                }
            } else if (item.equalsIgnoreCase("Call")) {
                int len = prm.length;
                if (len == 1) {
                    this.list.add(new MCH_HudItemCall(fileLine, prm[0]));
                }
            } else if (item.equalsIgnoreCase("DrawEntityRadar") || item.equalsIgnoreCase("DrawEnemyRadar")) {
                if (prm.length == 5) {
                    this.list.add(new MCH_HudItemRadar(fileLine, item.equalsIgnoreCase("DrawEntityRadar"), prm[0], prm[1], prm[2], prm[3], prm[4]));
                }
            } else if (item.equalsIgnoreCase("DrawGraduationYaw")
                    || item.equalsIgnoreCase("DrawGraduationPitch1")
                    || item.equalsIgnoreCase("DrawGraduationPitch2")
                    || item.equalsIgnoreCase("DrawGraduationPitch3")) {
                if (prm.length == 4) {
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
            } else if (item.equalsIgnoreCase("DrawCameraRot") && prm.length == 2) {
                this.list.add(new MCH_HudItemCameraRot(fileLine, prm[0], prm[1]));
            }
        }
    }

    public void draw(MCH_EntityAircraft ac, EntityPlayer player, float partialTicks) {
        if (MCH_HudItem.mc == null) {
            MCH_HudItem.mc = Minecraft.getMinecraft();
        }

        MCH_HudItem.ac = ac;
        MCH_HudItem.player = player;
        MCH_HudItem.partialTicks = partialTicks;
        ScaledResolution scaledresolution = new W_ScaledResolution(MCH_HudItem.mc, MCH_HudItem.mc.displayWidth, MCH_HudItem.mc.displayHeight);
        MCH_HudItem.scaleFactor = scaledresolution.getScaleFactor();
        if (MCH_HudItem.scaleFactor <= 0) {
            MCH_HudItem.scaleFactor = 1;
        }

        MCH_HudItem.width = (double) MCH_HudItem.mc.displayWidth / MCH_HudItem.scaleFactor;
        MCH_HudItem.height = (double) MCH_HudItem.mc.displayHeight / MCH_HudItem.scaleFactor;
        MCH_HudItem.centerX = MCH_HudItem.width / 2.0;
        MCH_HudItem.centerY = MCH_HudItem.height / 2.0;
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

            for (MCH_HudItem hud : this.list) {
                int line = -1;

                try {
                    line = hud.fileLine;
                    if (hud.canExecute()) {
                        hud.execute();
                        if (this.exit) {
                            break;
                        }
                    }
                } catch (Exception var5) {
                    MCH_Lib.Log("#### Draw HUD Error!!!: line=%d, file=%s", line, this.fileName);
                    var5.printStackTrace();
                    throw new RuntimeException(var5);
                }
            }

            this.exit = false;
            this.isIfFalse = false;
            this.isDrawing = false;
        }
    }

    @Override
    public void parse(List<String> lines, String fileExtension, boolean reload) {
        if ("txt".equals(fileExtension)) {
            int line = 0;

            try {
                for (String str : lines) {
                    line++;
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
