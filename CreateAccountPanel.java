import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateAccountPanel extends JPanel {
    private JTextField nameField;
    private JTextField phoneField;
    private JPasswordField pinField;
    private JPasswordField pinConfirmField;
    private JComboBox<Account.AccountType> accountTypeCombo;
    private RoundedButton btnCreate;
    private RoundedButton btnBack;

    public CreateAccountPanel(java.awt.event.ActionListener createAction, java.awt.event.ActionListener backAction) {
        initUI(createAction, backAction);
    }

    private void initUI(java.awt.event.ActionListener createAction, java.awt.event.ActionListener backAction) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 255));
        headerPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel header = new JLabel("✨ Create New Account", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.setForeground(Color.WHITE);
        headerPanel.add(header, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setOpaque(false);

        CardPanel formCard = new CardPanel(new BoxLayout(new JPanel() {}, BoxLayout.Y_AXIS));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(30, 50, 30, 50));
        formCard.setBackground(Color.WHITE);

        JPanel namePanel = new JPanel(new BorderLayout(10, 0));
        namePanel.setOpaque(false);
        namePanel.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(new Color(60, 60, 60));
        namePanel.add(nameLabel, BorderLayout.NORTH);

        nameField = new JTextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        nameField.setPreferredSize(new Dimension(350, 40));
        namePanel.add(nameField, BorderLayout.CENTER);

        formCard.add(namePanel);

        JPanel phonePanel = new JPanel(new BorderLayout(10, 0));
        phonePanel.setOpaque(false);
        phonePanel.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel phoneLabel = new JLabel("Phone Number (10 digits):");
        phoneLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        phoneLabel.setForeground(new Color(60, 60, 60));
        phonePanel.add(phoneLabel, BorderLayout.NORTH);

        phoneField = new JTextField();
        phoneField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        phoneField.setPreferredSize(new Dimension(350, 40));
        phonePanel.add(phoneField, BorderLayout.CENTER);

        formCard.add(phonePanel);

        JPanel typePanel = new JPanel(new BorderLayout(10, 0));
        typePanel.setOpaque(false);
        typePanel.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel typeLabel = new JLabel("Account Type:");
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        typeLabel.setForeground(new Color(60, 60, 60));
        typePanel.add(typeLabel, BorderLayout.NORTH);

        accountTypeCombo = new JComboBox<>(Account.AccountType.values());
        accountTypeCombo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        accountTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        accountTypeCombo.setPreferredSize(new Dimension(350, 40));
        accountTypeCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Account.AccountType) {
                    setText(((Account.AccountType) value).getDisplayName());
                }
                return this;
            }
        });
        typePanel.add(accountTypeCombo, BorderLayout.CENTER);

        formCard.add(typePanel);

        JPanel pinPanel = new JPanel(new BorderLayout(10, 0));
        pinPanel.setOpaque(false);
        pinPanel.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel pinLabel = new JLabel("Choose 4-Digit PIN:");
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
        pinField.setPreferredSize(new Dimension(350, 40));
        pinPanel.add(pinField, BorderLayout.CENTER);

        formCard.add(pinPanel);

        JPanel confirmPanel = new JPanel(new BorderLayout(10, 0));
        confirmPanel.setOpaque(false);
        confirmPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        JLabel confirmLabel = new JLabel("Confirm PIN:");
        confirmLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirmLabel.setForeground(new Color(60, 60, 60));
        confirmPanel.add(confirmLabel, BorderLayout.NORTH);

        pinConfirmField = new JPasswordField();
        pinConfirmField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        pinConfirmField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        pinConfirmField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pinConfirmField.setPreferredSize(new Dimension(350, 40));
        confirmPanel.add(pinConfirmField, BorderLayout.CENTER);

        formCard.add(confirmPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnCreate = new RoundedButton("Create Account", new Color(60, 130, 255), new Color(40, 100, 220));
        btnBack = new RoundedButton("Back", new Color(108, 117, 125), new Color(80, 90, 100));
        btnCreate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnCreate.addActionListener(createAction);
        btnBack.addActionListener(backAction);

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnBack);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(buttonPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        centerPanel.add(formCard, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    public String getName() {
        return nameField.getText().trim();
    }

    public String getPin() {
        return new String(pinField.getPassword()).trim();
    }

    public String getPinConfirm() {
        return new String(pinConfirmField.getPassword()).trim();
    }

    public Account.AccountType getAccountType() {
        return (Account.AccountType) accountTypeCombo.getSelectedItem();
    }

    public String getPhoneNumber() {
        return phoneField.getText().trim();
    }

    public void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        pinField.setText("");
        pinConfirmField.setText("");
        accountTypeCombo.setSelectedIndex(0);
    }

    public void focusNameField() {
        nameField.requestFocus();
    }
}