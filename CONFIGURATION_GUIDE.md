# Configuration Guide

## 3D Bin Packing Optimization System - Configuration Reference

**Student ID:** 215526U  
**Assignment:** Evolutionary Computing - 3D Bin Packing Problem

---

## 📋 Overview

This system provides **two ways** to manage configuration:

1. **Java Configuration Class** (`SystemConfig.java`) - Recommended
2. **Properties File** (`config.properties`) - Alternative

---

## 🎯 Quick Start

### Using SystemConfig (Recommended)

```java
import com.ga.binpacking.config.SystemConfig;
import com.ga.binpacking.model.*;

// Display all configuration
SystemConfig.displayConfiguration();

// Create objects using factory methods
Bin bin = SystemConfig.createBin();
List<Item> items = SystemConfig.createItems();

// Access individual parameters
int populationSize = SystemConfig.GA_POPULATION_SIZE;
double mutationRate = SystemConfig.GA_MUTATION_RATE;
```

### Running the Demo

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.ga.binpacking.ConfigDemo"
```

---

## 📁 Configuration Files

### 1. SystemConfig.java

**Location:** `src/main/java/com/ga/binpacking/config/SystemConfig.java`

**Type:** Java constants class

**Benefits:**

- ✅ Type-safe
- ✅ Compile-time checking
- ✅ IDE autocomplete
- ✅ No parsing needed
- ✅ Built-in validation

**Usage Example:**

```java
// In BinPackingMain.java
int binWidth = SystemConfig.BIN_WIDTH;
int binHeight = SystemConfig.BIN_HEIGHT;
int binDepth = SystemConfig.BIN_DEPTH;

Bin inventoryBin = new Bin(binWidth, binHeight, binDepth);
// Or simply:
Bin inventoryBin = SystemConfig.createBin();
```

### 2. config.properties

**Location:** `config.properties` (project root)

**Type:** Standard Java properties file

**Benefits:**

- ✅ External configuration
- ✅ No recompilation needed
- ✅ Easy to edit
- ✅ Can be environment-specific

**Usage Example:**

```java
Properties config = new Properties();
config.load(new FileInputStream("config.properties"));

int binWidth = Integer.parseInt(config.getProperty("bin.width"));
double mutationRate = Double.parseDouble(config.getProperty("ga.standard.mutation.rate"));
```

---

## ⚙️ Configuration Parameters

### 🏗️ Bin Configuration

| Parameter          | Value   | Description                   |
| ------------------ | ------- | ----------------------------- |
| `BIN_WIDTH`        | 100     | Bin width in units            |
| `BIN_HEIGHT`       | 80      | Bin height in units           |
| `BIN_DEPTH`        | 100     | Bin depth in units            |
| `BIN_TOTAL_VOLUME` | 800,000 | Total bin volume (calculated) |

**Factory Method:**

```java
Bin bin = SystemConfig.createBin();
```

---

### 📦 Item Configuration

#### Item A - Small Cubic Item

```java
SystemConfig.ITEM_A_ID = "A"
SystemConfig.ITEM_A_WIDTH = 10
SystemConfig.ITEM_A_HEIGHT = 10
SystemConfig.ITEM_A_DEPTH = 10
SystemConfig.ITEM_A_QUANTITY = 150
SystemConfig.ITEM_A_COST = 150.0
```

#### Item B - Medium Rectangular Item

```java
SystemConfig.ITEM_B_ID = "B"
SystemConfig.ITEM_B_WIDTH = 20
SystemConfig.ITEM_B_HEIGHT = 15
SystemConfig.ITEM_B_DEPTH = 12
SystemConfig.ITEM_B_QUANTITY = 70
SystemConfig.ITEM_B_COST = 70.0
```

#### Item C - Small Flat Item

```java
SystemConfig.ITEM_C_ID = "C"
SystemConfig.ITEM_C_WIDTH = 5
SystemConfig.ITEM_C_HEIGHT = 8
SystemConfig.ITEM_C_DEPTH = 6
SystemConfig.ITEM_C_QUANTITY = 60
SystemConfig.ITEM_C_COST = 60.0
```

#### Item D - Medium Item (Highest Quantity)

```java
SystemConfig.ITEM_D_ID = "D"
SystemConfig.ITEM_D_WIDTH = 10
SystemConfig.ITEM_D_HEIGHT = 12
SystemConfig.ITEM_D_DEPTH = 10
SystemConfig.ITEM_D_QUANTITY = 300
SystemConfig.ITEM_D_COST = 300.0
```

**Factory Method:**

```java
List<Item> items = SystemConfig.createItems();
```

---

### 🧬 Genetic Algorithm - Standard Version

| Parameter            | Value      | Description                          |
| -------------------- | ---------- | ------------------------------------ |
| `GA_POPULATION_SIZE` | 150        | Number of individuals per generation |
| `GA_MAX_GENERATIONS` | 100        | Maximum evolution cycles             |
| `GA_MUTATION_RATE`   | 0.15 (15%) | Probability of mutation              |
| `GA_CROSSOVER_RATE`  | 0.7 (70%)  | Probability of crossover             |
| `GA_TOURNAMENT_SIZE` | 5          | Tournament selection size            |

**Operators:**

- **Crossover:** Partially Matched Crossover (PMX)
- **Mutation:** Swap Mutation
- **Selection:** Tournament Selection
- **Chromosome:** EnumGene<String> (Permutation-based)

**Usage Example:**

```java
GeneticBinPacker optimizer = new GeneticBinPacker(
    problem,
    SystemConfig.GA_POPULATION_SIZE,
    SystemConfig.GA_MAX_GENERATIONS,
    SystemConfig.GA_MUTATION_RATE,
    SystemConfig.GA_CROSSOVER_RATE
);
```

---

### 🧬 Genetic Algorithm - Improved Version

| Parameter                     | Value      | Description                          |
| ----------------------------- | ---------- | ------------------------------------ |
| `GA_IMPROVED_POPULATION_SIZE` | 150        | Number of individuals per generation |
| `GA_IMPROVED_MAX_GENERATIONS` | 100        | Maximum evolution cycles             |
| `GA_IMPROVED_MUTATION_RATE`   | 0.20 (20%) | Higher for better diversity          |
| `GA_IMPROVED_CROSSOVER_RATE`  | 0.65 (65%) | Slightly lower crossover             |
| `GA_IMPROVED_TOURNAMENT_SIZE` | 5          | Tournament selection size            |

**Operators:**

- **Crossover:** Single Point Crossover
- **Mutation:** Standard Mutation
- **Selection:** Tournament Selection
- **Chromosome:** IntegerGene (Priority-based)

**Usage Example:**

```java
Engine<IntegerGene, Double> engine = Engine
    .builder(problem::fitness, problem.genotypeFactory())
    .populationSize(SystemConfig.GA_IMPROVED_POPULATION_SIZE)
    .maximizing()
    .alterers(
        new SinglePointCrossover<>(SystemConfig.GA_IMPROVED_CROSSOVER_RATE),
        new Mutator<>(SystemConfig.GA_IMPROVED_MUTATION_RATE)
    )
    .selector(new TournamentSelector<>(SystemConfig.GA_IMPROVED_TOURNAMENT_SIZE))
    .build();
```

---

### ⚖️ Fitness Function Weights

| Parameter                    | Value | Description                  |
| ---------------------------- | ----- | ---------------------------- |
| `FITNESS_UTILIZATION_WEIGHT` | 100.0 | Weight for space utilization |
| `FITNESS_VALUE_DIVISOR`      | 10.0  | Divisor for item value       |
| `FITNESS_WASTAGE_PENALTY`    | 50.0  | Penalty for wasted space     |

**Fitness Formula:**

```
Fitness = (usedVolume / totalVolume) × UTILIZATION_WEIGHT
        + (totalValue / VALUE_DIVISOR)
        - (wastedVolume / totalVolume) × WASTAGE_PENALTY
```

**Usage Example:**

```java
double utilization = (double) usedVolume / totalVolume;
double utilizationScore = utilization * SystemConfig.FITNESS_UTILIZATION_WEIGHT;
double valueScore = totalValue / SystemConfig.FITNESS_VALUE_DIVISOR;
double wastagePenalty = ((double) wastedVolume / totalVolume) * SystemConfig.FITNESS_WASTAGE_PENALTY;
double fitness = utilizationScore + valueScore - wastagePenalty;
```

---

### 🤖 Agent Configuration

| Parameter                   | Value              | Description                     |
| --------------------------- | ------------------ | ------------------------------- |
| `AGENT_NAME`                | "PackingBot-Alpha" | Default agent name              |
| `AGENT_EXCELLENT_THRESHOLD` | 80.0%              | Threshold for excellent packing |
| `AGENT_GOOD_THRESHOLD`      | 60.0%              | Threshold for good packing      |

**Usage Example:**

```java
PackingAgent agent = new PackingAgent(SystemConfig.AGENT_NAME);

if (utilization >= SystemConfig.AGENT_EXCELLENT_THRESHOLD) {
    System.out.println("✓ Excellent packing achieved!");
} else if (utilization >= SystemConfig.AGENT_GOOD_THRESHOLD) {
    System.out.println("→ Good packing with room for improvement");
}
```

---

### 📊 Display Configuration

| Parameter                   | Value | Description                       |
| --------------------------- | ----- | --------------------------------- |
| `DISPLAY_PROGRESS_INTERVAL` | 10    | Show progress every N generations |
| `DISPLAY_WIDTH`             | 60    | Console output width              |

---

### 🔬 Mutation Study Configuration

| Parameter                          | Value     | Description           |
| ---------------------------------- | --------- | --------------------- |
| `MUTATION_STUDY_CHROMOSOME_LENGTH` | 10        | Binary string length  |
| `MUTATION_STUDY_POPULATION_SIZE`   | 1000      | Study population size |
| `MUTATION_STUDY_GENERATIONS`       | 100       | Study generations     |
| `MUTATION_STUDY_LOW_RATE`          | 0.01 (1%) | Low mutation rate     |
| `MUTATION_STUDY_HIGH_RATE`         | 0.3 (30%) | High mutation rate    |

---

## 🔧 How to Modify Configuration

### Option 1: Modify SystemConfig.java (Recommended)

1. Open `src/main/java/com/ga/binpacking/config/SystemConfig.java`
2. Modify the constant values:

```java
// Change population size
public static final int GA_POPULATION_SIZE = 200; // Changed from 150

// Change mutation rate
public static final double GA_MUTATION_RATE = 0.20; // Changed from 0.15

// Change bin dimensions
public static final int BIN_WIDTH = 120; // Changed from 100
```

3. Recompile:

```bash
mvn clean compile
```

4. Run your program - all classes will use the new values!

### Option 2: Modify config.properties

1. Open `config.properties`
2. Modify the values:

```properties
ga.standard.population.size=200
ga.standard.mutation.rate=0.20
bin.width=120
```

3. Load in your code:

```java
Properties config = new Properties();
config.load(new FileInputStream("config.properties"));
int popSize = Integer.parseInt(config.getProperty("ga.standard.population.size"));
```

---

## 📝 Best Practices

### ✅ DO:

1. **Use SystemConfig for constants:**

   ```java
   int width = SystemConfig.BIN_WIDTH;
   ```

2. **Use factory methods:**

   ```java
   Bin bin = SystemConfig.createBin();
   List<Item> items = SystemConfig.createItems();
   ```

3. **Validate configuration:**

   ```java
   if (!SystemConfig.validateConfiguration()) {
       System.err.println("Invalid configuration!");
       return;
   }
   ```

4. **Display configuration on startup:**
   ```java
   SystemConfig.displayConfiguration();
   ```

### ❌ DON'T:

1. **Don't hardcode values:**

   ```java
   int binWidth = 100; // ❌ Bad
   int binWidth = SystemConfig.BIN_WIDTH; // ✅ Good
   ```

2. **Don't duplicate configuration:**

   ```java
   // ❌ Bad - Configuration in multiple files
   // BinPackingMain.java: int width = 100;
   // ImprovedMain.java: int width = 100;

   // ✅ Good - One source of truth
   int width = SystemConfig.BIN_WIDTH;
   ```

3. **Don't skip validation:**

   ```java
   // ❌ Bad - No validation
   new Bin(width, height, depth);

   // ✅ Good - Validate first
   if (SystemConfig.validateConfiguration()) {
       Bin bin = SystemConfig.createBin();
   }
   ```

---

## 🔍 Configuration Validation

The system includes built-in validation:

```java
boolean isValid = SystemConfig.validateConfiguration();
```

**Checks:**

- ✅ Bin dimensions are positive
- ✅ Population size is positive
- ✅ Generations count is positive
- ✅ Mutation rate is between 0 and 1
- ✅ Crossover rate is between 0 and 1
- ✅ Item quantities are non-negative

---

## 📖 Usage Examples

### Example 1: Standard GA with Configuration

```java
import com.ga.binpacking.config.SystemConfig;
import com.ga.binpacking.algorithm.*;
import com.ga.binpacking.model.*;

public class MyBinPacking {
    public static void main(String[] args) {
        // Validate configuration
        if (!SystemConfig.validateConfiguration()) {
            System.err.println("Configuration error!");
            return;
        }

        // Create objects using configuration
        Bin bin = SystemConfig.createBin();
        List<Item> items = SystemConfig.createItems();

        // Create problem
        BinPackingProblem problem = new BinPackingProblem(items, bin);

        // Create optimizer with configuration
        GeneticBinPacker optimizer = new GeneticBinPacker(
            problem,
            SystemConfig.GA_POPULATION_SIZE,
            SystemConfig.GA_MAX_GENERATIONS,
            SystemConfig.GA_MUTATION_RATE,
            SystemConfig.GA_CROSSOVER_RATE
        );

        // Run optimization
        GeneticBinPacker.OptimizationResult result = optimizer.optimize();
    }
}
```

### Example 2: Improved GA with Configuration

```java
import com.ga.binpacking.config.SystemConfig;

Engine<IntegerGene, Double> engine = Engine
    .builder(problem::fitness, problem.genotypeFactory())
    .populationSize(SystemConfig.GA_IMPROVED_POPULATION_SIZE)
    .maximizing()
    .alterers(
        new SinglePointCrossover<>(SystemConfig.GA_IMPROVED_CROSSOVER_RATE),
        new Mutator<>(SystemConfig.GA_IMPROVED_MUTATION_RATE)
    )
    .selector(new TournamentSelector<>(SystemConfig.GA_IMPROVED_TOURNAMENT_SIZE))
    .build();
```

### Example 3: Custom Item Configuration

```java
// Add a new item type
items.add(new Item(
    "E",                              // ID
    SystemConfig.ITEM_A_WIDTH * 2,   // Width (based on Item A)
    SystemConfig.ITEM_A_HEIGHT,      // Height
    SystemConfig.ITEM_A_DEPTH,       // Depth
    50,                               // Quantity
    200.0                             // Cost
));
```

---

## 🎨 Display Configuration Summary

```java
// Display full configuration
SystemConfig.displayConfiguration();

// Get configuration summary as string
String summary = SystemConfig.getConfigurationSummary();
System.out.println(summary);
```

**Output:**

```
============================================================
  SYSTEM CONFIGURATION
============================================================

📦 BIN CONFIGURATION:
  Dimensions: 100 x 80 x 100
  Total Volume: 800000 cubic units

📋 ITEMS CONFIGURATION:
  Item A: 10x10x10, Qty=150, Cost=$150.00
  Item B: 20x15x12, Qty=70, Cost=$70.00
  Item C: 5x8x6, Qty=60, Cost=$60.00
  Item D: 10x12x10, Qty=300, Cost=$300.00

🧬 GENETIC ALGORITHM (STANDARD):
  Population Size: 150
  Max Generations: 100
  Mutation Rate: 15.0%
  Crossover Rate: 70.0%
  ...
```

---

## 🚀 Quick Reference

### Common Parameters

```java
// Bin
SystemConfig.BIN_WIDTH
SystemConfig.BIN_HEIGHT
SystemConfig.BIN_DEPTH

// GA Standard
SystemConfig.GA_POPULATION_SIZE
SystemConfig.GA_MUTATION_RATE
SystemConfig.GA_CROSSOVER_RATE

// GA Improved
SystemConfig.GA_IMPROVED_MUTATION_RATE
SystemConfig.GA_IMPROVED_CROSSOVER_RATE

// Fitness
SystemConfig.FITNESS_UTILIZATION_WEIGHT
SystemConfig.FITNESS_WASTAGE_PENALTY

// Factory Methods
SystemConfig.createBin()
SystemConfig.createItems()
```

---

## 📚 Additional Resources

- **Main README:** [README.md](README.md)
- **Architecture:** [ARCHITECTURE.md](ARCHITECTURE.md)
- **Project Summary:** [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)
- **Visualization Guide:** [VISUALIZATION_GUIDE.md](VISUALIZATION_GUIDE.md)

---

## 📞 Support

For questions or issues with configuration:

1. Check this guide
2. Review `SystemConfig.java` comments
3. Run `ConfigDemo.java` for examples
4. Check `config.properties` for all available parameters

---

**Last Updated:** 2025  
**Assignment:** Evolutionary Computing - 3D Bin Packing Problem  
**Student ID:** 215526U
