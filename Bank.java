package bank;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents the bank itself — manages multiple accounts.
 */
public class Bank {

    private final String bankName;
    private final Map<String, Account> accounts;
    private static final Random RANDOM = new Random();

    public Bank(String bankName) {
        this.bankName = bankName;
        this.accounts = new HashMap<>();
    }

    // ── Account Management ───────────────────────────────────────────────────

    /**
     * Opens a new account and returns the generated account number.
     */
    public String openAccount(String holderName, String accountType, double initialDeposit) {
        String accNo = generateAccountNumber();
        Account account = new Account(accNo, holderName, accountType, initialDeposit);
        accounts.put(accNo, account);

        System.out.println();
        System.out.println("  ✔  Account opened successfully!");
        System.out.printf ("  ► Account Number : %s%n", accNo);
        System.out.printf ("  ► Holder         : %s%n", holderName);
        System.out.printf ("  ► Type           : %s%n", accountType);
        System.out.printf ("  ► Initial Balance: ₹%,.2f%n", initialDeposit);
        return accNo;
    }

    /** Retrieves an account by its number; throws if not found. */
    public Account getAccount(String accountNumber) {
        Account acc = accounts.get(accountNumber);
        if (acc == null)
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        return acc;
    }

    /** Prints a summary of every account held with the bank. */
    public void printAllAccounts() {
        System.out.println();
        System.out.println("  ══════════════  " + bankName + " — All Accounts  ══════════════");
        if (accounts.isEmpty()) {
            System.out.println("  No accounts found.");
        } else {
            accounts.values().forEach(a -> System.out.println("  • " + a.getSummary()));
        }
        System.out.println();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private String generateAccountNumber() {
        String num;
        do {
            num = "ACC" + (100000 + RANDOM.nextInt(900000));
        } while (accounts.containsKey(num));
        return num;
    }

    public String getBankName() { return bankName; }
    public int    getTotalAccounts() { return accounts.size(); }
}
