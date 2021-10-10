package com.cornholio.sahara.modules.render;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static org.lwjgl.opengl.GL11.*;

public class WallhackModule extends Module
{
    ModuleSetting lineWidth;
    ModuleSetting ignoreDepth;
    BufferBuilder buffer = Tessellator.getInstance().getBuffer();

    float partialTicks;
    public WallhackModule()
    {
        super("Wall Hack", "Renders entites through walls", ModuleCategory.Render);
        registerSetting(lineWidth = new ModuleSetting("Line Width", 0, 10, 1));
        registerSetting(ignoreDepth = new ModuleSetting("Ignore Depth", false));
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent e) {
        partialTicks = mc.getRenderPartialTicks();
        renderBoxes();
        //e.setCanceled(true);
    }


    public void renderBoxes()
    {
        if(mc.world == null || mc.getRenderViewEntity() == null || !isActive()) return;
        for(Entity entity : mc.world.getLoadedEntityList()) {
            if(entity == mc.getRenderViewEntity()) continue;

            double d0 = (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            double d1 = (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double d2 = (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            drawAABB(entity.getRenderBoundingBox().offset(d0,d1,d2));
        }
    }

    private void drawAABB(AxisAlignedBB box)
    {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        GlStateManager.glLineWidth(lineWidth.getFloat());
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        this.mc.entityRenderer.disableLightmap();
        glColor4f(255,255,255, 255);
        if(ignoreDepth.getBoolean()) GlStateManager.disableDepth();

        double x = mc.getRenderViewEntity().prevPosX + (mc.getRenderViewEntity().posX - mc.getRenderViewEntity().prevPosX) * partialTicks;;
        double y = mc.getRenderViewEntity().prevPosY + (mc.getRenderViewEntity().posY - mc.getRenderViewEntity().prevPosY) * partialTicks;
        double z = mc.getRenderViewEntity().prevPosZ + (mc.getRenderViewEntity().posZ - mc.getRenderViewEntity().prevPosZ) * partialTicks;

        AxisAlignedBB renderBox = box.offset(-x, -y, -z);
        buffer.begin(GL_LINES, DefaultVertexFormats.POSITION);


        buffer.pos(renderBox.minX, renderBox.minY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.maxY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.minY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.minY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.minY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.maxY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.maxY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.maxY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.maxY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.maxY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.minY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.minY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.minY, renderBox.minZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.minY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.maxX, renderBox.minY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.minY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.minY, renderBox.maxZ).endVertex();
        buffer.pos(renderBox.minX, renderBox.minY, renderBox.minZ).endVertex();
        Tessellator.getInstance().draw();
        this.mc.entityRenderer.enableLightmap();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        if(ignoreDepth.getBoolean()) GlStateManager.enableDepth();
    }

}
