package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.networking.handlers.*;
import com.norwood.mcheli.block.MCH_DraftingTablePacketHandler;
import com.norwood.mcheli.gltd.MCH_GLTDPacketHandler;
import com.norwood.mcheli.networking.handlers.MCH_HeliPacketHandler;
import com.norwood.mcheli.lweapon.MCH_LightWeaponPacketHandler;
import com.norwood.mcheli.multiplay.MCH_MultiplayPacketHandler;
import com.norwood.mcheli.networking.handlers.MCP_PlanePacketHandler;
import com.norwood.mcheli.wrapper.W_PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MCH_PacketHandler extends W_PacketHandler {
    @Override
    public void onPacket(ByteArrayDataInput data, EntityPlayer entityPlayer, MessageContext ctx) {
        int msgid = this.getMessageId(data);
        IThreadListener handler = FMLCommonHandler.instance().getWorldThread(ctx.netHandler);
        switch (msgid) {
            case 268437520 -> MCH_CommonPacketHandler.onPacketEffectExplosion(entityPlayer, data, handler);
            case 268437568 -> MCH_CommonPacketHandler.onPacketNotifyServerSettings(entityPlayer, data, handler);
            case 268437761 -> MCH_MultiplayPacketHandler.onPacket_NotifySpotedEntity(entityPlayer, data, handler);
            case 268437762 -> MCH_MultiplayPacketHandler.onPacket_NotifyMarkPoint(entityPlayer, data, handler);
            case 268438032 -> MCH_MultiplayPacketHandler.onPacket_IndClient(entityPlayer, data, handler);
//            case 268438272 -> MCH_CommandPacketHandler.onPacketTitle(entityPlayer, data, handler);
            case 268439569 -> MCH_AircraftPacketHandler.onPacketSeatListResponse(entityPlayer, data, handler);
            case 268439600 -> MCH_AircraftPacketHandler.onPacketNotifyTVMissileEntity(entityPlayer, data, handler);
            case 268439601 -> MCH_AircraftPacketHandler.onPacketNotifyWeaponID(entityPlayer, data, handler);
            case 268439602 -> MCH_AircraftPacketHandler.onPacketNotifyHitBullet(entityPlayer, data, handler);
            //case 268439604 -> MCH_AircraftPacketHandler.onPacketNotifyAmmoNum(entityPlayer, data, handler);
            case 268439632 -> MCH_AircraftPacketHandler.onPacketOnMountEntity(entityPlayer, data, handler);
            case 268439649 -> MCH_AircraftPacketHandler.onPacketStatusResponse(entityPlayer, data, handler);
            case 536872992 -> MCH_CommonPacketHandler.onPacketIndOpenScreen(entityPlayer, data, handler);
            case 536873088 -> MCH_MultiplayPacketHandler.onPacket_Command(entityPlayer, data, handler);
            case 536873216 -> MCH_ToolPacketHandler.onPacket_IndSpotEntity(entityPlayer, data, handler);
            case 536873472 -> MCH_MultiplayPacketHandler.onPacket_LargeData(entityPlayer, data, handler);
            case 536873473 -> MCH_MultiplayPacketHandler.onPacket_ModList(entityPlayer, data, handler);
            case 536873729 -> MCH_CommandPacketHandler.onPacketSave(entityPlayer, data, handler);
            case 536873984 -> MCH_CommonPacketHandler.onPacketNotifyLock(entityPlayer, data, handler);
            case 536875024 -> MCH_AircraftPacketHandler.onPacketSeatListRequest(entityPlayer, data, handler);
            case 536875040 -> MCH_AircraftPacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
            case 536875059 -> MCH_AircraftPacketHandler.onPacketIndReload(entityPlayer, data, handler);
//            case 536875061 -> MCH_AircraftPacketHandler.onPacketIndNotifyAmmoNum(entityPlayer, data, handler);
            case 536875062 -> MCH_AircraftPacketHandler.onPacketIndRotation(entityPlayer, data, handler);
            case 536875063 -> MCH_AircraftPacketHandler.onPacketNotifyInfoReloaded(entityPlayer, data, handler);
            case 536875072 -> MCH_AircraftPacketHandler.onPacket_ClientSetting(entityPlayer, data, handler);
            case 536875104 -> MCH_AircraftPacketHandler.onPacketStatusRequest(entityPlayer, data, handler);
            case 536879120 -> MCH_HeliPacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
            case 536887312 -> MCH_GLTDPacketHandler.onPacket_GLTDPlayerControl(entityPlayer, data, handler);
            case 536903696 -> MCP_PlanePacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
            case 536936464 -> MCH_LightWeaponPacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
            case 537002000 -> MCH_VehiclePacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
//            case 537133072 -> MCH_UavPacketHandler.onPacketUavStatus(entityPlayer, data, handler);
            case 537395216 -> MCH_DraftingTablePacketHandler.onPacketCreate(entityPlayer, data, handler);
            case 537919504 -> MCH_TankPacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
            case 536903698 -> MCH_ShipPacketHandler.onPacket_PlayerControl(entityPlayer, data, handler);
            default -> MCH_Lib.DbgLog(entityPlayer.world, "MCH_PacketHandler.onPacket invalid MSGID=0x%X(%d)", msgid, msgid);
        }
    }

    protected int getMessageId(ByteArrayDataInput data) {
        try {
            return data.readInt();
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0;
        }
    }
}
