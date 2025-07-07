package com.xrcraftmc.xrcUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class XRCUtils extends JavaPlugin {
    public static FileConfiguration config;
    public static Logger logger = Logger.getLogger("XRCUtils");

    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        saveDefaultConfig();
        XRCUtils.config = getConfig();

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
