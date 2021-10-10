package com.cornholio.sahara.modules.player.autoreconnect;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.GuiConnecting;

public class AutoReconnectButton extends GuiButton
{
    AutoReconnectModule bo$$ = SaharaClient.getSahara().getModuleManager().autoReconnect;
    Timer timer = new Timer();
    SaharaClient sh = SaharaClient.getSahara();

    public AutoReconnectButton(int buttonId, int x, int y, String buttonText)
    {
        super(buttonId, x, y, buttonText);
        timer.reset();
    }
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        super.drawButton(mc, mouseX, mouseY, partialTicks);

        float delay = bo$$.delay.getFloat() * 1000.f;
        if(visible) {
            this.displayString = (bo$$.isActive() ? "AutoReconnect(" + (int) Math.abs(timer.getTime() + (int)delay - System.currentTimeMillis()) + ")" : "AutoReconnect(Disabled)");
            if (timer.passed(delay) && bo$$.isActive()) {
                mc.displayGuiScreen(new GuiConnecting(null, mc, sh.LastIp, sh.LastPort));
            }
        }
        else
            timer.reset();
    }


    public void Pressed()
    {
        bo$$.toggle();
        timer.reset();
    }
}
