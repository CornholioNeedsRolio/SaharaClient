package com.cornholio.sahara.modules.player.clickgui;

public class VerticalList extends Window
{
    public VerticalList(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void refreshChildren()
    {
        int lastBottom = 0;
        for(Window child : children)
        {
            child.setPosition(0, lastBottom);
            lastBottom = child.getBottom(true);
        }
        refreshWindowSizeToMatchChildren();
        if(parent != null && parent.refreshable) parent.refreshWindowSizeToMatchChildren();
    }
}
