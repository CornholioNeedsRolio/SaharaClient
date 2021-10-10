package com.cornholio.sahara.modules.player.clickgui;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;

public class ModuleButton extends Window
{
    private boolean isHovered;
    private boolean isExpanded;
    private Module module;
    TexBoxWindow text;
    Window textBackground;
    VerticalList settingList;
    VerticalList moduleOrderer;
    KeyBindWindow keyBindWindow;

    public int normalColor = getIntFromColor(25,25,25,50);
    public int hoverColor = getIntFromColor(100,100,100,100);

    public int activeNormalColor = getIntFromColor(100,25,25,100);
    public int activeHoverColor = getIntFromColor(200,25,25,200);

    public ModuleButton(int x, int y, int width, int height, Module module) {
        super(x, y, width, height);
        this.isHovered = false;
        this.module = module;
        this.stretchToParentX = true;

        moduleOrderer = new VerticalList(0,0,0,0);
        moduleOrderer.stretchToParentX = true;
        addChildren(moduleOrderer);

        settingList = new VerticalList(x,y,0,0);
        settingList.stretchToParentX = true;
        for(ModuleSetting setting : module.getSettings())
        {
            if(setting.getType() == ModuleSetting.SettingType.BOOL)
                settingList.addChildren(new ToggleButton(0,0,width, height, setting));
            else
                settingList.addChildren(new SliderWindow(0,0,width, height, setting));
        }
        keyBindWindow = new KeyBindWindow(0,0,width,height,module);
        settingList.addChildren(keyBindWindow);


        settingList.refreshChildren();

        text = new TexBoxWindow(0,0,0,0);
        text.setPadding(2,2,2,2);
        moduleOrderer.background_color = text.background_color =  settingList.background_color = background_color = getIntFromColor(0,0,0,0);
        text.setText(this.module.getName());

        textBackground = new Window(0,0,0,0);
        textBackground.stretchToParentX = true;
        textBackground.addChildren(text);
        textBackground.refreshWindowSizeToMatchChildren();

        moduleOrderer.addChildren(textBackground);
        moduleOrderer.refreshWindowSizeToMatchChildren();
        refreshWindowSizeToMatchChildren();

    }

    @Override
    public void mouseUpdate(int mouseX, int mouseY, int clicked, boolean released)
    {
        super.mouseUpdate(mouseX, mouseY, clicked, released);
        if(released)
            return;
        int temp_x = text.getX(false);
        int temp_y = text.getY(false);
        if(temp_x < mouseX && temp_x+textBackground.width>mouseX && temp_y < mouseY && temp_y+textBackground.height>mouseY)
        {
            isHovered = true;
            if(clicked == 0)
                module.toggle();
            else if(clicked == 1) {
                isExpanded = !isExpanded;
                if(isExpanded) {
                    moduleOrderer.addChildren(settingList);
                    settingList.setPosition(0, getBottom(true));
                }
                else moduleOrderer.removeChildren(settingList);
                moduleOrderer.refreshChildren();
                refreshWindowSizeToMatchChildren();
                if(parent instanceof VerticalList)
                    ((VerticalList)parent).refreshChildren();
            }
            textBackground.background_color = module.isActive() ? activeHoverColor : hoverColor;
        }
        else {
            isHovered = false;
            textBackground.background_color = module.isActive() ? activeNormalColor : normalColor;
        }
    }
}
