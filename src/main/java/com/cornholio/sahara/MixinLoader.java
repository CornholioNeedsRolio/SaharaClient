package com.cornholio.sahara;

import com.cornholio.sahara.SaharaClient;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.Name("SaharaMixinLoader")
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class MixinLoader implements IFMLLoadingPlugin {

    public MixinLoader()
    {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins." + SaharaClient.MODID + ".json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        System.out.println("hey whatsup");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
