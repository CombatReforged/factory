package com.combatreforged.factory.builder.mixin.nbt;

import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTList;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ListTag.class)
public abstract class ListTagMixin implements Wrap<NBTList> {
    private NBTList wrapped;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedNBTList((ListTag) (Object) this);
    }

    @Override
    public NBTList wrap() {
        return wrapped;
    }
}
