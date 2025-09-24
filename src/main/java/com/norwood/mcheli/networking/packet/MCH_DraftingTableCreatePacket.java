package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.helper.network.PacketHelper;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.item.crafting.IRecipe;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_DraftingTableCreatePacket extends MCH_Packet {
    public IRecipe recipe;

    public static void send(IRecipe recipe) {
        if (recipe != null) {
            MCH_DraftingTableCreatePacket s = new MCH_DraftingTableCreatePacket();
            s.recipe = recipe;
            W_Network.sendToServer(s);
            MCH_Lib.DbgLog(true, "MCH_DraftingTableCreatePacket.send recipe = " + recipe.getRegistryName());
        }
    }

    @Override
    public int getMessageID() {
        return 537395216;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.recipe = PacketHelper.readRecipe(data);
        } catch (Exception var3) {
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            PacketHelper.writeRecipe(dos, this.recipe);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
