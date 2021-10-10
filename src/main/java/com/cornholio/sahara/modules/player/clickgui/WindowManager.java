package com.cornholio.sahara.modules.player.clickgui;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.ModuleManager;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class WindowManager extends GuiScreen
{
    List<Window> windowList;
    public WindowManager()
    {
        super();
        windowList = new ArrayList<Window>();
        instance = this;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if(SaharaClient.getSahara().getModuleManager().ClickGUI.isActive()) {
            renderEverything();
            for(Window window : windowList)
                window.mouseUpdate(mouseX, mouseY, 69, false);
        }
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        for(Window window : windowList)
            window.mouseUpdate(mouseX, mouseY, mouseButton, false);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        for(Window window : windowList)
            window.mouseUpdate(mouseX, mouseY, state, true);
    }

    public void InitClickGUI(ModuleManager manager)
    {
        int width = Minecraft.getMinecraft().displayWidth;
        int distance = width / ModuleCategory.values().length;
        int currentX = 0;
        for(ModuleCategory c : ModuleCategory.values())
        {
            CategoryFrame temp = null;
            addWindowToScreen(temp = new CategoryFrame(currentX,0, c));
            currentX += temp.width+10;
        }
    }

    public void renderEverything()
    {
        for(Window window : windowList)
        {
            window.renderWindow();
        }
    }

    public void addWindowToScreen(Window window)
    {
        windowList.add(window);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        for(Window window : windowList)
            window.keyTyped(typedChar, keyCode);
    }


    public static WindowManager instance;
    public static WindowManager getWindowManager() { return instance; }
}
