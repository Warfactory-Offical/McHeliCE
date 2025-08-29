package com.norwood.mcheli;

import com.norwood.mcheli.helper.addon.AddonManager;
import com.norwood.mcheli.helper.addon.AddonPack;
import com.norwood.mcheli.helper.client.MCH_ItemModelRenderers;
import com.norwood.mcheli.helper.client._IModelCustom;
import com.norwood.mcheli.helper.client.model.LegacyModelLoader;
import com.norwood.mcheli.helper.client.renderer.item.*;
import com.norwood.mcheli.helper.info.ContentRegistries;
import com.norwood.mcheli.aircraft.*;
import com.norwood.mcheli.block.MCH_DraftingTableRenderer;
import com.norwood.mcheli.block.MCH_DraftingTableTileEntity;
import com.norwood.mcheli.chain.MCH_EntityChain;
import com.norwood.mcheli.chain.MCH_RenderChain;
import com.norwood.mcheli.command.MCH_GuiTitle;
import com.norwood.mcheli.container.MCH_EntityContainer;
import com.norwood.mcheli.container.MCH_RenderContainer;
import com.norwood.mcheli.debug.MCH_RenderTest;
import com.norwood.mcheli.flare.MCH_EntityFlare;
import com.norwood.mcheli.flare.MCH_RenderFlare;
import com.norwood.mcheli.gltd.MCH_EntityGLTD;
import com.norwood.mcheli.gltd.MCH_RenderGLTD;
import com.norwood.mcheli.helicopter.MCH_EntityHeli;
import com.norwood.mcheli.helicopter.MCH_HeliInfo;
import com.norwood.mcheli.helicopter.MCH_RenderHeli;
import com.norwood.mcheli.mob.MCH_EntityGunner;
import com.norwood.mcheli.mob.MCH_RenderGunner;
import com.norwood.mcheli.multiplay.MCH_MultiplayClient;
import com.norwood.mcheli.multithread.MultiThreadModelManager;
import com.norwood.mcheli.parachute.MCH_EntityParachute;
import com.norwood.mcheli.parachute.MCH_RenderParachute;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.plane.MCP_EntityPlane;
import com.norwood.mcheli.plane.MCP_PlaneInfo;
import com.norwood.mcheli.plane.MCP_RenderPlane;
import com.norwood.mcheli.ship.MCH_ShipInfo;
import com.norwood.mcheli.tank.MCH_EntityTank;
import com.norwood.mcheli.tank.MCH_RenderTank;
import com.norwood.mcheli.tank.MCH_TankInfo;
import com.norwood.mcheli.throwable.MCH_EntityThrowable;
import com.norwood.mcheli.throwable.MCH_RenderThrowable;
import com.norwood.mcheli.throwable.MCH_ThrowableInfo;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import com.norwood.mcheli.uav.MCH_RenderUavStation;
import com.norwood.mcheli.vehicle.MCH_EntityVehicle;
import com.norwood.mcheli.vehicle.MCH_RenderVehicle;
import com.norwood.mcheli.vehicle.MCH_VehicleInfo;
import com.norwood.mcheli.weapon.*;
import com.norwood.mcheli.wrapper.*;
import com.norwood.mcheli.wrapper.modelloader.W_ModelCustom;
import com.norwood.mcheli.throwable.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.List;

public class MCH_ClientProxy extends MCH_CommonProxy {
    public String lastLoadHUDPath = "";

    public static void registerModels_Bullet() {
        for (MCH_WeaponInfo wi : ContentRegistries.weapon().values()) {
            _IModelCustom m;
            if (!wi.bulletModelName.isEmpty()) {
                m = MCH_ModelManager.load("bullets", wi.bulletModelName);
                if (m != null) {
                    wi.bulletModel = new MCH_BulletModel(wi.bulletModelName, m);
                }
            }

            if (!wi.bombletModelName.isEmpty()) {
                m = MCH_ModelManager.load("bullets", wi.bombletModelName);
                if (m != null) {
                    wi.bombletModel = new MCH_BulletModel(wi.bombletModelName, m);
                }
            }

            if (wi.cartridge != null && !wi.cartridge.name.isEmpty()) {
                wi.cartridge.model = MCH_ModelManager.load("bullets", wi.cartridge.name);
                if (wi.cartridge.model == null) {
                    wi.cartridge = null;
                }
            }
        }
    }

    @Override
    public String getDataDir() {
        return Minecraft.getMinecraft().gameDir.getPath();
    }

    @Override
    public void registerRenderer() {
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntitySeat.class, MCH_RenderTest.factory(0.0F, 0.3125F, 0.0F, "seat"));
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityHeli.class, MCH_RenderHeli.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCP_EntityPlane.class, MCP_RenderPlane.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityTank.class, MCH_RenderTank.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityGLTD.class, MCH_RenderGLTD.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityChain.class, MCH_RenderChain.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityParachute.class, MCH_RenderParachute.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityContainer.class, MCH_RenderContainer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityVehicle.class, MCH_RenderVehicle.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityUavStation.class, MCH_RenderUavStation.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityCartridge.class, MCH_RenderCartridge.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityHide.class, MCH_RenderNull.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_ViewEntityDummy.class, MCH_RenderNull.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityRocket.class, MCH_RenderBullet.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityTvMissile.class, MCH_RenderTvMissile.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityBullet.class, MCH_RenderBullet.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityA10.class, MCH_RenderA10.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityAAMissile.class, MCH_RenderAAMissile.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityASMissile.class, MCH_RenderASMissile.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityATMissile.class, MCH_RenderTvMissile.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityTorpedo.class, MCH_RenderBullet.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityBomb.class, MCH_RenderBomb.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityMarkerRocket.class, MCH_RenderBullet.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityDispensedItem.class, MCH_RenderNone.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityFlare.class, MCH_RenderFlare.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityThrowable.class, MCH_RenderThrowable.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(MCH_EntityGunner.class, MCH_RenderGunner.FACTORY);
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemJavelin, new BuiltInLightWeaponItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemStinger, new BuiltInLightWeaponItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.invisibleItem, new BuiltInInvisibleItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemGLTD, new BuiltInGLTDItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemWrench, new BuiltInWrenchItemRenderer());
        MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemRangeFinder, new BuiltInRangeFinderItemRenderer());
        ModelLoaderRegistry.registerLoader(LegacyModelLoader.INSTANCE);
    }

    @Override
    public void registerBlockRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(MCH_DraftingTableTileEntity.class, new MCH_DraftingTableRenderer());
        MCH_ItemModelRenderers.registerRenderer(W_Item.getItemFromBlock(MCH_MOD.blockDraftingTable), new BuiltInDraftingTableItemRenderer());
    }

    @Override
    public void registerModels() {
        MCH_ModelManager.setForceReloadMode(true);
        MCH_RenderAircraft.debugModel = MCH_ModelManager.load("box");
        MCH_ModelManager.load("a-10");
        MCH_RenderGLTD.model = MCH_ModelManager.load("gltd");
        MCH_ModelManager.load("chain");
        MCH_ModelManager.load("container");
        MCH_ModelManager.load("parachute1");
        MCH_ModelManager.load("parachute2");
        MCH_ModelManager.load("lweapons", "fim92");
        MCH_ModelManager.load("lweapons", "fgm148");

        for (String s : MCH_RenderUavStation.MODEL_NAME) {
            MCH_ModelManager.load(s);
        }

        MCH_ModelManager.load("wrench");
        MCH_ModelManager.load("rangefinder");

        if (MCH_Config.MultiThreadedModelLoading.prmBool) {
            System.out.println("Starting multithreaded model loading");
            MultiThreadModelManager.start(this);
            return;
        }

        ContentRegistries.heli().forEachValue(info -> this.registerModelsHeli(info, false));
        ContentRegistries.plane().forEachValue(info -> this.registerModelsPlane(info, false));
        ContentRegistries.ship().forEachValue(info -> this.registerModelsShip(info, false));
        ContentRegistries.tank().forEachValue(info -> this.registerModelsTank(info, false));
        ContentRegistries.vehicle().forEachValue(info -> this.registerModelsVehicle(info, false));
        registerModels_Bullet();
        MCH_DefaultBulletModels.Bullet = this.loadBulletModel("bullet");
        MCH_DefaultBulletModels.AAMissile = this.loadBulletModel("aamissile");
        MCH_DefaultBulletModels.ATMissile = this.loadBulletModel("asmissile");
        MCH_DefaultBulletModels.ASMissile = this.loadBulletModel("asmissile");
        MCH_DefaultBulletModels.Bomb = this.loadBulletModel("bomb");
        MCH_DefaultBulletModels.Rocket = this.loadBulletModel("rocket");
        MCH_DefaultBulletModels.Torpedo = this.loadBulletModel("torpedo");

        for (MCH_ThrowableInfo wi : ContentRegistries.throwable().values()) {
            wi.model = MCH_ModelManager.load("throwable", wi.name);
        }

        MCH_ModelManager.load("blocks", "drafting_table");
    }

    public static void registerModels_Throwable(){
        System.out.println("Loading throwable");

        for (Object obj : MCH_ThrowableInfoManager.getValues()) {
            if (obj instanceof MCH_ThrowableInfo) { // Ensure the object is of type MCH_ThrowableInfo
                MCH_ThrowableInfo throwableInfo = (MCH_ThrowableInfo) obj;
                _IModelCustom modelCustom = MCH_ModelManager.load("throwable", throwableInfo.name);
                if (modelCustom != null) {
                    System.out.println("Adding model for " + throwableInfo.name);
                    throwableInfo.model = modelCustom;
                } else {
                    System.out.println("ERROR: No model found for throwable " + throwableInfo.name);
                }
            } else {
                System.out.println("ERROR: Invalid object type in throwable info manager");
            }
        }
    }

    @Override
    public void registerModelsHeli(MCH_HeliInfo info, boolean reload) {
        MCH_ModelManager.setForceReloadMode(reload);
        info.model = MCH_ModelManager.load("helicopters", info.name);

        for (MCH_HeliInfo.Rotor rotor : info.rotorList) {
            rotor.model = this.loadPartModel("helicopters", info.name, info.model, rotor.modelName);
        }

        this.registerCommonPart("helicopters", info);
        MCH_ModelManager.setForceReloadMode(false);
    }

    /*** 1.7 method
     public void registerModelsHeli(String name, boolean reload) {
     MCH_ModelManager.setForceReloadMode(reload);
     MCH_HeliInfo info = (MCH_HeliInfo)MCH_HeliInfoManager.map.get(name);
     info.model = MCH_ModelManager.load("helicopters", info.name);

     MCH_HeliInfo.Rotor rotor;
     for(Iterator i$ = info.rotorList.iterator(); i$.hasNext(); rotor.model = this.loadPartModel("helicopters", info.name, info.model, rotor.modelName)) {
     rotor = (MCH_HeliInfo.Rotor)i$.next();
     }

     this.registerCommonPart("helicopters", info);
     MCH_ModelManager.setForceReloadMode(false);
     }
     */

    @Override
    public void registerModelsPlane(MCP_PlaneInfo info, boolean reload) {
        MCH_ModelManager.setForceReloadMode(reload);
        info.model = MCH_ModelManager.load("planes", info.name);

        for (MCH_AircraftInfo.DrawnPart n : info.nozzles) {
            n.model = this.loadPartModel("planes", info.name, info.model, n.modelName);
        }

        for (MCP_PlaneInfo.Rotor r : info.rotorList) {
            r.model = this.loadPartModel("planes", info.name, info.model, r.modelName);

            for (MCP_PlaneInfo.Blade b : r.blades) {
                b.model = this.loadPartModel("planes", info.name, info.model, b.modelName);
            }
        }

        for (MCP_PlaneInfo.Wing w : info.wingList) {
            w.model = this.loadPartModel("planes", info.name, info.model, w.modelName);
            if (w.pylonList != null) {
                for (MCP_PlaneInfo.Pylon p : w.pylonList) {
                    p.model = this.loadPartModel("planes", info.name, info.model, p.modelName);
                }
            }
        }

        this.registerCommonPart("planes", info);
        MCH_ModelManager.setForceReloadMode(false);
    }

    @Override
    public void registerModelsShip(MCH_ShipInfo info, boolean reload) {
        MCH_ModelManager.setForceReloadMode(reload);
        info.model = MCH_ModelManager.load("planes", info.name);

        for (MCH_AircraftInfo.DrawnPart n : info.nozzles) {
            n.model = this.loadPartModel("ships", info.name, info.model, n.modelName);
        }

        for (MCH_ShipInfo.Rotor r : info.rotorList) {
            r.model = this.loadPartModel("ships", info.name, info.model, r.modelName);

            for (MCH_ShipInfo.Blade b : r.blades) {
                b.model = this.loadPartModel("ships", info.name, info.model, b.modelName);
            }
        }

        for (MCH_ShipInfo.Wing w : info.wingList) {
            w.model = this.loadPartModel("ships", info.name, info.model, w.modelName);
            if (w.pylonList != null) {
                for (MCH_ShipInfo.Pylon p : w.pylonList) {
                    p.model = this.loadPartModel("ships", info.name, info.model, p.modelName);
                }
            }
        }

        this.registerCommonPart("ships", info);
        MCH_ModelManager.setForceReloadMode(false);
    }

    @Override
    public void registerModelsVehicle(MCH_VehicleInfo info, boolean reload) {
        MCH_ModelManager.setForceReloadMode(reload);
        info.model = MCH_ModelManager.load("vehicles", info.name);

        for (MCH_VehicleInfo.VPart vp : info.partList) {
            vp.model = this.loadPartModel("vehicles", info.name, info.model, vp.modelName);
            if (vp.child != null) {
                this.registerVCPModels(info, vp);
            }
        }

        this.registerCommonPart("vehicles", info);
        MCH_ModelManager.setForceReloadMode(false);
    }

    @Override
    public void registerModelsTank(MCH_TankInfo info, boolean reload) {
        MCH_ModelManager.setForceReloadMode(reload);
        info.model = MCH_ModelManager.load("tanks", info.name);
        this.registerCommonPart("tanks", info);
        MCH_ModelManager.setForceReloadMode(false);
    }

    public MCH_BulletModel loadBulletModel(String name) {
        _IModelCustom m = MCH_ModelManager.load("bullets", name);
        return m != null ? new MCH_BulletModel(name, m) : null;
    }

    private _IModelCustom loadPartModel(String path, String name, _IModelCustom body, String part) {
        return body instanceof W_ModelCustom && ((W_ModelCustom) body).containsPart("$" + part) ? null : MCH_ModelManager.load(path, name + "_" + part);
    }

    private void registerCommonPart(String path, MCH_AircraftInfo info) {
        for (MCH_AircraftInfo.Hatch h : info.hatchList) {
            h.model = this.loadPartModel(path, info.name, info.model, h.modelName);
        }

        for (MCH_AircraftInfo.Camera c : info.cameraList) {
            c.model = this.loadPartModel(path, info.name, info.model, c.modelName);
        }

        for (MCH_AircraftInfo.Throttle c : info.partThrottle) {
            c.model = this.loadPartModel(path, info.name, info.model, c.modelName);
        }

        for (MCH_AircraftInfo.RotPart c : info.partRotPart) {
            c.model = this.loadPartModel(path, info.name, info.model, c.modelName);
        }

        for (MCH_AircraftInfo.PartWeapon p : info.partWeapon) {
            p.model = this.loadPartModel(path, info.name, info.model, p.modelName);

            for (MCH_AircraftInfo.PartWeaponChild wc : p.child) {
                wc.model = this.loadPartModel(path, info.name, info.model, wc.modelName);
            }
        }

        for (MCH_AircraftInfo.Canopy c : info.canopyList) {
            c.model = this.loadPartModel(path, info.name, info.model, c.modelName);
        }

        for (MCH_AircraftInfo.DrawnPart n : info.landingGear) {
            n.model = this.loadPartModel(path, info.name, info.model, n.modelName);
        }

        for (MCH_AircraftInfo.WeaponBay w : info.partWeaponBay) {
            w.model = this.loadPartModel(path, info.name, info.model, w.modelName);
        }

        for (MCH_AircraftInfo.CrawlerTrack c : info.partCrawlerTrack) {
            c.model = this.loadPartModel(path, info.name, info.model, c.modelName);
        }

        for (MCH_AircraftInfo.TrackRoller c : info.partTrackRoller) {
            c.model = this.loadPartModel(path, info.name, info.model, c.modelName);
        }

        for (MCH_AircraftInfo.PartWheel c : info.partWheel) {
            c.model = this.loadPartModel(path, info.name, info.model, c.modelName);
        }

        for (MCH_AircraftInfo.PartWheel c : info.partSteeringWheel) {
            c.model = this.loadPartModel(path, info.name, info.model, c.modelName);
        }
    }

    private void registerVCPModels(MCH_VehicleInfo info, MCH_VehicleInfo.VPart vp) {
        for (MCH_VehicleInfo.VPart vcp : vp.child) {
            vcp.model = this.loadPartModel("vehicles", info.name, info.model, vcp.modelName);
            if (vcp.child != null) {
                this.registerVCPModels(info, vcp);
            }
        }
    }

    @Override
    public void registerClientTick() {
        Minecraft mc = Minecraft.getMinecraft();
        MCH_ClientCommonTickHandler.instance = new MCH_ClientCommonTickHandler(mc, MCH_MOD.config);
        W_TickRegistry.registerTickHandler(MCH_ClientCommonTickHandler.instance, Side.CLIENT);
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    @Override
    public String side() {
        return "Client";
    }

    @Override
    public MCH_SoundUpdater CreateSoundUpdater(MCH_EntityAircraft aircraft) {
        return aircraft != null && aircraft.world.isRemote ? new MCH_SoundUpdater(Minecraft.getMinecraft(), aircraft, Minecraft.getMinecraft().player) : null;
    }

    @Override
    public void registerSounds() {
        super.registerSounds();
        W_McClient.addSound("alert.ogg");
        W_McClient.addSound("locked.ogg");
        W_McClient.addSound("gltd.ogg");
        W_McClient.addSound("zoom.ogg");
        W_McClient.addSound("ng.ogg");
        W_McClient.addSound("a-10_snd.ogg");
        W_McClient.addSound("gau-8_snd.ogg");
        W_McClient.addSound("hit.ogg");
        W_McClient.addSound("helidmg.ogg");
        W_McClient.addSound("heli.ogg");
        W_McClient.addSound("plane.ogg");
        W_McClient.addSound("plane_cc.ogg");
        W_McClient.addSound("plane_cv.ogg");
        W_McClient.addSound("chain.ogg");
        W_McClient.addSound("chain_ct.ogg");
        W_McClient.addSound("eject_seat.ogg");
        W_McClient.addSound("fim92_snd.ogg");
        W_McClient.addSound("fim92_reload.ogg");
        W_McClient.addSound("lockon.ogg");

        for (MCH_WeaponInfo info : ContentRegistries.weapon().values()) {
            W_McClient.addSound(info.soundFileName + ".ogg");
        }

        for (MCH_AircraftInfo info : ContentRegistries.plane().values()) {
            if (!info.soundMove.isEmpty()) {
                W_McClient.addSound(info.soundMove + ".ogg");
            }
        }

        for (MCH_AircraftInfo infox : ContentRegistries.heli().values()) {
            if (!infox.soundMove.isEmpty()) {
                W_McClient.addSound(infox.soundMove + ".ogg");
            }
        }

        for (MCH_AircraftInfo infoxx : ContentRegistries.tank().values()) {
            if (!infoxx.soundMove.isEmpty()) {
                W_McClient.addSound(infoxx.soundMove + ".ogg");
            }
        }

        for (MCH_AircraftInfo infoxxx : ContentRegistries.vehicle().values()) {
            if (!infoxxx.soundMove.isEmpty()) {
                W_McClient.addSound(infoxxx.soundMove + ".ogg");
            }
        }
    }

    @Override
    public void loadConfig(String fileName) {
        this.lastConfigFileName = fileName;
        this.config = new MCH_Config(Minecraft.getMinecraft().gameDir.getPath(), "/" + fileName);
        this.config.load();
        this.config.write();
    }

    @Override
    public void reconfig() {
        MCH_Lib.DbgLog(false, "MCH_ClientProxy.reconfig()");
        this.loadConfig(this.lastConfigFileName);
        MCH_ClientCommonTickHandler.instance.updatekeybind(this.config);
    }

    @Override
    public void reloadHUD() {
        ContentRegistries.hud().reloadAll();
    }

    @Override
    public Entity getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new MCH_ParticlesUtil());
        MinecraftForge.EVENT_BUS.register(new MCH_ClientEventHook());
    }

    @Override
    public void setCreativeDigDelay(int n) {
        W_Reflection.setCreativeDigSpeed(n);
    }

    @Override
    public boolean isFirstPerson() {
        return Minecraft.getMinecraft().gameSettings.thirdPersonView == 0;
    }

    @Override
    public boolean isSinglePlayer() {
        return Minecraft.getMinecraft().isSingleplayer();
    }

    @Override
    public void readClientModList() {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            MCH_MultiplayClient.readModList(mc.getSession().getPlayerID(), mc.getSession().getUsername());
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    @Override
    public void printChatMessage(ITextComponent chat, int showTime, int pos) {
        ((MCH_GuiTitle) MCH_ClientCommonTickHandler.instance.gui_Title).setupTitle(chat, showTime, pos);
    }

    @Override
    public void hitBullet() {
        MCH_ClientCommonTickHandler.instance.gui_Common.hitBullet();
    }

    @Override
    public void clientLocked() {
        MCH_ClientCommonTickHandler.isLocked = true;
    }

    @Override
    public void setRenderEntityDistanceWeight(double renderDistWeight) {
        Entity.setRenderDistanceWeight(renderDistWeight);
    }

    @Override
    public List<AddonPack> loadAddonPacks(File addonDir) {
        return AddonManager.loadAddonsAndAddResources(addonDir);
    }

    @Override
    public boolean canLoadContentDirName(String dir) {
        return "hud".equals(dir) || super.canLoadContentDirName(dir);
    }

    @Override
    public void updateGeneratedLanguage() {
        W_LanguageRegistry.updateGeneratedLang();
    }

    @Deprecated
    @Override
    public void updateSoundsJson() {
        MCH_SoundsJson.updateGenerated();
    }
}
