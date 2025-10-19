package com.ga.binpacking.chromosome;

/**
 * Marker class for 3D bin packing chromosome representation
 * 
 * In this implementation, we use Jenetics' built-in PermutationChromosome
 * directly instead of extending it (since it's final).
 * 
 * The chromosome represents a sequence of items to be packed, where:
 * - Each gene contains an item ID (String)
 * - The order determines the packing sequence
 * - First-fit algorithm places items in this order
 */
public class PackingChromosome {

    /**
     * This class serves as documentation for the chromosome design.
     * The actual chromosome is created using:
     * PermutationChromosome.of(ISeq<String> itemIds)
     * 
     * See BinPackingProblem.genotypeFactory() for implementation.
     */

    private PackingChromosome() {
        // Utility class - not meant to be instantiated
    }
}
