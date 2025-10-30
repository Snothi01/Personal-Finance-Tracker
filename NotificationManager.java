import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class NotificationManager {
    private static final String FILE_NAME = "notifications.dat";
    private List<Notification> notifications;

    public NotificationManager() {
        notifications = load();
    }

    // The CRUD Method
    public void create(Notification n) {
        notifications.add(n);
        save();
    }

    public Notification read(String id) {
        for (Notification n : notifications) {
            if (n.getId().equals(id)) return n;
        }
        return null;
    }

    public void update(Notification n) {
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getId().equals(n.getId())) {
                notifications.set(i, n);
                save();
                return;
            }
        }
    }

    public void delete(String id) {
        notifications.removeIf(n -> n.getId().equals(id));
        save();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    // The Storage
    @SuppressWarnings("unchecked")
    private List<Notification> load() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Notification>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(notifications);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
