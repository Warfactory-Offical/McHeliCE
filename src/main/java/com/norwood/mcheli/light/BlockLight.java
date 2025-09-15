package com.norwood.mcheli.light;

//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
//import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//import net.minecraft.util.AxisAlignedBB;
//import net.minecraft.util.IIcon;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
//import net.minecraft.world.EnumSkyBlock;


public class BlockLight extends Block {
    //private IIcon icon;

    public BlockLight() {
        super(Material.ROCK); // ensure tanks dont break it
        this.setLightLevel(1.0F); // Light level 15
        this.setBlockUnbreakable();
        this.setResistance(6000000F);
        this.setTickRandomly(false);
        this.setCreativeTab(null);
    }

    @Override
    public int quantityDropped(Random random) {
        // Always drop 0 items
        return 0;
    }

    //@Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        // No item to drop
        return null;
    }

    //@Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z,
                                          int meta, float chance, int fortune) {
        // Prevent any dropping via this path
        // (don’t call super)
    }

    //@Override
    public void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack) {
        // Prevent the “player‑break” drop path as well
    }

    //@Override
    public void dropXpOnBlockBreak(World world, int x, int y, int z, int amount) {
        // No XP either
    }

    //@Override
    public int getExpDrop(IBlockAccess world, int meta, int fortune) {
        return 0;  // just in case
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        // Prevent explosion drops
        return false;
    }

    //@Override
    public int getRenderType() {
        return -1;
    }

    //@Override
    public boolean isOpaqueCube() {
        return false;
    }

    //@Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    //@Override
    //public boolean canCollideCheck(int meta, boolean hitIfLiquid) {
    //    return false;
    //}

    //@Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    //@Override
    public boolean isAir(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    //@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        // No collision box at all
        return null;
    }

    //@Override
    public void addCollisionBoxesToList(World world, int x, int y, int z,
                                        AxisAlignedBB mask,
                                        List list,
                                        Entity entity) {
        // Don’t add any boxes, so nothing collides
    }

    @Override
    public boolean isCollidable() {
        // Tells the engine “don’t even bother checking collision”
        return false;
    }

    //@Override
    public boolean canCollideCheck(int meta, boolean fullHit) {
        // Prevent any ray‑tracing collision checks (e.g. for clicking)
        return false;
    }

    //@SideOnly(Side.CLIENT)
    //@Override
    //public void registerBlockIcons(IIconRegister reg) {
    //    this.icon = reg.registerIcon("mcheli:lightblock");
    //}
    //not needed

    //@SideOnly(Side.CLIENT)
    //@Override
    //public IIcon getIcon(int side, int meta) {
    //    return icon;
    //}

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        world.markAndNotifyBlock(x, y, z);
        world.updateLightByType(EnumSkyBlock.BLOCK, x, y, z);
    }
}