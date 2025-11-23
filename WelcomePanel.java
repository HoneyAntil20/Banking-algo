import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class WelcomePanel extends JPanel {
    private RoundedButton btnLogin;
    private RoundedButton btnCreate;

    public WelcomePanel(ActionListener loginAction, ActionListener createAction) {
        initUI(loginAction, createAction);
    }

    private void initUI(ActionListener loginAction, ActionListener createAction) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 255));

        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(70, 130, 255), getWidth(), getHeight(), new Color(40, 100, 220));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(40, 20, 30, 20));

        JLabel title = new JLabel("<html><center><span style='font-size:36px; color:white; font-weight:bold'>🏦 SimpleBank</span><br/><span style='font-size:16px; color:#E8F4FD'>Secure · Modern · Reliable</span></center></html>", SwingConstants.CENTER);
        titlePanel.add(title, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        JTextArea desc = new JTextArea("Welcome to SimpleBank — your trusted digital banking companion.\n\n" +
                "✨ Create secure accounts with PIN protection\n" +
                "💰 Manage deposits, withdrawals, and transfers\n" +
                "📊 Track your transaction history\n" +
                "🔒 Bank-grade security with local storage\n\n" +
                "Get started by creating an account or logging into your existing one.");
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        desc.setForeground(new Color(60, 60, 60));
        desc.setBorder(new EmptyBorder(20, 40, 20, 40));
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        add(desc, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel() {
            @Override
            public void doLayout() {
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int btnWidth = 200;
                int btnHeight = 60;
                int gap = 30;

                btnCreate.setBounds(centerX - btnWidth - gap / 2, centerY - btnHeight / 2, btnWidth, btnHeight);
                btnLogin.setBounds(centerX + gap / 2, centerY - btnHeight / 2, btnWidth, btnHeight);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(getWidth(), 150);
            }
        };
        btnPanel.setLayout(null);
        btnPanel.setOpaque(false);

        btnLogin = new RoundedButton("🔐 Login", new Color(34, 139, 34), new Color(25, 100, 25));
        btnCreate = new RoundedButton("✨ Create Account", new Color(60, 130, 255), new Color(40, 100, 220));
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCreate.setFont(new Font("SansSerif", Font.BOLD, 16));

        btnLogin.addActionListener(loginAction);
        btnCreate.addActionListener(createAction);

        btnPanel.add(btnLogin);
        btnPanel.add(btnCreate);
        add(btnPanel, BorderLayout.SOUTH);
    }
}