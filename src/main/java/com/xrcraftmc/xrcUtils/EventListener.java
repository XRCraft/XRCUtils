package com.xrcraftmc.xrcUtils;

import com.google.gson.JsonParser;
import com.xrcraftmc.xrcUtils.utils.LobbyHandler;
import com.xrcraftmc.xrcUtils.utils.gui.GUIHandler;
import com.xrcraftmc.xrcUtils.utils.gui.GUIItem;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LobbyHandler.giveCompasses(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        LobbyHandler.flingPlayer(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().getItemMeta().hasCustomName()) {
                String itemMeta = JSONComponentSerializer.json().serialize(event.getItem().getItemMeta().customName());
                String itemName = JsonParser.parseString(itemMeta).getAsJsonObject().get("text").getAsString();
                Player player = event.getPlayer();

                switch (itemName) {
                    case "Lobbies":
                        GUIHandler.setInventory(player, "Lobby Selection Panel");
                        break;
                    case "Games":
                        GUIHandler.setInventory(player, "Game Selection Panel");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onGUIOpen(InventoryOpenEvent event) {
        if (!XRCUtils.config.getBoolean("enableCustomGUIs")) {return;}
        String invName = event.getView().getTitle();
        Player player = (Player) event.getPlayer();

        if (GUIHandler.inventories.stream().anyMatch(map -> map.containsKey(invName))) {
            for (ItemStack item : event.getInventory().getContents()) {
                if (item != null && item.getItemMeta() != null && item.getItemMeta().hasLore()) {
                    List<Component> lore = new ArrayList<>();
                    for (Component component : item.getItemMeta().lore()) {
                        String loreText = JSONComponentSerializer.json().serialize(component);
                        loreText = loreText.replaceAll("\"", "");
                        loreText = PlaceholderAPI.setPlaceholders(player, loreText);
                        String finalLoreText = loreText;
                        lore.add(Component.text(finalLoreText));
                    }

                    item.editMeta(meta -> meta.lore(lore));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onGUIItemClick(InventoryClickEvent event) {
        if (!XRCUtils.config.getBoolean("enableCustomGUIs")) {return;}
        String invName = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();

        for (Map<String, Inventory> inventoryMap : GUIHandler.inventories) {
            if (inventoryMap.containsKey(invName)) {
                if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                    String itemName = JSONComponentSerializer.json().serialize(event.getCurrentItem().getItemMeta().customName());
                    itemName = itemName.replaceAll("\"", "");

                    for (GUIItem item : GUIHandler.guiItems) {
                        if (item.getItemName().equals(itemName)) {
                            if (!item.getItemCommand().isEmpty() && !player.performCommand(item.getItemCommand())) {
                                player.sendMessage(Component.text("Failed to run item command, please report this to an admin!")
                                        .color(NamedTextColor.RED));
                                player.closeInventory();
                            }
                            break;
                        }
                    }
                }
                event.setCancelled(true);
                break;
            }
        }
    }
}
