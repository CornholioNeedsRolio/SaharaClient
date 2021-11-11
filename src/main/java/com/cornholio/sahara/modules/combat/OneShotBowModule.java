package com.cornholio.sahara.modules.combat;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OneShotBowModule extends Module
{
    ModuleSetting packet;
    ModuleSetting timeout;
    public OneShotBowModule()
    {
        super("One Shot Bow", "It showes an arrow down your enemy's ass and they die from pleasure", ModuleCategory.Combat);
        registerSetting(packet = new ModuleSetting("Packets", 0, 200, 75));
        registerSetting(timeout = new ModuleSetting("Timeout", 100, 20000, 5000));
    }

    private boolean shooting;
    private long lastShootTime;

    @Override
    public void onEnable() {
        super.onEnable();
        shooting = false;
        lastShootTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent event) {
        if(mc.player == null || mc.world == null) return;
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();

            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                ItemStack handStack = mc.player.getHeldItem(EnumHand.MAIN_HAND);

                if (!handStack.isEmpty() && handStack.getItem() != null && handStack.getItem().equals(Items.BOW)) {
                    doSpoofs();
                }
            }
        }
    }

    private void doSpoofs() {
        if (System.currentTimeMillis() - lastShootTime >= timeout.getFloat()) {
            shooting = true;
            lastShootTime = System.currentTimeMillis();

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));

            for (int index = 0; index < packet.getFloat(); ++index) {
                offsetPositionPacket(new Vec3d(0, -1e-6, 0), true);
                offsetPositionPacket(new Vec3d(0, 1e-6, 0), false);
            }
            shooting = false;
        }
    }

    private void offsetPositionPacket(Vec3d vector, boolean onGround) {
        Vec3d newVec = mc.player.getPositionVector().add(vector);
        mc.player.connection.sendPacket(new CPacketPlayer.Position(newVec.x, newVec.y, newVec.z, onGround));
    }
}
