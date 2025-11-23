import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern Banking Application with modular design
 *
 * Features:
 * - Account creation with different types
 * - Secure PIN authentication
 * - Deposit, Withdraw, Transfer operations
 * - Transaction history tracking
 * - Modern UI with improved design
 * - File-based storage
 *
 * Compile: javac *.java
 * Run:     java BankingApp
 */
public class BankingApp extends JFrame {
    private CardLayout cards = new CardLayout();
    private JPanel mainPanel = new JPanel(cards);

    private BankingService bankingService;
    private Account currentAccount = null;

    // UI Panels
    private WelcomePanel welcomePanel;
    private LoginPanel loginPanel;
    private CreateAccountPanel createAccountPanel;
    private DashboardPanel dashboardPanel;

    public BankingApp() {
        bankingService = new BankingService();
        setTitle("🏦 SimpleBank — Modern Banking Experience");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        // Create panels with action listeners
        welcomePanel = new WelcomePanel(
            e -> cards.show(mainPanel, "login"),
            e -> cards.show(mainPanel, "create")
        );

        loginPanel = new LoginPanel(
            e -> handleLogin(),
            e -> {
                loginPanel.clearFields();
                cards.show(mainPanel, "welcome");
            }
        );

        createAccountPanel = new CreateAccountPanel(
            e -> handleCreateAccount(),
            e -> {
                createAccountPanel.clearFields();
                cards.show(mainPanel, "welcome");
            }
        );

        dashboardPanel = new DashboardPanel(
            e -> doDeposit(),
            e -> doWithdraw(),
            e -> doTransfer(),
            e -> showTxnDialog(),
            e -> changePin(),
            e -> logout()
        );

        // Add panels to main panel
        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(loginPanel, "login");
        mainPanel.add(createAccountPanel, "create");
        mainPanel.add(dashboardPanel, "dash");

        add(mainPanel);
        cards.show(mainPanel, "welcome");
    }

    // Handler methods

    private void handleLogin() {
        String acc = loginPanel.getAccountNumber();
        String pin = loginPanel.getPin();
        if (acc.isEmpty()) { showError("Enter account number."); return; }
        if (pin.isEmpty()) { showError("Enter PIN."); return; }
        Account account = bankingService.authenticate(acc, pin);
        if (account == null) {
            showError("Invalid account number or PIN.");
            return;
        }
        currentAccount = account;
        dashboardPanel.updateAccountInfo(account);
        loginPanel.clearFields();
        cards.show(mainPanel, "dash");
    }

    private void handleCreateAccount() {
        String name = createAccountPanel.getName();
        String phoneNumber = createAccountPanel.getPhoneNumber();
        String pin = createAccountPanel.getPin();
        String pinConfirm = createAccountPanel.getPinConfirm();
        Account.AccountType accountType = createAccountPanel.getAccountType();

        if (name.isEmpty()) { showError("Please enter your name."); return; }
        if (!isValid10DigitPhone(phoneNumber)) { showError("Phone number must be exactly 10 digits."); return; }
        if (!isValid4Pin(pin)) { showError("PIN must be exactly 4 digits."); return; }
        if (!pin.equals(pinConfirm)) { showError("PIN and confirmation do not match."); return; }

        Account account = bankingService.createAccount(name, phoneNumber, pin, accountType);
        showInfo("Account created successfully!\n\nAccount Number: " + account.accountNumber +
                "\nAccount Type: " + account.accountType.getDisplayName() +
                "\n\nKeep your PIN safe: " + pin);
        createAccountPanel.clearFields();
        cards.show(mainPanel, "login");
        loginPanel.clearFields();
        // Pre-fill account number for convenience
        // Note: In a real app, this would be a security risk
    }

    private void doDeposit() {
        if (currentAccount == null) return;
        String amtStr = JOptionPane.showInputDialog(this, "Enter amount to deposit (₹):", "Deposit", JOptionPane.PLAIN_MESSAGE);
        if (amtStr == null) return;
        try {
            double amt = Double.parseDouble(amtStr);
            if (amt <= 0) { showError("Enter a positive amount."); return; }

            String reason = JOptionPane.showInputDialog(this, "Enter reason (optional):", "Deposit Reason", JOptionPane.PLAIN_MESSAGE);
            if (reason == null) reason = ""; // User cancelled, treat as empty

            currentAccount.deposit(amt, reason);
            bankingService.saveAccounts();
            showInfo("Successfully deposited ₹" + amt);
            dashboardPanel.updateAccountInfo(currentAccount);
        } catch (NumberFormatException ex) {
            showError("Invalid amount.");
        }
    }

    private void doWithdraw() {
        if (currentAccount == null) return;
        String amtStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw (₹):", "Withdraw", JOptionPane.PLAIN_MESSAGE);
        if (amtStr == null) return;
        try {
            double amt = Double.parseDouble(amtStr);
            if (amt <= 0) { showError("Enter a positive amount."); return; }

            String reason = JOptionPane.showInputDialog(this, "Enter reason (optional):", "Withdraw Reason", JOptionPane.PLAIN_MESSAGE);
            if (reason == null) reason = ""; // User cancelled, treat as empty

            if (!currentAccount.withdraw(amt, reason)) {
                showError("Insufficient balance.");
                return;
            }
            bankingService.saveAccounts();
            showInfo("Successfully withdrew ₹" + amt);
            dashboardPanel.updateAccountInfo(currentAccount);
        } catch (NumberFormatException ex) {
            showError("Invalid amount.");
        }
    }

    private void doTransfer() {
        if (currentAccount == null) return;

        // Create transfer dialog
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Recipient Account Number:"));
        JTextField recipientField = new JTextField();
        panel.add(recipientField);
        panel.add(new JLabel("Amount to Transfer (₹):"));
        JTextField amountField = new JTextField();
        panel.add(amountField);
        panel.add(new JLabel("Reason (optional):"));
        JTextField reasonField = new JTextField();
        panel.add(reasonField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Transfer Money",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        String recipientAcc = recipientField.getText().trim();
        String amtStr = amountField.getText().trim();
        String reason = reasonField.getText().trim();

        if (recipientAcc.isEmpty()) { showError("Enter recipient account number."); return; }
        if (recipientAcc.equals(currentAccount.accountNumber)) {
            showError("Cannot transfer to your own account."); return;
        }

        try {
            double amt = Double.parseDouble(amtStr);
            if (amt <= 0) { showError("Enter a positive amount."); return; }

            Account recipient = bankingService.getAccount(recipientAcc);
            if (recipient == null) { showError("Recipient account not found."); return; }

            if (!currentAccount.transferTo(recipient, amt, reason)) {
                showError("Insufficient balance.");
                return;
            }

            bankingService.saveAccounts();
            showInfo("Successfully transferred ₹" + amt + " to account " + recipientAcc);
            dashboardPanel.updateAccountInfo(currentAccount);
        } catch (NumberFormatException ex) {
            showError("Invalid amount.");
        }
    }

    private void showTxnDialog() {
        if (currentAccount == null) return;
        JTextArea area = new JTextArea(currentAccount.getAllTransactionsText());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scroll, "Transaction History", JOptionPane.PLAIN_MESSAGE);
    }

    private void changePin() {
        if (currentAccount == null) return;

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Current PIN:"));
        JPasswordField oldPin = new JPasswordField();
        panel.add(oldPin);
        panel.add(new JLabel("New PIN (4 digits):"));
        JPasswordField newPin = new JPasswordField();
        panel.add(newPin);
        panel.add(new JLabel("Confirm New PIN:"));
        JPasswordField confirmPin = new JPasswordField();
        panel.add(confirmPin);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change PIN",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        String old = new String(oldPin.getPassword()).trim();
        String n1 = new String(newPin.getPassword()).trim();
        String n2 = new String(confirmPin.getPassword()).trim();

        if (!currentAccount.pin.equals(old)) { showError("Current PIN is incorrect."); return; }
        if (!isValid4Pin(n1)) { showError("New PIN must be 4 digits."); return; }
        if (!n1.equals(n2)) { showError("New PIN confirmation does not match."); return; }

        currentAccount.pin = n1;
        currentAccount.addTransaction("PIN changed");
        bankingService.saveAccounts();
        showInfo("PIN changed successfully.");
    }

    private void logout() {
        currentAccount = null;
        cards.show(mainPanel, "welcome");
    }

    private boolean isValid4Pin(String pin) {
        return pin.matches("\\d{4}");
    }

    private boolean isValid10DigitPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Main method
    public static void main(String[] args) {
        // Use system look & feel for nicer UI
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            BankingApp app = new BankingApp();
            app.setVisible(true);
        });
    }
}
