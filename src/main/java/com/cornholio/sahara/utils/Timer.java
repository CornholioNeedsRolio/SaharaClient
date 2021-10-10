package com.cornholio.sahara.utils;

public class Timer
{
    private long time;

    public Timer()
    {
        time = -69;
    }

    public void reset()
    {
        this.time = System.currentTimeMillis();
    }
    public boolean passed(double ms)
    {
        return System.currentTimeMillis() - this.time >= ms;
    }
    public long getTime() { return time; }
}
