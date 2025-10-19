package com.ga.binpacking.model;

public class PlacedItem {
    private final Item item;
    private final Object position;
    private final int rotationCode;

    public PlacedItem(Item item, Position2D position, int rotationCode) {
        this.item = item;
        this.position = position;
        this.rotationCode = rotationCode;
    }

    public PlacedItem(Item item, Position3D position, int rotationCode) {
        this.item = item;
        this.position = position;
        this.rotationCode = rotationCode;
    }

    public Item getItem() {
        return item;
    }

    public Object getPosition() {
        return position;
    }

    public Position2D getPosition2D() {
        if (position instanceof Position2D) {
            return (Position2D) position;
        }
        return null;
    }

    public Position3D getPosition3D() {
        if (position instanceof Position3D) {
            return (Position3D) position;
        }
        return null;
    }

    public int getRotationCode() {
        return rotationCode;
    }

    public double getActualWidth() {
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

    public double getActualHeight() {
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

    public double getActualDepth() {
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
