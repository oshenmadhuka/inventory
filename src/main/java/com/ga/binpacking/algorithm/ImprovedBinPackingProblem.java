package com.ga.binpacking.algorithm;

import com.ga.binpacking.model.*;
import io.jenetics.*;
import io.jenetics.util.ISeq;

import java.util.*;
import java.util.stream.Collectors;

public class ImprovedBinPackingProblem {

    private final List<Item> availableItems;
    private final Bin bin;
    private final Map<String, Item> itemMap;

    public ImprovedBinPackingProblem(List<Item> availableItems, Bin bin) {
        this.availableItems = availableItems;
        this.bin = bin;
        this.itemMap = new HashMap<>();
        for (Item item : availableItems) {
            itemMap.put(item.getId(), item);
        }
    }

    public io.jenetics.util.Factory<Genotype<IntegerGene>> genotypeFactory() {
        return Genotype.of(
                IntegerChromosome.of(0, 100, 4),
                IntegerChromosome.of(0, 10, 2));
    }

    public double fitness(Genotype<IntegerGene> genotype) {
        IntegerChromosome priorities = (IntegerChromosome) genotype.get(0);
        IntegerChromosome strategy = (IntegerChromosome) genotype.get(1);

        List<String> itemSequence = createItemSequence(priorities);

        int packingStrategy = strategy.get(0).intValue() % 4;
        int layerPreference = strategy.get(1).intValue() % 3;

        List<PlacedItem> placedItems = packItems(itemSequence, packingStrategy, layerPreference);

        double usedArea = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getArea())
                .sum();

        double occupiedArea = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getBoundingBoxArea())
                .sum();

        double wastedArea = occupiedArea - usedArea;

        double totalValue = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getCost())
                .sum();

        double totalArea = bin.getTotalArea();

        double utilizationScore = (usedArea / totalArea) * 100.0;
        double valueScore = totalValue / 10.0;
        double wastePenalty = (wastedArea / totalArea) * 50.0;

        return utilizationScore + valueScore - wastePenalty;
    }

    private List<String> createItemSequence(IntegerChromosome priorities) {
        Map<String, Integer> itemPriorities = new HashMap<>();
        int index = 0;
        for (Item item : availableItems) {
            if (index < priorities.length()) {
                itemPriorities.put(item.getId(), priorities.get(index).intValue());
                index++;
            }
        }

        List<String> sequence = new ArrayList<>();

        List<Map.Entry<String, Integer>> sortedEntries = itemPriorities.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String itemId = entry.getKey();
            Item item = itemMap.get(itemId);
            int quantity = Math.min(item.getAvailableQuantity(), 100);

            for (int i = 0; i < quantity; i++) {
                sequence.add(itemId);
            }
        }

        return sequence;
    }

    private List<PlacedItem> packItems(List<String> sequence, int strategy, int layerPref) {
        List<PlacedItem> packed = new ArrayList<>();
        int gridWidth = (int) Math.ceil(bin.getWidth());
        int gridHeight = (int) Math.ceil(bin.getHeight());
        boolean[][] occupied = new boolean[gridWidth][gridHeight];

        for (String itemId : sequence) {
            Item item = itemMap.get(itemId);
            if (item == null)
                continue;

            Position2D position = findPosition(item, occupied, strategy);

            if (position != null) {
                PlacedItem placedItem = new PlacedItem(item, position, 0);
                packed.add(placedItem);
                markOccupied(occupied, position, item);
            }
        }

        return packed;
    }

    private Position2D findPosition(Item item, boolean[][] occupied, int strategy) {
        switch (strategy) {
            case 0:
                return findBottomLeft(item, occupied);
            case 1:
                return findTopLeft(item, occupied);
            case 2:
                return findBestFit(item, occupied);
            case 3:
                return findFirstFit(item, occupied);
            default:
                return findBottomLeft(item, occupied);
        }
    }

    private Position2D findBottomLeft(Item item, boolean[][] occupied) {
        int itemWidth = (int) Math.ceil(item.getWidth());
        int itemHeight = (int) Math.ceil(item.getHeight());

        for (int y = 0; y <= occupied[0].length - itemHeight; y++) {
            for (int x = 0; x <= occupied.length - itemWidth; x++) {
                if (canPlace(item, x, y, occupied)) {
                    return new Position2D(x, y);
                }
            }
        }
        return null;
    }

    private Position2D findTopLeft(Item item, boolean[][] occupied) {
        int itemWidth = (int) Math.ceil(item.getWidth());
        int itemHeight = (int) Math.ceil(item.getHeight());

        for (int y = occupied[0].length - itemHeight; y >= 0; y--) {
            for (int x = 0; x <= occupied.length - itemWidth; x++) {
                if (canPlace(item, x, y, occupied)) {
                    return new Position2D(x, y);
                }
            }
        }
        return null;
    }

    private Position2D findBestFit(Item item, boolean[][] occupied) {
        Position2D bestPos = null;
        double bestScore = Double.MAX_VALUE;
        int itemWidth = (int) Math.ceil(item.getWidth());
        int itemHeight = (int) Math.ceil(item.getHeight());

        for (int y = 0; y <= occupied[0].length - itemHeight; y++) {
            for (int x = 0; x <= occupied.length - itemWidth; x++) {
                if (canPlace(item, x, y, occupied)) {
                    double score = Math.sqrt(x * x + y * y);
                    if (score < bestScore) {
                        bestScore = score;
                        bestPos = new Position2D(x, y);
                    }
                }
            }
        }
        return bestPos;
    }

    private Position2D findFirstFit(Item item, boolean[][] occupied) {
        int itemWidth = (int) Math.ceil(item.getWidth());
        int itemHeight = (int) Math.ceil(item.getHeight());

        for (int x = 0; x <= occupied.length - itemWidth; x++) {
            for (int y = 0; y <= occupied[0].length - itemHeight; y++) {
                if (canPlace(item, x, y, occupied)) {
                    return new Position2D(x, y);
                }
            }
        }
        return null;
    }

    private boolean canPlace(Item item, int x, int y, boolean[][] occupied) {
        int itemWidth = (int) Math.ceil(item.getWidth());
        int itemHeight = (int) Math.ceil(item.getHeight());

        if (x + itemWidth > occupied.length || y + itemHeight > occupied[0].length) {
            return false;
        }

        for (int i = x; i < x + itemWidth; i++) {
            for (int j = y; j < y + itemHeight; j++) {
                if (occupied[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void markOccupied(boolean[][] occupied, Position2D position, Item item) {
        int x = (int) position.getX();
        int y = (int) position.getY();
        int itemWidth = (int) Math.ceil(item.getWidth());
        int itemHeight = (int) Math.ceil(item.getHeight());

        for (int i = x; i < x + itemWidth && i < occupied.length; i++) {
            for (int j = y; j < y + itemHeight && j < occupied[0].length; j++) {
                occupied[i][j] = true;
            }
        }
    }

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

        double usedArea = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getArea())
                .sum();
        double occupiedArea = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getBoundingBoxArea())
                .sum();
        double totalCost = placedItems.stream()
                .mapToDouble(pi -> pi.getItem().getCost())
                .sum();

        solution.setTotalWastage((int) (occupiedArea - usedArea));
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
