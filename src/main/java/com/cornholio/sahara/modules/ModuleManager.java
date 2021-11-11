package com.cornholio.sahara.modules;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.combat.CriticalsModule;
import com.cornholio.sahara.modules.combat.KillAuraModule;
import com.cornholio.sahara.modules.combat.OneShotBowModule;
import com.cornholio.sahara.modules.farming.AutoShear;
import com.cornholio.sahara.modules.movement.JesusModule;
import com.cornholio.sahara.modules.movement.VelocityModule;
import com.cornholio.sahara.modules.packetevent.PacketListener;
import com.cornholio.sahara.modules.player.*;
import com.cornholio.sahara.modules.player.autoreconnect.AutoReconnectModule;
import com.cornholio.sahara.modules.player.clickgui.ClickGuiModule;
import com.cornholio.sahara.modules.player.clickgui.WindowManager;
import com.cornholio.sahara.modules.render.ViewClipModule;
import com.cornholio.sahara.modules.render.WallhackModule;
import com.cornholio.sahara.modules.visuals.ColorModule;
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
    public ViewClipModule viewClipModule;
    public OneShotBowModule oneShotBowModule;
    public AutoShear autoShear;
    public AntiHungerModule antiHungerModule;
    public VelocityModule velocityModule;
    public PacketNukerModule packetNukerModule;
    public KillAuraModule killAuraModule;
    public CriticalsModule criticalsModule;
    public AutoFishModule autoFishModule;

    public ColorModule listTitleColorB;
    public ColorModule listTitleColorF;
    public ColorModule listColorB;
    public ColorModule barColorB;
    public ColorModule moduleTextF;
    public ColorModule moduleOptionTextF;

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
                listTitleColorB = new ColorModule("Title Background", 255, 255, 255, 50),
                listTitleColorF = new ColorModule("Title Foreground", 255, 255, 255, 255),
                listColorB = new ColorModule("List Background", 125, 125, 125, 50),
                barColorB = new ColorModule("Bar Background", 255, 255, 255, 255),
                moduleTextF = new ColorModule("Module Text Foreground", 0, 0, 0, 255),
                moduleOptionTextF = new ColorModule("Module Option Text Foreground", 0, 0, 0, 255),

                Freecam = new FreecamModule(),
                noRotation = new NoRotation(),
                antiHungerModule = new AntiHungerModule(),
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
                mountBypassModule = new MountBypassModule(),
                viewClipModule = new ViewClipModule(),
                oneShotBowModule = new OneShotBowModule(),
                autoShear = new AutoShear(),
                velocityModule = new VelocityModule(),
                packetNukerModule = new PacketNukerModule(),
                killAuraModule = new KillAuraModule(),
                criticalsModule = new CriticalsModule(),
                autoFishModule = new AutoFishModule()

        ));
        return Collections.unmodifiableList(output);
    }
}

