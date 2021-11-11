package com.cornholio.sahara.modules.player.clickgui;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.player.clickgui.TexBoxWindow;
import com.cornholio.sahara.modules.player.clickgui.Window;

public class SliderWindow extends Window implements IRefreshable
{
    Window slider;
    TexBoxWindow textBoxWindow;
    protected ModuleSetting setting;
    private boolean firstRun = true;

    private boolean isDragging = false;
    public SliderWindow(int x, int y, int width, int height, ModuleSetting setting) {
        super(x, y, width, height);
        slider = new Window(x,y,width, height);
        this.setting = setting;
        background_color = getIntFromColor(0,0,0,0);
        textBoxWindow = new TexBoxWindow(0,0,0,0);
        textBoxWindow.background_color = getIntFromColor(0,0,0,0);
        textBoxWindow.stretchToParentX = true;
        textBoxWindow.attachedColorModuleForeground = SaharaClient.getSahara().getModuleManager().moduleOptionTextF;
        stretchToParentX = true;
        slider.background_color = setting.getSliderColor();//getIntFromColor(255,0,0,150);
        setPadding(0,2,0,2);
        addChildren(textBoxWindow);
        this.width = width;
        updateText(true);
        this.width = width;//an idiot sandwich... at least it fixes that bug
        refreshable = false;

        this.setting.getParent().addRefreshable(this);
    }

    @Override
    public void renderWindow()
    {
        //please look away for one second
        if(slider.width == 0 && setting.getFloat() != setting.getMin() || firstRun) {
            updateText(false);
            firstRun = true;
        }
        //thanks now you can look back

        slider.xPos = getX(false)-1;
        slider.yPos = getY(false)-1;
        slider.height = height + 1;
        slider.renderWindow();
        super.renderWindow();
    }

    @Override
    public void mouseUpdate(int mouseX, int mouseY, int clicked, boolean released)
    {
        int temp_x = getX(false);
        int temp_y = getY(false);

        if(temp_x < mouseX && temp_x+width>mouseX && temp_y < mouseY && temp_y+height>mouseY) {
            if(released && clicked == 0)
                isDragging = false;
            else if(clicked == 0)
                isDragging = true;
        }
        else if(released)
            isDragging = false;


        if(isDragging) {
            int mouseDiff = temp_x - mouseX;
            if(mouseDiff > 0)
                setting.setFloat(setting.getMin());
            else {
                setting.setFloat((Math.abs(mouseDiff) / (float) width) * (setting.getMax() - setting.getMin()) + setting.getMin());
                if(setting.getClampMax())
                {
                    setting.setFloat(Math.min(setting.getFloat(), setting.getMax()));
                }
            }
            updateText(true);
        }
    }
    private void updateText(boolean text) //ironic isn't it
    {
        if(text)
            textBoxWindow.setText(setting.getName()+": "+setting.getFloat());
        slider.width = Math.min(width, (int)(((setting.getFloat()-setting.getMin())/(setting.getMax()-setting.getMin()))*(float)width));
    }

    @Override
    public void refreshWindow() {
        updateText(true);
    }
}
