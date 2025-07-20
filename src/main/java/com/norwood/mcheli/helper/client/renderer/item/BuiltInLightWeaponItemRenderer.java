package com.norwood.mcheli.helper.client.renderer.item;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.lweapon.MCH_ItemLightWeaponBase;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BuiltInLightWeaponItemRenderer implements IItemModelRenderer {
    @Override
    public boolean shouldRenderer(ItemStack itemStack, TransformType transformType) {
        return IItemModelRenderer.isFirstPerson(transformType) || IItemModelRenderer.isThirdPerson(transformType);
    }

    @Override
    public void renderItem(ItemStack itemStack, EntityLivingBase entityLivingBase, TransformType transformType, float partialTicks) {
        boolean isRender = false;
        if (IItemModelRenderer.isFirstPerson(transformType) || IItemModelRenderer.isThirdPerson(transformType)) {
            isRender = true;
            if (entityLivingBase instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLivingBase;
                if (MCH_ItemLightWeaponBase.isHeld(player) && W_Lib.isFirstPerson() && W_Lib.isClientPlayer(player)) {
                    isRender = false;
                }
            }
        }

        if (isRender) {
            this.renderItem(itemStack, IItemModelRenderer.isFirstPerson(transformType), entityLivingBase);
        }
    }

    private void renderItem(ItemStack itemStack, boolean isFirstPerson, EntityLivingBase entity) {
        String name = MCH_ItemLightWeaponBase.getName(itemStack);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glPushMatrix();
        if (MCH_Config.SmoothShading.prmBool) {
            GL11.glShadeModel(7425);
        }

        GL11.glEnable(2884);
        W_McClient.MOD_bindTexture("textures/lweapon/" + name + ".png");
        if (isFirstPerson && entity.isHandActive() && entity.getActiveHand() == EnumHand.MAIN_HAND) {
            GL11.glTranslated(0.13F, 0.27F, 0.01F);
        }

        MCH_ModelManager.render("lweapons", name);
        GL11.glShadeModel(7424);
        GL11.glPopMatrix();
        GL11.glDisable(32826);
    }
}
