package com.ga.binpacking.model;

/**
 * Represents an item with 3D dimensions, quantity, and cost
 */
public class Item {
    private final String id;
    private final int width;
    private final int height;
    private final int depth;
    private final int availableQuantity;
    private final double cost;

    public Item(String id, int width, int height, int depth, int availableQuantity, double cost) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.availableQuantity = availableQuantity;
        this.cost = cost;
    }

    public String getId() {
        return id;
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

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public double getCost() {
        return cost;
    }

    public int getVolume() {
        return width * height * depth;
    }

    @Override
    public String toString() {
        return String.format("Item[%s: %dx%dx%d, qty=%d, cost=$%.2f]",
                id, width, height, depth, availableQuantity, cost);
    }
}

