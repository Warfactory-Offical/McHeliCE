package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.weapon.MCH_WeaponParam;
import hohserg.elegant.networking.api.ClientToServerPacket;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ElegantSerializable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import static com.norwood.mcheli.networking.packet.PacketPlayerControlBase.HatchSwitch.UNFOLD;

public abstract class PacketPlayerControlBase extends PacketBase implements ClientToServerPacket {
    public UnmountAction isUnmount = UnmountAction.NONE;
    public VtolSwitch switchVtol = VtolSwitch.NONE;
    public ModeSwitch switchMode = ModeSwitch.NONE;
    public HatchSwitch switchHatch = HatchSwitch.NONE;
    public GearSwitch switchGear = GearSwitch.NONE;
    public RackAction putDownRack;
    public byte switchCameraMode = 0;
    public byte switchWeapon = -1;
    public byte useFlareType = 0;
    public boolean useWeapon = false;
    public int useWeaponOption1 = 0;
    public int useWeaponOption2 = 0;
    public double useWeaponPosX = 0.0;
    public double useWeaponPosY = 0.0;
    public double useWeaponPosZ = 0.0;
    public boolean throttleUp = false;
    public boolean throttleDown = false;
    public boolean moveLeft = false;
    public boolean moveRight = false;
    public boolean openGui;
    public byte switchFreeLook = 0;
    public boolean ejectSeat = false;
    public boolean switchSearchLight = false;
    public boolean useBrake = false;
    public boolean switchGunnerStatus = false;


    protected void process(MCH_EntityAircraft aircraft, EntityPlayer entity) {
        if (aircraft == null) return;

        handleUnmount(aircraft);
        handleEjectSeat(aircraft, entity);
        handleVtolSwitch(aircraft);
        handleModeSwitch(aircraft);
        handleSearchLight(aircraft);
        handleCameraMode(aircraft, entity);
        handleWeaponSwitch(aircraft, entity);
        handleUseWeapon(aircraft, entity);
        handlePilotControls(aircraft, entity);
        handleFlare(aircraft);
        handleGui(aircraft, entity);
        handleHatch(aircraft);
        handleFreeLook(aircraft);
        handleGear(aircraft);
        handleRack(aircraft);
        handleGunnerStatus(aircraft);
    }

    protected void handleUnmount(MCH_EntityAircraft aircraft) {
        switch (this.isUnmount) {
            case UNMOUNT_SELF -> aircraft.unmountEntity();
            case UNMOUNT_CREW -> aircraft.unmountCrew();
            case UNMOUNT_AIRCRAFT -> aircraft.unmountAircraft();
        }
    }

    protected void handleEjectSeat(MCH_EntityAircraft aircraft, Entity entity) {
        if (this.ejectSeat) aircraft.ejectSeat(entity);
    }

    //Planes only
    protected void handleVtolSwitch(MCH_EntityAircraft aircraft) {
    }

    protected void handleModeSwitch(MCH_EntityAircraft aircraft) {
        switch (this.switchMode) {
            case GUNNER_OFF -> aircraft.switchGunnerMode(false);
            case GUNNER_ON -> aircraft.switchGunnerMode(true);
            case HOVERING_OFF -> aircraft.switchHoveringMode(false);
            case HOVERING_ON -> aircraft.switchHoveringMode(true);
        }
    }

    protected void handleSearchLight(MCH_EntityAircraft aircraft) {
        if (this.switchSearchLight) aircraft.setSearchLight(!aircraft.isSearchLightON());
    }

    protected void handleCameraMode(MCH_EntityAircraft aircraft, EntityPlayer player) {
        if (this.switchCameraMode > 0) aircraft.switchCameraMode(player, this.switchCameraMode - 1);
    }

    protected void handleWeaponSwitch(MCH_EntityAircraft aircraft, EntityPlayer player) {
        if (this.switchWeapon >= 0) aircraft.switchWeapon(player, this.switchWeapon);
    }

    protected void handleUseWeapon(MCH_EntityAircraft aircraft, EntityPlayer player) {
        if (!this.useWeapon) return;

        MCH_WeaponParam prm = new MCH_WeaponParam();
        prm.entity = aircraft;
        prm.user = player;
        prm.setPosAndRot(this.useWeaponPosX, this.useWeaponPosY, this.useWeaponPosZ, 0.0F, 0.0F);
        prm.option1 = this.useWeaponOption1;
        prm.option2 = this.useWeaponOption2;
        aircraft.useCurrentWeapon(prm);
    }

    //TODO: integrate br
    protected void handlePilotControls(MCH_EntityAircraft aircraft, EntityPlayer player) {
        if (!aircraft.isPilot(player)) return;

        aircraft.throttleUp = this.throttleUp;
        aircraft.throttleDown = this.throttleDown;
        aircraft.moveLeft = this.moveLeft;
        aircraft.moveRight = this.moveRight;
    }

    protected void handleFlare(MCH_EntityAircraft aircraft) {
        if (this.useFlareType > 0) aircraft.useFlare(this.useFlareType);
    }

    protected void handleGui(MCH_EntityAircraft aircraft, EntityPlayer player) {
        if (this.openGui) aircraft.openGui(player);
    }

    protected void handleHatch(MCH_EntityAircraft aircraft) {
        switch (this.switchHatch) {
            case FOLD, UNFOLD -> {
                if (aircraft.getAcInfo().haveHatch()) {
                    aircraft.foldHatch(this.switchHatch == UNFOLD);
                }
            }
        }
    }

    protected void handleFreeLook(MCH_EntityAircraft aircraft) {
        if (this.switchFreeLook > 0) aircraft.switchFreeLookMode(this.switchFreeLook == 1);
    }

    //Plane
    protected void handleGear(MCH_EntityAircraft aircraft) {
        switch (switchGear) {
            case FOLD -> aircraft.foldLandingGear();
            case UNFOLD -> aircraft.unfoldLandingGear();
        }
    }

    protected void handleRack(MCH_EntityAircraft aircraft) {
        switch (this.putDownRack) {
            case MOUNT -> aircraft.mountEntityToRack();
            case UNMOUNT -> aircraft.unmountEntityFromRack();
            case RIDE -> aircraft.rideRack();
        }
    }

    protected void handleGunnerStatus(MCH_EntityAircraft aircraft) {
        if (this.switchGunnerStatus) aircraft.setGunnerStatus(!aircraft.getGunnerStatus());
    }


    public enum UnmountAction {
        NONE,           // 0
        UNMOUNT_SELF,   // 1
        UNMOUNT_CREW,   // 2
        UNMOUNT_AIRCRAFT // 3
    }

    public enum VtolSwitch {
        NONE,   // 0
        VTOL_OFF, // 1
        VTOL_ON   // 2
    }

    public enum ModeSwitch {
        NONE,            // 0
        GUNNER_OFF,      // 1
        GUNNER_ON,       // 2
        HOVERING_OFF,    // 3
        HOVERING_ON      // 4
    }

    public enum HatchSwitch {
        NONE,     // 0
        FOLD,     // 1
        UNFOLD    // 2
    }


    public enum RackAction {
        NONE,       // 0
        MOUNT,      // 1
        UNMOUNT,    // 2
        RIDE        // 3
    }

    public static enum GearSwitch {
        NONE,   // 0
        FOLD,   // 1
        UNFOLD  // 2
    }


}
