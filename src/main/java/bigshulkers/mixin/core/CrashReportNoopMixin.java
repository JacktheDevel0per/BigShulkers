package bigshulkers.mixin.core;

import bigshulkers.BigShulkerServer;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public class CrashReportNoopMixin {

    @Inject(method = "initCrashReport", at=@At("HEAD"))
    private static void bigShulker$noop(CallbackInfo ci) {
        BigShulkerServer.noop();
    }


}
