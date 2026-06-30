package com.microworld;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("microworld")
public class MicroWorldMod {

    public MicroWorldMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        System.out.println("[MicroWorld] Mod 加载成功！");
    }

    private void setup(final FMLCommonSetupEvent event) {
        BrewingRecipeRegistry.addRecipe(
                Ingredient.fromItems(Items.POTION),
                Ingredient.fromItems(ModItems.PROPELLANT),
                new ItemStack(ModItems.ASTRO_FUEL)
        );
        System.out.println("[MicroWorld] 初始化完成");
    }
}