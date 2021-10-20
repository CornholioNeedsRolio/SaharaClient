package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import com.cornholio.sahara.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.*;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Objects;

public class BoatFly extends Module
{
    ModuleSetting speed;
    ModuleSetting noClip;
    ModuleSetting CancelPlayerPackers;
    ModuleSetting glide;
    ModuleSetting upSpeed;
    ModuleSetting downSpeed;
    ModuleSetting fixYaw;
    ModuleSetting PacketCanceler;
    //ModuleSetting GoNUTS;

    EntityBoat the_boat;

    Timer timer;
    private boolean sendPacket;
    public BoatFly()
    {
        super("BoatFly", "Fly with the boat",  ModuleCategory.Player);
        registerSetting(PacketCanceler = new ModuleSetting("Packet Canceler", true));
        registerSetting(CancelPlayerPackers = new ModuleSetting("Cancel Player Packets", true));
        registerSetting(noClip = new ModuleSetting("No Clip", false));
        //registerSetting(GoNUTS = new ModuleSetting("Go NUTS", false));

        registerSetting(fixYaw = new ModuleSetting("Fixed Yaw", true));
        registerSetting(speed = new ModuleSetting("Speed", 0, 20, 1));
        registerSetting(upSpeed = new ModuleSetting("Up Speed", 0, 20, 1));
        registerSetting(downSpeed = new ModuleSetting("Down Speed", 0, 20, 1));
        registerSetting(glide = new ModuleSetting("Glide", 0, 20, 0.2f));

        timer = new Timer();
    }

    public boolean getFixYaw()
    {
        return fixYaw.getBoolean() && isActive();
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event) {
        if(mc.player == null || mc.player.getRidingEntity() == null) return;

        if (event.getPacket() instanceof SPacketPlayerPosLook && CancelPlayerPackers.getBoolean()) {
            event.setCanceled(true);
        }

        if (!PacketCanceler.getBoolean() || mc.world == null) return;
        if ((event.getPacket() instanceof SPacketSetPassengers) ) {
            SPacketSetPassengers packet = (SPacketSetPassengers)event.getPacket();

            if(the_boat != null && packet.getPassengerIds().length == 0) {
                //mc.player.connection.sendPacket(new CPacketUseEntity(the_boat, EnumHand.MAIN_HAND));
                timer.reset();
                sendPacket = true;
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event)
    {
        //if(!(event.getEntity() instanceof EntityBoat)) return;

        if(mc.player == null || mc.player.getRidingEntity() == null) return;
        if(!(mc.player.getRidingEntity() instanceof EntityBoat)) return;

        the_boat = (EntityBoat) mc.player.getRidingEntity();
        the_boat.noClip = noClip.getBoolean();
        //mc.player.noClip = noClip.getBoolean();
        if(timer.passed(75) && sendPacket)
        {
            mc.player.connection.sendPacket(new CPacketUseEntity(the_boat, EnumHand.MAIN_HAND));
            sendPacket = false;
        }

        Entity boat = mc.player.getRidingEntity();

        boat.rotationYaw = mc.player.rotationYaw;
        boat.rotationPitch = mc.player.rotationPitch;
        boat.prevRotationPitch = mc.player.prevRotationPitch;
        boat.prevRotationYaw = mc.player.prevRotationYaw;

        boat.motionY = -glide.getFloat();

        int forward = mc.gameSettings.keyBindForward.isKeyDown() ? 1 : mc.gameSettings.keyBindBack.isKeyDown() ? -1 : 0;
        int right = mc.gameSettings.keyBindRight.isKeyDown() ? 1 : mc.gameSettings.keyBindLeft.isKeyDown() ? -1 : 0;

        float yaw = boat.prevRotationYaw + (boat.rotationYaw - boat.prevRotationYaw) * mc.getRenderPartialTicks();
        //float pitch = boat.prevRotationPitch + (boat.rotationPitch - boat.prevRotationPitch) * mc.getRenderPartialTicks();

        double radYaw = yaw * 0.0174533;

        if (forward != 0 || right != 0) {
            double cos = Math.cos(radYaw), sin = Math.sin(radYaw);

            boat.motionX = (-sin*forward - cos*right )*speed.getFloat();
            boat.motionZ = (cos*forward - sin*right) *speed.getFloat();
        }
        else
        {
            boat.motionX = 0;
            boat.motionZ = 0;
        }

        if(mc.player.movementInput.jump)
            boat.motionY = upSpeed.getFloat();
        int c0 = Keyboard.getEventKey();
        if(c0 == Keyboard.KEY_LCONTROL)
            boat.motionY = -downSpeed.getFloat();
    }
}
