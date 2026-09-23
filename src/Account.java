import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Account {

    private String accountNumber;
    private String name;
    private String pin;
    private String accountType;
    private double balance;
    private List<Transaction> transactions;

    public Account(
            String accountNumber,
            String name,
            String pin,
            String accountType,
            double balance
    ) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.pin = pin;
        this.accountType = accountType;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public String getPin() {
        return pin;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public boolean checkPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public void deposit(double amount) {
        validateAmount(amount, "Deposit");

        balance += amount;

        transactions.add(
                new Transaction(
                        "DEPOSIT",
                        amount,
                        "Cash deposit",
                        balance,
                        null
                )
        );
    }

    public void withdraw(double amount) {
        validateAmount(amount, "Withdrawal");

        if (amount > balance) {
            throw new IllegalArgumentException(
                    "Insufficient funds."
            );
        }

        double dailyLimit = 20000;
        double todayWithdrawals = getTodayWithdrawals();

        if (todayWithdrawals + amount > dailyLimit) {
            double remainingLimit =
                    dailyLimit - todayWithdrawals;

            throw new IllegalArgumentException(
                    String.format(
                            "Daily withdrawal limit exceeded. Remaining limit: ₱%.2f",
                            remainingLimit
                    )
            );
        }

        balance -= amount;

        transactions.add(
                new Transaction(
                        "WITHDRAWAL",
                        amount,
                        "Cash withdrawal",
                        balance,
                        null
                )
        );
    }

    public void transferTo(
            Account recipient,
            double amount
    ) {
        if (recipient == null) {
            throw new IllegalArgumentException(
                    "Recipient account not found."
            );
        }

        if (recipient.getAccountNumber()
                .equals(this.accountNumber)) {

            throw new IllegalArgumentException(
                    "You cannot transfer money to your own account."
            );
        }

        validateAmount(amount, "Transfer");

        if (amount > balance) {
            throw new IllegalArgumentException(
                    "Insufficient funds."
            );
        }

        balance -= amount;
        recipient.balance += amount;

        transactions.add(
                new Transaction(
                        "TRANSFER_OUT",
                        amount,
                        "Transfer to " + recipient.getName(),
                        balance,
                        recipient.getAccountNumber()
                )
        );

        recipient.transactions.add(
                new Transaction(
                        "TRANSFER_IN",
                        amount,
                        "Transfer from " + this.name,
                        recipient.balance,
                        this.accountNumber
                )
        );
    }

    private void validateAmount(
            double amount,
            String operation
    ) {
        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException(
                    "Invalid " + operation.toLowerCase()
                            + " amount."
            );
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    operation
                            + " amount must be greater than zero."
            );
        }

        if (amount % 100 != 0) {
            throw new IllegalArgumentException(
                    operation
                            + " amount must be a multiple of ₱100."
            );
        }
    }

    public double getTodayWithdrawals() {
        double total = 0;

        String today = LocalDate.now().toString();

        for (Transaction transaction : transactions) {
            if (transaction.getType()
                    .equals("WITHDRAWAL")) {

                String transactionDate =
                        transaction.getDate()
                                .toString()
                                .substring(0, 10);

                if (transactionDate.equals(today)) {
                    total += transaction.getAmount();
                }
            }
        }

        return total;
    }

    /*
     * GUI helper method.
     *
     * Used by ATMGui when the GUI needs to update
     * the balance without automatically creating
     * a transaction.
     */
    public void depositWithoutTransaction(double amount) {

        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException(
                    "Invalid deposit amount."
            );
        }

        balance += amount;
    }

    /*
     * GUI helper method.
     *
     * Used by ATMGui when the GUI needs to update
     * the balance without automatically creating
     * a transaction.
     */
    public void withdrawWithoutTransaction(double amount) {

        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException(
                    "Invalid withdrawal amount."
            );
        }

        if (amount > balance) {
            throw new IllegalArgumentException(
                    "Insufficient funds."
            );
        }

        balance -= amount;
    }

    public void changePin(String newPin) {

        if (!newPin.matches("\\d{4}")) {
            throw new IllegalArgumentException(
                    "PIN must contain exactly 4 digits."
            );
        }

        String[] weakPins = {
                "0000",
                "1111",
                "2222",
                "3333",
                "4444",
                "5555",
                "6666",
                "7777",
                "8888",
                "9999",
                "1234",
                "4321"
        };

        for (String weakPin : weakPins) {
            if (newPin.equals(weakPin)) {
                throw new IllegalArgumentException(
                        "This PIN is too easy to guess. Please choose a stronger PIN."
                );
            }
        }

        if (newPin.equals(this.pin)) {
            throw new IllegalArgumentException(
                    "New PIN must be different from your current PIN."
            );
        }

        this.pin = newPin;
    }
}