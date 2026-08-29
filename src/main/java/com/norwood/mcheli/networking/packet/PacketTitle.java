package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.MCH_MOD;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ElegantPacket
@RequiredArgsConstructor
public class PacketTitle extends PacketBase implements ServerToClientPacket {

    final public String chatComponent;
    final public int showTime;
    final public int position;

    @Override
    @SideOnly(Side.CLIENT)
    public void onReceive(Minecraft mc) {
        if (mc.player != null) {
            MCH_MOD.proxy.printChatMessage(ITextComponent.Serializer.jsonToComponent(chatComponent), this.showTime,
                    this.position);
        }
    }
}
