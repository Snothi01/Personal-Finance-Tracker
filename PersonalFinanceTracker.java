import javax.swing.SwingUtilities;

public class PersonalFinanceTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinanceTrackerGUI());
    }
}
