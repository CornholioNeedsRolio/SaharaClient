package com.cornholio.sahara.modules;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ModuleSetting {
    private SettingType type;
    private boolean bVal;
    private float fValue;
    private String Name;
    private Module module;

    private float fMin = 0;
    private float fMax = 0;

    public ModuleSetting(String name, boolean val) {
        this.Name = name;
        this.bVal = val;
        this.type = SettingType.BOOL;
    }

    public ModuleSetting(String name, float min, float max, float val)
    {
        this.Name = name;
        this.fMin = min;
        this.fMax = max;
        this.fValue = val;
        this.type = SettingType.SLIDER;
    }
    public Module getParent() { return module; }
    public void setParent(Module parent) { module = parent; }
    public boolean getBoolean() { return bVal; }
    public float getFloat()
    {
        return fValue;
    }
    public float getMax() { return fMax; }
    public float getMin() { return fMin; }
    public void setFloat(float value) { fValue = value; }
    public void setBool(boolean value) { bVal = value; }
    public String getName() { return Name; }
    public SettingType getType(){ return type; }


    public void addSelfToJSON(JsonObject jsonObject)
    {
        if(type == ModuleSetting.SettingType.BOOL)
            jsonObject.addProperty(Name, bVal);
        else if (type == ModuleSetting.SettingType.SLIDER)
            jsonObject.addProperty(Name, fValue);
    }

    public void loadSelfFromJSON(JsonObject jsonObject)
    {
        JsonElement element = jsonObject.get(Name);
        if(element == null) return;
        try {
            if (type == ModuleSetting.SettingType.BOOL)
                bVal = element.getAsBoolean();
            else if (type == ModuleSetting.SettingType.SLIDER)
                fValue = element.getAsFloat();
        }catch (Exception e) { e.printStackTrace(); };
    }

    public enum SettingType
    {
        BOOL,
        SLIDER
    };
};

