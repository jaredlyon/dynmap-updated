package org.dynmap.fabric_26_3.mixin;

import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.dynmap.fabric_26_3.access.ProtoChunkAccessor;
import org.dynmap.fabric_26_3.event.CustomServerChunkEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChunkStatusTasks.class, priority = 666 /* fire before Fabric API CHUNK_LOAD event */)
public abstract class ChunkGeneratingMixin {
    @Inject(
            /* Same place as fabric-lifecycle-events-v1 event CHUNK_LOAD (we will fire before it) */
            method = "lambda$full$0(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/chunk/status/WorldGenContext;Lnet/minecraft/server/level/GenerationChunkHolder;)Lnet/minecraft/world/level/chunk/ChunkAccess;",
            at = @At("TAIL")
    )
    private static void onChunkGenerate(ChunkAccess chunk, WorldGenContext chunkGenerationContext, GenerationChunkHolder chunkHolder, CallbackInfoReturnable<ChunkAccess> callbackInfoReturnable) {
        if (((ProtoChunkAccessor)chunk).getTouchedByWorldGen()) {
            CustomServerChunkEvents.CHUNK_GENERATE.invoker().onChunkGenerate(chunkGenerationContext.level(), callbackInfoReturnable.getReturnValue());
        }
    }
}
