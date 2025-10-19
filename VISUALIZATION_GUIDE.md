# Visual Simulation Guide

## 3D Bin Packing Visualization System

This system provides **real-time visual feedback** during optimization and **3D visualization** of the final packing solution.

---

## 🎨 **Features**

### 1. **Evolution Visualizer** 📈

- Real-time fitness progress graph
- Shows Best and Average fitness over generations
- Diversity tracking
- Grid and axis labels
- Color-coded curves

### 2. **3D Bin Visualizer** 📦

Shows three orthogonal views:

- **Top View (X-Z plane):** Bird's eye view
- **Front View (X-Y plane):** Front perspective
- **Side View (Z-Y plane):** Side perspective

Features:

- Color-coded items
- Grid overlay
- Dimension labels
- Statistics panel
- Legend

---

## 🚀 **How to Run**

### **Option 1: Run Visual Demo**

```bash
# Update main class in pom.xml (already done if you followed previous steps)
mvn clean compile
mvn exec:java -Dexec.mainClass="com.ga.binpacking.VisualBinPackingDemo"
```

### **Option 2: Quick Command**

```bash
# Compile
mvn compile

# Run
java -cp target/classes com.ga.binpacking.VisualBinPackingDemo
```

### **Option 3: From JAR**

```bash
mvn package
java -cp target/binpacking-genetics-1.0-SNAPSHOT.jar com.ga.binpacking.VisualBinPackingDemo
```

---

## 📊 **What You'll See**

### **Window 1: Evolution Progress**

```
┌────────────────────────────────────────┐
│  Genetic Algorithm Evolution Progress  │
├────────────────────────────────────────┤
│  Fitness                               │
│    ↑                                   │
│    │     ╱─────────────                │
│    │   ╱                               │
│    │ ╱                 ──Best Fitness │
│    ╱           ╱───────   ─Avg Fitness│
│    └─────────────────────→ Generation │
└────────────────────────────────────────┘
```

**Displays:**

- Best fitness (green line)
- Average fitness (orange line)
- Current generation
- Diversity metric
- Real-time updates!

---

### **Window 2: 3D Bin Visualization**

```
┌──────────────────────────────────────────────────┐
│         3D Bin Packing Visualization             │
├────────────┬────────────┬────────────────────────┤
│  Top View  │ Front View │     Side View          │
│  (X-Z)     │   (X-Y)    │      (Z-Y)             │
│            │            │                        │
│  ┌───────┐ │ ┌───────┐  │   ┌───────┐           │
│  │ A B C │ │ │ A B   │  │   │ A     │           │
│  │ D A B │ │ │ C D   │  │   │ D C   │           │
│  │ C D A │ │ │ A C   │  │   │ B A   │           │
│  └───────┘ │ └───────┘  │   └───────┘           │
│            │            │                        │
├────────────┴────────────┴────────────────────────┤
│ Legend:        │ Statistics:                     │
│ █ Item A       │ Bin Size: 100×80×100            │
│ █ Item B       │ Utilization: 78.45%             │
│ █ Item C       │ Items Packed: 87                │
│ █ Item D       │ Total Value: $1234.56           │
└────────────────┴─────────────────────────────────┘
```

---

## 🎯 **Color Scheme**

| Item | Color          | RGB            |
| ---- | -------------- | -------------- |
| A    | 🔴 Tomato Red  | (255, 99, 71)  |
| B    | 🔷 Turquoise   | (64, 224, 208) |
| C    | 🟡 Gold        | (255, 215, 0)  |
| D    | 🟣 Blue Violet | (138, 43, 226) |
| E    | 🟢 Lime Green  | (50, 205, 50)  |

---

## ⚙️ **Customization**

### **Change Visualization Speed**

Edit `VisualBinPackingDemo.java`:

```java
// Find this line:
Thread.sleep(50); // 50ms delay per generation

// Change to:
Thread.sleep(100); // Slower (100ms)
Thread.sleep(20);  // Faster (20ms)
Thread.sleep(0);   // No delay (instant)
```

### **Change Window Size**

Edit `BinVisualizer2D.java`:

```java
setPreferredSize(new Dimension(1200, 500)); // Width, Height
```

Edit `EvolutionVisualizer.java`:

```java
setPreferredSize(new Dimension(800, 400)); // Width, Height
```

### **Change Colors**

Edit the `generateItemColors()` method in `BinVisualizer2D.java`:

```java
colors.put("A", new Color(255, 99, 71));   // Your custom RGB
colors.put("B", new Color(64, 224, 208));
// etc.
```

---

## 🖥️ **System Requirements**

- ✅ Java 11 or higher
- ✅ Java Swing (built-in, no extra dependencies)
- ✅ Display with 1280×720 or higher resolution
- ✅ Graphics support (any modern OS)

**Platform Support:**

- ✅ Windows (tested)
- ✅ macOS
- ✅ Linux

---

## 📸 **Export Results**

### **Take Screenshots**

**Windows:**

- Press `Win + Shift + S` (Snipping Tool)
- Or `Win + PrtScn` (saves to Pictures)

**macOS:**

- Press `Cmd + Shift + 4` (select area)
- Or `Cmd + Shift + 3` (full screen)

**Linux:**

- Press `PrtScn` or use Flameshot/Spectacle

### **Save to File (Future Enhancement)**

To add screenshot save functionality, update `BinVisualizer2D.java`:

```java
// Add save button
JButton saveButton = new JButton("Save Image");
saveButton.addActionListener(e -> {
    BufferedImage image = new BufferedImage(
        getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();
    paint(g2d);
    g2d.dispose();

    try {
        ImageIO.write(image, "PNG", new File("bin_packing.png"));
        JOptionPane.showMessageDialog(this, "Saved to bin_packing.png");
    } catch (IOException ex) {
        ex.printStackTrace();
    }
});
```

---

## 🎓 **For Your Assignment**

### **What to Include:**

1. **Screenshots:**

   - Evolution progress graph
   - Final 3D bin visualization
   - Terminal output

2. **Explain in Report:**

   - "The system provides real-time visualization..."
   - "Three orthogonal views show complete packing..."
   - "Evolution progress demonstrates convergence..."

3. **Observations:**
   - Point out fitness improvement over generations
   - Highlight space utilization percentage
   - Discuss item distribution

---

## 🐛 **Troubleshooting**

### **Windows Not Appearing**

1. Check if Java GUI is supported:

```bash
java -version
```

2. Run with explicit display:

```bash
java -Djava.awt.headless=false -cp target/classes com.ga.binpacking.VisualBinPackingDemo
```

### **Slow Performance**

1. Reduce delay in visualization
2. Run with more memory:

```bash
mvn exec:java -Dexec.args="-Xmx4g"
```

### **Colors Not Showing**

- Ensure your terminal/display supports color
- Try different color scheme (edit RGB values)

---

## 🚀 **Advanced: Add 3D Rotation**

For a rotating 3D view (future enhancement), consider:

1. **JavaFX 3D:**

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>17.0.2</version>
</dependency>
```

2. **LWJGL (OpenGL):**

```xml
<dependency>
    <groupId>org.lwjgl</groupId>
    <artifactId>lwjgl</artifactId>
    <version>3.3.1</version>
</dependency>
```

---

## 📊 **Interpreting the Visualization**

### **Evolution Graph:**

**Good Signs:**

- ✅ Steady upward trend (improving)
- ✅ Best > Average (diversity exists)
- ✅ Plateauing near end (convergence)

**Bad Signs:**

- ❌ No improvement after many generations
- ❌ Best = Average (no diversity)
- ❌ Erratic jumps (unstable)

### **3D Bin Visualization:**

**Good Packing:**

- ✅ Minimal white space (gaps)
- ✅ Items clustered efficiently
- ✅ High utilization percentage (>70%)

**Poor Packing:**

- ❌ Many gaps visible
- ❌ Items scattered
- ❌ Low utilization (<50%)

---

## 💡 **Tips for Best Results**

1. **Run Multiple Times:**

   - GA is stochastic (random)
   - Different runs = different results
   - Pick the best result!

2. **Adjust Parameters:**

   - Population: 100-200
   - Generations: 50-150
   - Mutation: 15-25%

3. **Screenshot at Right Time:**
   - Wait for full convergence
   - Capture both windows
   - Include console output

---

## 🎉 **You're Ready!**

Run the visual demo and watch your genetic algorithm in action:

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.ga.binpacking.VisualBinPackingDemo"
```

**Enjoy the show!** 🚀📊🎨

---

_Visual Simulation System - 3D Bin Packing Optimization_  
_Student: 215526U | Course: Evolutionary Computing_
