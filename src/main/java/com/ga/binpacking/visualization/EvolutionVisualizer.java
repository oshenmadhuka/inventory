package com.ga.binpacking.visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Real-time visualization of genetic algorithm evolution
 * Shows fitness progress over generations
 */
public class EvolutionVisualizer extends JPanel {

    private final List<Double> bestFitnessHistory;
    private final List<Double> avgFitnessHistory;
    private final int maxGenerations;
    private double maxFitness;
    private double minFitness;

    public EvolutionVisualizer(int maxGenerations) {
        this.maxGenerations = maxGenerations;
        this.bestFitnessHistory = new ArrayList<>();
        this.avgFitnessHistory = new ArrayList<>();
        this.maxFitness = 0;
        this.minFitness = Double.MAX_VALUE;

        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.WHITE);
    }

    /**
     * Add generation data
     */
    public void addGeneration(double bestFitness, double avgFitness) {
        bestFitnessHistory.add(bestFitness);
        avgFitnessHistory.add(avgFitness);

        maxFitness = Math.max(maxFitness, Math.max(bestFitness, avgFitness));
        minFitness = Math.min(minFitness, Math.min(bestFitness, avgFitness));

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (bestFitnessHistory.isEmpty()) {
            return;
        }

        int padding = 60;
        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;

        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Genetic Algorithm Evolution Progress", 250, 30);

        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(padding, padding, padding, padding + height); // Y-axis
        g2d.drawLine(padding, padding + height, padding + width, padding + height); // X-axis

        // Draw axis labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Fitness", 10, padding + height / 2);
        g2d.drawString("Generation", padding + width / 2 - 30, padding + height + 40);

        // Draw grid
        g2d.setColor(new Color(230, 230, 230));
        g2d.setStroke(new BasicStroke(1));
        int numGridLines = 10;
        for (int i = 0; i <= numGridLines; i++) {
            int y = padding + (height * i / numGridLines);
            g2d.drawLine(padding, y, padding + width, y);

            int x = padding + (width * i / numGridLines);
            g2d.drawLine(x, padding, x, padding + height);
        }

        // Draw Y-axis values
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        double range = maxFitness - minFitness;
        if (range == 0)
            range = 1;

        for (int i = 0; i <= numGridLines; i++) {
            int y = padding + height - (height * i / numGridLines);
            double value = minFitness + (range * i / numGridLines);
            g2d.drawString(String.format("%.1f", value), padding - 45, y + 5);
        }

        // Draw X-axis values
        for (int i = 0; i <= numGridLines; i++) {
            int x = padding + (width * i / numGridLines);
            int generation = (maxGenerations * i / numGridLines);
            g2d.drawString(String.valueOf(generation), x - 10, padding + height + 20);
        }

        // Draw fitness curves
        if (bestFitnessHistory.size() > 1) {
            drawCurve(g2d, bestFitnessHistory, new Color(0, 150, 0), "Best Fitness",
                    padding, width, height, minFitness, range);
            drawCurve(g2d, avgFitnessHistory, new Color(200, 100, 0), "Avg Fitness",
                    padding, width, height, minFitness, range);
        }

        // Draw legend
        drawLegend(g2d, padding + width - 150, padding + 20);

        // Draw current generation info
        if (!bestFitnessHistory.isEmpty()) {
            int currentGen = bestFitnessHistory.size();
            double currentBest = bestFitnessHistory.get(bestFitnessHistory.size() - 1);
            double currentAvg = avgFitnessHistory.get(avgFitnessHistory.size() - 1);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(String.format("Generation: %d / %d", currentGen, maxGenerations),
                    padding, padding - 10);
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            g2d.drawString(String.format("Best: %.2f | Avg: %.2f | Diversity: %.2f",
                    currentBest, currentAvg, currentBest - currentAvg),
                    padding + 200, padding - 10);
        }
    }

    /**
     * Draw a fitness curve
     */
    private void drawCurve(Graphics2D g2d, List<Double> data, Color color, String label,
            int padding, int width, int height, double minFitness, double range) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2.5f));

        Path2D path = new Path2D.Double();
        boolean first = true;

        for (int i = 0; i < data.size(); i++) {
            double value = data.get(i);
            int x = padding + (int) ((double) i / (maxGenerations - 1) * width);
            int y = padding + height - (int) ((value - minFitness) / range * height);

            if (first) {
                path.moveTo(x, y);
                first = false;
            } else {
                path.lineTo(x, y);
            }
        }

        g2d.draw(path);
    }

    /**
     * Draw legend
     */
    private void drawLegend(Graphics2D g2d, int x, int y) {
        // Background
        g2d.setColor(new Color(240, 240, 240, 200));
        g2d.fillRoundRect(x - 10, y - 5, 145, 50, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x - 10, y - 5, 145, 50, 10, 10);

        // Best fitness
        g2d.setColor(new Color(0, 150, 0));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawLine(x, y + 5, x + 30, y + 5);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString("Best Fitness", x + 35, y + 10);

        // Avg fitness
        g2d.setColor(new Color(200, 100, 0));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawLine(x, y + 25, x + 30, y + 25);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Avg Fitness", x + 35, y + 30);
    }

    /**
     * Create and display evolution visualizer window
     */
    public static EvolutionVisualizer createWindow(int maxGenerations) {
        EvolutionVisualizer visualizer = new EvolutionVisualizer(maxGenerations);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Evolution Progress");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(visualizer);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        return visualizer;
    }
}
