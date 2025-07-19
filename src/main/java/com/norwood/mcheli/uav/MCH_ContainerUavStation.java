package com.norwood.mcheli.uav;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class MCH_ContainerUavStation extends Container {
    protected MCH_EntityUavStation uavStation;

    public MCH_ContainerUavStation(InventoryPlayer inventoryPlayer, MCH_EntityUavStation te) {
        this.uavStation = te;
        this.addSlotToContainer(new Slot(this.uavStation, 0, 20, 20));
        this.bindPlayerInventory(inventoryPlayer);
    }

    public boolean canInteractWith(EntityPlayer player) {
        return this.uavStation.isUsableByPlayer(player);
    }

    public void onCraftMatrixChanged(IInventory par1IInventory) {
        super.onCraftMatrixChanged(par1IInventory);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(inventoryPlayer, 9 + j + i * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        return ItemStack.EMPTY;
    }
}
