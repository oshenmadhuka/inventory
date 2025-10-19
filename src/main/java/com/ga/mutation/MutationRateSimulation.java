package com.ga.mutation;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.Factory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Genetic Algorithm Simulation to study the effect of mutation rates
 * on binary string optimization.
 * 
 * Goal: Evolve a population of 10-bit binary strings to all 1s.
 * Fitness: Number of 1s in the string.
 */
public class MutationRateSimulation {

    private static final int CHROMOSOME_LENGTH = 10;
    private static final int POPULATION_SIZE = 1000;
    private static final int GENERATIONS = 100;

    // Mutation rates
    private static final double LOW_MUTATION_RATE = 0.01; // 1% mutation
    private static final double HIGH_MUTATION_RATE = 0.3; // 30% mutation

    /**
     * Fitness function: Count the number of 1s in the binary string
     */
    private static int fitness(Genotype<BitGene> genotype) {
        return (int) genotype.chromosome()
                .as(BitChromosome.class)
                .bitCount();
    }

    /**
     * Scenario 1: High mutation rate at initial population
     */
    public static void simulateHighMutationInitial() {
        System.out.println("=== Scenario 1: High Mutation Rate at Initial Population ===");
        System.out.println("Strategy: High mutation (30%) for first 20 generations, then low (1%)");

        List<GenerationData> data = new ArrayList<>();

        // Create the evolution engine with high mutation initially
        Factory<Genotype<BitGene>> genotypeFactory = Genotype.of(BitChromosome.of(CHROMOSOME_LENGTH, 0.5));

        Engine<BitGene, Integer> engine = Engine
                .builder(MutationRateSimulation::fitness, genotypeFactory)
                .populationSize(POPULATION_SIZE)
                .alterers(new Mutator<>(HIGH_MUTATION_RATE))
                .selector(new TournamentSelector<>(5))
                .build();

        Genotype<BitGene> result = null;

        engine.stream()
                .limit(GENERATIONS)
                .forEach(er -> {
                    int generation = (int) er.generation();

                    int bestFitness = er.bestFitness();
                    double avgFitness = er.population().stream()
                            .mapToInt(p -> fitness(p.genotype()))
                            .average()
                            .orElse(0.0);

                    data.add(new GenerationData(generation, bestFitness, avgFitness));

                    if (generation == 20) {
                        System.out.println(">>> Mutation rate switched to LOW (1%) at generation " + generation);
                    }

                    System.out.printf("Generation %d: Best = %d, Avg = %.2f%n",
                            generation, bestFitness, avgFitness);

                    // Early stopping if optimum found
                    if (bestFitness == CHROMOSOME_LENGTH) {
                        System.out.println("Optimum solution found!");
                    }
                });

        result = engine.stream().limit(1).findFirst().map(er -> er.bestPhenotype().genotype()).orElse(null);

        System.out.println("\nBest solution: " + result.chromosome());
        System.out.println("Fitness: " + fitness(result));

        // Save data to CSV
        saveDataToCSV(data, "scenario1_high_mutation_initial.csv");
    }

    /**
     * Scenario 2: High mutation rate at later generations (90-100)
     */
    public static void simulateHighMutationLater() {
        System.out.println("\n=== Scenario 2: High Mutation Rate at Later Generations ===");
        System.out.println("Strategy: Low mutation (1%) until gen 90, then high (30%)");

        List<GenerationData> data = new ArrayList<>();

        // Create the evolution engine with low mutation initially
        Factory<Genotype<BitGene>> genotypeFactory = Genotype.of(BitChromosome.of(CHROMOSOME_LENGTH, 0.5));

        Engine<BitGene, Integer> engine = Engine
                .builder(MutationRateSimulation::fitness, genotypeFactory)
                .populationSize(POPULATION_SIZE)
                .alterers(new Mutator<>(LOW_MUTATION_RATE))
                .selector(new TournamentSelector<>(5))
                .build();

        Genotype<BitGene> result = null;

        engine.stream()
                .limit(GENERATIONS)
                .forEach(er -> {
                    int generation = (int) er.generation();

                    int bestFitness = er.bestFitness();
                    double avgFitness = er.population().stream()
                            .mapToInt(p -> fitness(p.genotype()))
                            .average()
                            .orElse(0.0);

                    data.add(new GenerationData(generation, bestFitness, avgFitness));

                    if (generation == 90) {
                        System.out.println(">>> Mutation rate switched to HIGH (30%) at generation " + generation);
                    }

                    System.out.printf("Generation %d: Best = %d, Avg = %.2f%n",
                            generation, bestFitness, avgFitness);
                });

        result = engine.stream().limit(1).findFirst().map(er -> er.bestPhenotype().genotype()).orElse(null);

        System.out.println("\nBest solution: " + result.chromosome());
        System.out.println("Fitness: " + fitness(result));

        // Save data to CSV
        saveDataToCSV(data, "scenario2_high_mutation_later.csv");
    }

    /**
     * Baseline: Constant low mutation rate
     */
    public static void simulateBaselineLowMutation() {
        System.out.println("\n=== Baseline: Constant Low Mutation Rate ===");
        System.out.println("Strategy: Constant low mutation (1%) throughout");

        List<GenerationData> data = new ArrayList<>();

        Factory<Genotype<BitGene>> genotypeFactory = Genotype.of(BitChromosome.of(CHROMOSOME_LENGTH, 0.5));

        Engine<BitGene, Integer> engine = Engine
                .builder(MutationRateSimulation::fitness, genotypeFactory)
                .populationSize(POPULATION_SIZE)
                .alterers(new Mutator<>(LOW_MUTATION_RATE))
                .selector(new TournamentSelector<>(5))
                .build();

        Genotype<BitGene> result = null;

        engine.stream()
                .limit(GENERATIONS)
                .forEach(er -> {
                    int generation = (int) er.generation();

                    int bestFitness = er.bestFitness();
                    double avgFitness = er.population().stream()
                            .mapToInt(p -> fitness(p.genotype()))
                            .average()
                            .orElse(0.0);

                    data.add(new GenerationData(generation, bestFitness, avgFitness));

                    System.out.printf("Generation %d: Best = %d, Avg = %.2f%n",
                            generation, bestFitness, avgFitness);

                    if (bestFitness == CHROMOSOME_LENGTH) {
                        System.out.println("Optimum solution found!");
                    }
                });

        result = engine.stream().limit(1).findFirst().map(er -> er.bestPhenotype().genotype()).orElse(null);

        System.out.println("\nBest solution: " + result.chromosome());
        System.out.println("Fitness: " + fitness(result));

        saveDataToCSV(data, "baseline_low_mutation.csv");
    }

    /**
     * Save generation data to CSV file
     */
    private static void saveDataToCSV(List<GenerationData> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Generation,BestFitness,AverageFitness");
            for (GenerationData gd : data) {
                writer.printf("%d,%d,%.4f%n", gd.generation, gd.bestFitness, gd.avgFitness);
            }
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Data class to hold generation statistics
     */
    static class GenerationData {
        int generation;
        int bestFitness;
        double avgFitness;

        GenerationData(int generation, int bestFitness, double avgFitness) {
            this.generation = generation;
            this.bestFitness = bestFitness;
            this.avgFitness = avgFitness;
        }
    }

    public static void main(String[] args) {
        System.out.println("Mutation Rate Simulation - Binary String Optimization");
        System.out.println("======================================================");
        System.out.println("Chromosome Length: " + CHROMOSOME_LENGTH);
        System.out.println("Population Size: " + POPULATION_SIZE);
        System.out.println("Generations: " + GENERATIONS);
        System.out.println("Target: All 1s (1111111111)");
        System.out.println("Fitness: Number of 1s in binary string\n");

        // Run baseline
        simulateBaselineLowMutation();

        // Run Scenario 1: High mutation at initial population
        simulateHighMutationInitial();

        // Run Scenario 2: High mutation at later generations
        simulateHighMutationLater();

        System.out.println("\n=== Simulation Complete ===");
        System.out.println("CSV files generated. Run GraphGenerator to create plots.");
    }
}
