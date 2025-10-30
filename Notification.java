import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Notification implements Serializable {
    private String id;
    private String title;
    private String message;
    private LocalDateTime when;
    private boolean sent;

    public Notification(String title, String message, LocalDateTime when) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.message = message;
        this.when = when;
        this.sent = false;
    }

    /* The get and set method */
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public LocalDateTime getWhen() { return when; }
    public boolean isSent() { return sent; }

    public void setTitle(String title) { this.title = title; }
    public void setMessage(String message) { this.message = message; }
    public void setWhen(LocalDateTime when) { this.when = when; }
    public void setSent(boolean sent) { this.sent = sent; }
}
