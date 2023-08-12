package bigshulkers.mixin.sync;


import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Stolen from BiggerStacks, fixed for yarn and cleaned up usage of mixin.
 * <p>
 *
 * This modifies the way items counts are stored in a byte buffer. Vanilla stores item counts as a byte, which only allows values
 * up to 127. These mixins change it to be an int, which goes up to 2 billion (more than enough).
 * In code, the item count is treated as an int even though it is only supposed to go up to 64, so this will work fine.
 */
@Mixin(PacketByteBuf.class)
public class PacketByteBufMixin
{
    /**
     * This writes the item count as an int instead of a byte.
     */
    @Redirect(method = "writeItemStack",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/PacketByteBuf;writeByte(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf writeBiggerStackCount(PacketByteBuf instance, int count) {
        return instance.writeInt(count);
    }


    @ModifyArg(method = "readItemStack", at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;I)V"),index = 1)
    private int readStackItemCount(int value) {
        //actually read the item count here
        return ((PacketByteBuf) (Object) this).readInt();
    }
}