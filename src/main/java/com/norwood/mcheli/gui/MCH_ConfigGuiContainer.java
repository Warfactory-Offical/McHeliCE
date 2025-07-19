package com.norwood.mcheli.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class MCH_ConfigGuiContainer extends Container {
    public final EntityPlayer player;

    public MCH_ConfigGuiContainer(EntityPlayer player) {
        this.player = player;
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        return ItemStack.EMPTY;
    }
}
