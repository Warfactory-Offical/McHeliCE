package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.networking.handlers.PlayerControlBaseData;
import com.norwood.mcheli.weapon.MCH_WeaponParam;
import hohserg.elegant.networking.api.ClientToServerPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import static com.norwood.mcheli.networking.handlers.PlayerControlBaseData.HatchSwitch.UNFOLD;

public abstract class PacketPlayerControlBase extends PacketBase implements ClientToServerPacket {


    protected void process(MCH_EntityAircraft aircraft, PlayerControlBaseData data, EntityPlayer entity) {
        if (aircraft == null) return;

        handleUnmount(aircraft, data);
        handleEjectSeat(aircraft, data, entity);
        handleVtolSwitch(aircraft, data);
        handleModeSwitch(aircraft, data);
        handleSearchLight(aircraft, data);
        handleCameraMode(aircraft, data, entity);
        handleWeaponSwitch(aircraft, data, entity);
        handleUseWeapon(aircraft, data, entity);
        handlePilotControls(aircraft, data, entity);
        handleFlare(aircraft, data);
        handleGui(aircraft, data, entity);
        handleHatch(aircraft, data);
        handleFreeLook(aircraft, data);
        handleGear(aircraft, data);
        handleRack(aircraft, data);
        handleGunnerStatus(aircraft, data);

    }

    protected void handleUnmount(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        switch (data.isUnmount) {
            case UNMOUNT_SELF -> aircraft.unmountEntity();
            case UNMOUNT_CREW -> aircraft.unmountCrew();
            case UNMOUNT_AIRCRAFT -> aircraft.unmountAircraft();
        }
    }

    protected void handleEjectSeat(MCH_EntityAircraft aircraft, PlayerControlBaseData data, Entity entity) {
        if (data.isEjectSeat()) aircraft.ejectSeat(entity);
    }

    //Planes only
    protected void handleVtolSwitch(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
    }

    protected void handleModeSwitch(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        switch (data.switchMode) {
            case GUNNER_OFF -> aircraft.switchGunnerMode(false);
            case GUNNER_ON -> aircraft.switchGunnerMode(true);
            case HOVERING_OFF -> aircraft.switchHoveringMode(false);
            case HOVERING_ON -> aircraft.switchHoveringMode(true);
        }
    }

    protected void handleSearchLight(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        if (data.isSwitchSearchLight()) aircraft.setSearchLight(!aircraft.isSearchLightON());
    }

    protected void handleCameraMode(MCH_EntityAircraft aircraft, PlayerControlBaseData data, EntityPlayer player) {
        if (data.switchCameraMode > 0) aircraft.switchCameraMode(player, data.switchCameraMode - 1);
    }

    protected void handleWeaponSwitch(MCH_EntityAircraft aircraft, PlayerControlBaseData data, EntityPlayer player) {
        if (data.switchWeapon >= 0) aircraft.switchWeapon(player, data.switchWeapon);
    }

    protected void handleUseWeapon(MCH_EntityAircraft aircraft, PlayerControlBaseData data, EntityPlayer player) {
        if (!data.getSwitches().isUseWeapon()) return;

        MCH_WeaponParam prm = new MCH_WeaponParam();
        prm.entity = aircraft;
        prm.user = player;
        prm.setPosAndRot(data.useWeaponPosX, data.useWeaponPosY, data.useWeaponPosZ, 0.0F, 0.0F);
        prm.option1 = data.useWeaponOption1;
        prm.option2 = data.useWeaponOption2;
        aircraft.useCurrentWeapon(prm);
    }

    //TODO: integrate br
    protected void handlePilotControls(MCH_EntityAircraft aircraft, PlayerControlBaseData data, EntityPlayer player) {
        if (!aircraft.isPilot(player)) return;

        aircraft.throttleUp = data.isThrottleUp();
        aircraft.throttleDown = data.isThrottleDown();
        aircraft.moveLeft = data.isMoveLeft();
        aircraft.moveRight = data.isMoveRight();
    }

    protected void handleFlare(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        if (data.useFlareType > 0) aircraft.useFlare(data.useFlareType);
    }

    protected void handleGui(MCH_EntityAircraft aircraft, PlayerControlBaseData data, EntityPlayer player) {
        if (data.isOpenGui()) aircraft.openGui(player);
    }

    protected void handleHatch(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        switch (data.switchHatch) {
            case FOLD, UNFOLD -> {
                if (aircraft.getAcInfo().haveHatch()) {
                    aircraft.foldHatch(data.switchHatch == UNFOLD);
                }
            }
        }
    }

    protected void handleFreeLook(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        if (data.switchFreeLook > 0) aircraft.switchFreeLookMode(data.switchFreeLook == 1);
    }

    //Plane
    protected void handleGear(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        switch (data.switchGear) {
            case FOLD -> aircraft.foldLandingGear();
            case UNFOLD -> aircraft.unfoldLandingGear();
        }
    }

    protected void handleRack(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        switch (data.putDownRack) {
            case MOUNT -> aircraft.mountEntityToRack();
            case UNMOUNT -> aircraft.unmountEntityFromRack();
            case RIDE -> aircraft.rideRack();
        }
    }

    protected void handleGunnerStatus(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        if (data.isSwitchGunnerStatus()) aircraft.setGunnerStatus(!aircraft.getGunnerStatus());
    }


}
