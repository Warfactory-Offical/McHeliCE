package com.norwood.mcheli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_ItemAircraft;
import com.norwood.mcheli.wrapper.W_Item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_CreativeTabs extends CreativeTabs {
    private final List<ItemStack> iconItems = new ArrayList<>();
    private ItemStack lastItem;
    private int currentIconIndex = 0;
    private int switchItemWait = 0;
    private Item fixedItem = null;

    public MCH_CreativeTabs(String label) {
        super(label);
        this.lastItem = ItemStack.EMPTY;
    }

    public void setFixedIconItem(String itemName) {
        if (itemName.contains(":")) {
            this.fixedItem = W_Item.getItemByName(itemName);
        } else {
            this.fixedItem = W_Item.getItemByName("mcheli:" + itemName);
        }
    }

    private ItemStack getNextIcon() {
        if (this.iconItems.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.currentIconIndex = (this.currentIconIndex + 1) % this.iconItems.size();
        return this.iconItems.get(this.currentIconIndex);
    }

    @Override
    public ItemStack getTabIconItem() {
        if (this.fixedItem != null) {
            return new ItemStack(this.fixedItem);
        }

        if (this.switchItemWait > 0) {
            this.switchItemWait--;
        } else {
            this.lastItem = this.getNextIcon();
            this.switchItemWait = 60;
        }

        if (this.lastItem.isEmpty()) {
            this.lastItem = new ItemStack(W_Item.getItemByName("iron_block"));
        }

        return this.lastItem;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> items) {
        super.displayAllRelevantItems(items);

        // Sorting items by aircraft category and name
        items.sort(new Comparator<ItemStack>() {
            @Override
            public int compare(ItemStack i1, ItemStack i2) {
                if (i1.getItem() instanceof MCH_ItemAircraft && i2.getItem() instanceof MCH_ItemAircraft) {
                    MCH_AircraftInfo info1 = ((MCH_ItemAircraft) i1.getItem()).getAircraftInfo();
                    MCH_AircraftInfo info2 = ((MCH_ItemAircraft) i2.getItem()).getAircraftInfo();

                    if (info1 != null && info2 != null) {
                        String s1 = info1.category + "." + info1.name;
                        String s2 = info2.category + "." + info2.name;
                        return s1.compareTo(s2);
                    }
                }
                return i1.getItem().getUnlocalizedName().compareTo(i2.getItem().getUnlocalizedName());
            }
        });
    }

    public void addIconItem(Item item) {
        if (item != null) {
            this.iconItems.add(new ItemStack(item));
        }
    }

    @Override
    public String getTranslationKey() {
        return "MC Heli";
    }
}
