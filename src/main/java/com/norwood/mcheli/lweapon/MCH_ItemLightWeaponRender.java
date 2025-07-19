package com.norwood.mcheli.lweapon;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.__helper.client._IItemRenderer;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_ItemLightWeaponRender implements _IItemRenderer {
    @SideOnly(Side.CLIENT)
    public static void renderItem(ItemStack pitem, Entity entity, boolean isFirstPerson) {
        if (pitem != null) {
            pitem.getItem();
            String name = MCH_ItemLightWeaponBase.getName(pitem);
            GL11.glEnable(32826);
            GL11.glEnable(2903);
            GL11.glPushMatrix();
            if (MCH_Config.SmoothShading.prmBool) {
                GL11.glShadeModel(7425);
            }

            GL11.glEnable(2884);
            W_McClient.MOD_bindTexture("textures/lweapon/" + name + ".png");
            if (isFirstPerson) {
                GL11.glTranslatef(0.0F, 0.005F, -0.165F);
                GL11.glScalef(2.0F, 2.0F, 2.0F);
                GL11.glRotatef(-10.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-50.0F, 1.0F, 0.0F, 0.0F);
            } else {
                GL11.glTranslatef(0.3F, 0.3F, 0.0F);
                GL11.glScalef(2.0F, 2.0F, 2.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(10.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(15.0F, 1.0F, 0.0F, 0.0F);
            }

            MCH_ModelManager.render("lweapons", name);
            GL11.glShadeModel(7424);
            GL11.glPopMatrix();
            GL11.glDisable(32826);
        }
    }

    @Override
    public boolean handleRenderType(ItemStack item, _IItemRenderer.ItemRenderType type) {
        return type == _IItemRenderer.ItemRenderType.EQUIPPED || type == _IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(_IItemRenderer.ItemRenderType type, ItemStack item, _IItemRenderer.ItemRendererHelper helper) {
        return false;
    }

    public boolean useCurrentWeapon() {
        return false;
    }

    @Override
    public void renderItem(_IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        boolean isRender = false;
        if (type == _IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON || type == _IItemRenderer.ItemRenderType.EQUIPPED) {
            isRender = true;
            if (data[1] instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) data[1];
                if (MCH_ItemLightWeaponBase.isHeld(player) && W_Lib.isFirstPerson() && W_Lib.isClientPlayer(player)) {
                    isRender = false;
                }
            }
        }

        if (isRender) {
            renderItem(item, W_Lib.castEntityLivingBase(data[1]), type == _IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
        }
    }
}
