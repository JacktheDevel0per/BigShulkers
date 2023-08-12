package bigshulkers;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpet.utils.Translations;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class BigShulkerServer implements CarpetExtension {



    public static void noop() {}



    public static final String MODID = "big_shulkers";
    public static String MOD_VERSION = FabricLoader.getInstance().getModContainer(MODID).isPresent() ? FabricLoader.getInstance().getModContainer(MODID).get().getMetadata().getVersion().getFriendlyString() : "UNKNOWN";

    public static final Logger LOGGER = LogManager.getLogger("Big Shulkers");
    public static final SettingsManager settingsManager = new SettingsManager(MOD_VERSION, "big_shulkers", "Big Shulkers");


    @Override
    public void onGameStarted() {
        settingsManager.parseSettingsClass(BigShulkerSettings.class);
    }

    @Override
    public SettingsManager extensionSettingsManager() {
        return settingsManager;
    }

    static {
        CarpetServer.manageExtension(new BigShulkerServer());
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return Translations.getTranslationFromResourcePath("assets/bigshulkers/lang/%s.json".formatted(lang));
    }




    public static int bigShulkers$modifyStackMaxSizeChecks$helper(int original) {
        if (BigShulkerSettings.itemOverstacking) {
            return (original/64) * BigShulkerSettings.shulkerBoxSlotMaxStackSize;
        }


        return original;
    }

}
