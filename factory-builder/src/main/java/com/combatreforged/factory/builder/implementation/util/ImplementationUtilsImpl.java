package com.combatreforged.factory.builder.implementation.util;

import com.combatreforged.factory.api.interfaces.Namespaced;
import com.combatreforged.factory.api.util.Identifier;
import com.combatreforged.factory.api.util.ImplementationUtils;
import com.combatreforged.factory.api.world.block.BlockType;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.entity.EntityType;
import com.combatreforged.factory.api.world.item.Enchantment;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenuType;
import com.combatreforged.factory.api.world.item.container.menu.MenuHolder;
import com.combatreforged.factory.builder.extension.world.effect.MobEffectExtension;
import com.combatreforged.factory.builder.extension.world.inventory.MenuTypeExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import com.combatreforged.factory.builder.implementation.world.item.container.menu.WrappedMenuHolder;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import static com.combatreforged.factory.builder.implementation.util.ObjectMappings.MENU_TYPES;
import static com.combatreforged.factory.builder.implementation.util.ObjectMappings.convertComponent;

public class ImplementationUtilsImpl implements ImplementationUtils {
    @Override
    public int getMaxLevel(Enchantment enchantment) {
        return ObjectMappings.ENCHANTMENTS.get(enchantment).getMaxLevel();
    }

    @Override
    public boolean isCurse(Enchantment enchantment) {
        return ObjectMappings.ENCHANTMENTS.get(enchantment).isCurse();
    }

    @Override
    public boolean canApply(Enchantment enchantment, ItemStack itemStack) {
        net.minecraft.world.item.ItemStack mcStack = ((WrappedItemStack) itemStack).unwrap();
        net.minecraft.world.item.enchantment.Enchantment mcEnchant = ObjectMappings.ENCHANTMENTS.get(enchantment);
        return mcEnchant.canEnchant(mcStack, true);
    }

    @Override
    public StatusEffect.Type getType(StatusEffect effect) {
        switch(((MobEffectExtension) ObjectMappings.EFFECTS.get(effect)).getCategory()) {
            case HARMFUL:
                return StatusEffect.Type.HARMFUL;
            case BENEFICIAL:
                return StatusEffect.Type.BENEFICIAL;
            default:
                return StatusEffect.Type.NEUTRAL;
        }
    }

    @Override
    public boolean isBlockItem(ItemType item) {
        return ObjectMappings.ITEMS.get(item) instanceof BlockItem;
    }

    @Override
    public BlockType getBlock(ItemType item) {
        Item mcItem = ObjectMappings.ITEMS.get(item);
        return mcItem instanceof BlockItem ? ObjectMappings.BLOCKS.inverse().get(((BlockItem) mcItem).getBlock()) : null;
    }

    @Override
    public int getMaxStackSize(ItemType item) {
        return ObjectMappings.ITEMS.get(item).getMaxStackSize();
    }

    @Override
    public int getMaxDamage(ItemType item) {
        return ObjectMappings.ITEMS.get(item).getMaxDamage();
    }

    @Override
    public Identifier getIdentifier(Namespaced namespaced) {
        ResourceLocation loc = null;
        if (namespaced instanceof EntityType) {
            loc = Registry.ENTITY_TYPE.getKey(ObjectMappings.ENTITIES.get((EntityType) namespaced));
        }
        else if (namespaced instanceof StatusEffect) {
            loc = Registry.MOB_EFFECT.getKey(ObjectMappings.EFFECTS.get((StatusEffect) namespaced));
        }
        else if (namespaced instanceof Enchantment) {
            loc = Registry.ENCHANTMENT.getKey(ObjectMappings.ENCHANTMENTS.get((Enchantment) namespaced));
        }
        else if (namespaced instanceof ItemType) {
            loc = Registry.ITEM.getKey(ObjectMappings.ITEMS.get((ItemType) namespaced));
        }
        else if (namespaced instanceof BlockType) {
            loc = Registry.BLOCK.getKey(ObjectMappings.BLOCKS.get((BlockType) namespaced));
        }
        
        if (loc != null) {
            return new Identifier(loc.getNamespace(), loc.getPath());
        }
        
        return null;
    }

    @Override
    public MenuHolder createMenu(ContainerMenuType type, Component title) {
       return Wrapped.wrap(new SimpleMenuProvider((i, inventory, player) ->
               ((MenuTypeExtension) MENU_TYPES.get(type)).createServer(i, inventory),
               convertComponent(title)), WrappedMenuHolder.class);
    }
}
