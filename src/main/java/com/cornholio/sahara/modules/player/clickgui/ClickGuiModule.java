package com.cornholio.sahara.modules.player.clickgui;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class ClickGuiModule extends Module
{
    public ClickGuiModule() {
        super("ClickGUI", "Manages the utility mod's menu", ModuleCategory.Player);
        //dontLoadOrSave = true;
        //this.setKey(Keyboard.KEY_B);
    }

    @Override
    public void onEnable()
    {
        super.onEnable();
        mc.displayGuiScreen(WindowManager.getWindowManager());
    }

    @Override
    public void onDisable()
    {
        if(mc.player != null)
            mc.player.closeScreen();
        super.onDisable();
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {
        //I don't want the server to see that I closed the ClickGUI
        //if(event.getPacket() instanceof CPacketCloseWindow)
        //    event.setCanceled(true);
    }


    //@SubscribeEvent
    //public void onRenderGameOverlayEvent(TickEvent.ClientTickEvent event) {
    //    WindowManager.getWindowManager().renderEverything();
    //}
}
