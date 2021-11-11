package com.cornholio.sahara.modules.farming;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class AutoShear extends Module {
    ModuleSetting range;
    public AutoShear()
    {
        super("Auto Shear", "shears sheeps", ModuleCategory.Farming);
        registerSetting(range = new ModuleSetting("Range", 0, 5, 4.5f));
    }


    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event)
    {
        if(mc.player == null || mc.world == null) return;
        if(mc.player.getHeldItemMainhand().getItem() == Items.SHEARS) {
            List<Entity> list = mc.world.getLoadedEntityList();
            for (Entity entity : list) {
                if(entity instanceof EntitySheep && mc.player.getDistance(entity) <= range.getFloat() && !((EntitySheep)entity).getSheared())
                {
                    mc.player.connection.sendPacket(new CPacketUseEntity(entity, EnumHand.MAIN_HAND));
                }
            }
        }
    }
}
