package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.player.autoreconnect.AutoReconnectButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.List;

@Mixin(GuiDisconnected.class)
public class MixinGuiDisconnect extends GuiScreen
{

    AutoReconnectButton indianGuyThatIsAScretAgentAsAButtonInAMinecraftCheatOWO;
    Minecraft mc = Minecraft.getMinecraft();
    @Inject(method = "initGui()V", at = @At(value="RETURN"))
    public void initGui(CallbackInfo ci)
    {
        buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT, this.height - 30), I18n.format("gui.toMenu")));
        this.buttonList.add(new GuiButton(69420, this.width/2-100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT+25, this.height-5), "Reconnect"));
        this.buttonList.add(indianGuyThatIsAScretAgentAsAButtonInAMinecraftCheatOWO = new AutoReconnectButton(42069, this.width/2-100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT+50, this.height+20), "Reconnect"));
    }

    @Inject(method = "actionPerformed", at = @At(value="HEAD"), cancellable = true)
    protected void actionPerformed(GuiButton button, CallbackInfo ci) throws IOException
    {
        SaharaClient sh = SaharaClient.getSahara();
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parentScreen);
                break;
            case 69420:
                mc.displayGuiScreen(new GuiConnecting(null, mc, sh.LastIp, sh.LastPort));
                break;
            case 42069:
                indianGuyThatIsAScretAgentAsAButtonInAMinecraftCheatOWO.Pressed();
                break;
        }

        ci.cancel();
    }

    @Shadow private int textHeight;
    @Shadow private GuiScreen parentScreen;

}
