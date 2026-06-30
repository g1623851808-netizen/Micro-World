package com.microworld;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@Mod.EventBusSubscriber(modid = "microworld", value = Dist.CLIENT)
public class StarRenderer {

    private static final int STAR_COUNT = 600;
    private static final int NEBULA_COUNT = 20;
    private static final long STAR_SEED = 123456789L;
    private static final long NEBULA_SEED = 987654321L;

    private static float rotationAngle = 0f;

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.world == null) return;
        if (mc.player.world.getDimension().getType() != ModDimensions.COSMOS_TYPE) return;

        float partialTicks = event.getPartialTicks();
        double viewX = mc.player.prevPosX + (mc.player.posX - mc.player.prevPosX) * partialTicks;
        double viewY = mc.player.prevPosY + (mc.player.posY - mc.player.prevPosY) * partialTicks;
        double viewZ = mc.player.prevPosZ + (mc.player.posZ - mc.player.prevPosZ) * partialTicks;

        rotationAngle += 0.01f * partialTicks;

        GlStateManager.disableDepthTest();
        GlStateManager.disableTexture();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        // 星云（始终面朝玩家）
        drawNebulae(mc, viewX, viewY, viewZ);

        // 背景星星（使用点精灵，全方向可见）
        drawStaticStars(viewX, viewY, viewZ);
        drawRotatingStars(viewX, viewY, viewZ);

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture();
        GlStateManager.enableDepthTest();
    }

    private static void drawNebulae(Minecraft mc, double viewX, double viewY, double viewZ) {
        Random random = new Random(NEBULA_SEED);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        for (int i = 0; i < NEBULA_COUNT; i++) {
            double theta = random.nextDouble() * Math.PI * 2;
            double phi = Math.acos(2.0 * random.nextDouble() - 1.0);
            double radius = 500 + random.nextDouble() * 300;

            double dx = Math.sin(phi) * Math.cos(theta) * radius;
            double dy = Math.sin(phi) * Math.sin(theta) * radius;
            double dz = Math.cos(phi) * radius;

            float r = random.nextFloat() * 0.2f;
            float g = random.nextFloat() * 0.1f;
            float b = 0.3f + random.nextFloat() * 0.4f;
            float alpha = 0.06f + random.nextFloat() * 0.08f;
            float size = 25 + random.nextFloat() * 30;

            GlStateManager.pushMatrix();
            GlStateManager.translated(dx, dy, dz);
            // 面朝玩家
            GlStateManager.rotatef(-mc.player.rotationYaw, 0, 1, 0);
            GlStateManager.rotatef(mc.player.rotationPitch, 1, 0, 0);

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(-size, size, 0).color(r, g, b, alpha).endVertex();
            buffer.pos(-size, -size, 0).color(r, g, b, alpha).endVertex();
            buffer.pos(size, -size, 0).color(r, g, b, alpha).endVertex();
            buffer.pos(size, size, 0).color(r, g, b, alpha).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();
        }
    }

    private static void drawStaticStars(double viewX, double viewY, double viewZ) {
        Random random = new Random(STAR_SEED);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GL11.glPointSize(4.0f); // 点的大小
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i < STAR_COUNT; i++) {
            double theta = random.nextDouble() * Math.PI * 2;
            double phi = Math.acos(2.0 * random.nextDouble() - 1.0);
            double radius = 400 + random.nextDouble() * 600;

            double dx = Math.sin(phi) * Math.cos(theta) * radius;
            double dy = Math.sin(phi) * Math.sin(theta) * radius;
            double dz = Math.cos(phi) * radius;

            float brightness = 0.5f + random.nextFloat() * 0.5f;
            float r = brightness * (0.9f + random.nextFloat() * 0.1f);
            float g = brightness * (0.9f + random.nextFloat() * 0.1f);
            float bColor = brightness;

            buffer.pos(dx, dy, dz).color(r, g, bColor, brightness).endVertex();
        }

        tessellator.draw();
        GL11.glPointSize(1.0f);
    }

    private static void drawRotatingStars(double viewX, double viewY, double viewZ) {
        Random random = new Random(STAR_SEED + 999);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GL11.glPointSize(3.0f);
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i < 150; i++) {
            double theta = random.nextDouble() * Math.PI * 2 + rotationAngle * 0.05;
            double phi = Math.acos(2.0 * random.nextDouble() - 1.0);
            double radius = 150 + random.nextDouble() * 350;

            double dx = Math.sin(phi) * Math.cos(theta) * radius;
            double dy = Math.sin(phi) * Math.sin(theta) * radius;
            double dz = Math.cos(phi) * radius;

            float brightness = 0.6f + random.nextFloat() * 0.4f;
            buffer.pos(dx, dy, dz).color(brightness, brightness, brightness, 0.8f).endVertex();
        }

        tessellator.draw();
        GL11.glPointSize(1.0f);
    }
}