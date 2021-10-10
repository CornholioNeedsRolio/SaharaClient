package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class CornholioModule extends Module
{
    public CornholioModule()
    {
        super("Cornholio", "I need tp for my bunghole", ModuleCategory.Player);
    }
}
