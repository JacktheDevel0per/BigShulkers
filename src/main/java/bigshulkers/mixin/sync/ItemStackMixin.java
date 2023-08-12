package bigshulkers.mixin.sync;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public class ItemStackMixin {

/*

    @Inject(method = "getMaxCount",
            at = @At("RETURN"),
            cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo) {

    }
*/

    /**
     * Saves the stack size as an int instead of a byte.
     * Attempts to maintain some vanilla compatibility
     */
    @Redirect(method = "writeNbt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putByte(Ljava/lang/String;B)V"))
    private void saveBigStack(NbtCompound tag, String key, byte p_128346_) {
        int count = ((ItemStack) (Object) this).getCount();

        tag.putByte("Count", (byte) Math.min(count, Byte.MAX_VALUE));

        if (count > Byte.MAX_VALUE)
            tag.putInt("BigCount", count);
    }


    /**
     * Reads the stack size as an int instead of a byte
     * Attempts to maintain some vanilla compatibility
     */
    @Redirect(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;count:I",
                    opcode = Opcodes.PUTFIELD))
    private void readBigStack(ItemStack instance, int value, NbtCompound tag) {

        if (tag.contains("BigCount")) {
            instance.setCount(tag.getInt("BigCount"));
        }
        else if (tag.getType("Count") == NbtCompound.INT_TYPE) {
            instance.setCount(tag.getInt("Count"));
        }
        else {
            instance.setCount(tag.getByte("Count"));
        }
    }
}
