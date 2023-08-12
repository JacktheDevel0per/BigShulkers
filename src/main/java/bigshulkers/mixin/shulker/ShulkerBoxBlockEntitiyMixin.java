package bigshulkers.mixin.shulker;

import bigshulkers.BigShulkerSettings;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntitiyMixin implements SidedInventory {


	@Override
	public int getMaxCountPerStack() {
		return BigShulkerSettings.shulkerBoxSlotMaxStackSize;
	}
}