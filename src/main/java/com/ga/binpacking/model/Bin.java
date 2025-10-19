package com.ga.binpacking.model;

import java.util.ArrayList;
import java.util.List;

public class Bin {
    private final double width;
    private final double height;
    private final double depth;
    private final boolean is2D;
    private final List<PlacedItem> placedItems;

    public Bin(double width, double height) {
        this.width = width;
        this.height = height;
        this.depth = 0;
        this.is2D = true;
        this.placedItems = new ArrayList<>();
    }

    public Bin(double width, double height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.is2D = false;
        this.placedItems = new ArrayList<>();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDepth() {
        return depth;
    }

    public double getTotalArea() {
        return width * height;
    }

    public double getTotalVolume() {
        if (is2D) {
            return width * height;
        }
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

    public double getUsedArea() {
        return placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getArea())
                .sum();
    }

    public double getOccupiedArea() {
        return placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getBoundingBoxArea())
                .sum();
    }

    public double getWastedArea() {
        return getOccupiedArea() - getUsedArea();
    }

    public double getUtilization() {
        return (getUsedArea() / getTotalArea()) * 100;
    }

    @Override
    public String toString() {
        return String.format("Bin[%.1fx%.1f, area=%.2f, used=%.2f, occupied=%.2f, wastage=%.2f, utilization=%.2f%%]",
                width, height, getTotalArea(), getUsedArea(), getOccupiedArea(),
                getWastedArea(), getUtilization());
    }
}
