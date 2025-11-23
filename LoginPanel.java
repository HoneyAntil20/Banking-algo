import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField accountField;
    private JPasswordField pinField;
    private RoundedButton btnLogin;
    private RoundedButton btnBack;

    public LoginPanel(ActionListener loginAction, ActionListener backAction) {
        initUI(loginAction, backAction);
    }

    private void initUI(ActionListener loginAction, ActionListener backAction) {
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(240, 245, 255));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 255));
        headerPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel header = new JLabel("🔐 Secure Login", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.setForeground(Color.WHITE);
        headerPanel.add(header, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setOpaque(false);

        CardPanel formCard = new CardPanel(new BoxLayout(new JPanel() {}, BoxLayout.Y_AXIS));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(40, 40, 40, 40));
        formCard.setBackground(Color.WHITE);

        JPanel accPanel = new JPanel(new BorderLayout(10, 0));
        accPanel.setOpaque(false);
        accPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel accLabel = new JLabel("Account Number:");
        accLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        accLabel.setForeground(new Color(60, 60, 60));
        accPanel.add(accLabel, BorderLayout.NORTH);

        accountField = new JTextField();
        accountField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        accountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        accountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        accountField.setPreferredSize(new Dimension(300, 40));
        accPanel.add(accountField, BorderLayout.CENTER);

        formCard.add(accPanel);

        JPanel pinPanel = new JPanel(new BorderLayout(10, 0));
        pinPanel.setOpaque(false);
        pinPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        JLabel pinLabel = new JLabel("4-Digit PIN:");
        pinLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        pinLabel.setForeground(new Color(60, 60, 60));
        pinPanel.add(pinLabel, BorderLayout.NORTH);

        pinField = new JPasswordField();
        pinField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        pinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        pinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pinField.setPreferredSize(new Dimension(300, 40));
        pinPanel.add(pinField, BorderLayout.CENTER);

        formCard.add(pinPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnLogin = new RoundedButton("Login", new Color(34, 139, 34), new Color(25, 100, 25));
        btnBack = new RoundedButton("Back", new Color(108, 117, 125), new Color(80, 90, 100));
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnLogin.addActionListener(loginAction);
        btnBack.addActionListener(backAction);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnBack);
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(buttonPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        centerPanel.add(formCard, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    public String getAccountNumber() {
        return accountField.getText().trim();
    }

    public String getPin() {
        return new String(pinField.getPassword()).trim();
    }

    public void clearFields() {
        accountField.setText("");
        pinField.setText("");
    }

    public void focusAccountField() {
        accountField.requestFocus();
    }
}