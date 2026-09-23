import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {

    private static final String FILE_PATH =
            "data/accounts.txt";

    public static List<Account> loadAccounts() {

        List<Account> accounts = new ArrayList<>();

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createDefaultAccounts();
        }

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split("\\|", -1);

                if (data.length < 5) {
                    continue;
                }

                Account account = new Account(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        Double.parseDouble(data[4])
                );

                /*
                 * Transaction data can be stored after
                 * the first five account fields.
                 *
                 * Each transaction is separated by "~".
                 */

                if (data.length >= 6
                        && !data[5].trim().isEmpty()) {

                    String[] transactionData =
                            data[5].split("~", -1);

                    for (String transactionText
                            : transactionData) {

                        String[] transaction =
                                transactionText.split(";", -1);

                        if (transaction.length < 7) {
                            continue;
                        }

                        try {

                            Transaction loadedTransaction =
                                    Transaction.fromStorage(
                                            transaction
                                    );

                            account.getTransactions()
                                    .add(loadedTransaction);

                        } catch (Exception error) {

                            System.out.println(
                                    "Skipped invalid transaction data."
                            );
                        }
                    }
                }

                accounts.add(account);
            }

        } catch (Exception error) {

            System.out.println(
                    "Unable to load account data."
            );
        }

        return accounts;
    }

    public static boolean saveAccounts(
            List<Account> accounts
    ) {

        try (
                FileWriter writer =
                        new FileWriter(FILE_PATH)
        ) {

            for (Account account : accounts) {

                writer.write(
                        account.getAccountNumber()
                                + "|"
                                + account.getName()
                                + "|"
                                + account.getPin()
                                + "|"
                                + account.getAccountType()
                                + "|"
                                + account.getBalance()
                                + "|"
                                + serializeTransactions(account)
                                + System.lineSeparator()
                );
            }

            return true;

        } catch (IOException error) {

            System.out.println(
                    "Unable to save account data."
            );

            return false;
        }
    }

    private static String serializeTransactions(
            Account account
    ) {

        StringBuilder result =
                new StringBuilder();

        List<Transaction> transactions =
                account.getTransactions();

        for (int i = 0;
             i < transactions.size();
             i++) {

            Transaction transaction =
                    transactions.get(i);

            if (i > 0) {
                result.append("~");
            }

            result.append(
                    escape(transaction.getId())
            );

            result.append(";");

            result.append(
                    escape(transaction.getDate())
            );

            result.append(";");

            result.append(
                    escape(transaction.getType())
            );

            result.append(";");

            result.append(
                    transaction.getAmount()
            );

            result.append(";");

            result.append(
                    escape(transaction.getDescription())
            );

            result.append(";");

            result.append(
                    transaction.getBalanceAfter()
            );

            result.append(";");

            result.append(
                    escape(
                            transaction.getRelatedAccount()
                    )
            );
        }

        return result.toString();
    }

    private static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("~", "\\~")
                .replace("|", "\\|");
    }

    private static void createDefaultAccounts() {

        List<Account> accounts =
                new ArrayList<>();

        accounts.add(
                new Account(
                        "100001",
                        "Adrian Joseph",
                        "1234",
                        "Savings",
                        15000
                )
        );

        accounts.add(
                new Account(
                        "100002",
                        "Test User",
                        "5678",
                        "Savings",
                        10000
                )
        );

        saveAccounts(accounts);
    }
}