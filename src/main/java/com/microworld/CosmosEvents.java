package com.microworld;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CosmosEvents {

    private static final UUID CHEST_ARMOR_UUID = UUID.fromString("12345678-1234-1234-1234-123456789abc");
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        PlayerEntity player = event.player;
        if (player.world.isRemote) return;

        boolean inCosmos = player.world.getDimension().getType() == ModDimensions.COSMOS_TYPE;

        if (inCosmos) {
            // 触发光点生成
            if (player.world instanceof ServerWorld) {
                LightPointGenerator.triggerGeneration((ServerWorld) player.world);
            }

            // 下降速度0.1
            if (!player.abilities.isFlying) {
                player.setMotion(player.getMotion().x, -0.1, player.getMotion().z);
                player.velocityChanged = true;
            }

            tickCounter++;
            if (tickCounter % 20 == 0) {
                onSecondInCosmos(player);
            }
            if (tickCounter >= 1200) {
                tickCounter = 0;
                consumeAllArmorFuel(player);
            }
        } else {
            if (tickCounter % 20 == 0) {
                removeCosmosEffects(player);
            }
        }
    }

    private static void onSecondInCosmos(PlayerEntity player) {
        ItemStack helmet = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
        boolean helmetOK = helmet.getItem() == ModArmor.SPACESUIT_HELMET && ArmorFuelUtil.getFuel(helmet) > 0;
        boolean nearGenerator = OxygenGeneratorCache.isNearGenerator(player.getPosition());

        if (!helmetOK && !nearGenerator) {
            player.attackEntityFrom(DamageSource.GENERIC, 1.0f);
        }

        ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
        boolean chestOK = chest.getItem() == ModArmor.SPACESUIT_CHEST && ArmorFuelUtil.getFuel(chest) > 0;
        applyChestArmor(player, chestOK);

        ItemStack legs = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
        boolean legsOK = legs.getItem() == ModArmor.SPACESUIT_LEGS && ArmorFuelUtil.getFuel(legs) > 0;
        if (legsOK) {
            player.addPotionEffect(new EffectInstance(Effects.SPEED, 40, 0, true, false));
        }

        ItemStack boots = player.getItemStackFromSlot(EquipmentSlotType.FEET);
        boolean bootsOK = boots.getItem() == ModArmor.SPACESUIT_BOOTS && ArmorFuelUtil.getFuel(boots) > 0;
        player.abilities.allowFlying = bootsOK;
        if (!bootsOK) {
            player.abilities.isFlying = false;
        }
        player.sendPlayerAbilities();
    }

    private static void consumeAllArmorFuel(PlayerEntity player) {
        EquipmentSlotType[] slots = {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
        for (EquipmentSlotType slot : slots) {
            ItemStack stack = player.getItemStackFromSlot(slot);
            if (isSpacesuit(stack)) {
                int fuel = ArmorFuelUtil.getFuel(stack);
                if (fuel > 0) {
                    ArmorFuelUtil.setFuel(stack, fuel - 1);
                    if (ArmorFuelUtil.getFuel(stack) <= 1) {
                        ArmorFuelUtil.tryRefill(player, stack);
                    }
                }
            }
        }
    }

    private static void applyChestArmor(PlayerEntity player, boolean active) {
        IAttributeInstance armorAttr = player.getAttribute(SharedMonsterAttributes.ARMOR);
        if (armorAttr == null) return;
        armorAttr.removeModifier(CHEST_ARMOR_UUID);
        if (active) {
            armorAttr.applyModifier(new AttributeModifier(CHEST_ARMOR_UUID, "Spacesuit chest", 10.0, AttributeModifier.Operation.ADDITION));
        }
    }

    private static void removeCosmosEffects(PlayerEntity player) {
        IAttributeInstance armorAttr = player.getAttribute(SharedMonsterAttributes.ARMOR);
        if (armorAttr != null) {
            armorAttr.removeModifier(CHEST_ARMOR_UUID);
        }
        if (!player.isCreative()) {
            player.abilities.allowFlying = false;
            player.abilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }

    private static boolean isSpacesuit(ItemStack stack) {
        return stack.getItem() == ModArmor.SPACESUIT_HELMET
                || stack.getItem() == ModArmor.SPACESUIT_CHEST
                || stack.getItem() == ModArmor.SPACESUIT_LEGS
                || stack.getItem() == ModArmor.SPACESUIT_BOOTS;
    }
}