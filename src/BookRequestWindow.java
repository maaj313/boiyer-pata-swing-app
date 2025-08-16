import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookRequestWindow extends JFrame {
    JTextField tf_book;
    JButton btn_submit, btn_cancel;
    String currentUser;
    private static final String DATABASE_URL = "jdbc:sqlite:boiyer_pata_users.db";

    public BookRequestWindow(String username) {
        this.currentUser = username;
        setIconImage(Main.icon_boiyer_pata.getImage());

        setTitle("Request a Book");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        Container c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(167, 199, 231));

        JLabel lbl = new JLabel("Enter Book Name:");
        lbl.setBounds(50, 30, 150, 30);
        c.add(lbl);

        tf_book = new JTextField();
        tf_book.setBounds(180, 30, 150, 30);
        c.add(tf_book);

        btn_submit = new JButton("Submit");
        btn_submit.setBounds(80, 100, 100, 30);
        btn_submit.setCursor(Main.cur);
        c.add(btn_submit);

        btn_cancel = new JButton("Cancel");
        btn_cancel.setBounds(200, 100, 100, 30);
        btn_cancel.setCursor(Main.cur);
        c.add(btn_cancel);

        btn_submit.addActionListener(e -> saveRequest());
        btn_cancel.addActionListener(e -> this.dispose());

        
    }

    private void saveRequest() {
        String bookName = tf_book.getText();
        if (bookName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a book name");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO requests(username, book_name) VALUES(?,?)");
            pstmt.setString(1, currentUser);
            pstmt.setString(2, bookName);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book request submitted!");
            this.dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
