package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.utils.PlayerUtils;
import com.cornholio.sahara.utils.Timer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;

public class AutoFishModule extends Module {

    ModuleSetting recastingTimer;
    Timer timer;
    Timer delayReel;
    Timer rightClickTimer;
    int delayTicks = 0;
    public AutoFishModule()
    {
        super("Auto Fish", "Does some fishing", ModuleCategory.Player);
        registerSetting(recastingTimer = new ModuleSetting("Recasting Timer", 0, 1000, 500));
        timer = new Timer();
        delayReel = new Timer();
        rightClickTimer = new Timer();
    }

    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event)
    {
        if(mc.player == null || mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.FISHING_ROD) return;

        if(delayTicks != 0)
        {
            if(--delayTicks == 0)
                mc.rightClickMouse();
        }

        if(mc.player.fishEntity == null) {
            if (timer.passed(recastingTimer.getFloat())) {
                timer.reset();
                cast();
            }
        }else {
            if (isBobbing() && delayReel.passed(500)) {
                mc.rightClickMouse();
                delayReel.reset();
            }
        }

        //System.out.println(Math.abs(mc.player.fishEntity.motionY));
        //System.out.println(mc.player.fishEntity.motionX + " " + mc.player.fishEntity.motionY + " " + mc.player.fishEntity.motionZ);
    }

    BlockPos findBestWater()
    {
        int range = 4;

        BlockPos closest = null;
        double mindist = -1;
        for(int x = -range; x != range; ++x)
        {
            for(int y = -range; y != range; ++y)
            {
                for(int z = -range; z != range; ++z)
                {
                    BlockPos worldPos = mc.player.getPosition().add(new BlockPos(x,y,z));
                    if(mc.world.getBlockState(worldPos).getMaterial() != Material.WATER)
                        continue;
                    Vec3d EyePos = mc.player.getPositionEyes(1);
                    RayTraceResult result = mc.world.rayTraceBlocks(EyePos, new Vec3d(worldPos.getX(), worldPos.getY(), worldPos.getZ()), false, false, false);
                    double distance = worldPos.getDistance((int)EyePos.x, (int)EyePos.y, (int)EyePos.z);
                    if(result != null) {
                        if(mindist > distance)
                        {
                            closest = worldPos;
                            mindist = distance;
                        }
                    }
                    else if(closest == null)
                    {
                        closest = worldPos;
                        mindist = distance;
                    }
                }
            }
        }
        return closest;
    }

    private void cast()
    {
        BlockPos pos = findBestWater();
        if(pos == null) return;
        float[] rot = PlayerUtils.getLookAt(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(0.5f, 0, 0.5f));
        mc.player.rotationYaw = rot[0];
        mc.player.rotationPitch = rot[1];
        delayTicks = 5;
        //mc.rightClickMouse();
        //nextTickRightClick = true;
        //mc.rightClickMouse();
    }

    private boolean isBobbing()
    {
        if(Math.abs(mc.player.fishEntity.motionY) >= 0.05 && mc.player.fishEntity.motionX == 0 && mc.player.fishEntity.motionZ == 0) return true;
        return false;
    }
}
