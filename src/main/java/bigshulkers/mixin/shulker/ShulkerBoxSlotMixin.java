package bigshulkers.mixin.shulker;


import bigshulkers.BigShulkerSettings;
import bigshulkers.ItemOverStackable;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.ShulkerBoxSlot;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin extends Slot implements ItemOverStackable {


    public ShulkerBoxSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getMaxItemCount() {
        if (BigShulkerSettings.itemOverstacking) {
            return BigShulkerSettings.shulkerBoxSlotMaxStackSize;
        }

        return super.getMaxItemCount();
    }
}
