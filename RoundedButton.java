import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int cornerRadius = 25;
    private boolean isHovered = false;
    private Color primaryColor;
    private Color hoverColor;
    private float shadowDepth = 4f;

    public RoundedButton(String text, Color primaryColor) {
        super(text);
        this.primaryColor = primaryColor;
        this.hoverColor = primaryColor.darker();
        setupUI();
    }

    public RoundedButton(String text, Color primaryColor, Color hoverColor) {
        super(text);
        this.primaryColor = primaryColor;
        this.hoverColor = hoverColor;
        setupUI();
    }

    private void setupUI() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setMargin(new Insets(12, 30, 12, 30));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (isHovered) {
            drawShadow(g2d, width, height, shadowDepth + 2);
            drawButtonBackground(g2d, width, height, hoverColor);
        } else {
            drawShadow(g2d, width, height, shadowDepth);
            drawButtonBackground(g2d, width, height, primaryColor);
        }

        super.paintComponent(g);
    }

    private void drawShadow(Graphics2D g2d, int width, int height, float depth) {
        for (int i = (int) depth; i > 0; i--) {
            float alpha = (float) (0.1f * (1 - (i / (depth + 1))));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(new Color(0, 0, 0, 50));
            Shape shadowShape = new RoundRectangle2D.Float(i, i, width - 2 * i, height - 2 * i, cornerRadius, cornerRadius);
            g2d.fill(shadowShape);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawButtonBackground(Graphics2D g2d, int width, int height, Color baseColor) {
        Shape buttonShape = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

        GradientPaint gradient = new GradientPaint(0, 0, lighten(baseColor, 0.1f), 0, height, baseColor);
        g2d.setPaint(gradient);
        g2d.fill(buttonShape);

        g2d.setStroke(new BasicStroke(1.5f));
        g2d.setColor(lighten(baseColor, 0.2f));
        g2d.draw(buttonShape);
    }

    private Color lighten(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int) (color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int) (color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setShadowDepth(float depth) {
        this.shadowDepth = depth;
        repaint();
    }
}
