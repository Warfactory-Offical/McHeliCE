package com.norwood.mcheli.__helper.network;

import com.google.common.io.ByteArrayDataInput;
import io.netty.handler.codec.EncoderException;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PacketHelper {
   public static void writeCompoundTag(DataOutputStream dos, @Nullable NBTTagCompound nbt) throws IOException {
      if (nbt == null) {
         dos.writeByte(0);
      } else {
         dos.writeByte(1);

         try {
            CompressedStreamTools.func_74800_a(nbt, dos);
         } catch (IOException var3) {
            throw new EncoderException(var3);
         }
      }

   }

   @Nullable
   public static NBTTagCompound readCompoundTag(ByteArrayDataInput data) throws IOException {
      byte b0 = data.readByte();
      if (b0 == 0) {
         return null;
      } else {
         try {
            return CompressedStreamTools.func_152456_a(data, new NBTSizeTracker(2097152L));
         } catch (IOException var3) {
            throw new EncoderException(var3);
         }
      }
   }

   public static void writeItemStack(DataOutputStream dos, ItemStack itemstack) throws IOException {
      if (itemstack.func_190926_b()) {
         dos.writeShort(-1);
      } else {
         dos.writeShort(Item.func_150891_b(itemstack.func_77973_b()));
         dos.writeByte(itemstack.func_190916_E());
         dos.writeShort(itemstack.func_77960_j());
         NBTTagCompound nbttagcompound = null;
         if (itemstack.func_77973_b().func_77645_m() || itemstack.func_77973_b().func_77651_p()) {
            nbttagcompound = itemstack.func_77973_b().getNBTShareTag(itemstack);
         }

         writeCompoundTag(dos, nbttagcompound);
      }

   }

   public static ItemStack readItemStack(ByteArrayDataInput data) throws IOException {
      int i = data.readShort();
      if (i < 0) {
         return ItemStack.field_190927_a;
      } else {
         int j = data.readByte();
         int k = data.readShort();
         ItemStack itemstack = new ItemStack(Item.func_150899_d(i), j, k);
         itemstack.func_77973_b().readNBTShareTag(itemstack, readCompoundTag(data));
         return itemstack;
      }
   }

   public static void writeRecipe(DataOutputStream dos, IRecipe recipe) throws IOException {
      dos.writeUTF(recipe.getRegistryName().toString());
   }

   @Nullable
   public static IRecipe readRecipe(ByteArrayDataInput data) throws IOException {
      return (IRecipe)ForgeRegistries.RECIPES.getValue(new ResourceLocation(data.readUTF()));
   }
}
