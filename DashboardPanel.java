import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dashboard panel showing account information and actions
 */
public class DashboardPanel extends JPanel {
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");

    private JLabel lblWelcome;
    private JLabel lblBalance;
    private JLabel lblAccountType;
    private JLabel lblMonthlyAmount;
    private JLabel lblTransactionCount;
    private JTextArea txnArea;
    private JButton btnDeposit;
    private JButton btnWithdraw;
    private JButton btnTransfer;
    private JButton btnHistory;
    private JButton btnChangePin;
    private JButton btnLogout;

    public DashboardPanel(ActionListener depositAction, ActionListener withdrawAction,
                         ActionListener transferAction, ActionListener historyAction,
                         ActionListener changePinAction, ActionListener logoutAction) {
        initUI(depositAction, withdrawAction, transferAction, historyAction, changePinAction, logoutAction);
    }

    private void initUI(ActionListener depositAction, ActionListener withdrawAction,
                       ActionListener transferAction, ActionListener historyAction,
                       ActionListener changePinAction, ActionListener logoutAction) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(240, 242, 245));

        // Top info panel
        JPanel topPanel = new JPanel(new BorderLayout(15, 15));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        // Welcome and account info
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        infoPanel.setBackground(Color.WHITE);

        lblWelcome = new JLabel("Welcome, —");
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblWelcome.setForeground(new Color(52, 73, 94));

        lblAccountType = new JLabel("Account Type: —");
        lblAccountType.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblAccountType.setForeground(new Color(127, 140, 141));

        lblBalance = new JLabel("Balance: ₹0.00");
        lblBalance.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblBalance.setForeground(new Color(46, 204, 113));

        infoPanel.add(lblWelcome);
        infoPanel.add(lblAccountType);
        infoPanel.add(lblBalance);

        topPanel.add(infoPanel, BorderLayout.CENTER);

        // Quick stats
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new EmptyBorder(0, 25, 0, 0));

        JPanel stat1 = createStatPanel("💰 This Month", lblMonthlyAmount = new JLabel("+₹0.00"));
        JPanel stat2 = createStatPanel("📊 Transactions", lblTransactionCount = new JLabel("0"));

        statsPanel.add(stat1);
        statsPanel.add(stat2);

        topPanel.add(statsPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center content
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setBackground(new Color(240, 242, 245));

        // Actions panel
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new GridLayout(6, 1, 12, 12));
        actionsPanel.setPreferredSize(new Dimension(320, 0));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 214, 218), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel actionsTitle = new JLabel("⚡ Quick Actions");
        actionsTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        actionsTitle.setForeground(new Color(52, 73, 94));
        actionsTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        actionsPanel.add(actionsTitle);

        btnDeposit = createActionButton("⬆️ Deposit", "Add money to your account");
        btnWithdraw = createActionButton("⬇️ Withdraw", "Take money from your account");
        btnTransfer = createActionButton("🔄 Transfer", "Send money to another account");
        btnHistory = createActionButton("📋 History", "View all transactions");
        btnChangePin = createActionButton("🔑 Change PIN", "Update your security PIN");
        btnLogout = createActionButton("🚪 Logout", "Sign out securely");

        btnDeposit.addActionListener(depositAction);
        btnWithdraw.addActionListener(withdrawAction);
        btnTransfer.addActionListener(transferAction);
        btnHistory.addActionListener(historyAction);
        btnChangePin.addActionListener(changePinAction);
        btnLogout.addActionListener(logoutAction);

        actionsPanel.add(btnDeposit);
        actionsPanel.add(btnWithdraw);
        actionsPanel.add(btnTransfer);
        actionsPanel.add(btnHistory);
        actionsPanel.add(btnChangePin);
        actionsPanel.add(btnLogout);

        centerPanel.add(actionsPanel, BorderLayout.WEST);

        // Transaction display area
        JPanel txnPanel = new JPanel(new BorderLayout());
        txnPanel.setBackground(Color.WHITE);
        txnPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 214, 218), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel txnTitle = new JLabel("📈 Recent Activity");
        txnTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        txnTitle.setForeground(new Color(52, 73, 94));
        txnTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        txnPanel.add(txnTitle, BorderLayout.NORTH);

        txnArea = new JTextArea();
        txnArea.setEditable(false);
        txnArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txnArea.setBackground(new Color(248, 249, 250));
        txnArea.setForeground(new Color(52, 73, 94));
        txnArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(txnArea);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        txnPanel.add(scroll, BorderLayout.CENTER);

        centerPanel.add(txnPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatPanel(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 214, 218), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLabel.setForeground(new Color(108, 117, 125));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        valueLabel.setForeground(new Color(52, 152, 219));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createActionButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        button.setBackground(new Color(255, 255, 255));
        button.setForeground(new Color(52, 73, 94));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 214, 218), 1),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setToolTipText(tooltip);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
                    BorderFactory.createEmptyBorder(14, 18, 14, 18)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255));
                button.setForeground(new Color(52, 73, 94));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210, 214, 218), 1),
                    BorderFactory.createEmptyBorder(14, 18, 14, 18)
                ));
            }
        });

        return button;
    }

    private double calculateMonthlyDeposits(Account account) {
        double total = 0.0;
        LocalDate now = LocalDate.now();
        String currentMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        Pattern depositPattern = Pattern.compile("Deposit ₹([0-9,]+\\.[0-9]{2})");
        Pattern transferInPattern = Pattern.compile("Transfer from [A-Z0-9]+ ₹([0-9,]+\\.[0-9]{2})");

        for (String transaction : account.transactions) {
            if (transaction.startsWith(currentMonth)) {
                Matcher depositMatcher = depositPattern.matcher(transaction);
                if (depositMatcher.find()) {
                    try {
                        String amountStr = depositMatcher.group(1).replace(",", "");
                        total += Double.parseDouble(amountStr);
                    } catch (NumberFormatException e) {
                        // Skip invalid amounts
                    }
                }
                
                Matcher transferMatcher = transferInPattern.matcher(transaction);
                if (transferMatcher.find()) {
                    try {
                        String amountStr = transferMatcher.group(1).replace(",", "");
                        total += Double.parseDouble(amountStr);
                    } catch (NumberFormatException e) {
                        // Skip invalid amounts
                    }
                }
            }
        }
        return total;
    }

    private int getTransactionCount(Account account) {
        return account.transactions.size();
    }

    public void updateAccountInfo(Account account) {
        lblWelcome.setText("Welcome, " + account.name);
        lblAccountType.setText("Account Type: " + account.accountType.getDisplayName());
        lblBalance.setText("Balance: ₹" + MONEY.format(account.balance));

        // Update dynamic stats
        double monthlyDeposits = calculateMonthlyDeposits(account);
        lblMonthlyAmount.setText("+" + MONEY.format(monthlyDeposits));

        int transactionCount = getTransactionCount(account);
        lblTransactionCount.setText(String.valueOf(transactionCount));

        txnArea.setText(account.getRecentTransactionsText());
    }
}