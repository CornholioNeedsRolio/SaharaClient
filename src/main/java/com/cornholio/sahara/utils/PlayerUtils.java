package com.cornholio.sahara.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class PlayerUtils {
    public static float[] getLookAt(Vec3d place, Vec3d offset)
    {
        Minecraft mc = Minecraft.getMinecraft();
        double diffX = place.x+offset.x - mc.player.posX;
        double diffY = place.y+offset.y - mc.player.posY - 1.2;
        double diffZ = place.z+offset.z - mc.player.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180D / Math.PI) - 90F;
        float pitch = (float)-(Math.atan2(diffY, dist) * 180D / Math.PI);
        //mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, true));
        return new float[]{yaw, pitch};

    }
}
