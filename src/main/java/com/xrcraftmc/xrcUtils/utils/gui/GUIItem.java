package com.xrcraftmc.xrcUtils.utils.gui;

import java.util.ArrayList;

public class GUIItem {
    private final String itemMaterial;
    private final String itemName;
    private final ArrayList<String> itemLore;
    private final int itemPosition;
    private final String itemCommand;

    public GUIItem(String itemName, String itemMaterial, ArrayList<String> itemLore, int itemPosition, String itemCommand) {
        this.itemMaterial = itemMaterial;
        this.itemName = itemName;
        this.itemLore = itemLore;
        this.itemPosition = itemPosition;
        this.itemCommand = itemCommand;
    }

    public String getItemMaterial() {
        return itemMaterial;
    }

    public String getItemName() {
        return itemName;
    }

    public ArrayList<String> getItemLore() {
        return itemLore;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public String getItemCommand() {
        return itemCommand;
    }
}
