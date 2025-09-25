package com.norwood.mcheli.networking.handlers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerControlBaseData {
    public UnmountAction isUnmount = UnmountAction.NONE;
    public VtolSwitch switchVtol = VtolSwitch.NONE;
    public ModeSwitch switchMode = ModeSwitch.NONE;
    public HatchSwitch switchHatch = HatchSwitch.NONE;
    public GearSwitch switchGear = GearSwitch.NONE;
    public RackAction putDownRack = RackAction.NONE;
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


    public static enum UnmountAction {
        NONE,           // 0
        UNMOUNT_SELF,   // 1
        UNMOUNT_CREW,   // 2
        UNMOUNT_AIRCRAFT // 3
    }

    public static enum VtolSwitch {
        NONE,   // 0
        VTOL_OFF, // 1
        VTOL_ON   // 2
    }

    public static enum ModeSwitch {
        NONE,            // 0
        GUNNER_OFF,      // 1
        GUNNER_ON,       // 2
        HOVERING_OFF,    // 3
        HOVERING_ON      // 4
    }

    public static enum HatchSwitch {
        NONE,     // 0
        FOLD,     // 1
        UNFOLD    // 2
    }


    public static enum RackAction {
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
