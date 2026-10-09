package org.dynmap.fabric_26_3.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.dynmap.fabric_26_3.access.BiomeEffectsExt;
import java.util.Optional;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

@Mixin(BiomeSpecialEffects.class)
public class BiomeEffectsMixin implements BiomeEffectsExt {

    @Shadow private int waterColor;
    @Shadow private Optional<Integer> foliageColorOverride;
    @Shadow private Optional<Integer> dryFoliageColorOverride;
    @Shadow private Optional<Integer> grassColorOverride;
    @Shadow private BiomeSpecialEffects.GrassColorModifier grassColorModifier;

    private int dynmap$waterColor;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(int waterColor,
                        Optional<Integer> foliageColor,
                        Optional<Integer> dryFoliageColor,
                        Optional<Integer> grassColor,
                        BiomeSpecialEffects.GrassColorModifier grassColorModifier,
                        CallbackInfo ci)
    {
        this.dynmap$waterColor = waterColor;
    }

    @Override
    public int dynmap$getWaterColor() {
        return dynmap$waterColor;
    }

    @Override
    public Optional<Integer> dynmap$getFoliageColor() { return foliageColorOverride; }

    @Override
    public Optional<Integer> dynmap$getDryFoliageColor() { return dryFoliageColorOverride; }

    @Override
    public Optional<Integer> dynmap$getGrassColor() { return grassColorOverride; }

    @Override
    public BiomeSpecialEffects.GrassColorModifier dynmap$getGrassColorModifier() {
        return grassColorModifier;
    }
}
