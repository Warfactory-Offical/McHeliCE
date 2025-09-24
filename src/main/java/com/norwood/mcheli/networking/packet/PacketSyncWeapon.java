package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Lib;
import hohserg.elegant.networking.api.ServerToClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class PacketSyncWeapon extends PacketBase implements ServerToClientPacket {
    public int entityID_Ac = -1;
    public int seatID = -1;
    public int weaponID = -1;
    public short ammo = 0;
    public short reserveAmmo = 0;

    public static void send(Entity sender, int sid, int wid, int ammo, int rest_ammo) {
        var packet = new PacketSyncWeapon();
        packet.entityID_Ac = W_Entity.getEntityId(sender);
        packet.seatID = sid;
        packet.weaponID = wid;
        packet.ammo = (short) ammo;
        packet.reserveAmmo = (short) rest_ammo;
        packet.sendPacketToAllAround(sender.world, sender.posX, sender.posY, sender.posZ, 150);
    }

    @Override
    public void onReceive(Minecraft mc) {
        if (this.entityID_Ac > 0) {
            getScheduler().addScheduledTask(() -> {
                Entity e = mc.player.world.getEntityByID(this.entityID_Ac);
                if (e instanceof MCH_EntityAircraft ac) {
                    if (ac.isValidSeatID(this.seatID)) {
                        ac.getWeapon(this.weaponID).setAmmoNum(this.ammo);
                        ac.getWeapon(this.weaponID).setRestAllAmmoNum(this.reserveAmmo);
                        MCH_Lib.DbgLog(true, "onPacketNotifyWeaponID:WeaponID=%d (%d / %d)", this.weaponID, this.ammo, this.reserveAmmo);
                        if (W_Lib.isClientPlayer(ac.getEntityBySeatId(this.seatID))) {
                            MCH_Lib.DbgLog(true, "onPacketNotifyWeaponID:#discard:SeatID=%d, WeaponID=%d", this.seatID, this.weaponID);
                        } else {
                            MCH_Lib.DbgLog(true, "onPacketNotifyWeaponID:SeatID=%d, WeaponID=%d", this.seatID, this.weaponID);
                            ac.updateWeaponID(this.seatID, this.weaponID);
                        }
                    }
                }
            });
        }
    }
}
