package bigshulkers;

import carpet.api.settings.Rule;
import carpet.api.settings.RuleCategory;

public class BigShulkerSettings {



    @Rule(categories = RuleCategory.EXPERIMENTAL)
    public static int shulkerBoxSlotMaxStackSize = 64;


    @Rule(categories = RuleCategory.EXPERIMENTAL)
    public static boolean itemOverstacking = false;

}
