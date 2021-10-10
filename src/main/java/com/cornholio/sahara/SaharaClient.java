package com.cornholio.sahara;

import com.cornholio.sahara.modules.ModuleManager;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import com.cornholio.sahara.modules.packetevent.PacketListener;
import com.cornholio.sahara.modules.player.clickgui.WindowManager;
import com.cornholio.sahara.utils.Timer;
import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.channel.ChannelPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

@Mod(modid = SaharaClient.MODID, name = SaharaClient.NAME, version = SaharaClient.VERSION)
public class SaharaClient
{
    public static final String MODID = "sahara";
    public static final String NAME = "Sahara Client";
    public static final String VERSION = "1.0";

    public String LastIp = "";
    public int LastPort = 0;
    public static String minecraftFolder = null;
    public static String saharaFolder = null;

    private Minecraft mc = Minecraft.getMinecraft();
    private static SaharaClient client = null;
    private static ModuleManager module_manager = null;
    private static WindowManager window_manager = null;

    private Timer saveTimer = new Timer();


    public void sendMessage(String message)
    {
        if (mc.ingameGUI == null || mc.player == null || mc.ingameGUI.getChatGUI() == null) return;
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.WHITE + "[" + ChatFormatting.RED +"Sahara" + ChatFormatting.WHITE + "] " + ChatFormatting.RESET + message));
    }
    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event)
    {
        if(saveTimer.passed(60*5*1000))//auto save each 5 minutes
        {
            module_manager.saveModules();
            saveTimer.reset();
        }
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event){

        if(event.getPacket() instanceof C00Handshake)
        {
            C00Handshake packet = (C00Handshake)event.getPacket();
            LastIp = ObfuscationReflectionHelper.getPrivateValue(C00Handshake.class, packet, "field_149598_b");
            LastPort = ObfuscationReflectionHelper.getPrivateValue(C00Handshake.class, packet, "field_149599_c");
            return;
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        saveTimer.reset();
        minecraftFolder = event.getModConfigurationDirectory().getParent();
        saharaFolder = minecraftFolder + "/saharaClient/";
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        (client = new SaharaClient()).init();
    }

    public SaharaClient init()
    {
        MinecraftForge.EVENT_BUS.register(this);
        mc = Minecraft.getMinecraft();
        window_manager = new WindowManager();
        module_manager = new ModuleManager().Init();
        window_manager.InitClickGUI(module_manager);
        Runtime.getRuntime().addShutdownHook(new Thread("Sahara Shutdown")
        {
            public void run() {
                module_manager.saveModules();
            }
        });
        return this;
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) {
        if(mc.world == null || mc.player == null) return;

        try {
            if (Keyboard.isCreated())
            {
                if (Keyboard.getEventKeyState())
                {
                    int keyCode = Keyboard.getEventKey();
                    if (keyCode <= 0) return;
                    module_manager.checkKeys(keyCode);
                }
            }
        } catch (Exception ignored) { }
    }

    public static SaharaClient getSahara() { return client; };
    public ModuleManager getModuleManager() { return module_manager; };
}
