package com.ga.binpacking;

import com.ga.binpacking.algorithm.ImprovedBinPackingProblem;
import com.ga.binpacking.model.*;
import com.ga.binpacking.visualization.BinVisualizer2D;
import com.ga.binpacking.visualization.EvolutionVisualizer;
import io.jenetics.*;
import io.jenetics.engine.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Visual demonstration of 3D bin packing optimization
 * Shows real-time evolution and final packing visualization
 */
public class VisualBinPackingDemo {

    public static void main(String[] args) {
        // Print banner
        printBanner();

        // Setup
        Bin inventoryBin = new Bin(100, 80, 100);
        List<Item> availableItems = createItems();

        System.out.println("Inventory Space: " + inventoryBin.getWidth() + "×" +
                inventoryBin.getHeight() + "×" + inventoryBin.getDepth());
        System.out.println("Total Volume: " + inventoryBin.getTotalVolume() + " cubic units\n");

        // Create problem
        ImprovedBinPackingProblem problem = new ImprovedBinPackingProblem(availableItems, inventoryBin);

        // Create evolution visualizer
        int maxGenerations = 100;
        EvolutionVisualizer evolutionViz = EvolutionVisualizer.createWindow(maxGenerations);

        // Wait for window to be created
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("🚀 Starting Genetic Algorithm Evolution...");
        System.out.println("📊 Evolution visualizer window opened");
        System.out.println("⏳ Running " + maxGenerations + " generations...\n");

        // Build GA engine
        Engine<IntegerGene, Double> engine = Engine
                .builder(problem::fitness, problem.genotypeFactory())
                .populationSize(150)
                .maximizing()
                .alterers(
                        new SinglePointCrossover<>(0.65),
                        new Mutator<>(0.20))
                .selector(new TournamentSelector<>(5))
                .build();

        // Run evolution with real-time visualization
        Phenotype<IntegerGene, Double> best = engine.stream()
                .limit(maxGenerations)
                .peek(result -> {
                    long gen = result.generation();
                    double bestFitness = result.bestFitness();
                    double avgFitness = result.population().stream()
                            .mapToDouble(p -> p.fitness())
                            .average()
                            .orElse(0.0);

                    // Update evolution visualizer
                    evolutionViz.addGeneration(bestFitness, avgFitness);

                    // Print progress
                    if (gen % 10 == 0 || gen == 1) {
                        System.out.printf("Gen %3d: Best = %8.4f, Avg = %8.4f, Diversity = %6.4f%n",
                                gen, bestFitness, avgFitness, bestFitness - avgFitness);
                    }

                    // Small delay for visual effect
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .collect(EvolutionResult.toBestPhenotype());

        // Show results
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ OPTIMIZATION COMPLETE!");
        System.out.println("=".repeat(60));

        PackingSolution solution = problem.convertToSolution(best.genotype());

        int usedVolume = inventoryBin.getTotalVolume() - solution.getTotalWastage();
        double utilization = (double) usedVolume / inventoryBin.getTotalVolume() * 100.0;

        System.out.printf("🎯 Best Fitness: %.4f%n", best.fitness());
        System.out.printf("📦 Items Packed: %d%n", solution.getPlacements().size());
        System.out.printf("📊 Space Utilization: %.2f%%%n", utilization);
        System.out.printf("💰 Total Value: $%.2f%n", solution.getTotalCost());
        System.out.printf("🗑️  Wasted Volume: %d cubic units%n", solution.getTotalWastage());

        // Display chromosome
        IntegerChromosome priorities = (IntegerChromosome) best.genotype().get(0);
        IntegerChromosome strategy = (IntegerChromosome) best.genotype().get(1);

        System.out.println("\n🧬 Best Chromosome Configuration:");
        System.out.println("   Item Priorities:");
        for (int i = 0; i < Math.min(priorities.length(), availableItems.size()); i++) {
            System.out.printf("     %s: %d/100%n", availableItems.get(i).getId(),
                    priorities.get(i).intValue());
        }
        System.out.printf("   Packing Strategy: %d%n", strategy.get(0).intValue() % 4);
        System.out.printf("   Layer Preference: %d%n", strategy.get(1).intValue() % 3);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 Opening 2D Bin Visualization...");
        System.out.println("=".repeat(60) + "\n");

        // Show 2D visualization
        BinVisualizer2D.display(inventoryBin, solution, "2D Bin Packing Result");

        System.out.println("✅ Visualization windows are now open!");
        System.out.println("   - Evolution Progress (fitness over time)");
        System.out.println("   - 2D Bin Packing (single view)");
        System.out.println("\n💡 Close the windows to exit the program.");
    }

    private static void printBanner() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  ██████╗ ██████╗     ██████╗ ██╗███╗   ██╗");
        System.out.println("  ╚════██╗██╔══██╗    ██╔══██╗██║████╗  ██║");
        System.out.println("   █████╔╝██║  ██║    ██████╔╝██║██╔██╗ ██║");
        System.out.println("   ╚═══██╗██║  ██║    ██╔══██╗██║██║╚██╗██║");
        System.out.println("  ██████╔╝██████╔╝    ██████╔╝██║██║ ╚████║");
        System.out.println("  ╚═════╝ ╚═════╝     ╚═════╝ ╚═╝╚═╝  ╚═══╝");
        System.out.println("  PACKING OPTIMIZATION - Visual Simulation");
        System.out.println("  Using Genetic Algorithms & Real-time Visualization");
        System.out.println("=".repeat(70) + "\n");
    }

    private static List<Item> createItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("A", 10, 10, 10, 150, 150.0));
        items.add(new Item("B", 20, 15, 12, 70, 70.0));
        items.add(new Item("C", 5, 8, 6, 60, 60.0));
        items.add(new Item("D", 10, 12, 10, 300, 300.0));
        return items;
    }
}
