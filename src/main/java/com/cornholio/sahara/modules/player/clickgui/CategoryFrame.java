package com.cornholio.sahara.modules.player.clickgui;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleManager;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;

import java.util.List;

public class CategoryFrame extends Window {
    TexBoxWindow textBox;
    Window whiteBar;
    Window parentOfTextbox;
    VerticalList moduleList;

    private boolean isDragging = false;
    private int dragX;
    private int dragY;

    public CategoryFrame(int x, int y, ModuleCategory category) {
        super(x, y, 0, 0);
        refreshable = true;
        //TITLE
        addChildren(parentOfTextbox = new Window(0, 0, 0, 0));
        parentOfTextbox.background_color = getIntFromColor(0,0,0,0);
        parentOfTextbox.attachedColorModuleBackground = SaharaClient.getSahara().getModuleManager().listTitleColorB;
        parentOfTextbox.setPadding(2, 2, 2, 0);
        parentOfTextbox.stretchToParentX = true;

        parentOfTextbox.addChildren(textBox = new TexBoxWindow(0, 0, 0, 0).setText(category.getName()));
        textBox.attachedColorModuleForeground = SaharaClient.getSahara().getModuleManager().listTitleColorF;//getIntFromColor(255, 255, 255, 255);
        textBox.setPadding(2, 2, 2, 0);
        textBox.background_color = getIntFromColor(0, 0, 0, 0);
        textBox.stretchToParentX = true;
        parentOfTextbox.refreshWindowSizeToMatchChildren();
        //background_color = getIntFromColor(125, 125, 125, 50);
        attachedColorModuleBackground = SaharaClient.getSahara().getModuleManager().listColorB;
        //NICE WHITE BAR UwU
        addChildren(whiteBar = new Window(0, parentOfTextbox.getBottom(true), 0, 2));
        whiteBar.setPadding(2, 0, 2, 2);
        whiteBar.attachedColorModuleBackground = SaharaClient.getSahara().getModuleManager().barColorB;//getIntFromColor(255, 255, 255, 255);
        whiteBar.stretchToParentX = true;
        //Module list
        moduleList = new VerticalList(textBox.getLeft(false), whiteBar.getBottom(true), 0, 0);
        addChildren(moduleList);

        moduleList.stretchToParentX = true;
        moduleList.background_color = getIntFromColor(0, 0, 0, 0);//yay invisible black

        for (Module module : SaharaClient.getSahara().getModuleManager().modules) {
            if (module.getCategory() == category) {
                ModuleButton button = new ModuleButton(0, 0, 0, 0, module);
                button.setPadding(2, 1, 2, 1);
                button.stretchToParentX = true;
                moduleList.addChildren(button);
            }
        }
        moduleList.refreshWindowSizeToMatchChildren();
        moduleList.refreshChildren();

        refreshWindowSizeToMatchChildren();
    }

    @Override
    public void mouseUpdate(int mouseX, int mouseY, int clicked, boolean released)
    {
        super.mouseUpdate(mouseX, mouseY, clicked, released);
        int temp_x = textBox.getX(true);
        int temp_y = textBox.getY(true);

        if(clicked == 0 && released)
            isDragging = false;
        if(temp_x < mouseX && temp_x+textBox.width>mouseX && temp_y < mouseY && temp_y+textBox.height>mouseY) {
            if(clicked == 0 && !released) {
                dragX = mouseX-xReal;
                dragY = mouseY-yReal;
                isDragging = true;
            }
        }

        if(isDragging)
        {
            xReal = mouseX - dragX;
            yReal = mouseY - dragY;
        }
    }
}
