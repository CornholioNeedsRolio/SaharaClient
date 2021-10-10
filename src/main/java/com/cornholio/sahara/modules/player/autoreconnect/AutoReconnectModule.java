package com.cornholio.sahara.modules.player.autoreconnect;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;

public class AutoReconnectModule extends Module
{
    public ModuleSetting delay;
    public AutoReconnectModule() {
        super("AutoReconnect", "Auto Reconnects you to the last server you joined", ModuleCategory.Player);
        registerSetting(delay=new ModuleSetting("Delay", 0, 10, 5));
    }


}
