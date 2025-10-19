package com.ga.binpacking.model;

public class Item {
    public enum Shape {
        RECTANGLE, SQUARE, CIRCLE, TRIANGLE
    }

    private final String id;
    private final Shape shape;
    private final double width;
    private final double height;
    private final double depth;
    private final double radius;
    private final double base;
    private final int availableQuantity;
    private final double cost;
    private final boolean is3D;

    public Item(String id, double width, double height, double depth, int availableQuantity, double cost) {
        this.id = id;
        this.shape = Shape.RECTANGLE;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.radius = 0;
        this.base = 0;
        this.availableQuantity = availableQuantity;
        this.cost = cost;
        this.is3D = true;
    }

    public Item(String id, Shape shape, double param1, double param2, int availableQuantity, double cost) {
        this.id = id;
        this.shape = shape;
        this.availableQuantity = availableQuantity;
        this.cost = cost;
        this.depth = 0;
        this.is3D = false;

        switch (shape) {
            case RECTANGLE:
                this.width = param1;
                this.height = param2;
                this.radius = 0;
                this.base = 0;
                break;
            case SQUARE:
                this.width = param1;
                this.height = param1;
                this.radius = 0;
                this.base = 0;
                break;
            case CIRCLE:
                this.radius = param1;
                this.width = param1 * 2;
                this.height = param1 * 2;
                this.base = 0;
                break;
            case TRIANGLE:
                this.base = param1;
                this.height = param2;
                this.width = param1;
                this.radius = 0;
                break;
            default:
                this.width = 0;
                this.height = 0;
                this.radius = 0;
                this.base = 0;
        }
    }

    public String getId() {
        return id;
    }

    public Shape getShape() {
        return shape;
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

    public double getRadius() {
        return radius;
    }

    public double getBase() {
        return base;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public double getCost() {
        return cost;
    }

    public double getVolume() {
        if (is3D) {
            return width * height * depth;
        }
        return getArea();
    }

    public double getArea() {
        if (is3D) {
            return width * height;
        }
        switch (shape) {
            case RECTANGLE:
                return width * height;
            case SQUARE:
                return width * width;
            case CIRCLE:
                return Math.PI * radius * radius;
            case TRIANGLE:
                return 0.5 * base * height;
            default:
                return 0;
        }
    }

    public double getBoundingBoxArea() {
        return width * height;
    }

    public double getWastageFactor() {
        if (is3D) {
            return 1.0;
        }
        switch (shape) {
            case RECTANGLE:
            case SQUARE:
                return 1.0;
            case CIRCLE:
                return Math.PI / 4.0;
            case TRIANGLE:
                return 0.5;
            default:
                return 1.0;
        }
    }

    public double getEffectiveArea() {
        return getBoundingBoxArea();
    }

    @Override
    public String toString() {
        if (is3D) {
            return String.format("Item[%s: %.1fx%.1fx%.1f, qty=%d, cost=$%.2f]",
                    id, width, height, depth, availableQuantity, cost);
        }
        switch (shape) {
            case RECTANGLE:
                return String.format("Item[%s: RECTANGLE %.1fx%.1f, area=%.2f, qty=%d, cost=$%.2f]",
                        id, width, height, getArea(), availableQuantity, cost);
            case SQUARE:
                return String.format("Item[%s: SQUARE %.1fx%.1f, area=%.2f, qty=%d, cost=$%.2f]",
                        id, width, width, getArea(), availableQuantity, cost);
            case CIRCLE:
                return String.format("Item[%s: CIRCLE r=%.1f, area=%.2f, qty=%d, cost=$%.2f]",
                        id, radius, getArea(), availableQuantity, cost);
            case TRIANGLE:
                return String.format("Item[%s: TRIANGLE base=%.1f h=%.1f, area=%.2f, qty=%d, cost=$%.2f]",
                        id, base, height, getArea(), availableQuantity, cost);
            default:
                return String.format("Item[%s: qty=%d, cost=$%.2f]", id, availableQuantity, cost);
        }
    }
}
