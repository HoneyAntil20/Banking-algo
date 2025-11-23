import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service class for banking operations and data persistence
 */
public class BankingService {
    private static final File STORE_FILE = new File("accounts.db");

    private Map<String, Account> accounts = new LinkedHashMap<>();
    private int nextAccountNumber = 1001;

    public BankingService() {
        loadAccounts();
    }

    public Account createAccount(String name, String pin) {
        return createAccount(name, "", pin, Account.AccountType.SAVINGS);
    }

    public Account createAccount(String name, String phoneNumber, String pin, Account.AccountType accountType) {
        String acc = String.valueOf(nextAccountNumber++);
        while (accounts.containsKey(acc)) acc = String.valueOf(nextAccountNumber++);
        Account a = new Account(acc, name, phoneNumber, pin, 0.0, accountType);
        accounts.put(acc, a);
        saveAccounts();
        return a;
    }

    public Account authenticate(String accountNumber, String pin) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.pin.equals(pin)) {
            return account;
        }
        return null;
    }

    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void saveAccounts() {
        // write to temp then rename for safety
        File tmp = new File(STORE_FILE.getAbsolutePath() + ".tmp");
        try (PrintWriter w = new PrintWriter(new FileWriter(tmp))) {
            for (Account a : accounts.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(a.accountNumber).append("|")
                        .append(escape(a.name)).append("|")
                        .append(escape(a.phoneNumber)).append("|")
                        .append(a.pin).append("|")
                        .append(a.balance).append("|")
                        .append(a.accountType.name()).append("|");
                // txs separated by ';;'
                for (int i = 0; i < a.transactions.size(); i++) {
                    if (i > 0) sb.append(";;");
                    sb.append(escape(a.transactions.get(i)));
                }
                w.println(sb.toString());
            }
        } catch (IOException ex) {
            System.err.println("Failed to save accounts: " + ex.getMessage());
            return;
        }
        // rename
        if (STORE_FILE.exists()) {
            STORE_FILE.delete();
        }
        tmp.renameTo(STORE_FILE);
    }

    private void loadAccounts() {
        if (!STORE_FILE.exists()) {
            return;
        }
        try (BufferedReader r = new BufferedReader(new FileReader(STORE_FILE))) {
            String line;
            int highest = nextAccountNumber;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // accountNumber|name|phoneNumber|pin|balance|accountType|tx1;;tx2;;tx3
                String[] parts = line.split("\\|", 7);
                if (parts.length < 6) continue;
                String acc = parts[0];
                String name = unescape(parts[1]);
                String phoneNumber = unescape(parts[2]);
                String pin = parts[3];
                double bal = 0.0;
                try { bal = Double.parseDouble(parts[4]); } catch (Exception ignored) {}
                Account.AccountType accountType = Account.AccountType.SAVINGS;
                try { accountType = Account.AccountType.valueOf(parts[5]); } catch (Exception ignored) {}
                String txs = parts.length >= 7 ? parts[6] : "";
                Account a = new Account(acc, name, phoneNumber, pin, bal, accountType);
                a.transactions.clear(); // clear default transaction
                if (!txs.isEmpty()) {
                    String[] t = txs.split(";;");
                    for (String s : t) if (!s.isEmpty()) a.transactions.add(unescape(s));
                }
                accounts.put(acc, a);
                try {
                    int n = Integer.parseInt(acc);
                    if (n >= highest) highest = n + 1;
                } catch (Exception ex) { }
            }
            nextAccountNumber = Math.max(nextAccountNumber, highest);
        } catch (IOException ex) {
            System.err.println("Failed to load accounts: " + ex.getMessage());
        }
    }

    private static String escape(String s) {
        return s.replace("|", "%PIPE%").replace(";;", "%SEMI2%");
    }

    private static String unescape(String s) {
        return s.replace("%PIPE%", "|").replace("%SEMI2%", ";;");
    }

    public Map<String, Account> getAllAccounts() {
        return new LinkedHashMap<>(accounts);
    }
}