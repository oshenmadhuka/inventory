package com.ga.binpacking.algorithm;

import com.ga.binpacking.model.*;
import io.jenetics.*;
import io.jenetics.util.ISeq;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Improved 3D bin packing problem with better diversity
 * Uses integer chromosomes to represent item priorities and packing strategies
 */
public class ImprovedBinPackingProblem {

    private final List<Item> availableItems;
    private final Bin bin;
    private final Map<String, Item> itemMap;
    private final Random random;

    public ImprovedBinPackingProblem(List<Item> availableItems, Bin bin) {
        this.availableItems = availableItems;
        this.bin = bin;
        this.itemMap = new HashMap<>();
        for (Item item : availableItems) {
            itemMap.put(item.getId(), item);
        }
        this.random = new Random();
    }

    /**
     * Create genotype factory with integer genes representing:
     * - Gene 0-3: Priorities for items A, B, C, D (0-100)
     * - Gene 4: Packing strategy (0-3)
     * - Gene 5: Layer preference (0-2)
     */
    public io.jenetics.util.Factory<Genotype<IntegerGene>> genotypeFactory() {
        return Genotype.of(
                // Priorities for each item type (4 genes, values 0-100)
                IntegerChromosome.of(0, 100, 4),
                // Packing strategy and preferences (2 genes)
                IntegerChromosome.of(0, 10, 2));
    }

    /**
     * Improved fitness function with better diversity
     */
    public double fitness(Genotype<IntegerGene> genotype) {
        // Extract priorities from chromosome
        IntegerChromosome priorities = (IntegerChromosome) genotype.get(0);
        IntegerChromosome strategy = (IntegerChromosome) genotype.get(1);

        // Create item sequence based on priorities
        List<String> itemSequence = createItemSequence(priorities);

        // Pack items with strategy
        int packingStrategy = strategy.get(0).intValue() % 4;
        int layerPreference = strategy.get(1).intValue() % 3;

        List<PlacedItem> placedItems = packItems(itemSequence, packingStrategy, layerPreference);

        // Calculate fitness
        int usedVolume = placedItems.stream()
                .mapToInt(pi -> pi.getItem().getVolume())
                .sum();

        double totalValue = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getCost())
                .sum();

        int totalVolume = bin.getTotalVolume();

        // Multi-objective fitness
        double utilizationScore = (double) usedVolume / totalVolume * 100.0;
        double valueScore = totalValue / 10.0;
        double diversityBonus = Math.abs(packingStrategy - 2) * 2.0; // Encourage variety

        return utilizationScore + valueScore + diversityBonus;
    }

    /**
     * Create item sequence based on priorities
     */
    private List<String> createItemSequence(IntegerChromosome priorities) {
        // Get priority for each item type
        Map<String, Integer> itemPriorities = new HashMap<>();
        int index = 0;
        for (Item item : availableItems) {
            if (index < priorities.length()) {
                itemPriorities.put(item.getId(), priorities.get(index).intValue());
                index++;
            }
        }

        // Create sequence: higher priority items appear first
        List<String> sequence = new ArrayList<>();

        // Sort items by priority (descending)
        List<Map.Entry<String, Integer>> sortedEntries = itemPriorities.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toList());

        // Add items in priority order with quantities
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String itemId = entry.getKey();
            Item item = itemMap.get(itemId);
            int quantity = Math.min(item.getAvailableQuantity(), 50);

            for (int i = 0; i < quantity; i++) {
                sequence.add(itemId);
            }
        }

        return sequence;
    }

    /**
     * Pack items with specified strategy
     */
    private List<PlacedItem> packItems(List<String> sequence, int strategy, int layerPref) {
        List<PlacedItem> packed = new ArrayList<>();
        boolean[][][] occupied = new boolean[bin.getWidth()][bin.getHeight()][bin.getDepth()];

        for (String itemId : sequence) {
            Item item = itemMap.get(itemId);
            if (item == null)
                continue;

            Position3D position = findPosition(item, occupied, strategy, layerPref);

            if (position != null) {
                PlacedItem placedItem = new PlacedItem(item, position, 0);
                packed.add(placedItem);
                markOccupied(occupied, position, item);
            }
        }

        return packed;
    }

    /**
     * Find position based on strategy
     */
    private Position3D findPosition(Item item, boolean[][][] occupied, int strategy, int layerPref) {
        switch (strategy) {
            case 0: // Bottom-up, left-right
                return findBottomUp(item, occupied);
            case 1: // Layer-by-layer
                return findLayered(item, occupied, layerPref);
            case 2: // Front-to-back
                return findFrontToBack(item, occupied);
            case 3: // Mixed strategy
                return findMixed(item, occupied, layerPref);
            default:
                return findBottomUp(item, occupied);
        }
    }

    /**
     * Bottom-up packing strategy
     */
    private Position3D findBottomUp(Item item, boolean[][][] occupied) {
        for (int y = 0; y <= bin.getHeight() - item.getHeight(); y++) {
            for (int x = 0; x <= bin.getWidth() - item.getWidth(); x++) {
                for (int z = 0; z <= bin.getDepth() - item.getDepth(); z++) {
                    if (canPlace(item, x, y, z, occupied)) {
                        return new Position3D(x, y, z);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Layer-by-layer packing strategy
     */
    private Position3D findLayered(Item item, boolean[][][] occupied, int startLayer) {
        int layerSize = bin.getDepth() / 3;
        int start = startLayer * layerSize;

        for (int z = start; z <= bin.getDepth() - item.getDepth(); z++) {
            for (int y = 0; y <= bin.getHeight() - item.getHeight(); y++) {
                for (int x = 0; x <= bin.getWidth() - item.getWidth(); x++) {
                    if (canPlace(item, x, y, z, occupied)) {
                        return new Position3D(x, y, z);
                    }
                }
            }
        }

        // Try other layers if first choice failed
        for (int z = 0; z <= bin.getDepth() - item.getDepth(); z++) {
            for (int y = 0; y <= bin.getHeight() - item.getHeight(); y++) {
                for (int x = 0; x <= bin.getWidth() - item.getWidth(); x++) {
                    if (canPlace(item, x, y, z, occupied)) {
                        return new Position3D(x, y, z);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Front-to-back packing strategy
     */
    private Position3D findFrontToBack(Item item, boolean[][][] occupied) {
        for (int z = 0; z <= bin.getDepth() - item.getDepth(); z++) {
            for (int y = 0; y <= bin.getHeight() - item.getHeight(); y++) {
                for (int x = 0; x <= bin.getWidth() - item.getWidth(); x++) {
                    if (canPlace(item, x, y, z, occupied)) {
                        return new Position3D(x, y, z);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Mixed packing strategy
     */
    private Position3D findMixed(Item item, boolean[][][] occupied, int preference) {
        // Combine strategies based on preference
        Position3D pos = (preference == 0) ? findBottomUp(item, occupied) : findLayered(item, occupied, preference);
        if (pos != null)
            return pos;
        return findFrontToBack(item, occupied);
    }

    /**
     * Check if item can be placed
     */
    private boolean canPlace(Item item, int x, int y, int z, boolean[][][] occupied) {
        if (x + item.getWidth() > bin.getWidth() ||
                y + item.getHeight() > bin.getHeight() ||
                z + item.getDepth() > bin.getDepth()) {
            return false;
        }

        for (int i = x; i < x + item.getWidth(); i++) {
            for (int j = y; j < y + item.getHeight(); j++) {
                for (int k = z; k < z + item.getDepth(); k++) {
                    if (occupied[i][j][k]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Mark space as occupied
     */
    private void markOccupied(boolean[][][] occupied, Position3D position, Item item) {
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        for (int i = x; i < x + item.getWidth() && i < occupied.length; i++) {
            for (int j = y; j < y + item.getHeight() && j < occupied[0].length; j++) {
                for (int k = z; k < z + item.getDepth() && k < occupied[0][0].length; k++) {
                    occupied[i][j][k] = true;
                }
            }
        }
    }

    /**
     * Convert to packing solution
     */
    public PackingSolution convertToSolution(Genotype<IntegerGene> genotype) {
        IntegerChromosome priorities = (IntegerChromosome) genotype.get(0);
        IntegerChromosome strategy = (IntegerChromosome) genotype.get(1);

        List<String> itemSequence = createItemSequence(priorities);
        int packingStrategy = strategy.get(0).intValue() % 4;
        int layerPreference = strategy.get(1).intValue() % 3;

        List<PlacedItem> placedItems = packItems(itemSequence, packingStrategy, layerPreference);

        PackingSolution solution = new PackingSolution();

        Map<String, Integer> itemCounts = new HashMap<>();
        for (PlacedItem pi : placedItems) {
            String itemId = pi.getItem().getId();
            itemCounts.put(itemId, itemCounts.getOrDefault(itemId, 0) + 1);
            solution.addPlacement(new PackingSolution.ItemPlacement(
                    itemId, 1, pi.getPosition(), pi.getRotationCode()));
        }

        int usedVolume = placedItems.stream()
                .mapToInt(pi -> pi.getItem().getVolume())
                .sum();
        double totalCost = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getCost())
                .sum();

        solution.setTotalWastage(bin.getTotalVolume() - usedVolume);
        solution.setTotalCost(totalCost);
        solution.setFitness(fitness(genotype));

        return solution;
    }

    public Bin getBin() {
        return bin;
    }

    public List<Item> getAvailableItems() {
        return availableItems;
    }
}
