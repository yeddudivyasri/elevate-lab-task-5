package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a bank account with full transaction history.
 */
public class Account {

    // ── Fields ───────────────────────────────────────────────────────────────

    private final String accountNumber;
    private final String accountHolder;
    private final String accountType;       // e.g. "Savings", "Current"
    private double balance;
    private final List<Transaction> transactionHistory;

    // Minimum balance enforced for savings accounts
    private static final double MIN_SAVINGS_BALANCE = 500.00;

    // ── Constructor ──────────────────────────────────────────────────────────

    public Account(String accountNumber, String accountHolder,
                   String accountType, double initialDeposit) {
        if (initialDeposit < 0)
            throw new IllegalArgumentException("Initial deposit cannot be negative.");

        this.accountNumber    = accountNumber;
        this.accountHolder    = accountHolder;
        this.accountType      = accountType;
        this.balance          = 0;
        this.transactionHistory = new ArrayList<>();

        if (initialDeposit > 0) {
            deposit(initialDeposit, "Account opening deposit");
        }
    }

    // ── Core Operations ──────────────────────────────────────────────────────

    /**
     * Deposits the specified amount into the account.
     *
     * @param amount      Amount to deposit (must be > 0)
     * @param description Reason / note for the deposit
     */
    public void deposit(double amount, String description) {
        if (amount <= 0)
            throw new IllegalArgumentException("Deposit amount must be positive.");

        balance += amount;
        transactionHistory.add(
                new Transaction(Transaction.Type.DEPOSIT, amount, balance, description));
        System.out.printf("  ✔  Deposited ₹%,.2f successfully. New balance: ₹%,.2f%n",
                amount, balance);
    }

    /**
     * Withdraws the specified amount from the account.
     *
     * @param amount      Amount to withdraw (must be > 0)
     * @param description Reason / note for the withdrawal
     * @return {@code true} if the withdrawal succeeded, {@code false} otherwise
     */
    public boolean withdraw(double amount, String description) {
        if (amount <= 0)
            throw new IllegalArgumentException("Withdrawal amount must be positive.");

        double minRequired = accountType.equalsIgnoreCase("Savings")
                ? MIN_SAVINGS_BALANCE : 0;

        if (balance - amount < minRequired) {
            System.out.printf("  ✘  Withdrawal of ₹%,.2f failed. "
                    + "Insufficient funds (min balance ₹%,.2f required).%n",
                    amount, minRequired);
            return false;
        }

        balance -= amount;
        transactionHistory.add(
                new Transaction(Transaction.Type.WITHDRAWAL, amount, balance, description));
        System.out.printf("  ✔  Withdrew ₹%,.2f successfully. New balance: ₹%,.2f%n",
                amount, balance);
        return true;
    }

    /**
     * Transfers money from this account to {@code targetAccount}.
     *
     * @param targetAccount Recipient account
     * @param amount        Amount to transfer
     * @param description   Note for the transfer
     * @return {@code true} if the transfer succeeded
     */
    public boolean transfer(Account targetAccount, double amount, String description) {
        if (targetAccount == null)
            throw new IllegalArgumentException("Target account cannot be null.");

        System.out.printf("%n  Initiating transfer of ₹%,.2f from %s → %s…%n",
                amount, this.accountNumber, targetAccount.getAccountNumber());

        // Debit sender
        double minRequired = accountType.equalsIgnoreCase("Savings")
                ? MIN_SAVINGS_BALANCE : 0;
        if (balance - amount < minRequired) {
            System.out.printf("  ✘  Transfer failed. Insufficient funds.%n");
            return false;
        }

        balance -= amount;
        transactionHistory.add(new Transaction(
                Transaction.Type.TRANSFER_OUT, amount, balance,
                "Transfer to " + targetAccount.getAccountNumber() + " | " + description));

        // Credit receiver
        targetAccount.receiveTransfer(amount,
                "Transfer from " + this.accountNumber + " | " + description);

        System.out.printf("  ✔  Transfer successful.%n");
        return true;
    }

    /** Internal method used by {@link #transfer} to credit the receiving account. */
    void receiveTransfer(double amount, String description) {
        balance += amount;
        transactionHistory.add(
                new Transaction(Transaction.Type.TRANSFER_IN, amount, balance, description));
    }

    // ── Reporting ────────────────────────────────────────────────────────────

    /** Prints the full account statement to standard output. */
    public void printStatement() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════════════════════╗");
        System.out.printf ("  ║  Account Statement  %-49s║%n", "");
        System.out.printf ("  ║  Holder : %-59s║%n", accountHolder);
        System.out.printf ("  ║  Account: %-59s║%n", accountNumber + "  [" + accountType + "]");
        System.out.printf ("  ║  Balance: ₹%-58s║%n", String.format("%,.2f", balance));
        System.out.println("  ╠══════════════════════════════════════════════════════════════════════╣");

        if (transactionHistory.isEmpty()) {
            System.out.println("  ║  No transactions yet." + " ".repeat(48) + "║");
        } else {
            for (Transaction t : transactionHistory) {
                System.out.println(t);
            }
        }

        System.out.println("  ╚══════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /** Returns an unmodifiable view of the transaction history. */
    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    /** Returns a brief one-line summary of the account. */
    public String getSummary() {
        return String.format("Account[%s | %s | %s | Balance: ₹%,.2f | Txns: %d]",
                accountNumber, accountHolder, accountType,
                balance, transactionHistory.size());
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public String getAccountType()   { return accountType;   }
    public double getBalance()       { return balance;       }
}
