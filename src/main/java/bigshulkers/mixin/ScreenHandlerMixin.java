package bigshulkers.mixin;

import bigshulkers.BigShulkerServer;
import bigshulkers.ItemOverStackable;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.ShulkerBoxSlot;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {


    @Shadow public abstract Slot getSlot(int index);

    @Shadow public abstract boolean isValid(int slot);

    @ModifyExpressionValue(method = "internalOnSlotClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private int bigShulkers$ScreenHandler$modifyStackMaxSizeChecks(int original, @Local(argsOnly = true, ordinal = 0) int slotIndex) {

        if (isValid(slotIndex) && slotIndex  != -999 && this.getSlot(slotIndex) instanceof ItemOverStackable) {

            return BigShulkerServer.bigShulkers$modifyStackMaxSizeChecks$helper(original);
        }
        return original;
    }





}
