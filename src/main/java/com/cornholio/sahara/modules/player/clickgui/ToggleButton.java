package com.cornholio.sahara.modules.player.clickgui;

import com.cornholio.sahara.modules.ModuleSetting;

public class ToggleButton extends Window implements IRefreshable
{
    TexBoxWindow textBoxWindow;
    ModuleSetting setting;
    public ToggleButton(int x, int y, int width, int height, ModuleSetting setting) {
        super(x, y, width, height);

        this.setting = setting;

        this.textBoxWindow = new TexBoxWindow(0,0,0,0);
        this.background_color = this.textBoxWindow.background_color = getIntFromColor(0,0,0,0);
        this.stretchToParentX = true;
        addChildren(textBoxWindow);
        setPadding(0,2,0,2);
        updateText();

        this.setting.getParent().addRefreshable(this);
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
                setting.setBool(!setting.getBoolean());
                updateText();
            }
        }
    }

    private void updateText()
    {
        textBoxWindow.setText(setting.getName()+":"+(setting.getBoolean() ? "ON" : "OFF"));
    }

    @Override
    public void refreshWindow() {
        updateText();
    }
}
