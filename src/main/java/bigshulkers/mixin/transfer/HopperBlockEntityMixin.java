package bigshulkers.mixin.transfer;


import bigshulkers.BigShulkerServer;
import bigshulkers.BigShulkerSettings;
import bigshulkers.ItemOverStackable;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @ModifyExpressionValue(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private static int bigShulkers$modifyStackMaxSizeChecks$transfer(int original, @Local(argsOnly = true, ordinal = 1) Inventory inventory) {
        if (inventory instanceof ShulkerBoxBlockEntity) {
            return BigShulkerServer.bigShulkers$modifyStackMaxSizeChecks$helper(original);
        }
        return original;
    }

    @ModifyExpressionValue(method = "method_17769", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private static int bigShulkers$modifyStackMaxSizeChecks$lamda(int original, @Local(argsOnly = true) Inventory inventory) {
        if (inventory instanceof ShulkerBoxBlockEntity) {
            return BigShulkerServer.bigShulkers$modifyStackMaxSizeChecks$helper(original);
        }
        return original;
    }




    @ModifyExpressionValue(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at= @At(value = "INVOKE",target = "Lnet/minecraft/block/entity/HopperBlockEntity;canMergeItems(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private static boolean bigShulkers$modifyStackMaxSizeChecks$canMergeItems(boolean original, @Local(ordinal = 0, print = true) ItemStack first, @Local(ordinal = 0, print = true) ItemStack second, @Local(ordinal = 0, print = true) Inventory inventory) {
        return original ||
                (inventory instanceof ShulkerBoxBlockEntity &&
                        (first.getCount() <= BigShulkerSettings.shulkerBoxSlotMaxStackSize &&
                                ItemStack.canCombine(first, second)));
    }


/*    @ModifyExpressionValue(method = "canMergeItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private static int bigShulkers$modifyStackMaxSizeChecks$canMergeItems$2(int original) {
        if (inventory instanceof ShulkerBoxBlockEntity) {
            return BigShulkerServer.bigShulkers$modifyStackMaxSizeChecks$helper(original);
        }
        return original;
    }*/


}
