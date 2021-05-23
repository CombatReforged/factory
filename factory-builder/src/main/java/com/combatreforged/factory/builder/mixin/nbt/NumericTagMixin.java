package com.combatreforged.factory.builder.mixin.nbt;

import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTValue;
import net.minecraft.nbt.NumericTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NumericTag.class)
public abstract class NumericTagMixin implements Wrap<NBTValue> {
    private WrappedNBTValue wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedNBTValue((NumericTag) (Object) this);
    }

    @Override
    public NBTValue wrap() {
        return wrapped;
    }
}
