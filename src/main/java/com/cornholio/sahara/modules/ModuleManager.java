package com.cornholio.sahara.modules;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.movement.JesusModule;
import com.cornholio.sahara.modules.packetevent.PacketListener;
import com.cornholio.sahara.modules.player.*;
import com.cornholio.sahara.modules.player.autoreconnect.AutoReconnectModule;
import com.cornholio.sahara.modules.player.clickgui.ClickGuiModule;
import com.cornholio.sahara.modules.player.clickgui.WindowManager;
import com.cornholio.sahara.modules.render.WallhackModule;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.netty.channel.ChannelPipeline;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;

import java.io.*;
import java.util.*;


public class ModuleManager
{
    public List<Module> modules;
    private boolean added = false;
    private Minecraft mc;
    private SaharaClient sh;

    public FreecamModule Freecam;
    public NoRotation noRotation;
    public ClickGuiModule ClickGUI;
    public BoatFly boatFly;
    public AutoMount autoMount;
    public AutoReconnectModule autoReconnect;
    public FastMineModule fastMineModule;
    public WallhackModule wallhackModule;
    public JesusModule jesusModule;
    public StrangeModule strangeModule;
    public CornholioModule cornholioModule;
    public BoatPlaceConst boatPlaceConst;
    public MountBypassModule mountBypassModule;

    public ModuleManager()
    {
        mc = Minecraft.getMinecraft();
        sh = SaharaClient.getSahara();
    }

    public ModuleManager Init()
    {
        modules = CreateAllModules();
        loadModulesFromFile();
        return this;
    }

    public void checkKeys(int key)
    {
        for(Module m : modules)
            if(m.getKey() == key)
                m.toggle();
    }

    public void saveModules()
    {
        String saharaFolder = SaharaClient.saharaFolder;
        File directory = new File(saharaFolder);
        if (!directory.exists()){
            directory.mkdir();
        }

        for(Module module : modules)
        {
            if(module.dontLoadOrSave) continue;

            String currentFile = saharaFolder+module.getName().toLowerCase().replace(" ", "_")+".json";
            try {
                FileWriter file = new FileWriter(currentFile);
                file.write(module.generateJSON().toString());
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadModulesFromFile()
    {
        String saharaFolder = SaharaClient.saharaFolder;
        for(Module module : modules)
        {
            if(module.dontLoadOrSave) continue;

            String currentFile = saharaFolder+module.getName().toLowerCase().replace(" ", "_")+".json";
            try {
                module.loadFromJSON((new JsonParser()).parse(new FileReader(currentFile)).getAsJsonObject());
                module.refreshEverything();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Module> CreateAllModules()
    {
        List<Module> output = new ArrayList<Module>(Arrays.asList(
                Freecam = new FreecamModule(),
                noRotation = new NoRotation(),
                ClickGUI = new ClickGuiModule(),
                boatFly = new BoatFly(),
                autoMount = new AutoMount(),
                autoReconnect = new AutoReconnectModule(),
                fastMineModule = new FastMineModule(),
                wallhackModule = new WallhackModule(),
                jesusModule = new JesusModule(),
                strangeModule = new StrangeModule(),
                cornholioModule = new CornholioModule(),
                boatPlaceConst = new BoatPlaceConst(),
                mountBypassModule = new MountBypassModule()
        ));
        return Collections.unmodifiableList(output);
    }
}

