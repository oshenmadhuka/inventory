package com.ga.mutation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Generates graphs from the CSV data files produced by the simulation
 */
public class GraphGenerator {

    public static void main(String[] args) {
        try {
            // Generate individual graphs for each scenario
            generateScenarioGraph("baseline_low_mutation.csv", "Baseline: Constant Low Mutation",
                    "graph_baseline.png");
            generateScenarioGraph("scenario1_high_mutation_initial.csv",
                    "Scenario 1: High Mutation at Initial Population", "graph_scenario1.png");
            generateScenarioGraph("scenario2_high_mutation_later.csv",
                    "Scenario 2: High Mutation at Later Generations", "graph_scenario2.png");

            // Generate comparison graph
            generateComparisonGraph();

            System.out.println("All graphs generated successfully!");

        } catch (IOException e) {
            System.err.println("Error generating graphs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generate a graph for a single scenario
     */
    private static void generateScenarioGraph(String csvFile, String title, String outputFile)
            throws IOException {

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries bestSeries = new XYSeries("Best Fitness");
        XYSeries avgSeries = new XYSeries("Average Fitness");

        // Read CSV data
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int generation = Integer.parseInt(parts[0]);
                int bestFitness = Integer.parseInt(parts[1]);
                double avgFitness = Double.parseDouble(parts[2]);

                bestSeries.add(generation, bestFitness);
                avgSeries.add(generation, avgFitness);
            }
        }

        dataset.addSeries(bestSeries);
        dataset.addSeries(avgSeries);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Generation",
                "Fitness (Number of 1s)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        // Customize chart
        customizeChart(chart);

        // Save to file
        ChartUtils.saveChartAsPNG(new File(outputFile), chart, 800, 600);
        System.out.println("Generated: " + outputFile);
    }

    /**
     * Generate a comparison graph showing all scenarios
     */
    private static void generateComparisonGraph() throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();

        // Load data from all three scenarios
        loadSeriesFromCSV(dataset, "baseline_low_mutation.csv", "Baseline (Low Mutation)");
        loadSeriesFromCSV(dataset, "scenario1_high_mutation_initial.csv",
                "High Mutation Initial (Best)");
        loadSeriesFromCSV(dataset, "scenario2_high_mutation_later.csv",
                "High Mutation Later (Best)");

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Comparison of Mutation Rate Strategies",
                "Generation",
                "Best Fitness (Number of 1s)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        // Customize chart
        customizeChart(chart);

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // Customize line colors
        renderer.setSeriesPaint(0, new Color(0, 100, 200)); // Blue for baseline
        renderer.setSeriesPaint(1, new Color(0, 150, 0)); // Green for scenario 1
        renderer.setSeriesPaint(2, new Color(200, 50, 50)); // Red for scenario 2

        // Set line thickness
        for (int i = 0; i < 3; i++) {
            renderer.setSeriesStroke(i, new BasicStroke(2.5f));
            renderer.setSeriesShapesVisible(i, false);
        }

        plot.setRenderer(renderer);

        // Save to file
        ChartUtils.saveChartAsPNG(new File("graph_comparison.png"), chart, 1000, 700);
        System.out.println("Generated: graph_comparison.png");
    }

    /**
     * Load best fitness series from CSV file
     */
    private static void loadSeriesFromCSV(XYSeriesCollection dataset, String csvFile, String seriesName)
            throws IOException {

        XYSeries series = new XYSeries(seriesName);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int generation = Integer.parseInt(parts[0]);
                int bestFitness = Integer.parseInt(parts[1]);

                series.add(generation, bestFitness);
            }
        }

        dataset.addSeries(series);
    }

    /**
     * Apply common customizations to charts
     */
    private static void customizeChart(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        renderer.setSeriesStroke(1, new BasicStroke(2.5f));
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);

        plot.setRenderer(renderer);
    }
}
