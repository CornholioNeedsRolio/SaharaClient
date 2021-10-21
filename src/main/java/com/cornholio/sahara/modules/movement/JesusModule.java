package com.cornholio.sahara.modules.movement;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;

import java.util.Random;

public class JesusModule extends Module
{
    public JesusModule()
    {
        super("Jesus", "Jesus used this hack a few years ago", ModuleCategory.Movement);
    }
    private float offset = 0;
    private int ticks = 0;
    public AxisAlignedBB getCollisionBoundingBox(AxisAlignedBB defaultBB)
    {
        if(!isActive() || mc.player == null || mc.player.fallDistance >= 3 || mc.player.motionY > 0.09) return defaultBB;
        AxisAlignedBB box = (new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
        return box;
    }

    public boolean canCollide(boolean defaultBool)
    {
        if(!isActive()) return defaultBool;
        return false;
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if(mc.player == null || mc.world == null) return;

        offset += 0.01;
        if(offset >= 0.2)
            offset = 0.04F;

        if(isInWater())
            mc.player.motionY = 0.1;

    }


    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {
        if(mc.player == null || mc.world == null) return;
        float values[] = new float[]
                {
                    0, 0.019f, 0.06f, 0.08f, 0.02f
                };

        if(event.getPacket() instanceof CPacketPlayer && !isInWater() && isOnWater())
        {
            CPacketPlayer ev = (CPacketPlayer) event.getPacket();
            ev.y = mc.player.posY - values[ticks++ % values.length];

            //System.out.println(ev.y);
        }
        if(event.getPacket() instanceof SPacketPlayerPosLook && isOnWater() && !isInWater())
        {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            packet.y = mc.player.posY;
        }
    }

    public boolean somefuncwithoutaname(boolean onWater) {
        if(mc.player == null || mc.player.fallDistance >= 3) return false;

        AxisAlignedBB playerBox = mc.player.getEntityBoundingBox().offset(0, onWater ? -0.1 : 0, 0);

        int y = (int)Math.floor(playerBox.minY);
        boolean possiblyOnWater = false;
        for (int x = (int)Math.floor(playerBox.minX); x <= Math.floor(playerBox.maxX); ++x)
        {
            for (int z = (int)Math.floor(playerBox.minZ); z <= Math.floor(playerBox.maxZ) ; ++z)
            {
                Block state = mc.world.getBlockState(new BlockPos(x,y,z)).getBlock();
                if(!(state instanceof BlockAir)) {
                    if (!(state instanceof BlockLiquid))
                        return false;
                    possiblyOnWater = true;
                }
            }
        }
        return possiblyOnWater;
    }

    public boolean isOnWater()
    {
        return somefuncwithoutaname(true);
    }

    public boolean isInWater()
    {
        return somefuncwithoutaname(false);
    }
}
