package com.cornholio.sahara.modules.player.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class TexBoxWindow extends Window
{
    private String text;
    public TexBoxWindow(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public TexBoxWindow setText(String text)
    {
        this.text = text;
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        width = font.getStringWidth(text);
        height = font.FONT_HEIGHT;
        if(parent != null && parent.refreshable) parent.refreshWindowSizeToMatchChildren();
        return this;
    }
    @Override
    public void renderWindow()
    {
        super.renderWindow();
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int x = getX(false);
        int y = getY(false);

        //if(parent.width < font.getStringWidth(text)) {
        //    int max = parent.width / font.getCharWidth(' ');
        //}

        font.drawString(text, x, y, foreground_color);

    }
}

