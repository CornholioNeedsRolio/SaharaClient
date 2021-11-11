package com.cornholio.sahara.modules.movement;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VelocityModule extends Module {
    ModuleSetting vertical;
    ModuleSetting horizontal;

    public VelocityModule()
    {
        super("Velocity", "Multiples knockback", ModuleCategory.Movement);
        registerSetting(vertical = new ModuleSetting("Vertical", 0, 1, 1));
        registerSetting(horizontal = new ModuleSetting("Horizontal", 0, 1, 1));
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {
        if(mc.player == null) return;

        if(event.getPacket() instanceof SPacketEntityVelocity)
        {
            SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();
            if(packet.getEntityID() == mc.player.getEntityId())
            {
                packet.motionX *= horizontal.getFloat();
                packet.motionY *= vertical.getFloat();
                packet.motionZ *= horizontal.getFloat();
            }
        }
    }
}
