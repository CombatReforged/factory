package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.api.event.player.PlayerContainerClickEvent;
import com.combatreforged.factory.api.world.item.container.menu.Button;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenu;
import com.combatreforged.factory.api.world.item.container.menu.SlotClickType;
import com.combatreforged.factory.api.world.util.Pair;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import com.combatreforged.factory.builder.implementation.world.item.container.menu.WrappedContainerMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin implements Wrap<ContainerMenu> {
    @Shadow @Final public List<Slot> slots;

    @Shadow public abstract MenuType<?> getType();

    private WrappedContainerMenu wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrap(MenuType<?> menuType, int i, CallbackInfo ci) {
        this.wrapped = new WrappedContainerMenu((AbstractContainerMenu) (Object) this, ObjectMappings.getType((AbstractContainerMenu) (Object) this));
    }

    @Override
    public ContainerMenu wrap() {
        return wrapped;
    }

    @Inject(method = "doClick", at = @At(value = "JUMP", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
    public void injectPlayerContainerClickEvent(int i, int j, ClickType clickType, Player player, CallbackInfoReturnable<ItemStack> cir) {
        WrappedPlayer apiPlayer = Wrapped.wrap(player, WrappedPlayer.class);
        ContainerMenu menu = Wrapped.wrap(this, WrappedContainerMenu.class);
        Pair<SlotClickType, Button> typeAndButton = getAPITypes(i, j, clickType);
        SlotClickType slotClickType = typeAndButton.a();
        Button button = typeAndButton.b();

        WrappedItemStack cursorStack = player.inventory.getCarried() != null
                && !player.inventory.getCarried().isEmpty()
                ? Wrapped.wrap(player.inventory.getCarried(), WrappedItemStack.class)
                : null;
        WrappedItemStack targetStack = null;
        if (i >= 0 && i < slots.size() && slots.get(i).getItem() != null && !slots.get(i).getItem().isEmpty()) {
            targetStack = Wrapped.wrap(slots.get(i).getItem(), WrappedItemStack.class);
        }
        PlayerContainerClickEvent clickEvent = new PlayerContainerClickEvent(apiPlayer, menu, i, slotClickType, button, targetStack, cursorStack);
        PlayerContainerClickEvent.BACKEND.invoke(clickEvent);

        ServerPlayer serverPlayer = (ServerPlayer) player;
        if (clickEvent.isCancelled()) {
            serverPlayer.refreshContainer((AbstractContainerMenu) (Object) this);
            cir.setReturnValue(targetStack != null ? targetStack.unwrap() : ItemStack.EMPTY);
        }

        PlayerContainerClickEvent.BACKEND.invokeEndFunctions(clickEvent);
    }

    public Pair<SlotClickType, Button> getAPITypes(int slot, int button, ClickType type) {
        SlotClickType slotClickType;
        Button apiButton;
        switch (type) {
            case PICKUP:
            case SWAP:
            case CLONE:
                slotClickType = SlotClickType.CLICK;
                break;
            case QUICK_MOVE:
                slotClickType = SlotClickType.SHIFT_CLICK;
                break;
            case THROW:
                slotClickType = button == 0
                        ? SlotClickType.CLICK
                        : SlotClickType.SHIFT_CLICK;
                break;
            case QUICK_CRAFT:
                switch (button % 4) {
                    case 0:
                        slotClickType = SlotClickType.DRAG_START;
                        break;
                    case 1:
                        slotClickType = SlotClickType.DRAG_CONTINUE;
                        break;
                    case 2:
                        slotClickType = SlotClickType.DRAG_STOP;
                        break;
                    default:
                        slotClickType = SlotClickType.NONE;
                }
                break;
            case PICKUP_ALL:
                slotClickType = SlotClickType.DOUBLE_CLICK;
                break;
            default:
                slotClickType = SlotClickType.NONE;
        }

        switch (type) {
            case PICKUP:
            case QUICK_MOVE:
            case CLONE:
            case PICKUP_ALL:
                switch (button) {
                    case 0:
                        apiButton = Button.LEFT_CLICK;
                        break;
                    case 1:
                        apiButton = Button.RIGHT_CLICK;
                        break;
                    case 2:
                        apiButton = Button.MIDDLE_CLICK;
                        break;
                    default:
                        apiButton = Button.NONE;
                }
                break;
            case SWAP:
                apiButton = Button.hotkey(button);
                break;
            case THROW:
                apiButton = Button.DROP;
                break;
            case QUICK_CRAFT:
                switch (button / 4) {
                    case 0:
                        apiButton = Button.LEFT_CLICK;
                        break;
                    case 1:
                        apiButton = Button.RIGHT_CLICK;
                        break;
                    case 2:
                        apiButton = Button.MIDDLE_CLICK;
                        break;
                    default:
                        apiButton = Button.NONE;
                }
                break;
            default:
                apiButton = Button.NONE;
        }

        return new Pair<>(slotClickType, apiButton);
    }
}
