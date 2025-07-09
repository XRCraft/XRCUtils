package com.xrcraftmc.xrcUtils.utils.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xrcraftmc.xrcUtils.XRCUtils;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIHandler {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static ArrayList<Map<String, Inventory>> inventories = new ArrayList<>();
    public static ArrayList<GUIItem> guiItems = new ArrayList<>();

    public static void setupGUISystem() {
        try {GUIHandler.createGUIs();} catch (Exception e) {XRCUtils.logger.severe("Failed to create GUIs: " + e.getMessage());}

        XRCUtils.plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("gui", (sender, args) -> {
                if (args.length != 0) {
                    switch (args[0]) {
                        case "reload":
                            try {GUIHandler.createGUIs();} catch (Exception e) {
                                XRCUtils.logger.severe("Failed to reload GUIs: " + e.getMessage());
                                sender.getExecutor().sendMessage("§cFailed to reload GUIs. Check console for details.");
                                return;
                            }
                            sender.getExecutor().sendMessage("§aGUIs reloaded successfully.");
                            break;
                        default:
                            sender.getExecutor().sendMessage("§cUnknown command.");
                    }
                } else {
                    sender.getExecutor().sendMessage("§cUsage: /gui reload");
                }
            });
        });
    }

    public static void createGUIs() throws Exception {
        inventories.clear();
        guiItems.clear();
        File guiFile = XRCUtils.plugin.getDataFolder().toPath().resolve("guis.json").toFile();
        if (!guiFile.exists()) {
            createGUIFile(guiFile);
        }

        GUI[] guis = gson.fromJson(new FileReader(guiFile), GUI[].class);
        for (GUI gui : guis) {
            String guiTitle = gui.getTitle();

            Inventory inventory = XRCUtils.plugin.getServer().createInventory(
                    null,
                    gui.getRows() * 9,
                    Component.text(guiTitle)
            );

            ItemStack defaultItem = new ItemStack(Material.getMaterial(gui.getBlankSpaceItem().toUpperCase()));
            defaultItem.editMeta(meta -> meta.customName(Component.text("")));

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, defaultItem);
            }

            for (GUIItem item : gui.getItems()) {
                guiItems.add(item);
                String itemMaterial = item.getItemMaterial();
                String itemName = item.getItemName();
                List<String> itemLore = item.getItemLore();
                int itemSlot = item.getItemPosition();

                ItemStack itemStack = new ItemStack(Material.getMaterial(itemMaterial.toUpperCase()));

                itemStack.editMeta(meta -> {
                    meta.customName(Component.text(itemName));
                    meta.lore(itemLore.stream().map(Component::text).toList());
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                });

                inventory.setItem(itemSlot, itemStack);
            }

            inventories.add(Map.of(gui.getTitle(), inventory));
        }
    }

    public static void setInventory(Player player, String guiName) {
        for (Map<String, Inventory> inventoryMap : inventories) {
            if (inventoryMap.containsKey(guiName)) {
                player.openInventory(inventoryMap.get(guiName));
                return;
            }
        }
        player.sendMessage(Component.text("GUI not found: " + guiName).color(NamedTextColor.RED));
    }

    private static void createGUIFile(File guiFile) throws IOException {
        guiFile.createNewFile();
        FileWriter writer = new FileWriter(guiFile);

        ArrayList<String> exampleLore = new ArrayList<>();
        exampleLore.add("This is an example item.");

        List<GUI> gui = List.of(new GUI("Example GUI Title", 2, "WHITE_STAINED_GLASS_PANE", List.of(
                new GUIItem("Example Item", "DIAMOND", exampleLore, 10, "broadcast Example command ran")
        )));

        gson.toJson(gui, writer);
        writer.close();
        XRCUtils.logger.info("Created guis.json file in plugin folder, please edit it to add your GUIs.");
    }
}