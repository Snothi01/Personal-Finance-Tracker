public import javax.swing.*;
import java.awt.*;

/**
 * Small rounded panel to create card-like white boxes
 */
public class RoundedPanel extends JPanel {
    private int cornerRadius = 20;

    public RoundedPanel() {
        super();
        setOpaque(false);
    }

    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // card background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        // subtle border
        g2.setColor(new Color(200, 200, 200, 120));
        g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
        g2.dispose();

        super.paintComponent(g);
    }
}
 RoundedPanel {
    
}
