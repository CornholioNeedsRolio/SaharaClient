package com.cornholio.sahara.modules.player.clickgui;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Window
{
    protected int xPos;
    protected int yPos;
    protected int width;
    protected int height;
    protected int PaddingLeft;
    protected int PaddingTop;
    protected int PaddingRight;
    protected int PaddingBottom;
    protected int xReal;
    protected int yReal;
    protected boolean stretchToParentX;
    protected boolean stretchToParentY;
    protected boolean refreshable;

    protected int background_color = getIntFromColor(255, 255, 255, 255);
    protected int foreground_color = getIntFromColor(0, 0, 0, 255);

    protected List<Window> children = new ArrayList<>();
    Window parent = null;

    public Window(int x, int y, int width, int height)
    {
        xReal = x;
        yReal = y;
        this.width = width;
        this.height = height;
        this.PaddingLeft = 0;
        this.PaddingTop = 0;
        this.PaddingRight = 0;
        this.PaddingBottom = 0;
        this.stretchToParentX = false;
        this.stretchToParentY = false;
        this.refreshable = true;
        UpdateDrawPos();
    }

    public void UpdateDrawPos()
    {
        xPos = xReal+PaddingLeft;
        yPos = yReal+PaddingTop;
    }

    public Window setPadding(int PaddingLeft, int PaddingTop, int PaddingRight, int PaddingBottom)
    {
        this.PaddingLeft = PaddingLeft;
        this.PaddingTop = PaddingTop;
        this.PaddingRight = PaddingRight;
        this.PaddingBottom = PaddingBottom;
        return this;
    }

    public void setPosition(int x, int y)
    {
        xReal = x;
        yReal = y;
        UpdateDrawPos();
    }

    public int getX(boolean real)
    {
        int val = real ? xReal : xPos;
        if(parent != null)
            return parent.getX(real) + val;
        return val;
    }

    public int getY(boolean real)
    {
        int val = real ? yReal : yPos;
        if(parent != null)
            return parent.getY(real) + val;
        return val;
    }

    public void renderWindow()
    {
        int temp_x = getX(false);
        int temp_y = getY(false);

        UpdateDrawPos();
        if(parent != null)
        {
            if(stretchToParentX) width = parent.width - PaddingRight - PaddingLeft;
            if(stretchToParentY) height = parent.height - PaddingBottom - PaddingTop;
        }
        Gui.drawRect(temp_x, temp_y, temp_x+width, temp_y+height, background_color);
        for(Window child : children)
            child.renderWindow();
    }

    public int getTop(boolean padding)
    {
        return yReal + (padding ? PaddingTop : 0);
    }

    public int getLeft(boolean padding)
    {
        return xReal + (padding ? PaddingLeft : 0);
    }

    public int getBottom(boolean padding)
    {
        return yReal+height + (padding ? PaddingBottom : 0);
    }

    public int getRight(boolean padding)
    {
        return xReal+width + (padding ? PaddingRight : 0);
    }


    public void addChildren(Window window)
    {
        children.add(window);
        window.parent = this;
    }

    public void removeChildren(Window window)
    {
        children.remove(window);
        window.parent = null;
    }

    public void refreshWindowSizeToMatchChildren()
    {
        width = 0;
        height = 0;
        for(Window window : children)
        {
            width = Math.max(width, window.xReal + window.width + window.PaddingRight + window.PaddingLeft);
            height = Math.max(height, window.yPos + window.height + window.PaddingBottom + window.PaddingTop);
        }
    }

    //0 = left click
    //1 = right click
    //3 = middle click
    public void mouseUpdate(int mouseX, int mouseY, int clicked, boolean released)
    {
        for(Window window : children)
            window.mouseUpdate(mouseX, mouseY, clicked, released);
    }

    public void keyTyped(char typedChar, int keyCode)
    {
        for(Window window : children)
            window.keyTyped(typedChar, keyCode);
    }

    public static int getIntFromColor(int red, int green, int blue, int alpha) {
        alpha = (alpha & 255) << 24;
        red = (red & 255) << 16;
        green = (green & 255) << 8;
        blue = (blue & 255);
        return red | green | blue | alpha;
    }
}
