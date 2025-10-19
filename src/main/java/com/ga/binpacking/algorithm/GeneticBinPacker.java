package com.ga.binpacking.algorithm;

import com.ga.binpacking.model.*;
import io.jenetics.*;
import io.jenetics.engine.*;

/**
 * Genetic Algorithm engine for solving the 3D bin packing problem
 * Uses Jenetics library
 */
public class GeneticBinPacker {

    private final BinPackingProblem problem;
    private final int populationSize;
    private final int maxGenerations;
    private final double mutationRate;
    private final double crossoverRate;

    public GeneticBinPacker(BinPackingProblem problem) {
        this(problem, 100, 50, 0.15, 0.7);
    }

    public GeneticBinPacker(BinPackingProblem problem, int populationSize,
            int maxGenerations, double mutationRate, double crossoverRate) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
    }

    /**
     * Run the genetic algorithm optimization
     */
    public OptimizationResult optimize() {
        System.out.println("========================================");
        System.out.println("  3D Bin Packing Genetic Algorithm");
        System.out.println("========================================");
        System.out.println("Population Size: " + populationSize);
        System.out.println("Max Generations: " + maxGenerations);
        System.out.println("Mutation Rate: " + (mutationRate * 100) + "%");
        System.out.println("Crossover Rate: " + (crossoverRate * 100) + "%");
        System.out.println("Bin Dimensions: " + problem.getBin().getWidth() + "x" +
                problem.getBin().getHeight() + "x" + problem.getBin().getDepth());
        System.out.println("Bin Total Volume: " + problem.getBin().getTotalVolume());
        System.out.println("Available Items: " + problem.getAvailableItems().size());
        System.out.println("========================================\n");

        // Build the GA engine
        Engine<EnumGene<String>, Double> engine = Engine
                .builder(problem::fitness, problem.genotypeFactory())
                .populationSize(populationSize)
                .maximizing() // We want to maximize fitness
                .alterers(
                        new PartiallyMatchedCrossover<>(crossoverRate),
                        new SwapMutator<>(mutationRate))
                .selector(new TournamentSelector<>(5))
                .build();

        // Track evolution statistics
        EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();

        // Run the evolution
        System.out.println("Starting evolution...\n");

        Phenotype<EnumGene<String>, Double> best = engine.stream()
                .limit(maxGenerations)
                .peek(result -> {
                    long gen = result.generation();
                    double bestFitness = result.bestFitness();
                    double avgFitness = result.population().stream()
                            .mapToDouble(p -> p.fitness())
                            .average()
                            .orElse(0.0);

                    if (gen % 10 == 0 || gen == 1) {
                        System.out.printf("Generation %3d: Best Fitness = %8.4f, Avg Fitness = %8.4f%n",
                                gen, bestFitness, avgFitness);
                    }
                })
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("\n========================================");
        System.out.println("  Optimization Complete!");
        System.out.println("========================================");
        System.out.printf("Best Fitness: %.4f%n", best.fitness());

        // Convert to solution
        PackingSolution solution = problem.convertToSolution(best.genotype());

        // Calculate detailed metrics
        int usedVolume = problem.getBin().getTotalVolume() - solution.getTotalWastage();
        double utilization = (double) usedVolume / problem.getBin().getTotalVolume() * 100.0;

        System.out.printf("Space Utilization: %.2f%%%n", utilization);
        System.out.printf("Used Volume: %d / %d%n", usedVolume, problem.getBin().getTotalVolume());
        System.out.printf("Wasted Volume: %d%n", solution.getTotalWastage());
        System.out.printf("Total Value: $%.2f%n", solution.getTotalCost());
        System.out.printf("Items Packed: %d%n", solution.getPlacements().size());
        System.out.println("========================================\n");

        return new OptimizationResult(best, solution, statistics);
    }

    /**
     * Result of the optimization
     */
    public static class OptimizationResult {
        private final Phenotype<EnumGene<String>, Double> bestPhenotype;
        private final PackingSolution solution;
        private final EvolutionStatistics<Double, ?> statistics;

        public OptimizationResult(Phenotype<EnumGene<String>, Double> bestPhenotype,
                PackingSolution solution,
                EvolutionStatistics<Double, ?> statistics) {
            this.bestPhenotype = bestPhenotype;
            this.solution = solution;
            this.statistics = statistics;
        }

        public Phenotype<EnumGene<String>, Double> getBestPhenotype() {
            return bestPhenotype;
        }

        public PackingSolution getSolution() {
            return solution;
        }

        public EvolutionStatistics<Double, ?> getStatistics() {
            return statistics;
        }
    }
}
