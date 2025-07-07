package com.xrcraftmc.xrcUtils.utils;

import com.xrcraftmc.xrcUtils.XRCUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LobbyHandler {
    public static void giveCompasses(Player player) {
        if (!XRCUtils.config.getBoolean("giveCompassesOnJoin")) {return;}

        ItemStack lobbies = new ItemStack(Material.COMPASS);
        ItemStack games = new ItemStack(Material.RECOVERY_COMPASS);

        lobbies.editMeta(meta -> {
            meta.customName(Component.text("Lobbies").color(NamedTextColor.DARK_PURPLE));
            meta.lore(List.of(Component.text("Click to pick a lobby").color(NamedTextColor.RED)));
        });

        games.editMeta(meta -> {
            meta.customName(Component.text("Games").color(NamedTextColor.GOLD));
            meta.lore(List.of(Component.text("Click to pick a game").color(NamedTextColor.DARK_AQUA)));
        });

        XRCUtils.logger.info("Item names are " + lobbies.getItemMeta().customName());

        player.getInventory().clear();
        player.getInventory().setItem(8, lobbies);
        player.getInventory().setItem(0, games);
    }

    public static void flingPlayer(Player player) {
        if (!XRCUtils.config.getBoolean("flingPlayerOnGold")) {return;}

        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GOLD_BLOCK) {
            player.setVelocity(player.getLocation().getDirection().multiply(5).setY(1).setX(-2.5).setZ(0));
        }
    }
}
