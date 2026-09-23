import java.util.List;
import java.util.Scanner;

public class ATM {

    private List<Account> accounts;
    private Account currentAccount;
    private Scanner scanner;

    public ATM() {
        accounts = Storage.loadAccounts();
        currentAccount = null;
        scanner = new Scanner(System.in);
    }

    private String ask(String question) {
        System.out.print(question);
        return scanner.nextLine().trim();
    }

    private Account findAccount(String accountNumber) {

        for (Account account : accounts) {

            if (account.getAccountNumber()
                    .equals(accountNumber)) {

                return account;
            }
        }

        return null;
    }

    private void saveData() {
        Storage.saveAccounts(accounts);
    }

    public boolean login() {

        System.out.println();
        System.out.println("================================");
        System.out.println("        ATM SIMULATOR");
        System.out.println("================================");
        System.out.println();

        String accountNumber =
                ask("Account Number: ");

        Account account =
                findAccount(accountNumber);

        if (account == null) {
            System.out.println(
                    "\nAccount not found."
            );

            return false;
        }

        for (int attempt = 1; attempt <= 3; attempt++) {

            String pin = ask("PIN: ");

            if (account.checkPin(pin)) {

                currentAccount = account;

                System.out.println(
                        "\nLogin successful!"
                );

                System.out.println(
                        "Welcome, "
                                + account.getName()
                                + "!"
                );

                return true;
            }

            int remaining = 3 - attempt;

            if (remaining > 0) {
                System.out.println(
                        "Incorrect PIN. Attempts remaining: "
                                + remaining
                );
            }
        }

        System.out.println(
                "\nToo many incorrect attempts."
        );

        System.out.println(
                "Account access denied."
        );

        return false;
    }

    public void showBalance() {

        System.out.println();
        System.out.println("================================");
        System.out.println("         ACCOUNT BALANCE");
        System.out.println("================================");

        System.out.printf(
                "Available Balance: ₱%.2f%n",
                currentAccount.getBalance()
        );
    }

    public void deposit() {

        System.out.println();
        System.out.println("================================");
        System.out.println("            DEPOSIT");
        System.out.println("================================");

        try {

            double amount =
                    Double.parseDouble(
                            ask("Enter deposit amount: ₱")
                    );

            currentAccount.deposit(amount);

            saveData();

            System.out.println(
                    "\nDeposit successful!"
            );

            System.out.printf(
                    "Amount Deposited: ₱%.2f%n",
                    amount
            );

            System.out.printf(
                    "New Balance: ₱%.2f%n",
                    currentAccount.getBalance()
            );

        } catch (NumberFormatException error) {

            System.out.println(
                    "\nInvalid amount."
            );

        } catch (IllegalArgumentException error) {

            System.out.println(
                    "\n" + error.getMessage()
            );
        }
    }

    public void withdraw() {

        System.out.println();
        System.out.println("================================");
        System.out.println("           WITHDRAW");
        System.out.println("================================");

        try {

            double amount =
                    Double.parseDouble(
                            ask("Enter withdrawal amount: ₱")
                    );

            currentAccount.withdraw(amount);

            saveData();

            System.out.println(
                    "\nWithdrawal successful!"
            );

            System.out.printf(
                    "Amount Withdrawn: ₱%.2f%n",
                    amount
            );

            System.out.println(
                    "Please take your cash."
            );

            System.out.printf(
                    "Remaining Balance: ₱%.2f%n",
                    currentAccount.getBalance()
            );

            double todayWithdrawals =
                    currentAccount
                            .getTodayWithdrawals();

            double remainingLimit =
                    20000 - todayWithdrawals;

            System.out.printf(
                    "Remaining Daily Withdrawal Limit: ₱%.2f%n",
                    remainingLimit
            );

        } catch (NumberFormatException error) {

            System.out.println(
                    "\nInvalid amount."
            );

        } catch (IllegalArgumentException error) {

            System.out.println(
                    "\n" + error.getMessage()
            );
        }
    }

    public void transfer() {

        System.out.println();
        System.out.println("================================");
        System.out.println("           TRANSFER");
        System.out.println("================================");

        String recipientNumber =
                ask("Recipient Account Number: ");

        Account recipient =
                findAccount(recipientNumber);

        if (recipient == null) {

            System.out.println(
                    "\nRecipient account not found."
            );

            return;
        }

        if (recipient.getAccountNumber()
                .equals(
                        currentAccount.getAccountNumber()
                )) {

            System.out.println(
                    "\nYou cannot transfer money to your own account."
            );

            return;
        }

        System.out.println(
                "\nRecipient: "
                        + recipient.getName()
        );

        System.out.println(
                "Account: "
                        + recipient.getAccountNumber()
        );

        String confirmation =
                ask("Confirm recipient? (Y/N): ");

        if (!confirmation.equalsIgnoreCase("y")) {

            System.out.println(
                    "\nTransfer cancelled."
            );

            return;
        }

        try {

            double amount =
                    Double.parseDouble(
                            ask("Enter transfer amount: ₱")
                    );

            currentAccount.transferTo(
                    recipient,
                    amount
            );

            saveData();

            System.out.println(
                    "\n================================"
            );

            System.out.println(
                    "       TRANSFER SUCCESSFUL"
            );

            System.out.println(
                    "================================"
            );

            System.out.println(
                    "Recipient: "
                            + recipient.getName()
            );

            System.out.printf(
                    "Amount: ₱%.2f%n",
                    amount
            );

            System.out.printf(
                    "Remaining Balance: ₱%.2f%n",
                    currentAccount.getBalance()
            );

        } catch (NumberFormatException error) {

            System.out.println(
                    "\nInvalid amount."
            );

        } catch (IllegalArgumentException error) {

            System.out.println(
                    "\n" + error.getMessage()
            );
        }
    }

    public void showTransactions() {

        System.out.println();
        System.out.println("================================");
        System.out.println("       TRANSACTION HISTORY");
        System.out.println("================================");

        List<Transaction> transactions =
                currentAccount.getTransactions();

        if (transactions.isEmpty()) {

            System.out.println(
                    "\nNo transactions found."
            );

            return;
        }

        for (int i = 0;
             i < transactions.size();
             i++) {

            Transaction transaction =
                    transactions.get(i);

            System.out.println(
                    "\n--------------------------------"
            );

            System.out.println(
                    "Transaction #" + (i + 1)
            );

            System.out.println(
                    "--------------------------------"
            );

            System.out.println(
                    "Transaction ID : "
                            + transaction.getId()
            );

            System.out.println(
                    "Type           : "
                            + transaction.getType()
            );

            System.out.printf(
                    "Amount         : ₱%.2f%n",
                    transaction.getAmount()
            );

            System.out.println(
                    "Description    : "
                            + transaction.getDescription()
            );

            if (transaction.getRelatedAccount()
                    != null) {

                System.out.println(
                        "Related Account: "
                                + transaction
                                .getRelatedAccount()
                );
            }

            System.out.println(
                    "Date           : "
                            + transaction.getDate()
            );

            System.out.printf(
                    "Balance After  : ₱%.2f%n",
                    transaction.getBalanceAfter()
            );
        }
    }

    public void showAccountInformation() {

        System.out.println();
        System.out.println("================================");
        System.out.println("      ACCOUNT INFORMATION");
        System.out.println("================================");

        System.out.println(
                "Account Number : "
                        + currentAccount.getAccountNumber()
        );

        System.out.println(
                "Account Holder : "
                        + currentAccount.getName()
        );

        System.out.println(
                "Account Type   : "
                        + currentAccount.getAccountType()
        );

        System.out.printf(
                "Balance        : ₱%.2f%n",
                currentAccount.getBalance()
        );
    }

    public void changePin() {

        System.out.println();
        System.out.println("================================");
        System.out.println("           CHANGE PIN");
        System.out.println("================================");

        String currentPin =
                ask("Current PIN: ");

        if (!currentAccount.checkPin(currentPin)) {

            System.out.println(
                    "\nIncorrect current PIN."
            );

            return;
        }

        String newPin =
                ask("New PIN: ");

        String confirmPin =
                ask("Confirm New PIN: ");

        if (!newPin.equals(confirmPin)) {

            System.out.println(
                    "\nPINs do not match."
            );

            return;
        }

        try {

            currentAccount.changePin(newPin);

            saveData();

            System.out.println(
                    "\nPIN successfully changed."
            );

        } catch (IllegalArgumentException error) {

            System.out.println(
                    "\n" + error.getMessage()
            );
        }
    }

    public void showMenu() {

        while (currentAccount != null) {

            System.out.println();
            System.out.println("================================");
            System.out.println("          ATM MAIN MENU");
            System.out.println("================================");

            System.out.println(
                    "1. Check Balance"
            );

            System.out.println(
                    "2. Deposit"
            );

            System.out.println(
                    "3. Withdraw"
            );

            System.out.println(
                    "4. Transfer"
            );

            System.out.println(
                    "5. Transaction History"
            );

            System.out.println(
                    "6. Account Information"
            );

            System.out.println(
                    "7. Change PIN"
            );

            System.out.println(
                    "8. Logout"
            );

            String choice =
                    ask("\nSelect an option: ");

            switch (choice) {

                case "1":
                    showBalance();
                    break;

                case "2":
                    deposit();
                    break;

                case "3":
                    withdraw();
                    break;

                case "4":
                    transfer();
                    break;

                case "5":
                    showTransactions();
                    break;

                case "6":
                    showAccountInformation();
                    break;

                case "7":
                    changePin();
                    break;

                case "8":
                    currentAccount = null;

                    System.out.println(
                            "\nYou have been logged out."
                    );

                    break;

                default:
                    System.out.println(
                            "\nInvalid option."
                    );
            }
        }
    }

    public void start() {

        boolean running = true;

        while (running) {

            boolean loggedIn = login();

            if (loggedIn) {
                showMenu();
            }

            String answer =
                    ask(
                            "\nDo you want to use the ATM again? (Y/N): "
                    );

            if (!answer.equalsIgnoreCase("y")) {
                running = false;
            }
        }

        scanner.close();

        System.out.println(
                "\nThank you for using the ATM Simulator."
        );
    }
}