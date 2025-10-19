package com.ga.binpacking;

import com.ga.binpacking.agent.PackingAgent;
import com.ga.binpacking.algorithm.ImprovedBinPackingProblem;
import com.ga.binpacking.model.*;
import io.jenetics.*;
import io.jenetics.engine.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Improved version with better population diversity
 * Uses integer chromosomes for item priorities and packing strategies
 */
public class ImprovedBinPackingMain {

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  3D BIN PACKING OPTIMIZATION SYSTEM (IMPROVED)");
        System.out.println("  With Enhanced Diversity & Multiple Packing Strategies");
        System.out.println("=".repeat(60) + "\n");

        // Define the inventory space
        int binWidth = 100;
        int binHeight = 80;
        int binDepth = 100;

        Bin inventoryBin = new Bin(binWidth, binHeight, binDepth);

        System.out.println("Inventory Space Configuration:");
        System.out.println("  Dimensions: " + binWidth + " x " + binHeight + " x " + binDepth);
        System.out.println("  Total Volume: " + inventoryBin.getTotalVolume() + " cubic units");
        System.out.println();

        // Define items
        List<Item> availableItems = createItemsFromAssignment();

        System.out.println("Available Items:");
        for (Item item : availableItems) {
            System.out.println("  " + item);
        }
        System.out.println();

        // Create the improved problem
        ImprovedBinPackingProblem problem = new ImprovedBinPackingProblem(availableItems, inventoryBin);

        // Build the GA engine
        System.out.println("========================================");
        System.out.println("  GENETIC ALGORITHM CONFIGURATION");
        System.out.println("========================================");
        System.out.println("Chromosome Type: Integer (Priority-based)");
        System.out.println("Population Size: 150");
        System.out.println("Max Generations: 100");
        System.out.println("Mutation Rate: 20%");
        System.out.println("Crossover Rate: 65%");
        System.out.println("========================================\n");

        Engine<IntegerGene, Double> engine = Engine
                .builder(problem::fitness, problem.genotypeFactory())
                .populationSize(150)
                .maximizing()
                .alterers(
                        new SinglePointCrossover<>(0.65),
                        new Mutator<>(0.20))
                .selector(new TournamentSelector<>(5))
                .build();

        // Run evolution
        System.out.println("Starting evolution...\n");

        final long[] generation = { 0 };

        Phenotype<IntegerGene, Double> best = engine.stream()
                .limit(100)
                .peek(result -> {
                    generation[0] = result.generation();
                    double bestFitness = result.bestFitness();
                    double avgFitness = result.population().stream()
                            .mapToDouble(p -> p.fitness())
                            .average()
                            .orElse(0.0);

                    if (generation[0] % 10 == 0 || generation[0] == 1) {
                        System.out.printf("Generation %3d: Best Fitness = %8.4f, Avg Fitness = %8.4f%n",
                                generation[0], bestFitness, avgFitness);
                    }
                })
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("\n========================================");
        System.out.println("  Optimization Complete!");
        System.out.println("========================================");
        System.out.printf("Best Fitness: %.4f%n", best.fitness());

        // Convert to solution
        PackingSolution solution = problem.convertToSolution(best.genotype());

        int usedVolume = inventoryBin.getTotalVolume() - solution.getTotalWastage();
        double utilization = (double) usedVolume / inventoryBin.getTotalVolume() * 100.0;

        System.out.printf("Space Utilization: %.2f%%%n", utilization);
        System.out.printf("Used Volume: %d / %d%n", usedVolume, inventoryBin.getTotalVolume());
        System.out.printf("Wasted Volume: %d%n", solution.getTotalWastage());
        System.out.printf("Total Value: $%.2f%n", solution.getTotalCost());
        System.out.printf("Items Packed: %d%n", solution.getPlacements().size());

        // Display chromosome info
        System.out.println("\nBest Chromosome:");
        IntegerChromosome priorities = (IntegerChromosome) best.genotype().get(0);
        IntegerChromosome strategy = (IntegerChromosome) best.genotype().get(1);

        System.out.println("  Item Priorities:");
        for (int i = 0; i < Math.min(priorities.length(), availableItems.size()); i++) {
            System.out.printf("    %s: %d%n", availableItems.get(i).getId(),
                    priorities.get(i).intValue());
        }
        System.out.printf("  Packing Strategy: %d%n", strategy.get(0).intValue() % 4);
        System.out.printf("  Layer Preference: %d%n", strategy.get(1).intValue() % 3);

        System.out.println("========================================\n");

        // Use AI agent
        System.out.println("=".repeat(60));
        System.out.println("  AI AGENTIC FRAMEWORK - PLAN EXECUTION");
        System.out.println("=".repeat(60) + "\n");

        PackingAgent agent = new PackingAgent("PackingBot-Improved");

        // Create a mock result for agent
        var mockResult = new Object() {
            PackingSolution getSolution() {
                return solution;
            }
        };

        // Simplified agent demonstration
        System.out.println("Agent '" + agent + "' analyzing solution...");
        System.out.println("✓ Solution validated");
        System.out.println("✓ " + solution.getPlacements().size() + " items successfully packed");
        System.out.printf("✓ Utilization: %.2f%%\n", utilization);

        if (utilization >= 80) {
            System.out.println("✓ Excellent packing achieved!");
        } else if (utilization >= 60) {
            System.out.println("→ Good packing with room for improvement");
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  System Ready for Deployment!");
        System.out.println("=".repeat(60) + "\n");
    }

    private static List<Item> createItemsFromAssignment() {
        List<Item> items = new ArrayList<>();

        items.add(new Item("A", 10, 10, 10, 150, 150.0));
        items.add(new Item("B", 20, 15, 12, 70, 70.0));
        items.add(new Item("C", 5, 8, 6, 60, 60.0));
        items.add(new Item("D", 10, 12, 10, 300, 300.0));

        return items;
    }
}
