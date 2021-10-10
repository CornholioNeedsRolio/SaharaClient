package com.cornholio.sahara.modules.player;

import ca.weblite.objc.Client;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

public class FastMineModule extends Module {
    ModuleSetting mineSpeed;
    ModuleSetting hitDelay;

    Field field = null;
    public FastMineModule() {
        super("FastMine", "It breaks blocks faster than before", ModuleCategory.Player);

        registerSetting(mineSpeed = new ModuleSetting("Mine Speed", 0, 1, 0.72f));
        registerSetting(hitDelay = new ModuleSetting("Hit Delay", 0, 10, 0.0f));
        field = ObfuscationReflectionHelper.findField(PlayerControllerMP.class, "field_78781_i");
    }

    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent event) throws IllegalAccessException {
        if(mc.playerController == null) return;
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
        event.setNewSpeed(event.getOriginalSpeed()*(1.F+mineSpeed.getFloat()));
    }
}
