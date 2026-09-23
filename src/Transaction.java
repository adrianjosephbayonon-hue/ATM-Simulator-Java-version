import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transaction {

    private String id;
    private String date;
    private String type;
    private double amount;
    private String description;
    private double balanceAfter;
    private String relatedAccount;

    public Transaction(
            String type,
            double amount,
            String description,
            double balanceAfter,
            String relatedAccount
    ) {

        this.id =
                "TXN-"
                        + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        this.date =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd HH:mm:ss"
                                )
                        );

        this.type = type;
        this.amount = amount;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.relatedAccount = relatedAccount;
    }

    private Transaction(
            String id,
            String date,
            String type,
            double amount,
            String description,
            double balanceAfter,
            String relatedAccount
    ) {

        this.id = id;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.relatedAccount = relatedAccount;
    }

    public static Transaction fromStorage(
            String[] data
    ) {

        String id = data[0];
        String date = data[1];
        String type = data[2];
        double amount =
                Double.parseDouble(data[3]);
        String description = data[4];
        double balanceAfter =
                Double.parseDouble(data[5]);

        String relatedAccount = data[6];

        if (relatedAccount.isEmpty()) {
            relatedAccount = null;
        }

        return new Transaction(
                id,
                date,
                type,
                amount,
                description,
                balanceAfter,
                relatedAccount
        );
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getRelatedAccount() {
        return relatedAccount;
    }
}