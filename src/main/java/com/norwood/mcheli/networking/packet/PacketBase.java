package com.norwood.mcheli.networking.packet;

import hohserg.elegant.networking.api.ClientToServerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class PacketBase {

    protected IThreadListener getScheduler() {
        if (this instanceof ClientToServerPacket)
            return FMLCommonHandler.instance().getMinecraftServerInstance();
        else
            return Minecraft.getMinecraft();

    }
}
