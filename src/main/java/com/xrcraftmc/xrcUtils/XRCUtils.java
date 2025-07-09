package com.xrcraftmc.xrcUtils;

import com.xrcraftmc.xrcUtils.utils.gui.GUIHandler;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class XRCUtils extends JavaPlugin {
    public static XRCUtils plugin;
    public static FileConfiguration config;
    public static Logger logger = Logger.getLogger("XRCUtils");

    @Override
    public void onEnable() {
        plugin = this;

        saveResource("config.yml", false);
        saveDefaultConfig();
        XRCUtils.config = getConfig();

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        if (config.getBoolean("enableCustomGUIs")) {GUIHandler.setupGUISystem();}

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("xrcutils", (sender, args) -> {
                if (args.length != 0) {
                    switch (args[0]) {
                        case "reload":
                            reloadConfig();
                            XRCUtils.config = getConfig();
                            sender.getExecutor().sendMessage("§aXRCUtils reloaded successfully.");
                            break;
                        default:
                            sender.getExecutor().sendMessage("§cUnknown command.");
                    }
                } else {
                    sender.getExecutor().sendMessage("§cUsage: /xrcutils reload");
                }
            });
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
