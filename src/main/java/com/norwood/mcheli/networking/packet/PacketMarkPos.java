package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.multiplay.MCH_GuiTargetMarker;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@RequiredArgsConstructor
@ElegantPacket
public class PacketMarkPos implements ServerToClientPacket {

    public final int x, y, z;

    public PacketMarkPos(BlockPos pos) {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onReceive(Minecraft mc) {
        MCH_GuiTargetMarker.markPoint(x, y, z);
    }
}
