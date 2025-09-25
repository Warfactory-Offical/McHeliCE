package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.networking.handlers.PlayerControlBaseData;
import com.norwood.mcheli.plane.MCP_EntityPlane;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ElegantSerializable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import net.minecraft.entity.player.EntityPlayerMP;

@ElegantPacket
@RequiredArgsConstructor
public class PacketPlayerControlPlane extends PacketPlayerControlBase {

    @Delegate
    public final PlayerControlBaseData controlBaseData;

    @Override
    public void onReceive(EntityPlayerMP player) {
        getScheduler().addScheduledTask(() -> {
            MCP_EntityPlane plane = null;
            if (player.getRidingEntity() instanceof MCP_EntityPlane) {
                plane = (MCP_EntityPlane) player.getRidingEntity();
            } else if (player.getRidingEntity() instanceof MCH_EntitySeat) {
                if (((MCH_EntitySeat) player.getRidingEntity()).getParent() instanceof MCP_EntityPlane) {
                    plane = (MCP_EntityPlane) ((MCH_EntitySeat) player.getRidingEntity()).getParent();
                }
            } else if (player.getRidingEntity() instanceof MCH_EntityUavStation uavStation) {
                if (uavStation.getControlAircract() instanceof MCP_EntityPlane) {
                    plane = (MCP_EntityPlane) uavStation.getControlAircract();
                }
            }

            if (plane != null) process(plane, controlBaseData, player);
        });

    }

    @Override
    protected void handleHatch(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        switch (data.switchHatch) {
            case FOLD, UNFOLD -> {
                if (aircraft.getAcInfo().haveHatch()) {
                    aircraft.foldHatch(controlBaseData.switchHatch == PlayerControlBaseData.HatchSwitch.UNFOLD);
                } else {
                    ((MCP_EntityPlane) aircraft).foldWing(data.switchHatch == PlayerControlBaseData.HatchSwitch.UNFOLD);
                }
            }
        }
    }


    @Override
    protected void handleVtolSwitch(MCH_EntityAircraft aircraft, PlayerControlBaseData data) {
        MCP_EntityPlane plane = (MCP_EntityPlane) aircraft;
        switch (data.switchVtol) {
            case VTOL_OFF -> plane.swithVtolMode(false);
            case VTOL_ON -> plane.swithVtolMode(true);
        }
    }

}

