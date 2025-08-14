import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginWindow extends JFrame {
    JTextField tf_username;
    JPasswordField tf_password;
    JButton btn_login, btn_back;
    JLabel lbl_user, lbl_pass;

    private static final String DATABASE_URL = "jdbc:sqlite:boiyer_pata_users.db";

    public LoginWindow() {
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(200, 220, 240));
        setTitle("Boiyer Pata - Login");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        lbl_user = new JLabel("Username:");
        lbl_user.setBounds(100, 80, 80, 30);
        add(lbl_user);

        tf_username = new JTextField();
        tf_username.setBounds(190, 80, 200, 30);
        add(tf_username);

        lbl_pass = new JLabel("Password:");
        lbl_pass.setBounds(100, 130, 80, 30);
        add(lbl_pass);

        tf_password = new JPasswordField();
        tf_password.setBounds(190, 130, 200, 30);
        add(tf_password);

        btn_login = new JButton("Login");
        btn_login.setBounds(190, 180, 90, 30);
        add(btn_login);

        btn_back = new JButton("Back");
        btn_back.setBounds(300, 180, 90, 30);
        add(btn_back);

        btn_login.addActionListener(e -> loginUser());

        btn_back.addActionListener(e -> {
            this.dispose();
            new Main();
        });

    }

    private void loginUser() {
        String username = tf_username.getText();
        String password = new String(tf_password.getPassword());

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                this.dispose();
                new BookCatalogue(username);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
