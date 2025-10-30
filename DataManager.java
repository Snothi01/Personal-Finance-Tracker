import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class DataManager {
    private static final String FILE = "finance_data.ser";
    private static final String LOG = "finance_log.txt";

    public static void save(List<Transaction> load) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(txs);
            log("Saved " + txs.size() + " transactions");
        } catch (IOException e) {
            log("Save error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Transaction> load() {
        File f = new File(FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            List<Transaction> txs = (List<Transaction>) ois.readObject();
            log("Loaded " + txs.size() + " transactions");
            return txs;
        } catch (Exception e) {
            log("Load error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void log(String msg) {
        log(msg, LOG);
    }

    private static void log(String msg, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            pw.println(LocalDate.now() + " " + java.time.LocalTime.now() + " - " + msg);
        } catch (IOException ignored) {}
    }
}
