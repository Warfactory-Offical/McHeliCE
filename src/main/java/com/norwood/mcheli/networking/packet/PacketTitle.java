package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.MCH_MOD;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

@ElegantPacket
public class PacketTitle implements ServerToClientPacket {

    public String chatComponent = null;
    public int showTime = 1;
    public int position = 0;

    public static void send(ITextComponent chat, int showTime, int pos) {
        var packet = new PacketTitle();
        packet.chatComponent = ITextComponent.Serializer.componentToJson(chat);
        packet.showTime = showTime;
        packet.position = pos;
        packet.sendToClients();
    }

    @Override
    public void onReceive(Minecraft mc) {
        if (mc.player != null && mc.player.world.isRemote) {
            mc.addScheduledTask(() -> MCH_MOD.proxy.printChatMessage(ITextComponent.Serializer.jsonToComponent(chatComponent), this.showTime, this.position));
        }
    }
}
