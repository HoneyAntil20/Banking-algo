import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CardPanel extends JPanel {
    private int cornerRadius = 20;
    private float shadowDepth = 8f;
    private Color cardColor = Color.WHITE;
    private Color shadowColor = new Color(0, 0, 0, 40);

    public CardPanel() {
        setOpaque(false);
    }

    public CardPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int shadowOffset = (int) shadowDepth;

        drawShadow(g2d, width, height, shadowOffset);
        drawCard(g2d, width, height);

        super.paintComponent(g);
    }

    private void drawShadow(Graphics2D g2d, int width, int height, int shadowOffset) {
        for (int i = shadowOffset; i > 0; i--) {
            float alpha = (float) (0.08f * (1 - (i / (float) shadowOffset)));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(shadowColor);
            Shape shadowShape = new RoundRectangle2D.Float(i, i + 2, width - 2 * i - 4, height - 2 * i - 4, cornerRadius, cornerRadius);
            g2d.fill(shadowShape);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawCard(Graphics2D g2d, int width, int height) {
        Shape cardShape = new RoundRectangle2D.Float(0, 0, width - 4, height - 4, cornerRadius, cornerRadius);

        g2d.setColor(cardColor);
        g2d.fill(cardShape);

        g2d.setStroke(new BasicStroke(1f));
        g2d.setColor(new Color(200, 200, 200, 80));
        g2d.draw(cardShape);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setShadowDepth(float depth) {
        this.shadowDepth = depth;
        repaint();
    }

    public void setCardColor(Color color) {
        this.cardColor = color;
        repaint();
    }

    public void setShadowColor(Color color) {
        this.shadowColor = color;
        repaint();
    }
}
