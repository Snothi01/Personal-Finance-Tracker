import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class FinanceTrackerGUI extends JFrame {
    private FinanceManager manager;
    private NotificationManager notifManager;

    private DefaultTableModel tableModel;
    private JTable table;

    private DefaultTableModel notifModel;
    private PieChartPanel chartPanel;

    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel netLabel;

    private SystemTray tray;
    private TrayIcon trayIcon;

    public FinanceTrackerGUI(FinanceManager manager) {
        super();
        this.manager = manager;
        this.notifManager = new NotificationManager();

        initLookAndFeel();
        initTray();
        initComponents();
        refreshAll();

        setTitle("Personal Finance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void initLookAndFeel() {
        // set font defaults
        Font base = new Font("Segoe UI", Font.PLAIN, 13);
        UIManager.put("Label.font", base);
        UIManager.put("Button.font", base.deriveFont(Font.BOLD));
        UIManager.put("TextField.font", base);
        UIManager.put("Table.font", base);
        UIManager.put("Table.rowHeight", 24);
    }

    private void initTray() {
        try {
            if (SystemTray.isSupported()) {
                tray = SystemTray.getSystemTray();
                // Create a tiny default image for the tray icon
                Image img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = (Graphics2D) img.getGraphics();
                g2.setColor(new Color(0, 166, 166));
                g2.fillOval(0, 0, 16, 16);
                g2.dispose();
                trayIcon = new TrayIcon(img, "Personal Finance Tracker");
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);
            }
        } catch (Exception e) {
            // ignore tray init errors
            e.printStackTrace();
        }
    }

    private void showTrayMessage(String title, String message) {
        if (trayIcon != null) {
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
        } else {
            // fallback to dialog
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, title + "\n" + message));
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(234, 241, 247)); // #EAF1F7

        // --> Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("🌿 Personal Finance Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBorder(new EmptyBorder(16, 16, 16, 16));
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // --> Main content area
        JPanel main = new JPanel(new GridBagLayout());
        main.setOpaque(false);
        GridBagConstraints mg = new GridBagConstraints();
        mg.insets = new Insets(8, 8, 8, 8);
        mg.fill = GridBagConstraints.BOTH;

        // Left: Input card
        RoundedPanel leftCard = new RoundedPanel(18);
        leftCard.setBackground(Color.WHITE);
        leftCard.setLayout(new GridBagLayout());
        leftCard.setPreferredSize(new Dimension(300, 380));
        GridBagConstraints lg = new GridBagConstraints();
        lg.insets = new Insets(8, 12, 8, 12);
        lg.anchor = GridBagConstraints.NORTHWEST;
        lg.fill = GridBagConstraints.HORIZONTAL;

        JLabel leftTitle = new JLabel("Input & Controls");
        leftTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lg.gridx = 0; lg.gridy = 0; lg.gridwidth = 2;
        leftCard.add(leftTitle, lg);

        lg.gridwidth = 1;
        lg.gridy++;
        leftCard.add(new JLabel("Description"), lg);
        JTextField desc = new JTextField();
        lg.gridx = 1; leftCard.add(desc, lg);

        lg.gridx = 0; lg.gridy++;
        leftCard.add(new JLabel("Amount (R)"), lg);
        JTextField amountField = new JTextField();
        lg.gridx = 1; leftCard.add(amountField, lg);

        lg.gridx = 0; lg.gridy++;
        leftCard.add(new JLabel("Type"), lg);
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        lg.gridx = 1; leftCard.add(typeBox, lg);

        lg.gridx = 0; lg.gridy++;
        leftCard.add(new JLabel("Category"), lg);
        JComboBox<Category> catBox = new JComboBox<>(Category.values());
        lg.gridx = 1; leftCard.add(catBox, lg);

        lg.gridx = 0; lg.gridy++; lg.gridwidth = 2;
        lg.fill = GridBagConstraints.NONE;
        JButton addBtn = styledButton("Add Transaction", new Color(0, 166, 166), Color.WHITE);
        addBtn.setPreferredSize(new Dimension(160, 36));
        leftCard.add(addBtn, lg);

        // center: transactions card
        RoundedPanel centerCard = new RoundedPanel(18);
        centerCard.setBackground(Color.WHITE);
        centerCard.setLayout(new BorderLayout());
        centerCard.setPreferredSize(new Dimension(500, 480));
        JLabel centerTitle = new JLabel("I/E", SwingConstants.LEFT);
        centerTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        centerTitle.setBorder(new EmptyBorder(8, 12, 6, 12));
        centerCard.add(centerTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Date", "Type", "Category", "Description", "Amount (R)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        centerCard.add(new JScrollPane(table), BorderLayout.CENTER);

        // Right: chart card
        RoundedPanel rightCard = new RoundedPanel(18);
        rightCard.setBackground(Color.WHITE);
        rightCard.setLayout(new BorderLayout());
        rightCard.setPreferredSize(new Dimension(300, 380));
        JLabel rightTitle = new JLabel("Expense Breakdown", SwingConstants.CENTER);
        rightTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rightTitle.setBorder(new EmptyBorder(8, 8, 8, 8));
        rightCard.add(rightTitle, BorderLayout.NORTH);

        chartPanel = new PieChartPanel();
        rightCard.add(chartPanel, BorderLayout.CENTER);

        // Place left, center and right into main panel
        mg.gridx = 0; mg.gridy = 0; mg.weightx = 0.18; mg.weighty = 1.0;
        main.add(leftCard, mg);
        mg.gridx = 1; mg.weightx = 0.64;
        main.add(centerCard, mg);
        mg.gridx = 2; mg.weightx = 0.18;
        main.add(rightCard, mg);

        add(main, BorderLayout.CENTER);

        // Bottom summary and action buttons
        RoundedPanel bottom = new RoundedPanel(18);
        bottom.setBackground(Color.WHITE);
        bottom.setLayout(new GridBagLayout());
        bottom.setBorder(new EmptyBorder(10, 12, 10, 12));
        GridBagConstraints bgc = new GridBagConstraints();
        bgc.insets = new Insets(6, 8, 6, 8);
        bgc.anchor = GridBagConstraints.WEST;

        incomeLabel = new JLabel("Total Income: R0.00");
        expenseLabel = new JLabel("Total Expenses: R0.00");
        netLabel = new JLabel("Net Balance: R0.00");
        netLabel.setFont(netLabel.getFont().deriveFont(Font.BOLD));

        bgc.gridx = 0; bgc.gridy = 0;
        bottom.add(incomeLabel, bgc);
        bgc.gridx = 1;
        bottom.add(expenseLabel, bgc);
        bgc.gridx = 2;
        bottom.add(netLabel, bgc);

        bgc.gridx = 3; bgc.anchor = GridBagConstraints.EAST; bgc.weightx = 1.0;
        JButton downloadBtn = styledButton("Download CSV", new Color(0, 150, 199), Color.WHITE);
        JButton deleteBtn = styledButton("Delete Selected", new Color(247, 108, 108), Color.WHITE);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(downloadBtn); actions.add(deleteBtn);
        bottom.add(actions, bgc);

        add(bottom, BorderLayout.SOUTH);

        // --- Button actions
        addBtn.addActionListener(ev -> {
            try {
                String d = desc.getText().trim();
                double amt = Double.parseDouble(amountField.getText().trim());
                TransactionType tt = typeBox.getSelectedItem().toString().equals("INCOME") ? TransactionType.INCOME : TransactionType.EXPENSE;
                Category c = (Category) catBox.getSelectedItem();
                Transaction t = new Transaction(d, amt, tt, c);
                manager.addTransaction(t);
                DataManager.save(manager.getTransactions());
                desc.setText(""); amountField.setText("");
                refreshAll();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding transaction: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(ev -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                JOptionPane.showMessageDialog(this, "Select a transaction to delete");
                return;
            }
            String id = tableModel.getValueAt(sel, 0).toString();
            manager.deleteTransaction(id);
            DataManager.save(manager.getTransactions());
            refreshAll();
        });

        downloadBtn.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    DataManager.exportCSV(manager.getTransactions(), fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "CSV exported.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error exporting CSV: " + ex.getMessage());
                }
            }
        });

        // Notifications UI: add small dialog accessible on right click on the chart area
        chartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.isPopupTrigger() || SwingUtilities.isRightMouseButton(me)) {
                    // open notifications manager frame/dialog
                    SwingUtilities.invokeLater(() -> openNotificationsDialog());
                }
            }
        });

        // Start the notification scheduler
        startNotificationScheduler();
    }

    private JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void openNotificationsDialog() {
        // Simple dialog to display the notifications tab (reuse any UI you already have)
        JDialog d = new JDialog(this, "Notifications", true);
        d.setSize(600, 420);
        d.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Top: form
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField titleField = new JTextField();
        JTextField messageField = new JTextField();
        JTextField whenField = new JTextField(); // yyyy-MM-dd HH:mm

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Title:"), g);
        g.gridx = 1; form.add(titleField, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Message:"), g);
        g.gridx = 1; form.add(messageField, g);
        g.gridx = 0; g.gridy = 2; form.add(new JLabel("When (yyyy-MM-dd HH:mm):"), g);
        g.gridx = 1; form.add(whenField, g);

        JButton create = new JButton("Create");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JPanel bpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bpanel.add(create); bpanel.add(update); bpanel.add(delete);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2; form.add(bpanel, g);

        panel.add(form, BorderLayout.NORTH);

        // Center: table of notifications
        DefaultTableModel nm = new DefaultTableModel(new Object[]{"ID", "Title", "When", "Sent"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable nt = new JTable(nm);
        panel.add(new JScrollPane(nt), BorderLayout.CENTER);

        // load current notifications into table
        nm.setRowCount(0);
        for (Notification n : notifManager.getNotifications()) {
            nm.addRow(new Object[]{n.getId(), n.getTitle(), n.getWhen().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), n.isSent()});
        }

        nt.getSelectionModel().addListSelectionListener(ev -> {
            int s = nt.getSelectedRow();
            if (s >= 0) {
                String id = nm.getValueAt(s, 0).toString();
                Notification n = notifManager.read(id);
                if (n != null) {
                    titleField.setText(n.getTitle());
                    messageField.setText(n.getMessage());
                    whenField.setText(n.getWhen().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }
            }
        });

        create.addActionListener(ev -> {
            try {
                String t = titleField.getText().trim();
                String m = messageField.getText().trim();
                LocalDateTime when = LocalDateTime.parse(whenField.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                Notification n = new Notification(t, m, when);
                notifManager.create(n);
                nm.addRow(new Object[]{n.getId(), n.getTitle(), n.getWhen().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), n.isSent()});
                titleField.setText(""); messageField.setText(""); whenField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Error: " + ex.getMessage());
            }
        });

        update.addActionListener(ev -> {
            int s = nt.getSelectedRow();
            if (s < 0) { JOptionPane.showMessageDialog(d, "Select a notification first"); return; }
            String id = nm.getValueAt(s, 0).toString();
            Notification n = notifManager.read(id);
            if (n == null) return;
            try {
                n.setTitle(titleField.getText().trim());
                n.setMessage(messageField.getText().trim());
                n.setWhen(LocalDateTime.parse(whenField.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                notifManager.update(n);
                nm.setValueAt(n.getTitle(), s, 1);
                nm.setValueAt(n.getWhen().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), s, 2);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Error: " + ex.getMessage());
            }
        });

        delete.addActionListener(ev -> {
            int s = nt.getSelectedRow();
            if (s < 0) { JOptionPane.showMessageDialog(d, "Select a notification first"); return; }
            String id = nm.getValueAt(s, 0).toString();
            notifManager.delete(id);
            nm.removeRow(s);
        });

        d.setContentPane(panel);
        d.setVisible(true);
    }

    private void startNotificationScheduler() {
        Thread scheduler = new Thread(() -> {
            while (true) {
                try {
                    List<Notification> all = notifManager.getNotifications();
                    boolean any = false;
                    for (Notification n : all) {
                        if (!n.isSent() && (n.getWhen().isBefore(LocalDateTime.now()) || n.getWhen().isEqual(LocalDateTime.now()))) {
                            showTrayMessage("Reminder: " + n.getTitle(), n.getMessage());
                            n.setSent(true);
                            notifManager.update(n);
                            any = true;
                        }
                    }
                    if (any) SwingUtilities.invokeLater(() -> refreshAll());
                    Thread.sleep(30_000); // check every 30s responsive Desktop schedule
                } catch (InterruptedException ex) {
                    return;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, "Notification-Scheduler");
        scheduler.setDaemon(true);
        scheduler.start();
    }

    private void refreshAll() {
        // for transactions
        tableModel.setRowCount(0);
        for (Transaction t : manager.getTransactions()) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getDate(),
                    t.getType(),
                    t.getCategory(),
                    t.getDescription(),
                    String.format("%.2f", t.getAmount())
            });
        }

        // the labels summary
        incomeLabel.setText(String.format("Total Income: R%.2f", manager.totalIncome()));
        expenseLabel.setText(String.format("Total Expenses: R%.2f", manager.totalExpenses()));
        netLabel.setText(String.format("Net Balance: R%.2f", manager.netBalance()));

        // update chart live: using spendingByCategory which returns Map<Category, Double>
        Map<Category, Double> map = manager.spendingByCategory();
        chartPanel.setData(map);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    public static void main(String[] args) {
        // load transactions from DataManager
        java.util.List<Transaction> loaded = DataManager.load();
        FinanceManager fm = new FinanceManager(loaded != null ? loaded : new java.util.ArrayList<>());
        SwingUtilities.invokeLater(() -> {
            new FinanceTrackerGUI(fm).setVisible(true);
        });
    }
}
