package com.norwood.mcheli.throwable;

import com.norwood.mcheli.MCH_BaseInfo;
import com.norwood.mcheli.MCH_Color;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import com.norwood.mcheli.helper.client._IModelCustom;
import com.norwood.mcheli.helper.info.IItemContent;
import com.norwood.mcheli.wrapper.W_Item;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MCH_ThrowableInfo extends MCH_BaseInfo implements IItemContent {
    public final String name;
    public String displayName;
    public final HashMap<String, String> displayNameLang;
    public int itemID;
    public W_Item item;
    public List<String> recipeString;
    public final List<IRecipe> recipe;
    public boolean isShapedRecipe;
    public int power;
    public float acceleration;
    public float accelerationInWater;
    public float dispenseAcceleration;
    public int explosion;
    public int delayFuse;
    public float bound;
    public int timeFuse;
    public boolean flaming;
    public int stackSize;
    public float soundVolume;
    public float soundPitch;
    public float proximityFuseDist;
    public float accuracy;
    public int aliveTime;
    public int bomblet;
    public float bombletDiff;
    public _IModelCustom model;
    public float smokeSize;
    public int smokeNum;
    public float smokeVelocityVertical;
    public float smokeVelocityHorizontal;
    public float gravity;
    public float gravityInWater;
    public String particleName;
    public boolean disableSmoke;
    public MCH_Color smokeColor;

    public MCH_ThrowableInfo(AddonResourceLocation location, String path) {
        super(location, path);
        this.name = location.getPath();
        this.displayName = location.getPath();
        this.displayNameLang = new HashMap<>();
        this.itemID = 0;
        this.item = null;
        this.recipeString = new ArrayList<>();
        this.recipe = new ArrayList<>();
        this.isShapedRecipe = true;
        this.power = 0;
        this.acceleration = 1.0F;
        this.accelerationInWater = 1.0F;
        this.dispenseAcceleration = 1.0F;
        this.explosion = 0;
        this.delayFuse = 0;
        this.bound = 0.2F;
        this.timeFuse = 0;
        this.flaming = false;
        this.stackSize = 1;
        this.soundVolume = 1.0F;
        this.soundPitch = 1.0F;
        this.proximityFuseDist = 0.0F;
        this.accuracy = 0.0F;
        this.aliveTime = 10;
        this.bomblet = 0;
        this.bombletDiff = 0.3F;
        this.model = null;
        this.smokeSize = 10.0F;
        this.smokeNum = 0;
        this.smokeVelocityVertical = 1.0F;
        this.smokeVelocityHorizontal = 1.0F;
        this.gravity = 0.0F;
        this.gravityInWater = -0.04F;
        this.particleName = "explode";
        this.disableSmoke = true;
        this.smokeColor = new MCH_Color();
    }

    @Override
    public boolean validate() throws Exception {
        this.timeFuse *= 20;
        this.aliveTime *= 20;
        this.delayFuse *= 20;
        return super.validate();
    }

    @Override
    public void onPostReload() {
        item = (W_Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(MCH_MOD.MOD_ID, name));

    }

    @Override
    public Item getItem() {
        return this.item;
    }


    public static class RoundItem {
        public final int num;
        public final Item item;

        public RoundItem(MCH_ThrowableInfo paramMCH_ThrowableInfo, int n, Item i) {
            this.num = n;
            this.item = i;
        }
    }
}
