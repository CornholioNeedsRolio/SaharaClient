package com.cornholio.sahara.modules.visuals;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;

public class ColorModule extends Module
{

    ModuleSetting R;
    ModuleSetting G;
    ModuleSetting B;
    ModuleSetting A;

    float org_R, org_G, org_B, org_A;

    public ColorModule(String name, float R, float G, float B, float A)
    {
        super(name, "Module used for colors", ModuleCategory.Visuals);

        org_R = R;
        org_G = G;
        org_B = B;
        org_A = A;

        registerSetting(this.R = new ModuleSetting("R", 0, 255, R).setClampMax(true));
        registerSetting(this.G = new ModuleSetting("G", 0, 255, G).setClampMax(true).setSliderColor(getIntFromColor(0, 255, 0, 255)));
        registerSetting(this.B = new ModuleSetting("B", 0, 255, B).setClampMax(true).setSliderColor(getIntFromColor(0, 0, 255, 255)));
        registerSetting(this.A = new ModuleSetting("A", 0, 255, A).setClampMax(true).setSliderColor(getIntFromColor(255, 255, 255, 255)));
    }

    public int getColor()
    {
        if(!isActive())
            return getIntFromColor((int)org_R, (int)org_G, (int)org_B, (int)org_A);
        return getIntFromColor((int)this.R.getFloat(), (int)this.G.getFloat(), (int)this.B.getFloat(), (int)this.A.getFloat());
    }

    private static int getIntFromColor(int red, int green, int blue, int alpha) {
        alpha = (alpha & 255) << 24;
        red = (red & 255) << 16;
        green = (green & 255) << 8;
        blue = (blue & 255);
        return red | green | blue | alpha;
    }
}
