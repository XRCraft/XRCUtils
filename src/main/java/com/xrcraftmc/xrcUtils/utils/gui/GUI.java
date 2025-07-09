package com.xrcraftmc.xrcUtils.utils.gui;

import java.util.List;

public class GUI {
    private final String title;
    private final int rows;
    private final String blankSpaceItem;
    private final List<GUIItem> items;

    public GUI(String title, int rows, String blankSpaceItem, List<GUIItem> items) {
        this.title = title;
        this.rows = rows;
        this.blankSpaceItem = blankSpaceItem;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public String getBlankSpaceItem() {
        return blankSpaceItem;
    }

    public List<GUIItem> getItems() {
        return items;
    }
}
