package com.ga.binpacking.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 3D inventory space (bin)
 */
public class Bin {
    private final int width;
    private final int height;
    private final int depth;
    private final List<PlacedItem> placedItems;

    public Bin(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.placedItems = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public int getTotalVolume() {
        return width * height * depth;
    }

    public List<PlacedItem> getPlacedItems() {
        return placedItems;
    }

    public void addPlacedItem(PlacedItem item) {
        placedItems.add(item);
    }

    public void clear() {
        placedItems.clear();
    }

    public int getUsedVolume() {
        return placedItems.stream()
                .mapToInt(pi -> pi.getItem().getVolume())
                .sum();
    }

    public int getWastedVolume() {
        return getTotalVolume() - getUsedVolume();
    }

    public double getUtilization() {
        return (double) getUsedVolume() / getTotalVolume() * 100;
    }

    @Override
    public String toString() {
        return String.format("Bin[%dx%dx%d, volume=%d, used=%d, wastage=%d, utilization=%.2f%%]",
                width, height, depth, getTotalVolume(), getUsedVolume(),
                getWastedVolume(), getUtilization());
    }
}

