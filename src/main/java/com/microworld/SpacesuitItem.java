package com.microworld;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SpacesuitItem extends ArmorItem {

    public SpacesuitItem(net.minecraft.item.IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        int fuel = ArmorFuelUtil.getFuel(stack);
        tooltip.add(new StringTextComponent("燃料: " + fuel + " / " + ArmorFuelUtil.MAX_FUEL));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        if (world.isRemote) return new ActionResult<>(ActionResultType.SUCCESS, heldStack);

        // 另一只手拿燃料 → 补充燃料
        Hand otherHand = (hand == Hand.MAIN_HAND) ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack otherStack = player.getHeldItem(otherHand);
        if (otherStack.getItem() == ModItems.ASTRO_FUEL) {
            int fuel = ArmorFuelUtil.getFuel(heldStack);
            if (fuel < ArmorFuelUtil.MAX_FUEL) {
                otherStack.shrink(1);
                ArmorFuelUtil.setFuel(heldStack, fuel + ArmorFuelUtil.REFILL_AMOUNT);
                return new ActionResult<>(ActionResultType.SUCCESS, heldStack);
            }
            return new ActionResult<>(ActionResultType.PASS, heldStack);
        }

        // 自动穿戴到原版装备栏
        EquipmentSlotType targetSlot = this.getEquipmentSlot();
        ItemStack currentArmor = player.getItemStackFromSlot(targetSlot);
        if (currentArmor.isEmpty()) {
            player.setItemStackToSlot(targetSlot, heldStack.copy());
            heldStack.setCount(0);
            return new ActionResult<>(ActionResultType.SUCCESS, heldStack);
        }

        player.sendMessage(new StringTextComponent("该部位已有装备"));
        return new ActionResult<>(ActionResultType.FAIL, heldStack);
    }
}