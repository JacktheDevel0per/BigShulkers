package bigshulkers.mixin.client.render;

import bigshulkers.BigShulkerSettings;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;


/**
 * Stolen from BiggerStacks, fixed for yarn and DrawContext
 */


@Mixin(DrawContext.class)
public class DrawContextMixin
{
    @Shadow @Final private MatrixStack matrices;
    @Unique
    private static final DecimalFormat BILLION_FORMAT  = new DecimalFormat("#.##B");
    @Unique
    private static final DecimalFormat MILLION_FORMAT  = new DecimalFormat("#.##M");
    @Unique
    private static final DecimalFormat THOUSAND_FORMAT = new DecimalFormat("#.##K");


    @Unique
    private static final double ONE_THOUSAND = 1_000;

    @Unique
    private static final double ONE_MILLION = ONE_THOUSAND*ONE_THOUSAND;

    @Unique
    private static final double ONE_BILLION = ONE_MILLION*ONE_THOUSAND;

    @Redirect(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(I)Ljava/lang/String;"))
    private String getStringForBigStackCount(int count)
    {
        if (BigShulkerSettings.itemOverstacking)
        {
            var decimal = new BigDecimal(count).round(new MathContext(3)); //pinnacle of over engineering

            var value = decimal.doubleValue();

            if (value >= ONE_BILLION)
                return BILLION_FORMAT.format(value / ONE_BILLION);
            else if (value >= ONE_MILLION)
                return MILLION_FORMAT.format(value / ONE_MILLION);
            else if (value > ONE_THOUSAND)
                return THOUSAND_FORMAT.format(value / ONE_THOUSAND);
        }

        return String.valueOf(count);
    }

    //scale down fonts to fit
    @Surrogate
    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V",
                    shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void pushStack(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci, String countString)
    {
        matrices.push();
        var scale = (float) calculateStringScale(textRenderer, countString);

        matrices.scale(scale, scale, 1);
    }

    //move the text to the correct place
    @Surrogate
    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void translateStackBack(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci, String countString)
    {
        var width        = textRenderer.getWidth(countString);
        var scale        = calculateStringScale(textRenderer, countString);
        var extraXOffset = scale == 1 ? 0 : 1 / (scale * 2);
        var extraYOffset = scale == 1 ? 0 : 1.5 / (scale);

        //translate back to 0,0 for easier accounting for scaling
        matrices.translate(-(x + 19 - 2 - width),
                -(y + 6 + 3),
                0
        );

        //i just messed around until i found something that felt right
        matrices.translate(
                (x + 19 - 2 - extraXOffset - width * scale) / scale,
                (y + 6 + 3) / scale - (9 - 9 / scale) - extraYOffset, //this is stupid
                0
        );
    }

    @Unique
    private double calculateStringScale(TextRenderer renderer, String countString)
    {
        var width = renderer.getWidth(countString);

        if (width < 16)
            return 1.0;
        else
            return 16.0 / width;
    }

    @Surrogate
    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I",
                    shift = At.Shift.AFTER))
    private void popStack(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci)
    {
        matrices.pop();
    }
}