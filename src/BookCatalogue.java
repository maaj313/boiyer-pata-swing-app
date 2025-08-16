import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BookCatalogue extends JFrame {
    JTable table;
    JButton btn_order, btn_logout, btn_request;
    DefaultTableModel model;
    String currentUser;
    private static final String DATABASE_URL = "jdbc:sqlite:boiyer_pata_users.db";

    public BookCatalogue(String username) {
        this.currentUser = username;
        this.setIconImage(Main.icon_boiyer_pata.getImage());

        setTitle("Boiyer Pata - Book Catalogue");
        setSize(1024, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        // Container setup
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.setBackground(new Color(167, 199, 231));   

        // Welcome label
        JLabel lblWelcome = new JLabel("Welcome, " + username + " | Browse and Order Books", JLabel.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        c.add(lblWelcome, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Title", "Author", "Pages", "Copies Available"};
        model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { 
            	return false; 
            }
        };
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(new Color(167, 199, 231));
        c.add(sp, BorderLayout.CENTER);

        // Buttons
        btn_order = new JButton("Order Selected Book");
        btn_order.setBackground(Color.green);
        btn_logout = new JButton("Logout");
        btn_logout.setBackground(Color.red);
        btn_logout.setForeground(Color.white);
        btn_request = new JButton("Request a Book");
        btn_logout.setCursor(Main.cur);
        btn_order.setCursor(Main.cur);
        btn_request.setCursor(Main.cur);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(167, 199, 231)); 
        bottomPanel.add(btn_order);
        bottomPanel.add(btn_logout);
        bottomPanel.add(btn_request);
        c.add(bottomPanel, BorderLayout.SOUTH);

        loadBooksFromDB();

        btn_order.addActionListener(e -> orderBook());
        btn_logout.addActionListener(e -> {
            this.dispose();
            new Main();
        });
        
        btn_request.addActionListener(e -> {
            new BookRequestWindow(currentUser);
        });

        
    }

    private void loadBooksFromDB() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("pages"),
                    rs.getInt("copies")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
        }
    }

    private void orderBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) model.getValueAt(selectedRow, 0);
            String bookTitle = model.getValueAt(selectedRow, 1).toString();
            int copies = (int) model.getValueAt(selectedRow, 4);

            if (copies > 0) {
                try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
                    conn.setAutoCommit(false);

                    // Reduce copies
                    PreparedStatement updateBook = conn.prepareStatement("UPDATE books SET copies = copies - 1 WHERE id = ?");
                    updateBook.setInt(1, bookId);
                    updateBook.executeUpdate();

                    // Insert order
                    PreparedStatement insertOrder = conn.prepareStatement("INSERT INTO orders (username, book_id) VALUES (?, ?)");
                    insertOrder.setString(1, currentUser);
                    insertOrder.setInt(2, bookId);
                    insertOrder.executeUpdate();

                    conn.commit();
                    JOptionPane.showMessageDialog(this, "You have ordered: " + bookTitle + "\nDelivery will be on Friday.");
                    loadBooksFromDB();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error ordering book: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "No copies available for this book.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to order.");
        }
    }
}
