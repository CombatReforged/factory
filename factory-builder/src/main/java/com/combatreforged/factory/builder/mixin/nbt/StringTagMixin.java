package com.combatreforged.factory.builder.mixin.nbt;

import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTValue;
import net.minecraft.nbt.StringTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StringTag.class)
public abstract class StringTagMixin implements Wrap<NBTValue> {
    private WrappedNBTValue wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrapped(String string, CallbackInfo ci) {
        this.wrapped = new WrappedNBTValue((StringTag) (Object) this);
    }

    @Override
    public NBTValue wrap() {
        return wrapped;
    }
}
