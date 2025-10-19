package com.ga.binpacking.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete packing solution (chromosome)
 * Contains the sequence of items and their placement strategy
 */
public class PackingSolution {
    private final List<ItemPlacement> placements;
    private double fitness;
    private int totalWastage;
    private double totalCost;

    public PackingSolution() {
        this.placements = new ArrayList<>();
        this.fitness = 0.0;
        this.totalWastage = 0;
        this.totalCost = 0.0;
    }

    public List<ItemPlacement> getPlacements() {
        return placements;
    }

    public void addPlacement(ItemPlacement placement) {
        placements.add(placement);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getTotalWastage() {
        return totalWastage;
    }

    public void setTotalWastage(int totalWastage) {
        this.totalWastage = totalWastage;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return String.format("PackingSolution[placements=%d, fitness=%.4f, wastage=%d, cost=$%.2f]",
                placements.size(), fitness, totalWastage, totalCost);
    }

    public static class ItemPlacement {
        private final String itemId;
        private final int quantity;
        private final Object position;
        private final int rotationCode;

        public ItemPlacement(String itemId, int quantity, Object position, int rotationCode) {
            this.itemId = itemId;
            this.quantity = quantity;
            this.position = position;
            this.rotationCode = rotationCode;
        }

        public String getItemId() {
            return itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public Object getPosition() {
            return position;
        }

        public int getRotationCode() {
            return rotationCode;
        }

        @Override
        public String toString() {
            return String.format("ItemPlacement[item=%s, qty=%d, pos=%s, rot=%d]",
                    itemId, quantity, position, rotationCode);
        }
    }
}
