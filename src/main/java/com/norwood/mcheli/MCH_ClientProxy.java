package com.norwood.mcheli;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import com.norwood.mcheli.__helper.addon.AddonManager;
import com.norwood.mcheli.__helper.addon.AddonPack;
import com.norwood.mcheli.__helper.client.MCH_ItemModelRenderers;
import com.norwood.mcheli.__helper.client.RecipeDescriptionManager;
import com.norwood.mcheli.__helper.client._IModelCustom;
import com.norwood.mcheli.__helper.client.model.LegacyModelLoader;
import com.norwood.mcheli.__helper.client.renderer.item.BuiltInDraftingTableItemRenderer;
import com.norwood.mcheli.__helper.client.renderer.item.BuiltInGLTDItemRenderer;
import com.norwood.mcheli.__helper.client.renderer.item.BuiltInInvisibleItemRenderer;
import com.norwood.mcheli.__helper.client.renderer.item.BuiltInLightWeaponItemRenderer;
import com.norwood.mcheli.__helper.client.renderer.item.BuiltInRangeFinderItemRenderer;
import com.norwood.mcheli.__helper.client.renderer.item.BuiltInWrenchItemRenderer;
import com.norwood.mcheli.__helper.info.ContentRegistries;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntityHide;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.aircraft.MCH_RenderAircraft;
import com.norwood.mcheli.aircraft.MCH_SoundUpdater;
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
import com.norwood.mcheli.parachute.MCH_EntityParachute;
import com.norwood.mcheli.parachute.MCH_RenderParachute;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.plane.MCP_EntityPlane;
import com.norwood.mcheli.plane.MCP_PlaneInfo;
import com.norwood.mcheli.plane.MCP_RenderPlane;
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
import com.norwood.mcheli.weapon.MCH_BulletModel;
import com.norwood.mcheli.weapon.MCH_DefaultBulletModels;
import com.norwood.mcheli.weapon.MCH_EntityA10;
import com.norwood.mcheli.weapon.MCH_EntityAAMissile;
import com.norwood.mcheli.weapon.MCH_EntityASMissile;
import com.norwood.mcheli.weapon.MCH_EntityATMissile;
import com.norwood.mcheli.weapon.MCH_EntityBomb;
import com.norwood.mcheli.weapon.MCH_EntityBullet;
import com.norwood.mcheli.weapon.MCH_EntityCartridge;
import com.norwood.mcheli.weapon.MCH_EntityDispensedItem;
import com.norwood.mcheli.weapon.MCH_EntityMarkerRocket;
import com.norwood.mcheli.weapon.MCH_EntityRocket;
import com.norwood.mcheli.weapon.MCH_EntityTorpedo;
import com.norwood.mcheli.weapon.MCH_EntityTvMissile;
import com.norwood.mcheli.weapon.MCH_RenderA10;
import com.norwood.mcheli.weapon.MCH_RenderAAMissile;
import com.norwood.mcheli.weapon.MCH_RenderASMissile;
import com.norwood.mcheli.weapon.MCH_RenderBomb;
import com.norwood.mcheli.weapon.MCH_RenderBullet;
import com.norwood.mcheli.weapon.MCH_RenderCartridge;
import com.norwood.mcheli.weapon.MCH_RenderNone;
import com.norwood.mcheli.weapon.MCH_RenderTvMissile;
import com.norwood.mcheli.weapon.MCH_WeaponInfo;
import com.norwood.mcheli.wrapper.W_LanguageRegistry;
import com.norwood.mcheli.wrapper.W_McClient;
import com.norwood.mcheli.wrapper.W_Reflection;
import com.norwood.mcheli.wrapper.W_TickRegistry;
import com.norwood.mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_ClientProxy extends MCH_CommonProxy {
   public String lastLoadHUDPath = "";

   public String getDataDir() {
      return Minecraft.func_71410_x().field_71412_D.getPath();
   }

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
      MCH_ItemModelRenderers.registerRenderer(MCH_MOD.itemDraftingTable, new BuiltInDraftingTableItemRenderer());
      ModelLoaderRegistry.registerLoader(LegacyModelLoader.INSTANCE);
   }

   public void registerBlockRenderer() {
      ClientRegistry.bindTileEntitySpecialRenderer(MCH_DraftingTableTileEntity.class, new MCH_DraftingTableRenderer());
   }

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
      String[] var1 = MCH_RenderUavStation.MODEL_NAME;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String s = var1[var3];
         MCH_ModelManager.load(s);
      }

      MCH_ModelManager.load("wrench");
      MCH_ModelManager.load("rangefinder");
      ContentRegistries.heli().forEachValue((info) -> {
         this.registerModelsHeli(info, false);
      });
      ContentRegistries.plane().forEachValue((info) -> {
         this.registerModelsPlane(info, false);
      });
      ContentRegistries.tank().forEachValue((info) -> {
         this.registerModelsTank(info, false);
      });
      ContentRegistries.vehicle().forEachValue((info) -> {
         this.registerModelsVehicle(info, false);
      });
      registerModels_Bullet();
      MCH_DefaultBulletModels.Bullet = this.loadBulletModel("bullet");
      MCH_DefaultBulletModels.AAMissile = this.loadBulletModel("aamissile");
      MCH_DefaultBulletModels.ATMissile = this.loadBulletModel("asmissile");
      MCH_DefaultBulletModels.ASMissile = this.loadBulletModel("asmissile");
      MCH_DefaultBulletModels.Bomb = this.loadBulletModel("bomb");
      MCH_DefaultBulletModels.Rocket = this.loadBulletModel("rocket");
      MCH_DefaultBulletModels.Torpedo = this.loadBulletModel("torpedo");

      MCH_ThrowableInfo wi;
      for(Iterator var5 = ContentRegistries.throwable().values().iterator(); var5.hasNext(); wi.model = MCH_ModelManager.load("throwable", wi.name)) {
         wi = (MCH_ThrowableInfo)var5.next();
      }

      MCH_ModelManager.load("blocks", "drafting_table");
   }

   public static void registerModels_Bullet() {
      Iterator var0 = ContentRegistries.weapon().values().iterator();

      while(var0.hasNext()) {
         MCH_WeaponInfo wi = (MCH_WeaponInfo)var0.next();
         _IModelCustom m = null;
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

   public void registerModelsHeli(MCH_HeliInfo info, boolean reload) {
      MCH_ModelManager.setForceReloadMode(reload);
      info.model = MCH_ModelManager.load("helicopters", info.name);

      MCH_HeliInfo.Rotor rotor;
      for(Iterator var3 = info.rotorList.iterator(); var3.hasNext(); rotor.model = this.loadPartModel("helicopters", info.name, info.model, rotor.modelName)) {
         rotor = (MCH_HeliInfo.Rotor)var3.next();
      }

      this.registerCommonPart("helicopters", info);
      MCH_ModelManager.setForceReloadMode(false);
   }

   public void registerModelsPlane(MCP_PlaneInfo info, boolean reload) {
      MCH_ModelManager.setForceReloadMode(reload);
      info.model = MCH_ModelManager.load("planes", info.name);

      Iterator var3;
      MCH_AircraftInfo.DrawnPart n;
      for(var3 = info.nozzles.iterator(); var3.hasNext(); n.model = this.loadPartModel("planes", info.name, info.model, n.modelName)) {
         n = (MCH_AircraftInfo.DrawnPart)var3.next();
      }

      var3 = info.rotorList.iterator();

      Iterator var5;
      while(var3.hasNext()) {
         MCP_PlaneInfo.Rotor r = (MCP_PlaneInfo.Rotor)var3.next();
         r.model = this.loadPartModel("planes", info.name, info.model, r.modelName);

         MCP_PlaneInfo.Blade b;
         for(var5 = r.blades.iterator(); var5.hasNext(); b.model = this.loadPartModel("planes", info.name, info.model, b.modelName)) {
            b = (MCP_PlaneInfo.Blade)var5.next();
         }
      }

      var3 = info.wingList.iterator();

      while(true) {
         MCP_PlaneInfo.Wing w;
         do {
            if (!var3.hasNext()) {
               this.registerCommonPart("planes", info);
               MCH_ModelManager.setForceReloadMode(false);
               return;
            }

            w = (MCP_PlaneInfo.Wing)var3.next();
            w.model = this.loadPartModel("planes", info.name, info.model, w.modelName);
         } while(w.pylonList == null);

         MCP_PlaneInfo.Pylon p;
         for(var5 = w.pylonList.iterator(); var5.hasNext(); p.model = this.loadPartModel("planes", info.name, info.model, p.modelName)) {
            p = (MCP_PlaneInfo.Pylon)var5.next();
         }
      }
   }

   public void registerModelsVehicle(MCH_VehicleInfo info, boolean reload) {
      MCH_ModelManager.setForceReloadMode(reload);
      info.model = MCH_ModelManager.load("vehicles", info.name);
      Iterator var3 = info.partList.iterator();

      while(var3.hasNext()) {
         MCH_VehicleInfo.VPart vp = (MCH_VehicleInfo.VPart)var3.next();
         vp.model = this.loadPartModel("vehicles", info.name, info.model, vp.modelName);
         if (vp.child != null) {
            this.registerVCPModels(info, vp);
         }
      }

      this.registerCommonPart("vehicles", info);
      MCH_ModelManager.setForceReloadMode(false);
   }

   public void registerModelsTank(MCH_TankInfo info, boolean reload) {
      MCH_ModelManager.setForceReloadMode(reload);
      info.model = MCH_ModelManager.load("tanks", info.name);
      this.registerCommonPart("tanks", info);
      MCH_ModelManager.setForceReloadMode(false);
   }

   private MCH_BulletModel loadBulletModel(String name) {
      _IModelCustom m = MCH_ModelManager.load("bullets", name);
      return m != null ? new MCH_BulletModel(name, m) : null;
   }

   private _IModelCustom loadPartModel(String path, String name, _IModelCustom body, String part) {
      return body instanceof W_ModelCustom && ((W_ModelCustom)body).containsPart("$" + part) ? null : MCH_ModelManager.load(path, name + "_" + part);
   }

   private void registerCommonPart(String path, MCH_AircraftInfo info) {
      Iterator var3;
      MCH_AircraftInfo.Hatch h;
      for(var3 = info.hatchList.iterator(); var3.hasNext(); h.model = this.loadPartModel(path, info.name, info.model, h.modelName)) {
         h = (MCH_AircraftInfo.Hatch)var3.next();
      }

      MCH_AircraftInfo.Camera c;
      for(var3 = info.cameraList.iterator(); var3.hasNext(); c.model = this.loadPartModel(path, info.name, info.model, c.modelName)) {
         c = (MCH_AircraftInfo.Camera)var3.next();
      }

      MCH_AircraftInfo.Throttle c;
      for(var3 = info.partThrottle.iterator(); var3.hasNext(); c.model = this.loadPartModel(path, info.name, info.model, c.modelName)) {
         c = (MCH_AircraftInfo.Throttle)var3.next();
      }

      MCH_AircraftInfo.RotPart c;
      for(var3 = info.partRotPart.iterator(); var3.hasNext(); c.model = this.loadPartModel(path, info.name, info.model, c.modelName)) {
         c = (MCH_AircraftInfo.RotPart)var3.next();
      }

      var3 = info.partWeapon.iterator();

      while(var3.hasNext()) {
         MCH_AircraftInfo.PartWeapon p = (MCH_AircraftInfo.PartWeapon)var3.next();
         p.model = this.loadPartModel(path, info.name, info.model, p.modelName);

         MCH_AircraftInfo.PartWeaponChild wc;
         for(Iterator var5 = p.child.iterator(); var5.hasNext(); wc.model = this.loadPartModel(path, info.name, info.model, wc.modelName)) {
            wc = (MCH_AircraftInfo.PartWeaponChild)var5.next();
         }
      }

      MCH_AircraftInfo.Canopy c;
      for(var3 = info.canopyList.iterator(); var3.hasNext(); c.model = this.loadPartModel(path, info.name, info.model, c.modelName)) {
         c = (MCH_AircraftInfo.Canopy)var3.next();
      }

      MCH_AircraftInfo.DrawnPart n;
      for(var3 = info.landingGear.iterator(); var3.hasNext(); n.model = this.loadPartModel(path, info.name, info.model, n.modelName)) {
         n = (MCH_AircraftInfo.DrawnPart)var3.next();
      }

      MCH_AircraftInfo.WeaponBay w;
      for(var3 = info.partWeaponBay.iterator(); var3.hasNext(); w.model = this.loadPartModel(path, info.name, info.model, w.modelName)) {
         w = (MCH_AircraftInfo.WeaponBay)var3.next();
      }

      MCH_AircraftInfo.CrawlerTrack c;
      for(var3 = info.partCrawlerTrack.iterator(); var3.hasNext(); c.model = this.loadPartModel(path, info.name, info.model, c.modelName)) {
         c = (MCH_AircraftInfo.CrawlerTrack)var3.next();
      }

      MCH_AircraftInfo.TrackRoller c;
      for(var3 = info.partTrackRoller.iterator(); var3.hasNext(); c.model = this.loadPartModel(path, info.name, info.model, c.modelName)) {
         c = (MCH_AircraftInfo.TrackRoller)var3.next();
      }

      MCH_AircraftInfo.PartWheel c;
      for(var3 = info.partWheel.iterator(); var3.hasNext(); c.model = this.loadPartModel(path, info.name, info.model, c.modelName)) {
         c = (MCH_AircraftInfo.PartWheel)var3.next();
      }

      for(var3 = info.partSteeringWheel.iterator(); var3.hasNext(); c.model = this.loadPartModel(path, info.name, info.model, c.modelName)) {
         c = (MCH_AircraftInfo.PartWheel)var3.next();
      }

   }

   private void registerVCPModels(MCH_VehicleInfo info, MCH_VehicleInfo.VPart vp) {
      Iterator var3 = vp.child.iterator();

      while(var3.hasNext()) {
         MCH_VehicleInfo.VPart vcp = (MCH_VehicleInfo.VPart)var3.next();
         vcp.model = this.loadPartModel("vehicles", info.name, info.model, vcp.modelName);
         if (vcp.child != null) {
            this.registerVCPModels(info, vcp);
         }
      }

   }

   public void registerClientTick() {
      Minecraft mc = Minecraft.func_71410_x();
      MCH_ClientCommonTickHandler.instance = new MCH_ClientCommonTickHandler(mc, MCH_MOD.config);
      W_TickRegistry.registerTickHandler(MCH_ClientCommonTickHandler.instance, Side.CLIENT);
   }

   public boolean isRemote() {
      return true;
   }

   public String side() {
      return "Client";
   }

   public MCH_SoundUpdater CreateSoundUpdater(MCH_EntityAircraft aircraft) {
      return aircraft != null && aircraft.world.isRemote ? new MCH_SoundUpdater(Minecraft.func_71410_x(), aircraft, Minecraft.func_71410_x().field_71439_g) : null;
   }

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
      Iterator var1 = ContentRegistries.weapon().values().iterator();

      while(var1.hasNext()) {
         MCH_WeaponInfo info = (MCH_WeaponInfo)var1.next();
         W_McClient.addSound(info.soundFileName + ".ogg");
      }

      var1 = ContentRegistries.plane().values().iterator();

      MCH_AircraftInfo info;
      while(var1.hasNext()) {
         info = (MCH_AircraftInfo)var1.next();
         if (!info.soundMove.isEmpty()) {
            W_McClient.addSound(info.soundMove + ".ogg");
         }
      }

      var1 = ContentRegistries.heli().values().iterator();

      while(var1.hasNext()) {
         info = (MCH_AircraftInfo)var1.next();
         if (!info.soundMove.isEmpty()) {
            W_McClient.addSound(info.soundMove + ".ogg");
         }
      }

      var1 = ContentRegistries.tank().values().iterator();

      while(var1.hasNext()) {
         info = (MCH_AircraftInfo)var1.next();
         if (!info.soundMove.isEmpty()) {
            W_McClient.addSound(info.soundMove + ".ogg");
         }
      }

      var1 = ContentRegistries.vehicle().values().iterator();

      while(var1.hasNext()) {
         info = (MCH_AircraftInfo)var1.next();
         if (!info.soundMove.isEmpty()) {
            W_McClient.addSound(info.soundMove + ".ogg");
         }
      }

   }

   public void loadConfig(String fileName) {
      this.lastConfigFileName = fileName;
      this.config = new MCH_Config(Minecraft.func_71410_x().field_71412_D.getPath(), "/" + fileName);
      this.config.load();
      this.config.write();
   }

   public void reconfig() {
      MCH_Lib.DbgLog(false, "MCH_ClientProxy.reconfig()");
      this.loadConfig(this.lastConfigFileName);
      MCH_ClientCommonTickHandler.instance.updatekeybind(this.config);
   }

   public void reloadHUD() {
      ContentRegistries.hud().reloadAll();
   }

   public Entity getClientPlayer() {
      return Minecraft.func_71410_x().field_71439_g;
   }

   public void init() {
      MinecraftForge.EVENT_BUS.register(new MCH_ParticlesUtil());
      MinecraftForge.EVENT_BUS.register(new MCH_ClientEventHook());
   }

   public void setCreativeDigDelay(int n) {
      W_Reflection.setCreativeDigSpeed(n);
   }

   public boolean isFirstPerson() {
      return Minecraft.func_71410_x().field_71474_y.field_74320_O == 0;
   }

   public boolean isSinglePlayer() {
      return Minecraft.func_71410_x().func_71356_B();
   }

   public void readClientModList() {
      try {
         Minecraft mc = Minecraft.func_71410_x();
         MCH_MultiplayClient.readModList(mc.func_110432_I().func_148255_b(), mc.func_110432_I().func_111285_a());
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void printChatMessage(ITextComponent chat, int showTime, int pos) {
      ((MCH_GuiTitle)MCH_ClientCommonTickHandler.instance.gui_Title).setupTitle(chat, showTime, pos);
   }

   public void hitBullet() {
      MCH_ClientCommonTickHandler.instance.gui_Common.hitBullet();
   }

   public void clientLocked() {
      MCH_ClientCommonTickHandler.isLocked = true;
   }

   public void setRenderEntityDistanceWeight(double renderDistWeight) {
      Entity.func_184227_b(renderDistWeight);
   }

   public List<AddonPack> loadAddonPacks(File addonDir) {
      return AddonManager.loadAddonsAndAddResources(addonDir);
   }

   public boolean canLoadContentDirName(String dir) {
      return "hud".equals(dir) || super.canLoadContentDirName(dir);
   }

   public void updateGeneratedLanguage() {
      W_LanguageRegistry.updateGeneratedLang();
   }

   public void registerRecipeDescriptions() {
      RecipeDescriptionManager.registerDescriptionInfos(Minecraft.func_71410_x().func_110442_L());
   }
}
