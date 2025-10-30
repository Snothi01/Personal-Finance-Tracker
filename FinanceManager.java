import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class FinanceManager {
    private final List<Transaction> transactions;

    public FinanceManager() {
        this.transactions = new ArrayList<>();
    }

    public FinanceManager(List<Transaction> loaded) {
        this.transactions = loaded != null ? loaded : new ArrayList<>();
    }

    public List<Transaction> getTransactions() { return transactions; }

    public void addTransaction(Transaction t) { transactions.add(t); }

    public boolean deleteById(String id) {
        return transactions.removeIf(t -> t.getId().equals(id));
    }

    public void clearAll() { transactions.clear(); }

    public double totalIncome() {
        return transactions.stream().filter(t -> t.getType() == TransactionType.INCOME).mapToDouble(Transaction::getAmount).sum();
    }
    public double totalExpenses() {
        return transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE).mapToDouble(Transaction::getAmount).sum();
    }
    public double netBalance() { return totalIncome() - totalExpenses(); }

    public Map<Category, Double> spendingByCategory() {
        Map<Category, Double> map = new HashMap<>();
        for (Transaction t: transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                map.merge(t.getCategory(), t.getAmount(), Double::sum);
            }
        }
        return map;
    }
}
