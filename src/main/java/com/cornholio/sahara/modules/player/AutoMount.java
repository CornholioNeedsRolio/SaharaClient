package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.*;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Date;
import java.util.List;

public class AutoMount extends Module
{
    ModuleSetting boat;
    ModuleSetting llama;
    ModuleSetting donkey;
    ModuleSetting pigs;
    ModuleSetting horse;
    ModuleSetting skeletonHorse;


    ModuleSetting delay;
    ModuleSetting range;
    private Timer timer = new Timer();
    public AutoMount()
    {
        super("AutoMount", "Auto mounts to a nearby entity", ModuleCategory.Player);
        registerSetting(boat = new ModuleSetting("Boats", false));
        registerSetting(llama = new ModuleSetting("Llama", false));
        registerSetting(donkey = new ModuleSetting("Donkey", false));
        registerSetting(pigs = new ModuleSetting("Pigs", false));
        registerSetting(horse = new ModuleSetting("Horse", false));
        registerSetting(skeletonHorse = new ModuleSetting("Skeleton Horse", false));

        registerSetting(delay = new ModuleSetting("Delay", 0, 10, 0.75f));
        registerSetting(range = new ModuleSetting("Range", 0, 10, 4.5f));
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(mc.player != null && mc.player.isRiding() || !timer.passed(delay.getFloat() * 1000)) return;

        List<Entity> entities = mc.world.getLoadedEntityList();
        for(Entity entity : entities)
        {
            if(isEntityValid(entity) && mc.player.getDistanceSq(entity.getPosition()) <= range.getFloat())
            {
                mc.playerController.interactWithEntity(mc.player, entity, EnumHand.MAIN_HAND);
                timer.reset();
                return;
            }
        }
    }

    private boolean isEntityValid(Entity entity)
    {
        return (entity instanceof EntityBoat && boat.getBoolean()       ||
                entity instanceof EntityLlama && llama.getBoolean()     ||
                entity instanceof EntityDonkey && donkey.getBoolean()   ||
                entity instanceof EntityPig && pigs.getBoolean()        ||
                entity instanceof EntityHorse && horse.getBoolean()     ||
                entity instanceof EntitySkeletonHorse && skeletonHorse.getBoolean());
    }
}
