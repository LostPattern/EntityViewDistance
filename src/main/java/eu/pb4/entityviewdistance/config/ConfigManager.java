package eu.pb4.entityviewdistance.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.pb4.entityviewdistance.EVDMod;
import eu.pb4.entityviewdistance.config.data.ConfigData;
import eu.pb4.entityviewdistance.config.data.VersionConfigData;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigManager {
    public static final int VERSION = 1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    private static Config CONFIG;

    public static Config getConfig() {
        return CONFIG;
    }

    public static boolean loadConfig() {
        CONFIG = null;
        try {
            ConfigData config;
            File configFile = new File(FMLPaths.CONFIGDIR.get().toFile(), "entity-view-distance.json");


            if (configFile.exists()) {
                String json = IOUtils.toString(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
                VersionConfigData versionConfigData = GSON.fromJson(json, VersionConfigData.class);

                config = switch (versionConfigData.CONFIG_VERSION_DONT_TOUCH_THIS) {
                    default -> GSON.fromJson(json, ConfigData.class);
                };
            } else {
                config = new ConfigData();
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8"));
            writer.write(GSON.toJson(config));
            writer.close();


            CONFIG = new Config(config);
            return true;
        } catch(Throwable exception) {
            EVDMod.LOGGER.error("Something went wrong while reading config!");
            exception.printStackTrace();
            CONFIG = new Config(new ConfigData());
            return false;
        }
    }
    public static void overrideConfig() {
        overrideConfig(CONFIG.toConfigData());
    }

    public static void overrideConfig(ConfigData configData) {
        File configFile = new File(FMLPaths.CONFIGDIR.get().toFile(), "entity-view-distance.json");
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
            writer.write(GSON.toJson(configData));
            writer.close();
            CONFIG = new Config(configData);
        } catch (Exception e) {
            EVDMod.LOGGER.error("Something went wrong while saving config!");
            e.printStackTrace();
        }
    }
}
