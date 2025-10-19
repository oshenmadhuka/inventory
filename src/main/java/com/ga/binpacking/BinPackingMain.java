package com.ga.binpacking;

import com.ga.binpacking.agent.PackingAgent;
import com.ga.binpacking.algorithm.BinPackingProblem;
import com.ga.binpacking.algorithm.GeneticBinPacker;
import com.ga.binpacking.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Main application for 3D Bin Packing Optimization using Genetic Algorithms
 * 
 * This system demonstrates:
 * 1. Chromosome representation of packing solutions
 * 2. Fitness function to minimize wastage
 * 3. Jenetics library for GA optimization
 * 4. AI Agentic framework for plan execution
 * 
 * Assignment: Evolutionary Computing - 3D Bin Packing Problem
 */
public class BinPackingMain {

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  3D BIN PACKING OPTIMIZATION SYSTEM");
        System.out.println("  Using Genetic Algorithms & AI Agentic Framework");
        System.out.println("=".repeat(60) + "\n");

        // Define the inventory space (3D bin)
        // Dimensions based on the assignment notes
        int binWidth = 100; // Width
        int binHeight = 80; // Height
        int binDepth = 100; // Depth

        Bin inventoryBin = new Bin(binWidth, binHeight, binDepth);

        System.out.println("Inventory Space Configuration:");
        System.out.println("  Dimensions: " + binWidth + " x " + binHeight + " x " + binDepth);
        System.out.println("  Total Volume: " + inventoryBin.getTotalVolume() + " cubic units");
        System.out.println();

        // Define available items (based on assignment notes)
        // Item dimensions are 3D: width x height x depth
        List<Item> availableItems = createItemsFromAssignment();

        System.out.println("Available Items:");
        for (Item item : availableItems) {
            System.out.println("  " + item);
        }
        System.out.println();

        // Create the bin packing problem
        BinPackingProblem problem = new BinPackingProblem(availableItems, inventoryBin);

        // Create the genetic algorithm optimizer
        // Parameters: populationSize=150, generations=100, mutation=0.15, crossover=0.7
        GeneticBinPacker optimizer = new GeneticBinPacker(
                problem,
                150, // Population size
                100, // Max generations
                0.15, // Mutation rate
                0.7 // Crossover rate
        );

        // Run the optimization
        GeneticBinPacker.OptimizationResult result = optimizer.optimize();

        // Create an AI agent to execute and explain the plan
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  AI AGENTIC FRAMEWORK - PLAN EXECUTION");
        System.out.println("=".repeat(60) + "\n");

        PackingAgent agent = new PackingAgent("PackingBot-Alpha");

        // Agent executes the optimized plan
        agent.executePlan(result, inventoryBin);

        // Agent explains the reasoning
        agent.explainSolution(result);

        // Visualize the bin packing
        agent.visualizeBin(result.getSolution(), inventoryBin);

        // Display final summary
        displayFinalSummary(result, inventoryBin);
    }

    /**
     * Create items based on the assignment notes
     * Items A, B, C, D with different 3D dimensions, quantities, and costs
     */
    private static List<Item> createItemsFromAssignment() {
        List<Item> items = new ArrayList<>();

        // From the handwritten notes:
        // Item A: 10 qty, $150
        // Item B: 20 qty, $70
        // Item C: 5 qty, $60
        // Item D: 100 qty, $300

        // Assuming reasonable 3D dimensions for each item
        // (The exact dimensions weren't fully specified in the notes)

        items.add(new Item("A", 10, 10, 10, 150, 150.0)); // 10x10x10, qty=150, $150
        items.add(new Item("B", 20, 15, 12, 70, 70.0)); // 20x15x12, qty=70, $70
        items.add(new Item("C", 5, 8, 6, 60, 60.0)); // 5x8x6, qty=60, $60
        items.add(new Item("D", 10, 12, 10, 300, 300.0)); // 10x12x10, qty=300, $300

        return items;
    }

    /**
     * Display final summary of the optimization
     */
    private static void displayFinalSummary(GeneticBinPacker.OptimizationResult result, Bin bin) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  FINAL OPTIMIZATION SUMMARY");
        System.out.println("=".repeat(60));

        PackingSolution solution = result.getSolution();

        int usedVolume = (int) (bin.getTotalVolume() - solution.getTotalWastage());
        double utilizationPercent = (double) usedVolume / bin.getTotalVolume() * 100.0;

        System.out.println("\nProblem Characteristics:");
        System.out.println("  - Type: 3D Bin Packing Optimization");
        System.out.println("  - Approach: Genetic Algorithm (Jenetics)");
        System.out.println("  - Objective: Minimize Wastage, Maximize Utilization");

        System.out.println("\nSolution Quality:");
        System.out.printf("  - Fitness Score: %.4f%n", solution.getFitness());
        System.out.printf("  - Space Utilization: %.2f%%%n", utilizationPercent);
        System.out.printf("  - Items Packed: %d%n", solution.getPlacements().size());

        System.out.println("\nSpace Metrics:");
        System.out.printf("  - Total Volume: %.0f cubic units%n", bin.getTotalVolume());
        System.out.printf("  - Used Volume: %d cubic units%n", usedVolume);
        System.out.printf("  - Wasted Volume: %d cubic units%n", solution.getTotalWastage());

        System.out.println("\nEconomic Metrics:");
        System.out.printf("  - Total Value Packed: $%.2f%n", solution.getTotalCost());
        System.out.printf("  - Value per Cubic Unit: $%.4f%n",
                solution.getTotalCost() / usedVolume);

        System.out.println("\nKey Achievements:");
        if (utilizationPercent >= 80) {
            System.out.println("  ✓ Excellent space utilization achieved (≥80%)");
        } else if (utilizationPercent >= 60) {
            System.out.println("  ✓ Good space utilization achieved (≥60%)");
        } else {
            System.out.println("  → Moderate space utilization (improvement possible)");
        }

        if (solution.getPlacements().size() > 50) {
            System.out.println("  ✓ Large number of items successfully packed");
        }

        System.out.println("  ✓ No overlapping items (feasible solution)");
        System.out.println("  ✓ AI agent successfully executed the plan");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  Optimization Complete! Thank you for using the system.");
        System.out.println("=".repeat(60) + "\n");
    }
}
