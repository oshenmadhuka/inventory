package com.ga.mutation;

import io.jenetics.BitGene;
import io.jenetics.Mutator;

public class AdaptiveMutator {

    public enum MutationStrategy {
        BASELINE, // Constant low mutation
        HIGH_INITIAL, // High then low
        HIGH_LATER // Low then high
    }

    /**
     * Create a mutator with the specified rate
     */
    public static Mutator<BitGene, Integer> create(double mutationRate) {
        return new Mutator<>(mutationRate);
    }

    /**
     * Get the appropriate mutation rate based on strategy and generation
     */
    public static double getMutationRate(MutationStrategy strategy, long generation) {
        switch (strategy) {
            case BASELINE:
                // Constant low mutation rate
                return 0.01;

            case HIGH_INITIAL:
                // High mutation for first 20 generations, then low
                return (generation < 20) ? 0.3 : 0.01;

            case HIGH_LATER:
                // Low mutation until generation 90, then high
                return (generation < 90) ? 0.01 : 0.3;

            default:
                return 0.01;
        }
    }
}
