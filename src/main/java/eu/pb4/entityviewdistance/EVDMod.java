package eu.pb4.entityviewdistance;

import eu.pb4.entityviewdistance.config.ConfigManager;
import eu.pb4.entityviewdistance.screen.EvdSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.IdMappingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("entityviewdistance")
@EventBusSubscriber
public class EVDMod {
    public static final Logger LOGGER = LogManager.getLogger("Entity View Distance");
    public static final String ID = "entity-view-distance";

    public static String VERSION;

    public EVDMod(ModContainer container) {
        VERSION = container.getModInfo().getVersion().toString();
        ConfigManager.loadConfig();
        container.registerExtensionPoint(IConfigScreenFactory.class, (a, b) -> new EvdSettingsScreen(b, MinecraftClient.getInstance().options));
    }



    @SubscribeEvent
    public static void setup(IdMappingEvent event) {
        for (var type : Registries.ENTITY_TYPE) {
            EvdUtils.initialize(type, Registries.ENTITY_TYPE.getId(type));
        }

        ConfigManager.overrideConfig();
    }

}
