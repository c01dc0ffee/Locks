package melonslise.locks.mixin;

import com.google.gson.JsonElement;
import melonslise.locks.common.util.LocksUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.LootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LootTables.class)
public class LootTableManagerMixin {
	@Inject(at = @At("HEAD"), method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V")
	private void apply(Map<ResourceLocation, JsonElement> map, ResourceManager mgr, ProfilerFiller p, CallbackInfo ci) {
		LocksUtil.resourceManager = mgr;
	}
}