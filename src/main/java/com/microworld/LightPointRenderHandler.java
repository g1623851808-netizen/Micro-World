package com.microworld;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "microworld", value = Dist.CLIENT)
public class LightPointRenderHandler {

    // 颜色数组：白、绿、橙、黄、蓝、红、紫、亮白、暗灰、棕、青、品红
    private static final int[] COLORS = {
            0xFFFFFF, 0x00FF00, 0xFF8800, 0xFFFF00, 0x0088FF,
            0xFF0000, 0xAA00FF, 0xFFFFFF, 0x444444, 0x8B4513,
            0x00FFFF, 0xFF00FF
    };

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.world == null) return;
        if (mc.player.world.getDimension().getType() != ModDimensions.COSMOS_TYPE) return;

        List<LightPointGenerator.LightPointData> snapshot = new ArrayList<>(LightPointGenerator.LIGHT_POINTS);
        for (LightPointGenerator.LightPointData data : snapshot) {            BlockPos pos = data.pos;
            int type = data.type;
            double dx = pos.getX() + 0.5 - mc.player.posX;
            double dy = pos.getY() + 0.5 - mc.player.posY;
            double dz = pos.getZ() + 0.5 - mc.player.posZ;
            double distSq = dx * dx + dy * dy + dz * dz;
            if (distSq > 256 * 256) continue;

            int color = COLORS[type % COLORS.length];
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;

            GlStateManager.pushMatrix();
            GlStateManager.translated(dx, dy, dz);
            GlStateManager.rotatef(-mc.player.rotationYaw, 0, 1, 0);
            GlStateManager.rotatef(mc.player.rotationPitch, 1, 0, 0);
            GlStateManager.disableCull();
            GlStateManager.disableTexture();
            GlStateManager.disableLighting();
            GlStateManager.disableDepthTest();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            float radius = 2.0f;
            int segments = 16;

            buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0, 0, 0).color(r, g, b, 1.0f).endVertex();
            for (int i = 0; i <= segments; i++) {
                double angle = 2 * Math.PI * i / segments;
                float xPos = (float) (Math.cos(angle) * radius);
                float yPos = (float) (Math.sin(angle) * radius);
                buffer.pos(xPos, yPos, 0).color(r, g, b, 0.6f).endVertex(); // 边缘半透明
            }
            tessellator.draw();
            // 外层光晕
            float outerRadius = radius * 2.5f;
            buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0, 0, 0).color(r, g, b, 0.3f).endVertex();
            for (int i = 0; i <= segments; i++) {
                double angle = 2 * Math.PI * i / segments;
                float xPos = (float) (Math.cos(angle) * outerRadius);
                float yPos = (float) (Math.sin(angle) * outerRadius);
                buffer.pos(xPos, yPos, 0).color(r, g, b, 0.0f).endVertex();
            }
            tessellator.draw();

            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.enableDepthTest();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture();
            GlStateManager.popMatrix();
        }
    }
}