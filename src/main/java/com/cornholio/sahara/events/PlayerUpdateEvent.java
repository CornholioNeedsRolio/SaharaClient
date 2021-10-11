package com.cornholio.sahara.events;


import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerUpdateEvent extends Event
{
    boolean pre;
    public PlayerUpdateEvent(boolean pre)
    {
        super();
        this.pre = pre;
    }

    public boolean isPre()
    {
        return pre;
    }
}
