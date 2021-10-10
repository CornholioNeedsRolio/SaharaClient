package com.cornholio.sahara.modules;


import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.player.clickgui.IRefreshable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scala.util.parsing.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Module
{
    protected Minecraft mc;

    private boolean toggled = false;
    private String Name;
    private String Description;
    private int key;
    private ModuleCategory category;
    private List<ModuleSetting> settings;
    private List<IRefreshable> refreshableList;

    public boolean dontLoadOrSave = false;

    public Module(String name, String description, ModuleCategory category)
    {
        this.Name = name;
        this.Description = description;
        this.key = key;
        this.category = category;
        this.settings = new ArrayList<ModuleSetting>();
        refreshableList = new ArrayList<>();
        mc = Minecraft.getMinecraft();

    }


    public String getName() { return Name; }

    public ModuleCategory getCategory() { return category; }

    public void registerSetting(ModuleSetting setting) { settings.add(setting); setting.setParent(this); }
    public void addRefreshable(IRefreshable refreshable) { refreshableList.add(refreshable); }
    public void refreshEverything() { for(IRefreshable refreshable : refreshableList) refreshable.refreshWindow(); }

    public List<ModuleSetting> getSettings() { return settings; }

    public boolean onTurn(Entity entity, float yaw, float pitch, CallbackInfo ci){ return false;};

    public void setKey(int key) { this.key = key; };

    public void onEnable()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable()
    {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public boolean isActive()
    {
        return toggled;
    }

    public void onToggle(boolean toggled)
    {
        this.toggled = toggled;
        if(this.toggled)
            onEnable();
        else
            onDisable();
    }

    public int getKey()
    {
        return key;
    }

    public void toggle() { onToggle(!toggled); }

    public JsonObject generateJSON()
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("isActive", isActive());
        obj.addProperty("key", key);
        for(ModuleSetting setting : settings)
            setting.addSelfToJSON(obj);

        return obj;
    }

    public void loadFromJSON(JsonObject json)
    {
        JsonElement activeJson = json.get("isActive");
        JsonElement keyJson = json.get("key");
        if(activeJson != null && keyJson != null) {
            onToggle(activeJson.getAsBoolean());
            key = keyJson.getAsInt();
        }
        for(ModuleSetting setting : settings)
            setting.loadSelfFromJSON(json);
    }
}
