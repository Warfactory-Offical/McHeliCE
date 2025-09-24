package com.norwood.mcheli.gltd;

import com.norwood.mcheli.*;
import com.norwood.mcheli.networking.packet.PacketGLTDControl;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_ClientGLTDTickHandler extends MCH_ClientTickHandlerBase {
    public MCH_Key KeyUseWeapon;
    public MCH_Key KeySwitchWeapon1;
    public MCH_Key KeySwitchWeapon2;
    public MCH_Key KeySwWeaponMode;
    public MCH_Key KeyZoom;
    public MCH_Key KeyCameraMode;
    public MCH_Key KeyUnmount;
    public MCH_Key KeyUnmount_1_6;
    public MCH_Key[] Keys;
    protected boolean isRiding = false;
    protected boolean isBeforeRiding = false;

    public MCH_ClientGLTDTickHandler(Minecraft minecraft, MCH_Config config) {
        super(minecraft);
        this.updateKeybind(config);
    }

    @Override
    public void updateKeybind(MCH_Config config) {
        this.KeyUseWeapon = new MCH_Key(MCH_Config.KeyUseWeapon.prmInt);
        this.KeySwitchWeapon1 = new MCH_Key(MCH_Config.KeySwitchWeapon1.prmInt);
        this.KeySwitchWeapon2 = new MCH_Key(MCH_Config.KeySwitchWeapon2.prmInt);
        this.KeySwWeaponMode = new MCH_Key(MCH_Config.KeySwWeaponMode.prmInt);
        this.KeyZoom = new MCH_Key(MCH_Config.KeyZoom.prmInt);
        this.KeyCameraMode = new MCH_Key(MCH_Config.KeyCameraMode.prmInt);
        this.KeyUnmount = new MCH_Key(MCH_Config.KeyUnmount.prmInt);
        this.KeyUnmount_1_6 = new MCH_Key(42);
        this.Keys = new MCH_Key[]{
                this.KeyUseWeapon,
                this.KeySwWeaponMode,
                this.KeySwitchWeapon1,
                this.KeySwitchWeapon2,
                this.KeyZoom,
                this.KeyCameraMode,
                this.KeyUnmount,
                this.KeyUnmount_1_6
        };
    }

    protected void updateGLTD(EntityPlayer player, MCH_EntityGLTD gltd) {
        if (player.rotationPitch < -70.0F) {
            player.rotationPitch = -70.0F;
        }

        if (player.rotationPitch > 70.0F) {
            player.rotationPitch = 70.0F;
        }

        float yaw = gltd.rotationYaw;
        if (player.rotationYaw < yaw - 70.0F) {
            player.rotationYaw = yaw - 70.0F;
        }

        if (player.rotationYaw > yaw + 70.0F) {
            player.rotationYaw = yaw + 70.0F;
        }

        gltd.camera.rotationYaw = player.rotationYaw;
        gltd.camera.rotationPitch = player.rotationPitch;
    }

    @Override
    protected void onTick(boolean inGUI) {
        for (MCH_Key k : this.Keys) {
            k.update();
        }

        this.isBeforeRiding = this.isRiding;
        EntityPlayer player = this.mc.player;
        MCH_ViewEntityDummy viewEntityDummy = null;
        if (player != null && player.getRidingEntity() instanceof MCH_EntityGLTD gltd) {
            this.updateGLTD(player, gltd);
            MCH_Lib.disableFirstPersonItemRender(player.getHeldItemMainhand());
            viewEntityDummy = MCH_ViewEntityDummy.getInstance(this.mc.world);
            viewEntityDummy.update(gltd.camera);
            if (!inGUI) {
                this.playerControl(player, gltd);
            }

            MCH_Lib.setRenderViewEntity(viewEntityDummy);
            this.isRiding = true;
        } else {
            this.isRiding = false;
        }

        if (this.isBeforeRiding != this.isRiding) {
            if (this.isRiding) {
                viewEntityDummy.prevPosX = viewEntityDummy.posX;
                viewEntityDummy.prevPosY = viewEntityDummy.posY;
                viewEntityDummy.prevPosZ = viewEntityDummy.posZ;
            } else {
                MCH_Lib.enableFirstPersonItemRender();
                MCH_Lib.setRenderViewEntity(player);
            }
        }
    }

    protected void playerControl(EntityPlayer player, MCH_EntityGLTD gltd) {
        var pc = new PacketGLTDControl();
        boolean send = false;
        if (this.KeyUnmount.isKeyDown()) {
            pc.unmount = true;
            send = true;
        }

        if ((!this.KeySwitchWeapon1.isKeyDown() || !this.KeySwitchWeapon2.isKeyDown()) && this.KeyUseWeapon.isKeyPress()) {
            if (gltd.useCurrentWeapon(0, 0)) {
                pc.useWeapon = true;
                send = true;
            } else if (this.KeyUseWeapon.isKeyDown()) {
                playSoundNG();
            }
        }

        float prevZoom = gltd.camera.getCameraZoom();
        if (this.KeyZoom.isKeyPress() && !this.KeySwWeaponMode.isKeyPress()) {
            gltd.zoomCamera(0.1F * gltd.camera.getCameraZoom());
        }

        if (!this.KeyZoom.isKeyPress() && this.KeySwWeaponMode.isKeyPress()) {
            gltd.zoomCamera(-0.1F * gltd.camera.getCameraZoom());
        }

        if (prevZoom != gltd.camera.getCameraZoom()) {
            playSound("zoom", 0.1F, prevZoom < gltd.camera.getCameraZoom() ? 1.0F : 0.85F);
        }

        if (this.KeyCameraMode.isKeyDown()) {
            int beforeMode = gltd.camera.getMode(0);
            gltd.camera.setMode(0, gltd.camera.getMode(0) + 1);
            int mode = gltd.camera.getMode(0);
            if (mode != beforeMode) {
                pc.switchCameraMode = (byte) mode;
                playSoundOK();
                send = true;
            }
        }

        if (send) {
            pc.sendToServer();
        }
    }
}
