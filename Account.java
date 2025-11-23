import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank account with transaction history
 */
public class Account {
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String accountNumber;
    public String name;
    public String pin;
    public double balance;
    public String phoneNumber;
    public List<String> transactions = new ArrayList<>();
    public AccountType accountType;

    public enum AccountType {
        SAVINGS("Savings Account", 0.02),
        CHECKING("Checking Account", 0.0);

        private final String displayName;
        private final double interestRate;

        AccountType(String displayName, double interestRate) {
            this.displayName = displayName;
            this.interestRate = interestRate;
        }

        public String getDisplayName() { return displayName; }
        public double getInterestRate() { return interestRate; }
    }

    public Account(String accountNumber, String name, String pin, double balance) {
        this(accountNumber, name, pin, balance, AccountType.SAVINGS);
    }

    public Account(String accountNumber, String name, String pin, double balance, AccountType accountType) {
        this(accountNumber, name, "", pin, balance, accountType);
    }

    public Account(String accountNumber, String name, String phoneNumber, String pin, double balance, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
        this.balance = balance;
        this.accountType = accountType;
        addTransaction("Account opened - " + accountType.getDisplayName());
    }

    public void addTransaction(String desc) {
        addTransaction(desc, "");
    }

    public void addTransaction(String desc, String reason) {
        String t = DT.format(LocalDateTime.now()) + " - " + desc;
        if (!reason.isEmpty()) {
            t += " (" + reason + ")";
        }
        t += " - Bal: ₹" + MONEY.format(balance);
        transactions.add(0, t); // add to front (most recent first)
        // trim history to 200 records to avoid huge file
        if (transactions.size() > 200) transactions = transactions.subList(0, 200);
    }

    public String getRecentTransactionsText() {
        StringBuilder sb = new StringBuilder();
        int n = Math.min(transactions.size(), 8);
        for (int i = 0; i < n; i++) sb.append(transactions.get(i)).append("\n");
        if (n == 0) sb.append("No activity yet.");
        return sb.toString();
    }

    public String getAllTransactionsText() {
        if (transactions.isEmpty()) return "No transactions.";
        StringBuilder sb = new StringBuilder();
        for (String s : transactions) sb.append(s).append("\n");
        return sb.toString();
    }

    public boolean canWithdraw(double amount) {
        return balance >= amount;
    }

    public void deposit(double amount) {
        deposit(amount, "");
    }

    public void deposit(double amount, String reason) {
        balance += amount;
        addTransaction("Deposit ₹" + MONEY.format(amount), reason);
    }

    public boolean withdraw(double amount) {
        return withdraw(amount, "");
    }

    public boolean withdraw(double amount, String reason) {
        if (!canWithdraw(amount)) return false;
        balance -= amount;
        addTransaction("Withdraw ₹" + MONEY.format(amount), reason);
        return true;
    }

    public boolean transferTo(Account target, double amount) {
        return transferTo(target, amount, "");
    }

    public boolean transferTo(Account target, double amount, String reason) {
        if (!canWithdraw(amount)) return false;
        balance -= amount;
        target.balance += amount;
        addTransaction("Transfer to " + target.accountNumber + " ₹" + MONEY.format(amount), reason);
        target.addTransaction("Transfer from " + accountNumber + " ₹" + MONEY.format(amount), reason);
        return true;
    }
}