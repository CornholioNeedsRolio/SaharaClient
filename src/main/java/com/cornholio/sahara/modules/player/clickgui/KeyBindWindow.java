package com.cornholio.sahara.modules.player.clickgui;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.player.clickgui.TexBoxWindow;
import com.cornholio.sahara.modules.player.clickgui.Window;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class KeyBindWindow extends Window implements IRefreshable
{
    TexBoxWindow textBoxWindow;
    private boolean choosingKey;
    private Module module;
    public KeyBindWindow(int x, int y, int width, int height, Module module) {
        super(x, y, width, height);

        this.module = module;
        module.addRefreshable(this);

        choosingKey = false;
        textBoxWindow = new TexBoxWindow(0,0,0,0);
        textBoxWindow.setPadding(2,2,2,2);
        textBoxWindow.stretchToParentX = true;
        background_color = textBoxWindow.background_color = getIntFromColor(0,0,0,0);
        stretchToParentX = true;
        addChildren(textBoxWindow);
        setPadding(0,2,0,2);
        updateText();
    }

    @Override
    public void mouseUpdate(int mouseX, int mouseY, int clicked, boolean released)
    {
        if(released) return;
        int temp_x = getX(false);
        int temp_y = getY(false);
        if(temp_x < mouseX && temp_x+width>mouseX && temp_y < mouseY && temp_y+height>mouseY)
        {
            if(clicked == 0) {
                if(choosingKey)
                    module.setKey(0);
                choosingKey = !choosingKey;
                updateText();
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        super.keyTyped(typedChar, keyCode);
        if(choosingKey)
        {
            module.setKey(keyCode);
            choosingKey = false;
            updateText();
        }
    }

    private void updateText()
    {
        background_color = choosingKey ? getIntFromColor(255,0,0, 200) : getIntFromColor(100,100,100,50);
        textBoxWindow.setText("CurrentKey: "+(choosingKey?"Choose a key" : Keyboard.getKeyName(module.getKey())));

        refreshWindowSizeToMatchChildren();
        if(parent != null && parent.refreshable)
            parent.refreshWindowSizeToMatchChildren();
    }

    @Override
    public void refreshWindow() {
        updateText();
    }
}
