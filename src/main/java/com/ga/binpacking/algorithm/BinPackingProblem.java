package com.ga.binpacking.algorithm;

import com.ga.binpacking.model.*;
import io.jenetics.*;
import io.jenetics.util.ISeq;

import java.util.*;

/**
 * Defines the 3D bin packing problem and fitness evaluation
 */
public class BinPackingProblem {

    private final List<Item> availableItems;
    private final Bin bin;
    private final Map<String, Item> itemMap;

    public BinPackingProblem(List<Item> availableItems, Bin bin) {
        this.availableItems = availableItems;
        this.bin = bin;
        this.itemMap = new HashMap<>();
        for (Item item : availableItems) {
            itemMap.put(item.getId(), item);
        }
    }

    /**
     * Create the genotype factory for the genetic algorithm
     * Uses permutation encoding - the order of items to pack
     */
    public io.jenetics.util.Factory<Genotype<EnumGene<String>>> genotypeFactory() {
        // Create a list of item IDs based on available quantities
        List<String> itemSequence = new ArrayList<>();
        for (Item item : availableItems) {
            // Add multiple copies based on limited quantity (to make it realistic)
            int maxItems = Math.min(item.getAvailableQuantity(), 50); // Limit for computational efficiency
            for (int i = 0; i < maxItems; i++) {
                itemSequence.add(item.getId());
            }
        }

        ISeq<String> validAlleles = ISeq.of(itemSequence);

        return Genotype.of(
                PermutationChromosome.of(validAlleles));
    }

    /**
     * Fitness function: Maximize space utilization and minimize wastage
     * Higher fitness = better solution
     */
    public double fitness(Genotype<EnumGene<String>> genotype) {
        // Extract the sequence of items from the chromosome
        Chromosome<EnumGene<String>> chromosome = genotype.chromosome();

        // Create a fresh bin for evaluation
        Bin testBin = new Bin(bin.getWidth(), bin.getHeight(), bin.getDepth());

        // Try to pack items in the order specified by the chromosome
        List<PlacedItem> placedItems = packItems(chromosome, testBin);

        // Calculate fitness metrics
        double usedVolume = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getVolume())
                .sum();

        double totalValue = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getCost())
                .sum();

        double totalVolume = testBin.getTotalVolume();
        double wastedVolume = totalVolume - usedVolume;

        // Fitness components:
        // 1. Space utilization (0-100%)
        double utilizationScore = (double) usedVolume / totalVolume * 100.0;

        // 2. Value score (normalize by a factor)
        double valueScore = totalValue / 10.0; // Normalize

        // 3. Penalty for wastage
        double wastagePenalty = (double) wastedVolume / totalVolume * 50.0;

        // Combined fitness (higher is better)
        // We want to maximize utilization and value, minimize wastage
        double fitness = utilizationScore + valueScore - wastagePenalty;

        return Math.max(0, fitness); // Ensure non-negative
    }

    /**
     * Pack items into the bin using a simple layer-based first-fit algorithm
     */
    private List<PlacedItem> packItems(Chromosome<EnumGene<String>> chromosome, Bin testBin) {
        List<PlacedItem> packed = new ArrayList<>();

        // Track occupied spaces using a simplified 3D grid approach
        boolean[][][] occupied = new boolean[(int) bin.getWidth()][(int) bin.getHeight()][(int) bin.getDepth()];

        // Keep track of the next available position for faster packing
        int currentX = 0, currentY = 0, currentZ = 0;

        // Try to place each item in the chromosome sequence
        for (int i = 0; i < chromosome.length(); i++) {
            String itemId = chromosome.get(i).allele();
            Item item = itemMap.get(itemId);

            if (item == null)
                continue;

            // Try to find a valid position for this item (starting from last position)
            Position3D position = findValidPosition(item, occupied, testBin, currentX, currentY, currentZ);

            if (position != null) {
                // Place the item (rotation = 0 for simplicity)
                PlacedItem placedItem = new PlacedItem(item, position, 0);
                packed.add(placedItem);

                // Mark the space as occupied
                markOccupied(occupied, position, item);

                // Update current position hint for next item
                currentX = (int) (position.getX() + item.getWidth());
                if (currentX >= testBin.getWidth()) {
                    currentX = 0;
                    currentZ += 5; // Move to next layer
                    if (currentZ >= testBin.getDepth()) {
                        currentZ = 0;
                        currentY += 5;
                    }
                }
            }
        }

        return packed;
    }

    /**
     * Find a valid position to place an item using first-fit approach
     * Now considers a starting hint to make order matter more
     */
    private Position3D findValidPosition(Item item, boolean[][][] occupied, Bin testBin,
            int startX, int startY, int startZ) {
        // First, try positions starting from the hint
        Position3D pos = tryPlacementFromPosition(item, occupied, testBin, startX, startY, startZ);
        if (pos != null)
            return pos;

        // If that didn't work, try from the beginning
        return tryPlacementFromPosition(item, occupied, testBin, 0, 0, 0);
    }

    /**
     * Try to place item starting from given coordinates
     */
    private Position3D tryPlacementFromPosition(Item item, boolean[][][] occupied, Bin testBin,
            int startX, int startY, int startZ) {
        // Try positions layer by layer
        for (int y = startY; y <= testBin.getHeight() - item.getHeight(); y++) {
            for (int z = (y == startY ? startZ : 0); z <= testBin.getDepth() - item.getDepth(); z++) {
                for (int x = (y == startY && z == startZ ? startX : 0); x <= testBin.getWidth()
                        - item.getWidth(); x++) {
                    if (canPlace(item, x, y, z, occupied, testBin)) {
                        return new Position3D(x, y, z);
                    }
                }
            }
        }
        return null; // No valid position found
    }

    /**
     * Check if an item can be placed at the given position
     */
    private boolean canPlace(Item item, int x, int y, int z, boolean[][][] occupied, Bin testBin) {
        // Check bounds
        if (x + item.getWidth() > testBin.getWidth() ||
                y + item.getHeight() > testBin.getHeight() ||
                z + item.getDepth() > testBin.getDepth()) {
            return false;
        }

        // Check for overlap
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
     * Mark the space as occupied after placing an item
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
     * Convert a genotype to a packing solution for visualization
     */
    public PackingSolution convertToSolution(Genotype<EnumGene<String>> genotype) {
        PackingSolution solution = new PackingSolution();

        Chromosome<EnumGene<String>> chromosome = genotype.chromosome();
        Bin testBin = new Bin(bin.getWidth(), bin.getHeight(), bin.getDepth());

        List<PlacedItem> placedItems = packItems(chromosome, testBin);

        // Convert to solution format
        Map<String, Integer> itemCounts = new HashMap<>();
        for (PlacedItem pi : placedItems) {
            String itemId = pi.getItem().getId();
            itemCounts.put(itemId, itemCounts.getOrDefault(itemId, 0) + 1);
        }

        for (PlacedItem pi : placedItems) {
            solution.addPlacement(new PackingSolution.ItemPlacement(
                    pi.getItem().getId(),
                    itemCounts.get(pi.getItem().getId()),
                    pi.getPosition(),
                    pi.getRotationCode()));
        }

        // Calculate metrics
        double usedVolume = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getVolume())
                .sum();
        double totalCost = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getCost())
                .sum();

        solution.setTotalWastage((int) (testBin.getTotalVolume() - usedVolume));
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
