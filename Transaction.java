package bank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single bank transaction.
 */
public class Transaction {

    public enum Type {
        DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    }

    private final Type type;
    private final double amount;
    private final double balanceAfter;
    private final String description;
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Transaction(Type type, double amount, double balanceAfter, String description) {
        this.type        = type;
        this.amount      = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.timestamp   = LocalDateTime.now();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public Type   getType()         { return type; }
    public double getAmount()       { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getDescription()  { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        String sign = (type == Type.DEPOSIT || type == Type.TRANSFER_IN) ? "+" : "-";
        return String.format("  [%s]  %-14s  %s%-10.2f  Balance: ₹%,.2f  | %s",
                timestamp.format(FORMATTER),
                type,
                sign,
                amount,
                balanceAfter,
                description);
    }
}
