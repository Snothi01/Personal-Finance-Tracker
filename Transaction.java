import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction implements Serializable {
    private String id;
    private String description;
    private double amount;
    private TransactionType type;
    private Category category;
    private LocalDateTime date;

    public Transaction(String description, double amount, TransactionType type, Category category) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public Category getCategory() { return category; }
    public LocalDateTime getDate() { return date; }
}