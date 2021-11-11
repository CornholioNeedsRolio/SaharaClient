package com.cornholio.sahara.modules.player;

import ca.weblite.objc.Client;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;

import java.lang.reflect.Field;

public class FastMineModule extends Module {
    ModuleSetting mineSpeed;
    ModuleSetting hitDelay;

    Field field = null;
    public FastMineModule() {
        super("FastMine", "It breaks blocks faster than before", ModuleCategory.Player);

        registerSetting(mineSpeed = new ModuleSetting("Mine Speed", 0, 20, 5));
        registerSetting(hitDelay = new ModuleSetting("Hit Delay", 0, 10, 0.0f));
        field = ObfuscationReflectionHelper.findField(PlayerControllerMP.class, "field_78781_i");
    }

    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent event) throws IllegalAccessException {
        if(mc.playerController == null) return;

        //System.out.println(currentSpeed);
        field.setInt(mc.playerController, (int)hitDelay.getFloat());
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        //PlayerInteractEvent.LeftClickBlock.
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {

    }
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if(mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null) return;
        IBlockState state = mc.world.getBlockState(mc.objectMouseOver.getBlockPos());
        float currentSpeed = mc.player.getHeldItem(EnumHand.MAIN_HAND).getDestroySpeed(state);

        event.setNewSpeed((currentSpeed)*(mineSpeed.getFloat()));
    }
}
