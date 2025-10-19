# Configuration Files Summary

## 3D Bin Packing Optimization System - Configuration Overview

**Student ID:** 215526U

---

## 📁 Available Configuration Files

Your system now has **4 configuration files** that contain all the settings:

| File                     | Type          | Location                                  | Use Case                                             |
| ------------------------ | ------------- | ----------------------------------------- | ---------------------------------------------------- |
| `SystemConfig.java`      | Java Class    | `src/main/java/com/ga/binpacking/config/` | ✅ **Recommended** - Type-safe, compile-time checked |
| `config.properties`      | Properties    | Project root                              | External configuration, no recompilation             |
| `config.json`            | JSON          | Project root                              | Modern format, easy parsing, API-friendly            |
| `CONFIGURATION_GUIDE.md` | Documentation | Project root                              | Complete guide with examples                         |

---

## 🎯 Quick Start

### Option 1: Use SystemConfig.java (Recommended)

```java
import com.ga.binpacking.config.SystemConfig;

// Display all configuration
SystemConfig.displayConfiguration();

// Use factory methods
Bin bin = SystemConfig.createBin();
List<Item> items = SystemConfig.createItems();

// Use constants
int popSize = SystemConfig.GA_POPULATION_SIZE;
double mutationRate = SystemConfig.GA_MUTATION_RATE;
```

**Run the demo:**

```bash
mvn exec:java -Dexec.mainClass="com.ga.binpacking.ConfigDemo"
```

### Option 2: Use config.properties

```java
Properties config = new Properties();
config.load(new FileInputStream("config.properties"));

int binWidth = Integer.parseInt(config.getProperty("bin.width"));
```

### Option 3: Use config.json

```java
Gson gson = new Gson();
JsonObject config = gson.fromJson(
    new FileReader("config.json"),
    JsonObject.class
);

int binWidth = config.getAsJsonObject("bin").get("width").getAsInt();
```

---

## 📦 What's Configured?

### 1. Bin Dimensions

- Width: 100
- Height: 80
- Depth: 100
- Total Volume: 800,000 cubic units

### 2. Items (4 Types)

| Item | Dimensions | Quantity | Cost |
| ---- | ---------- | -------- | ---- |
| A    | 10×10×10   | 150      | $150 |
| B    | 20×15×12   | 70       | $70  |
| C    | 5×8×6      | 60       | $60  |
| D    | 10×12×10   | 300      | $300 |

### 3. Genetic Algorithm - Standard

- Population: 150
- Generations: 100
- Mutation: 15%
- Crossover: 70%
- Selection: Tournament (size 5)

### 4. Genetic Algorithm - Improved

- Population: 150
- Generations: 100
- Mutation: 20% (higher for diversity)
- Crossover: 65%
- Selection: Tournament (size 5)

### 5. Fitness Function Weights

- Utilization Weight: 100.0
- Value Divisor: 10.0
- Wastage Penalty: 50.0

### 6. Other Settings

- Agent name and thresholds
- Display intervals
- Mutation study parameters
- Visualization settings
- Performance tuning

---

## 📊 File Comparison

| Feature     | SystemConfig.java | config.properties   | config.json     |
| ----------- | ----------------- | ------------------- | --------------- |
| Type Safety | ✅ Yes            | ❌ No               | ❌ No           |
| IDE Support | ✅ Autocomplete   | ⚠️ Limited          | ⚠️ Limited      |
| Recompile?  | ✅ Required       | ❌ Not needed       | ❌ Not needed   |
| Validation  | ✅ Built-in       | ⚠️ Manual           | ⚠️ Manual       |
| Comments    | ✅ Javadoc        | ✅ Yes              | ⚠️ Limited      |
| Parsing     | ⚡ None needed    | 🔧 Properties class | 🔧 Gson/Jackson |
| Best For    | Development       | Deployment          | APIs/Web        |

---

## 🔧 How to Change Configuration

### Method 1: Modify SystemConfig.java

**When to use:** Development, compile-time settings

**Steps:**

1. Edit `src/main/java/com/ga/binpacking/config/SystemConfig.java`
2. Change the constants (e.g., `GA_POPULATION_SIZE = 200`)
3. Recompile: `mvn clean compile`
4. All classes automatically use new values

**Example:**

```java
// Before
public static final int GA_POPULATION_SIZE = 150;

// After
public static final int GA_POPULATION_SIZE = 200;
```

### Method 2: Modify config.properties

**When to use:** Runtime configuration, production deployments

**Steps:**

1. Edit `config.properties`
2. Change the value (e.g., `ga.standard.population.size=200`)
3. No recompilation needed
4. Load in code using Properties class

**Example:**

```properties
# Before
ga.standard.population.size=150

# After
ga.standard.population.size=200
```

### Method 3: Modify config.json

**When to use:** Modern apps, web APIs, configuration management

**Steps:**

1. Edit `config.json`
2. Change the value in JSON structure
3. No recompilation needed
4. Parse with Gson or Jackson

**Example:**

```json
{
  "geneticAlgorithm": {
    "standard": {
      "populationSize": 200
    }
  }
}
```

---

## 📖 Usage Examples

### Example 1: Using SystemConfig in Your Code

```java
import com.ga.binpacking.config.SystemConfig;
import com.ga.binpacking.algorithm.GeneticBinPacker;

public class MyProgram {
    public static void main(String[] args) {
        // Validate first
        if (!SystemConfig.validateConfiguration()) {
            System.err.println("Invalid configuration!");
            return;
        }

        // Create objects
        Bin bin = SystemConfig.createBin();
        List<Item> items = SystemConfig.createItems();

        // Use GA parameters
        GeneticBinPacker optimizer = new GeneticBinPacker(
            problem,
            SystemConfig.GA_POPULATION_SIZE,
            SystemConfig.GA_MAX_GENERATIONS,
            SystemConfig.GA_MUTATION_RATE,
            SystemConfig.GA_CROSSOVER_RATE
        );
    }
}
```

### Example 2: Loading config.properties

```java
import java.util.Properties;
import java.io.FileInputStream;

Properties config = new Properties();
config.load(new FileInputStream("config.properties"));

// Get values
int binWidth = Integer.parseInt(config.getProperty("bin.width"));
int binHeight = Integer.parseInt(config.getProperty("bin.height"));
double mutationRate = Double.parseDouble(
    config.getProperty("ga.standard.mutation.rate")
);

// Use values
Bin bin = new Bin(binWidth, binHeight, binDepth);
```

### Example 3: Parsing config.json

```java
import com.google.gson.*;
import java.io.FileReader;

// Parse JSON
Gson gson = new Gson();
JsonObject config = gson.fromJson(
    new FileReader("config.json"),
    JsonObject.class
);

// Get bin configuration
JsonObject binConfig = config.getAsJsonObject("bin");
int width = binConfig.get("width").getAsInt();
int height = binConfig.get("height").getAsInt();

// Get GA configuration
JsonObject gaConfig = config
    .getAsJsonObject("geneticAlgorithm")
    .getAsJsonObject("standard");
int popSize = gaConfig.get("populationSize").getAsInt();
double mutRate = gaConfig.get("mutationRate").getAsDouble();
```

---

## ✅ Best Practices

### DO:

- ✅ Use `SystemConfig` for development (recommended)
- ✅ Use factory methods: `SystemConfig.createBin()`
- ✅ Validate configuration on startup
- ✅ Display configuration for transparency
- ✅ Keep all three files in sync if using multiple

### DON'T:

- ❌ Don't hardcode values in multiple places
- ❌ Don't skip validation
- ❌ Don't mix configuration sources without reason
- ❌ Don't forget to recompile after changing SystemConfig

---

## 🚀 Running Examples

### 1. Configuration Demo

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.ga.binpacking.ConfigDemo"
```

### 2. Standard GA (Using SystemConfig)

```bash
mvn exec:java -Dexec.mainClass="com.ga.binpacking.BinPackingMain"
```

### 3. Improved GA (Using SystemConfig)

```bash
mvn exec:java -Dexec.mainClass="com.ga.binpacking.ImprovedBinPackingMain"
```

### 4. Visual Demo

```bash
mvn exec:java -Dexec.mainClass="com.ga.binpacking.VisualBinPackingDemo"
```

---

## 📚 Documentation Files

All configuration documentation:

1. **CONFIGURATION_GUIDE.md** - Complete guide with detailed examples
2. **CONFIGURATION_FILES_SUMMARY.md** - This file (quick reference)
3. **SystemConfig.java** - Javadoc comments in the code
4. **config.properties** - Inline comments
5. **config.json** - Structured data with descriptions

---

## 🔍 Finding Configuration Values

### By Category:

**Bin Settings:**

- Java: `SystemConfig.BIN_WIDTH`, `BIN_HEIGHT`, `BIN_DEPTH`
- Properties: `bin.width`, `bin.height`, `bin.depth`
- JSON: `bin.width`, `bin.height`, `bin.depth`

**GA Settings:**

- Java: `SystemConfig.GA_POPULATION_SIZE`, `GA_MUTATION_RATE`
- Properties: `ga.standard.population.size`, `ga.standard.mutation.rate`
- JSON: `geneticAlgorithm.standard.populationSize`

**Item Settings:**

- Java: `SystemConfig.ITEM_A_WIDTH`, `ITEM_A_QUANTITY`
- Properties: `item.a.width`, `item.a.quantity`
- JSON: `items[0].dimensions.width`, `items[0].quantity`

---

## 📞 Need Help?

1. **Quick reference:** This file
2. **Detailed guide:** [CONFIGURATION_GUIDE.md](CONFIGURATION_GUIDE.md)
3. **Code examples:** `ConfigDemo.java`
4. **Architecture:** [ARCHITECTURE.md](ARCHITECTURE.md)
5. **Main docs:** [README.md](README.md)

---

## 🎯 Summary

You now have **3 configuration formats** + **complete documentation**:

| File                        | Purpose                                             |
| --------------------------- | --------------------------------------------------- |
| ✅ `SystemConfig.java`      | **Use this** for type-safe, validated configuration |
| 📝 `config.properties`      | Alternative for external configuration              |
| 🌐 `config.json`            | Alternative for modern/web applications             |
| 📖 `CONFIGURATION_GUIDE.md` | Complete documentation and examples                 |

**Recommendation:** Use `SystemConfig.java` for simplicity and type safety!

---

**Last Updated:** 2025  
**Assignment:** Evolutionary Computing - 3D Bin Packing Problem  
**Student ID:** 215526U
