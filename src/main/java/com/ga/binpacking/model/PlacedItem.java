package com.ga.binpacking.model;

/**
 * Represents an item placed at a specific position in the bin
 */
public class PlacedItem {
    private final Item item;
    private final Position3D position;
    private final int rotationCode; // 0-5 for different orientations

    public PlacedItem(Item item, Position3D position, int rotationCode) {
        this.item = item;
        this.position = position;
        this.rotationCode = rotationCode;
    }

    public Item getItem() {
        return item;
    }

    public Position3D getPosition() {
        return position;
    }

    public int getRotationCode() {
        return rotationCode;
    }

    /**
     * Get the actual width considering rotation
     */
    public int getActualWidth() {
        switch (rotationCode) {
            case 0:
            case 1:
                return item.getWidth();
            case 2:
            case 3:
                return item.getHeight();
            case 4:
            case 5:
                return item.getDepth();
            default:
                return item.getWidth();
        }
    }

    /**
     * Get the actual height considering rotation
     */
    public int getActualHeight() {
        switch (rotationCode) {
            case 0:
            case 5:
                return item.getHeight();
            case 1:
            case 4:
                return item.getDepth();
            case 2:
            case 3:
                return item.getWidth();
            default:
                return item.getHeight();
        }
    }

    /**
     * Get the actual depth considering rotation
     */
    public int getActualDepth() {
        switch (rotationCode) {
            case 0:
            case 3:
                return item.getDepth();
            case 1:
            case 2:
                return item.getHeight();
            case 4:
            case 5:
                return item.getWidth();
            default:
                return item.getDepth();
        }
    }

    @Override
    public String toString() {
        return String.format("PlacedItem[%s at %s, rotation=%d]",
                item.getId(), position, rotationCode);
    }
}

