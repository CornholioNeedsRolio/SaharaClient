package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import com.cornholio.sahara.utils.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;

public class PacketNukerModule extends Module {
    ModuleSetting range;
    ModuleSetting packetAmount;
    ModuleSetting timerS;

    BlockPos current;

    Timer timer;

    EnumFacing lastFacing = EnumFacing.UP;

    public PacketNukerModule()
    {
        super("Packet Nuker", "packet nuker", ModuleCategory.Player);
        registerSetting(range = new ModuleSetting("Range", 0, 5, 4.5f));
        registerSetting(packetAmount = new ModuleSetting("Packets", 0, 100, 1));
        registerSetting(timerS = new ModuleSetting("Timer", 0, 1000, 500));

        timer = new Timer();
    }

    @Override
    public void onEnable()
    {
        super.onEnable();
        current = null;
    }

    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event)
    {
        if(mc.player == null || mc.world == null) return;
        mine();
        System.out.println(lastFacing);
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {
        if(event.getPacket() instanceof CPacketPlayerDigging)
        {
            CPacketPlayerDigging digging = (CPacketPlayerDigging)event.getPacket();
            if(digging.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK && digging.getPosition() == current)
                event.setCanceled(true);
        }
    }

    void mine()
    {
        int roundedRange = Math.round(range.getFloat());
        boolean nothingFound = true;
        for(int x = -roundedRange-1; x <= roundedRange; ++x)
        {
            for(int y = roundedRange+1; y >= -roundedRange; --y)
            {
                for(int z = -roundedRange-1; z <= roundedRange; ++z)
                {
                    BlockPos pos = new BlockPos(x,y,z).add(mc.player.getPosition());
                    if(mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN && pos.getDistance(mc.player.getPosition().getX(), mc.player.getPosition().getY(), mc.player.getPosition().getZ()) <= range.getFloat())
                    {
                        //RayTraceResult result = mc.world.rayTraceBlocks(mc.player.getPositionEyes(1.0F), new Vec3d(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5));
                        //if(result == null)
                        //    continue;
                        if(current == null || mc.world.isAirBlock(current))
                        {
                            current = pos;
                            //lastFacing = result.sideHit;
                            timer.reset();
                            spamPackets(current, 1, true);
                            return;
                        }
                        nothingFound = false;
                    }
                }
            }
        }
        if(nothingFound)
        {
            current = null;
        } else if(current != null) {
            if(current.getDistance(mc.player.getPosition().getX(), mc.player.getPosition().getY(), mc.player.getPosition().getZ()) > range.getFloat())
            {
                current = null;
                return;
            }

            if(timer.passed(timerS.getFloat()))
            {
                spamPackets(current, 1, true);
                timer.reset();
            }

            spamPackets(current, (int) Math.ceil(packetAmount.getFloat()), false);
        }
    }

    void spamPackets(BlockPos pos, int amount, boolean breakBlock)
    {
        lockAt(pos, 0.5f, 0.5f);
        //mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));

        if(mc.objectMouseOver != null)
            lastFacing = mc.objectMouseOver.sideHit;
        if(lastFacing == null)
            lastFacing = EnumFacing.UP;

        if(breakBlock)
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, lastFacing));
        else for(int i = 0; i < amount; ++i)
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, lastFacing));
    }

    void lockAt(BlockPos place, float x, float z)
    {
        double diffX = place.getX()+x - mc.player.posX;
        double diffY = place.getY() - mc.player.posY - 1.2;
        double diffZ = place.getZ()+z - mc.player.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180D / Math.PI) - 90F;
        float pitch = (float)-(Math.atan2(diffY, dist) * 180D / Math.PI);
        //mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, true));
        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;

    }
}
