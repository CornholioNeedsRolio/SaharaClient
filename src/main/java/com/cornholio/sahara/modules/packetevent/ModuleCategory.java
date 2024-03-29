package com.cornholio.sahara.modules.packetevent;

public enum ModuleCategory
{
    Player("Player"),
    Render("Render"),
    Movement("Movement"),
    Combat("Combat"),
    Farming("Farming"),
    Visuals("Visuals");


    private final String name;
    ModuleCategory(String ign)
    {
        this.name = ign;
    }
    public String getName()
    {
        return name;
    }
}