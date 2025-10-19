package com.ga.binpacking.visualization;

import com.ga.binpacking.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple 2D Visualization of bin packing using Java Swing
 * Shows a single 2D view of the packing solution
 */
public class BinVisualizer2D extends JPanel {

    private final Bin bin;
    private final PackingSolution solution;
    private final Map<String, Color> itemColors;
    private final int scale;

    public BinVisualizer2D(Bin bin, PackingSolution solution) {
        this.bin = bin;
        this.solution = solution;
        this.itemColors = generateItemColors();
        this.scale = calculateScale();

        setPreferredSize(new Dimension(900, 700));
        setBackground(Color.WHITE);
    }

    /**
     * Calculate appropriate scale for visualization
     */
    private int calculateScale() {
        int maxDim = (int) Math.max(bin.getWidth(), bin.getHeight());
        return Math.max(1, 500 / maxDim);
    }

    /**
     * Generate distinct colors for each item type
     */
    private Map<String, Color> generateItemColors() {
        Map<String, Color> colors = new HashMap<>();
        colors.put("A", new Color(255, 99, 71)); // Tomato Red
        colors.put("B", new Color(64, 224, 208)); // Turquoise
        colors.put("C", new Color(255, 215, 0)); // Gold
        colors.put("D", new Color(138, 43, 226)); // Blue Violet
        colors.put("E", new Color(50, 205, 50)); // Lime Green
        return colors;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.drawString("2D Bin Packing Visualization", 280, 35);

        // Draw main 2D view
        draw2DView(g2d, 150, 80);

        // Draw legend
        drawLegend(g2d, 50, 580);

        // Draw statistics
        drawStatistics(g2d, 450, 580);
    }

    /**
     * Draw single 2D view of the bin packing
     */
    private void draw2DView(Graphics2D g2d, int offsetX, int offsetY) {
        int width = (int) (bin.getWidth() * scale);
        int height = (int) (bin.getHeight() * scale);

        // Draw bin background with shadow
        g2d.setColor(new Color(180, 180, 180));
        g2d.fillRect(offsetX + 5, offsetY + 5, width, height);

        g2d.setColor(new Color(245, 245, 250));
        g2d.fillRect(offsetX, offsetY, width, height);

        // Draw grid
        g2d.setColor(new Color(220, 220, 220));
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i <= bin.getWidth(); i += 10) {
            g2d.drawLine(offsetX + i * scale, offsetY, offsetX + i * scale, offsetY + height);
        }
        for (int i = 0; i <= bin.getHeight(); i += 10) {
            g2d.drawLine(offsetX, offsetY + i * scale, offsetX + width, offsetY + i * scale);
        }

        // Draw bin outline
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(offsetX, offsetY, width, height);

        // Draw items
        for (PackingSolution.ItemPlacement placement : solution.getPlacements()) {
            String itemId = placement.getItemId();
            Object posObj = placement.getPosition();
            if (!(posObj instanceof Position3D))
                continue;
            Position3D pos = (Position3D) posObj;
            Color color = itemColors.getOrDefault(itemId, Color.GRAY);

            // Map 3D position to 2D (use X and Y coordinates)
            int x = offsetX + pos.getX() * scale;
            int y = offsetY + (int) ((bin.getHeight() - pos.getY() - 10) * scale); // Flip Y axis

            int itemWidth = 10 * scale;
            int itemHeight = 10 * scale;

            // Draw item with gradient effect
            GradientPaint gradient = new GradientPaint(
                    x, y, color,
                    x + itemWidth, y + itemHeight, color.darker());
            g2d.setPaint(gradient);
            g2d.fillRect(x, y, itemWidth, itemHeight);

            // Draw item border
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, itemWidth, itemHeight);

            // Draw item label
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(itemId);
            int labelHeight = fm.getHeight();
            g2d.setColor(Color.WHITE);
            g2d.drawString(itemId, x + (itemWidth - labelWidth) / 2, y + (itemHeight + labelHeight / 2) / 2);
        }

        // Draw dimension labels
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        // Width label (X-axis)
        g2d.drawString("Width: " + bin.getWidth() + " units", offsetX + width / 2 - 50, offsetY + height + 30);
        g2d.drawLine(offsetX, offsetY + height + 15, offsetX + width, offsetY + height + 15);
        g2d.drawLine(offsetX, offsetY + height + 12, offsetX, offsetY + height + 18);
        g2d.drawLine(offsetX + width, offsetY + height + 12, offsetX + width, offsetY + height + 18);

        // Height label (Y-axis)
        g2d.rotate(-Math.PI / 2, offsetX - 30, offsetY + height / 2);
        g2d.drawString("Height: " + bin.getHeight() + " units", offsetX - 30, offsetY + height / 2);
        g2d.rotate(Math.PI / 2, offsetX - 30, offsetY + height / 2);
        g2d.drawLine(offsetX - 15, offsetY, offsetX - 15, offsetY + height);
        g2d.drawLine(offsetX - 18, offsetY, offsetX - 12, offsetY);
        g2d.drawLine(offsetX - 18, offsetY + height, offsetX - 12, offsetY + height);
    }

    /**
     * Draw legend showing item colors
     */
    private void drawLegend(Graphics2D g2d, int offsetX, int offsetY) {
        // Calculate legend size based on number of items
        int legendHeight = 40 + (itemColors.size() * 22);

        // Draw legend box background
        g2d.setColor(new Color(240, 240, 245));
        g2d.fillRoundRect(offsetX - 10, offsetY - 20, 180, legendHeight, 15, 15);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(offsetX - 10, offsetY - 20, 180, legendHeight, 15, 15);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("🎨 Item Legend", offsetX, offsetY);

        int y = offsetY + 20;
        for (Map.Entry<String, Color> entry : itemColors.entrySet()) {
            // Draw colored box with gradient
            GradientPaint gradient = new GradientPaint(
                    offsetX, y, entry.getValue(),
                    offsetX + 25, y + 18, entry.getValue().darker());
            g2d.setPaint(gradient);
            g2d.fillRect(offsetX, y, 25, 18);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(offsetX, y, 25, 18);

            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("Item " + entry.getKey(), offsetX + 32, y + 14);
            y += 22;
        }
    }

    /**
     * Draw statistics
     */
    private void drawStatistics(Graphics2D g2d, int offsetX, int offsetY) {
        // Draw statistics box background
        g2d.setColor(new Color(240, 240, 245));
        g2d.fillRoundRect(offsetX - 10, offsetY - 20, 380, 110, 15, 15);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(offsetX - 10, offsetY - 20, 380, 110, 15, 15);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("📊 Packing Statistics", offsetX, offsetY);

        int usedVolume = (int) (bin.getTotalVolume() - solution.getTotalWastage());
        double utilization = (double) usedVolume / bin.getTotalVolume() * 100.0;

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        int y = offsetY + 20;
        g2d.drawString(String.format("Bin Dimensions: %.0f × %.0f units",
                bin.getWidth(), bin.getHeight()), offsetX, y);
        y += 18;
        g2d.drawString(String.format("Space Utilization: %.2f%%", utilization), offsetX, y);
        y += 18;
        g2d.drawString(String.format("Items Packed: %d",
                solution.getPlacements().size()), offsetX, y);
        y += 18;
        g2d.drawString(String.format("Total Value: $%.2f",
                solution.getTotalCost()), offsetX, y);
    }

    /**
     * Display the visualization in a window
     */
    public static void display(Bin bin, PackingSolution solution, String title) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new BinVisualizer2D(bin, solution));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
