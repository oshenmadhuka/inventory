package com.ga.binpacking.config;

import com.ga.binpacking.model.Bin;
import com.ga.binpacking.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralized Configuration for 3D Bin Packing Optimization System
 * 
 * This class contains all configuration parameters used throughout the system:
 * - Bin dimensions
 * - Item specifications
 * - Genetic Algorithm parameters
 * - Fitness function weights
 * - Selection and operator settings
 * 
 * Assignment: Evolutionary Computing - 3D Bin Packing Problem
 * Student ID: 215526U
 */
public class SystemConfig {

    // ========================================
    // BIN CONFIGURATION
    // ========================================

    /**
     * 3D Bin (Inventory Space) Dimensions
     */
    public static final int BIN_WIDTH = 100; // Width in units
    public static final int BIN_HEIGHT = 80; // Height in units
    public static final int BIN_DEPTH = 100; // Depth in units

    /**
     * Total Bin Volume (calculated)
     */
    public static final int BIN_TOTAL_VOLUME = BIN_WIDTH * BIN_HEIGHT * BIN_DEPTH; // 800,000 cubic units

    // ========================================
    // ITEM CONFIGURATION
    // ========================================

    /**
     * Item A Configuration
     * Small cubic item with high quantity
     */
    public static final String ITEM_A_ID = "A";
    public static final int ITEM_A_WIDTH = 10;
    public static final int ITEM_A_HEIGHT = 10;
    public static final int ITEM_A_DEPTH = 10;
    public static final int ITEM_A_QUANTITY = 150;
    public static final double ITEM_A_COST = 150.0;

    /**
     * Item B Configuration
     * Medium rectangular item
     */
    public static final String ITEM_B_ID = "B";
    public static final int ITEM_B_WIDTH = 20;
    public static final int ITEM_B_HEIGHT = 15;
    public static final int ITEM_B_DEPTH = 12;
    public static final int ITEM_B_QUANTITY = 70;
    public static final double ITEM_B_COST = 70.0;

    /**
     * Item C Configuration
     * Small flat item with lowest quantity
     */
    public static final String ITEM_C_ID = "C";
    public static final int ITEM_C_WIDTH = 5;
    public static final int ITEM_C_HEIGHT = 8;
    public static final int ITEM_C_DEPTH = 6;
    public static final int ITEM_C_QUANTITY = 60;
    public static final double ITEM_C_COST = 60.0;

    /**
     * Item D Configuration
     * Medium item with highest quantity and cost
     */
    public static final String ITEM_D_ID = "D";
    public static final int ITEM_D_WIDTH = 10;
    public static final int ITEM_D_HEIGHT = 12;
    public static final int ITEM_D_DEPTH = 10;
    public static final int ITEM_D_QUANTITY = 300;
    public static final double ITEM_D_COST = 300.0;

    // ========================================
    // GENETIC ALGORITHM - STANDARD VERSION
    // ========================================

    /**
     * Population Size for Standard GA
     * Number of individuals in each generation
     */
    public static final int GA_POPULATION_SIZE = 150;

    /**
     * Maximum Generations for Standard GA
     * Number of evolution cycles
     */
    public static final int GA_MAX_GENERATIONS = 100;

    /**
     * Mutation Rate for Standard GA
     * Probability of mutation (15%)
     */
    public static final double GA_MUTATION_RATE = 0.15;

    /**
     * Crossover Rate for Standard GA
     * Probability of crossover (70%)
     */
    public static final double GA_CROSSOVER_RATE = 0.7;

    /**
     * Tournament Selection Size
     * Number of individuals in tournament selection
     */
    public static final int GA_TOURNAMENT_SIZE = 5;

    // ========================================
    // GENETIC ALGORITHM - IMPROVED VERSION
    // ========================================

    /**
     * Population Size for Improved GA
     */
    public static final int GA_IMPROVED_POPULATION_SIZE = 150;

    /**
     * Maximum Generations for Improved GA
     */
    public static final int GA_IMPROVED_MAX_GENERATIONS = 100;

    /**
     * Mutation Rate for Improved GA
     * Higher mutation rate for better diversity (20%)
     */
    public static final double GA_IMPROVED_MUTATION_RATE = 0.20;

    /**
     * Crossover Rate for Improved GA
     * Slightly lower crossover rate (65%)
     */
    public static final double GA_IMPROVED_CROSSOVER_RATE = 0.65;

    /**
     * Tournament Selection Size for Improved GA
     */
    public static final int GA_IMPROVED_TOURNAMENT_SIZE = 5;

    // ========================================
    // FITNESS FUNCTION WEIGHTS
    // ========================================

    /**
     * Utilization Score Weight
     * Multiplier for space utilization in fitness calculation
     */
    public static final double FITNESS_UTILIZATION_WEIGHT = 100.0;

    /**
     * Value Score Divisor
     * Divisor for item value in fitness calculation
     */
    public static final double FITNESS_VALUE_DIVISOR = 10.0;

    /**
     * Wastage Penalty Weight
     * Multiplier for wastage penalty in fitness calculation
     */
    public static final double FITNESS_WASTAGE_PENALTY = 50.0;

    // ========================================
    // ALGORITHM OPERATORS
    // ========================================

    /**
     * Crossover Operator Type
     * Standard: Partially Matched Crossover (PMX)
     * Improved: Single Point Crossover
     */
    public static final String CROSSOVER_TYPE_STANDARD = "PartiallyMatchedCrossover";
    public static final String CROSSOVER_TYPE_IMPROVED = "SinglePointCrossover";

    /**
     * Mutation Operator Type
     * Standard: Swap Mutation
     * Improved: Standard Mutation
     */
    public static final String MUTATION_TYPE_STANDARD = "SwapMutator";
    public static final String MUTATION_TYPE_IMPROVED = "Mutator";

    /**
     * Selection Operator Type
     * Both versions use Tournament Selection
     */
    public static final String SELECTION_TYPE = "TournamentSelector";

    // ========================================
    // CHROMOSOME CONFIGURATION
    // ========================================

    /**
     * Chromosome Type for Standard Version
     * Uses EnumGene for permutation-based encoding
     */
    public static final String CHROMOSOME_TYPE_STANDARD = "EnumGene<String> (Permutation)";

    /**
     * Chromosome Type for Improved Version
     * Uses IntegerGene for priority-based encoding
     */
    public static final String CHROMOSOME_TYPE_IMPROVED = "IntegerGene (Priority-based)";

    // ========================================
    // AGENT CONFIGURATION
    // ========================================

    /**
     * Default Agent Name
     */
    public static final String AGENT_NAME = "PackingBot-Alpha";

    /**
     * Agent Utilization Thresholds
     */
    public static final double AGENT_EXCELLENT_THRESHOLD = 80.0; // >= 80% utilization
    public static final double AGENT_GOOD_THRESHOLD = 60.0; // >= 60% utilization

    // ========================================
    // DISPLAY CONFIGURATION
    // ========================================

    /**
     * Progress Display Interval
     * Show progress every N generations
     */
    public static final int DISPLAY_PROGRESS_INTERVAL = 10;

    /**
     * Display Width for Console Output
     */
    public static final int DISPLAY_WIDTH = 60;

    // ========================================
    // MUTATION STUDY CONFIGURATION
    // ========================================

    /**
     * Binary String Length for Mutation Study
     */
    public static final int MUTATION_STUDY_CHROMOSOME_LENGTH = 10;

    /**
     * Population Size for Mutation Study
     */
    public static final int MUTATION_STUDY_POPULATION_SIZE = 1000;

    /**
     * Generations for Mutation Study
     */
    public static final int MUTATION_STUDY_GENERATIONS = 100;

    /**
     * Low Mutation Rate for Study (1%)
     */
    public static final double MUTATION_STUDY_LOW_RATE = 0.01;

    /**
     * High Mutation Rate for Study (30%)
     */
    public static final double MUTATION_STUDY_HIGH_RATE = 0.3;

    // ========================================
    // FACTORY METHODS
    // ========================================

    /**
     * Create a Bin instance with configured dimensions
     * 
     * @return Bin with system-configured dimensions
     */
    public static Bin createBin() {
        return new Bin(BIN_WIDTH, BIN_HEIGHT, BIN_DEPTH);
    }

    /**
     * Create a list of items based on assignment specifications
     * 
     * @return List of items with configured dimensions, quantities, and costs
     */
    public static List<Item> createItems() {
        List<Item> items = new ArrayList<>();

        items.add(new Item(
                ITEM_A_ID,
                ITEM_A_WIDTH,
                ITEM_A_HEIGHT,
                ITEM_A_DEPTH,
                ITEM_A_QUANTITY,
                ITEM_A_COST));

        items.add(new Item(
                ITEM_B_ID,
                ITEM_B_WIDTH,
                ITEM_B_HEIGHT,
                ITEM_B_DEPTH,
                ITEM_B_QUANTITY,
                ITEM_B_COST));

        items.add(new Item(
                ITEM_C_ID,
                ITEM_C_WIDTH,
                ITEM_C_HEIGHT,
                ITEM_C_DEPTH,
                ITEM_C_QUANTITY,
                ITEM_C_COST));

        items.add(new Item(
                ITEM_D_ID,
                ITEM_D_WIDTH,
                ITEM_D_HEIGHT,
                ITEM_D_DEPTH,
                ITEM_D_QUANTITY,
                ITEM_D_COST));

        return items;
    }

    // ========================================
    // CONFIGURATION DISPLAY
    // ========================================

    /**
     * Display all configuration parameters
     */
    public static void displayConfiguration() {
        System.out.println("\n" + "=".repeat(DISPLAY_WIDTH));
        System.out.println("  SYSTEM CONFIGURATION");
        System.out.println("=".repeat(DISPLAY_WIDTH));

        System.out.println("\n📦 BIN CONFIGURATION:");
        System.out.println("  Dimensions: " + BIN_WIDTH + " x " + BIN_HEIGHT + " x " + BIN_DEPTH);
        System.out.println("  Total Volume: " + BIN_TOTAL_VOLUME + " cubic units");

        System.out.println("\n📋 ITEMS CONFIGURATION:");
        System.out.printf("  Item A: %dx%dx%d, Qty=%d, Cost=$%.2f%n",
                ITEM_A_WIDTH, ITEM_A_HEIGHT, ITEM_A_DEPTH, ITEM_A_QUANTITY, ITEM_A_COST);
        System.out.printf("  Item B: %dx%dx%d, Qty=%d, Cost=$%.2f%n",
                ITEM_B_WIDTH, ITEM_B_HEIGHT, ITEM_B_DEPTH, ITEM_B_QUANTITY, ITEM_B_COST);
        System.out.printf("  Item C: %dx%dx%d, Qty=%d, Cost=$%.2f%n",
                ITEM_C_WIDTH, ITEM_C_HEIGHT, ITEM_C_DEPTH, ITEM_C_QUANTITY, ITEM_C_COST);
        System.out.printf("  Item D: %dx%dx%d, Qty=%d, Cost=$%.2f%n",
                ITEM_D_WIDTH, ITEM_D_HEIGHT, ITEM_D_DEPTH, ITEM_D_QUANTITY, ITEM_D_COST);

        System.out.println("\n🧬 GENETIC ALGORITHM (STANDARD):");
        System.out.println("  Population Size: " + GA_POPULATION_SIZE);
        System.out.println("  Max Generations: " + GA_MAX_GENERATIONS);
        System.out.println("  Mutation Rate: " + (GA_MUTATION_RATE * 100) + "%");
        System.out.println("  Crossover Rate: " + (GA_CROSSOVER_RATE * 100) + "%");
        System.out.println("  Tournament Size: " + GA_TOURNAMENT_SIZE);
        System.out.println("  Crossover Type: " + CROSSOVER_TYPE_STANDARD);
        System.out.println("  Mutation Type: " + MUTATION_TYPE_STANDARD);
        System.out.println("  Chromosome Type: " + CHROMOSOME_TYPE_STANDARD);

        System.out.println("\n🧬 GENETIC ALGORITHM (IMPROVED):");
        System.out.println("  Population Size: " + GA_IMPROVED_POPULATION_SIZE);
        System.out.println("  Max Generations: " + GA_IMPROVED_MAX_GENERATIONS);
        System.out.println("  Mutation Rate: " + (GA_IMPROVED_MUTATION_RATE * 100) + "%");
        System.out.println("  Crossover Rate: " + (GA_IMPROVED_CROSSOVER_RATE * 100) + "%");
        System.out.println("  Tournament Size: " + GA_IMPROVED_TOURNAMENT_SIZE);
        System.out.println("  Crossover Type: " + CROSSOVER_TYPE_IMPROVED);
        System.out.println("  Mutation Type: " + MUTATION_TYPE_IMPROVED);
        System.out.println("  Chromosome Type: " + CHROMOSOME_TYPE_IMPROVED);

        System.out.println("\n⚖️ FITNESS FUNCTION WEIGHTS:");
        System.out.println("  Utilization Weight: " + FITNESS_UTILIZATION_WEIGHT);
        System.out.println("  Value Divisor: " + FITNESS_VALUE_DIVISOR);
        System.out.println("  Wastage Penalty: " + FITNESS_WASTAGE_PENALTY);

        System.out.println("\n🤖 AGENT CONFIGURATION:");
        System.out.println("  Agent Name: " + AGENT_NAME);
        System.out.println("  Excellent Threshold: " + AGENT_EXCELLENT_THRESHOLD + "%");
        System.out.println("  Good Threshold: " + AGENT_GOOD_THRESHOLD + "%");

        System.out.println("\n" + "=".repeat(DISPLAY_WIDTH) + "\n");
    }

    /**
     * Get configuration summary as a formatted string
     * 
     * @return Configuration summary
     */
    public static String getConfigurationSummary() {
        StringBuilder sb = new StringBuilder();

        sb.append("System Configuration Summary:\n");
        sb.append("- Bin: ").append(BIN_WIDTH).append("x").append(BIN_HEIGHT)
                .append("x").append(BIN_DEPTH).append(" (").append(BIN_TOTAL_VOLUME).append(" cubic units)\n");
        sb.append("- Items: 4 types (A, B, C, D) with total ")
                .append(ITEM_A_QUANTITY + ITEM_B_QUANTITY + ITEM_C_QUANTITY + ITEM_D_QUANTITY).append(" items\n");
        sb.append("- GA Population: ").append(GA_POPULATION_SIZE).append(" individuals\n");
        sb.append("- GA Generations: ").append(GA_MAX_GENERATIONS).append("\n");
        sb.append("- Mutation Rate: ").append(GA_MUTATION_RATE * 100).append("%\n");
        sb.append("- Crossover Rate: ").append(GA_CROSSOVER_RATE * 100).append("%\n");

        return sb.toString();
    }

    // ========================================
    // VALIDATION
    // ========================================

    /**
     * Validate that all configurations are within acceptable ranges
     * 
     * @return true if configuration is valid, false otherwise
     */
    @SuppressWarnings("all")
    public static boolean validateConfiguration() {
        // Validate bin dimensions
        if (BIN_WIDTH <= 0 || BIN_HEIGHT <= 0 || BIN_DEPTH <= 0) {
            System.err.println("ERROR: Bin dimensions must be positive");
            return false;
        }

        // Validate GA parameters
        if (GA_POPULATION_SIZE <= 0) {
            System.err.println("ERROR: Population size must be positive");
            return false;
        }

        if (GA_MAX_GENERATIONS <= 0) {
            System.err.println("ERROR: Max generations must be positive");
            return false;
        }

        if (GA_MUTATION_RATE < 0 || GA_MUTATION_RATE > 1) {
            System.err.println("ERROR: Mutation rate must be between 0 and 1");
            return false;
        }

        if (GA_CROSSOVER_RATE < 0 || GA_CROSSOVER_RATE > 1) {
            System.err.println("ERROR: Crossover rate must be between 0 and 1");
            return false;
        }

        // Validate items
        if (ITEM_A_QUANTITY < 0 || ITEM_B_QUANTITY < 0 || ITEM_C_QUANTITY < 0 || ITEM_D_QUANTITY < 0) {
            System.err.println("ERROR: Item quantities must be non-negative");
            return false;
        }

        return true;
    }

    /**
     * Private constructor to prevent instantiation
     * This is a utility class with only static members
     */
    private SystemConfig() {
        throw new AssertionError("SystemConfig is a utility class and should not be instantiated");
    }
}
