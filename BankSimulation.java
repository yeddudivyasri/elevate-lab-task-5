package bank;

/**
 * BankSimulation — Entry point.
 *
 * Demonstrates real-world banking scenarios:
 *   1. Opening accounts (Savings & Current)
 *   2. Deposits
 *   3. Withdrawals (including edge-cases: insufficient funds, min-balance rule)
 *   4. Fund transfers between accounts
 *   5. Printing full account statements
 *   6. Bank-wide account overview
 */
public class BankSimulation {

    public static void main(String[] args) {

        banner("Welcome to JavaBank Simulation");

        // ── 1. Create the bank ────────────────────────────────────────────────
        Bank bank = new Bank("JavaBank");

        // ── 2. Open accounts ─────────────────────────────────────────────────
        section("SCENARIO 1 — Opening Accounts");

        String aliceAcc = bank.openAccount("Alice Sharma",   "Savings", 10_000.00);
        String bobAcc   = bank.openAccount("Bob Reddy",      "Savings",  5_000.00);
        String corpAcc  = bank.openAccount("TechCorp Pvt.", "Current", 50_000.00);

        // ── 3. Deposits ───────────────────────────────────────────────────────
        section("SCENARIO 2 — Deposits");

        Account alice = bank.getAccount(aliceAcc);
        Account bob   = bank.getAccount(bobAcc);
        Account corp  = bank.getAccount(corpAcc);

        System.out.println("\n  Alice's deposits:");
        alice.deposit(5_000.00, "Salary credit — May 2025");
        alice.deposit(2_500.00, "Freelance payment");

        System.out.println("\n  Bob's deposits:");
        bob.deposit(3_000.00, "Part-time income");

        System.out.println("\n  TechCorp's deposits:");
        corp.deposit(1_00_000.00, "Client invoice #INV-2025-042");

        // ── 4. Withdrawals ────────────────────────────────────────────────────
        section("SCENARIO 3 — Withdrawals");

        System.out.println("\n  Alice withdraws for rent and groceries:");
        alice.withdraw(8_000.00, "Monthly rent");
        alice.withdraw(1_500.00, "Grocery shopping");

        System.out.println("\n  Bob attempts a large withdrawal (should fail — min balance):");
        bob.withdraw(7_900.00, "Attempt to exceed min balance");

        System.out.println("\n  Bob makes a valid withdrawal:");
        bob.withdraw(2_000.00, "Medical expense");

        System.out.println("\n  TechCorp pays salaries:");
        corp.withdraw(40_000.00, "Monthly payroll — June 2025");

        // ── 5. Transfers ──────────────────────────────────────────────────────
        section("SCENARIO 4 — Fund Transfers");

        System.out.println("\n  Alice transfers money to Bob:");
        alice.transfer(bob, 1_000.00, "Splitting dinner bill");

        System.out.println("\n  TechCorp reimburses Alice:");
        corp.transfer(alice, 5_500.00, "Travel reimbursement");

        System.out.println("\n  Bob tries to transfer more than allowed (should fail):");
        bob.transfer(alice, 10_000.00, "Overly optimistic transfer");

        // ── 6. Statements ─────────────────────────────────────────────────────
        section("SCENARIO 5 — Full Account Statements");

        alice.printStatement();
        bob.printStatement();
        corp.printStatement();

        // ── 7. Bank-wide overview ─────────────────────────────────────────────
        section("SCENARIO 6 — Bank Overview");
        bank.printAllAccounts();

        banner("Simulation Complete");
    }

    // ── Formatting helpers ────────────────────────────────────────────────────

    private static void banner(String text) {
        int width = 70;
        String line = "═".repeat(width);
        System.out.println("\n  ╔" + line + "╗");
        System.out.printf ("  ║  %-68s  ║%n", text);
        System.out.println("  ╚" + line + "╝\n");
    }

    private static void section(String title) {
        System.out.println("\n  ── " + title + " " + "─".repeat(Math.max(0, 55 - title.length())));
    }
}
