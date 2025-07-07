package com.xrcraftmc.xrcUtils;

import com.google.gson.JsonParser;
import com.xrcraftmc.xrcUtils.utils.LobbyHandler;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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

                switch (itemName) {
                    case "Lobbies":
                        event.getPlayer().performCommand("cp lobbies");
                        break;
                    case "Games":
                        event.getPlayer().performCommand("cp selector");
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
