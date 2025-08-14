import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class BookCatalogue extends JFrame {
    JTable table;
    JButton btn_order, btn_logout;
    DefaultTableModel model;

    public BookCatalogue(String username) {
        setTitle("Boiyer Pata - Book Catalogue");
        setSize(1024, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // Welcome label
        JLabel lblWelcome = new JLabel("Welcome, " + username + " | Browse and Order Books", JLabel.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        c.add(lblWelcome, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Title", "Author", "Pages", "Availability"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        c.add(sp, BorderLayout.CENTER);

        // Books
        addBooks();

        // Buttons
        JPanel bottomPanel = new JPanel();
        btn_order = new JButton("Order Selected Book");
        bottomPanel.add(btn_order);
        
        btn_logout = new JButton("Logout");
        bottomPanel.add(btn_logout);
        c.add(bottomPanel, BorderLayout.SOUTH);

        // Action for ordering
        btn_order.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String bookTitle = model.getValueAt(selectedRow, 0).toString();
                String status = model.getValueAt(selectedRow, 3).toString();

                if (status.equalsIgnoreCase("Available")) {
                    JOptionPane.showMessageDialog(this, "You have ordered: " + bookTitle + "\nDelivery on Friday.");
                    model.setValueAt("Ordered", selectedRow, 3); // mark as ordered
                } else {
                    JOptionPane.showMessageDialog(this, "Sorry, this book is not available right now.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to order.");
            }
        });

        // Logout action
        btn_logout.addActionListener(e -> {
            this.dispose();
            new Main();
        });

        
    }

    private void addBooks() {
        model.addRow(new Object[]{"The Alchemist", "Paulo Coelho", 197, "Available"});
        model.addRow(new Object[]{"1984", "George Orwell", 328, "Available"});
        model.addRow(new Object[]{"To Kill a Mockingbird", "Harper Lee", 281, "Available"});
        model.addRow(new Object[]{"A Game of Thrones", "George R.R. Martin", 694, "Available"});
    }
}
