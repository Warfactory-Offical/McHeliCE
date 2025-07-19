package com.norwood.mcheli.particles;

import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.__helper.entity.ITargetMarkerObject;
import com.norwood.mcheli.multiplay.MCH_GuiTargetMarker;
import com.norwood.mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class MCH_EntityParticleMarkPoint extends MCH_EntityParticleBase implements ITargetMarkerObject {
    final Team team;

    public MCH_EntityParticleMarkPoint(World par1World, double x, double y, double z, Team team) {
        super(par1World, x, y, z, 0.0, 0.0, 0.0);
        this.setParticleMaxAge(30);
        this.team = team;
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player == null) {
            this.setExpired();
        } else if (player.getTeam() == null && this.team != null) {
            this.setExpired();
        } else if (player.getTeam() != null && !player.isOnScoreboardTeam(this.team)) {
            this.setExpired();
        }
    }

    public void setExpired() {
        super.setExpired();
        MCH_Lib.DbgLog(true, "MCH_EntityParticleMarkPoint.setExpired : " + this);
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float par2, float par3, float par4, float par5, float par6, float par7) {
        GL11.glPushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player != null) {
            double ix = interpPosX;
            double iy = interpPosY;
            double iz = interpPosZ;
            if (mc.gameSettings.thirdPersonView > 0 && entityIn != null) {
                double dist = W_Reflection.getThirdPersonDistance();
                float yaw = -entityIn.rotationYaw;
                float pitch = -entityIn.rotationPitch;
                Vec3d v = MCH_Lib.RotVec3(0.0, 0.0, -dist, yaw, pitch);
                if (mc.gameSettings.thirdPersonView == 2) {
                    v = new Vec3d(-v.x, -v.y, -v.z);
                }

                Vec3d vs = new Vec3d(entityIn.posX, entityIn.posY + entityIn.getEyeHeight(), entityIn.posZ);
                RayTraceResult mop = entityIn.world.rayTraceBlocks(vs.add(0.0, 0.0, 0.0), vs.add(v.x, v.y, v.z));
                double block_dist = dist;
                if (mop != null && mop.typeOfHit == Type.BLOCK) {
                    block_dist = vs.distanceTo(mop.hitVec) - 0.4;
                    if (block_dist < 0.0) {
                        block_dist = 0.0;
                    }
                }

                GL11.glTranslated(v.x * (block_dist / dist), v.y * (block_dist / dist), v.z * (block_dist / dist));
                ix += v.x * (block_dist / dist);
                iy += v.y * (block_dist / dist);
                iz += v.z * (block_dist / dist);
            }

            double px = (float) (this.prevPosX + (this.posX - this.prevPosX) * par2 - ix);
            double py = (float) (this.prevPosY + (this.posY - this.prevPosY) * par2 - iy);
            double pz = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * par2 - iz);
            double scale = Math.sqrt(px * px + py * py + pz * pz) / 10.0;
            if (scale < 1.0) {
                scale = 1.0;
            }

            MCH_GuiTargetMarker.addMarkEntityPos(100, this, px / scale, py / scale, pz / scale, false);
            GL11.glPopMatrix();
        }
    }

    @Override
    public double getX() {
        return this.posX;
    }

    @Override
    public double getY() {
        return this.posY;
    }

    @Override
    public double getZ() {
        return this.posZ;
    }
}
