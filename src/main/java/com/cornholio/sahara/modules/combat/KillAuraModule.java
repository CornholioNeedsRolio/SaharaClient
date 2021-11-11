package com.cornholio.sahara.modules.combat;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.utils.PlayerUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class KillAuraModule extends Module
{
    ModuleSetting range;
    ModuleSetting monsters;
    ModuleSetting players;
    ModuleSetting animal;
    public KillAuraModule()
    {
        super("Kill Aura", "kills things around you", ModuleCategory.Combat);
        registerSetting(range = new ModuleSetting("Range", 0, 10, 4.5f));
        registerSetting(monsters = new ModuleSetting("Monsters", false));
        registerSetting(players = new ModuleSetting("Players", false));
        registerSetting(animal = new ModuleSetting("Animals", false));

    }

    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event)
    {
        if(mc.player == null) return;
        if(mc.player.getCooledAttackStrength(0.0F) <= 0.9f) return;
        for(Entity entity : mc.world.getLoadedEntityList())
        {
            if(entity.getDistance(mc.player) <= range.getFloat() && canEntityBeAttacked(entity) && mc.player != entity && !entity.isDead)
            {
                AxisAlignedBB box = entity.getEntityBoundingBox();
                float offsetX = (float)Math.abs(box.minX-box.maxX)*0.01f;
                float offsetY = (float)Math.abs(box.minY-box.maxY)*0.01f;
                float offsetZ = (float)Math.abs(box.minZ-box.maxZ)*0.01f;
                //System.out.println(offsetX + " " + offsetY + " " + offsetZ);
                //lookAt(new Vec3d(entity.posX, entity.posY, entity.posZ), new Vec3d(0, 0, 0));
                float[] rot = PlayerUtils.getLookAt(new Vec3d(entity.posX, entity.posY, entity.posZ), new Vec3d(0, 0, 0));
                mc.player.rotationYaw = rot[0];
                mc.player.rotationPitch = rot[1];
                //mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rot[0], rot[1], true));
                //mc.player.setSprinting(false);

                mc.playerController.attackEntity(mc.player, entity);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                return;
            }
        }
    }

    boolean canEntityBeAttacked(Entity entity)
    {
        //if(!(entity instanceof EntityLiving)) return false;
        return (entity instanceof EntityMob && monsters.getBoolean()) ||
                (entity instanceof EntityAnimal && animal.getBoolean()) ||
                (entity instanceof EntityPlayerSP && players.getBoolean()) ||
                (entity instanceof EntityBoat);
    }

}
