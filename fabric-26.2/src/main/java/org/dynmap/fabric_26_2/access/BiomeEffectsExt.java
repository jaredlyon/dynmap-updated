package org.dynmap.fabric_26_2.access;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

public interface BiomeEffectsExt {
    int dynmap$getWaterColor();
    Optional<Integer> dynmap$getFoliageColor();
    Optional<Integer> dynmap$getDryFoliageColor();
    Optional<Integer> dynmap$getGrassColor();
    BiomeSpecialEffects.GrassColorModifier dynmap$getGrassColorModifier();
}