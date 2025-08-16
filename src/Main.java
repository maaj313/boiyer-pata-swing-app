import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Main extends JFrame {
    JTextField tf_username;
    JPasswordField tf_password;
    JButton btn_login, btn_clear, btn_signup;
    JLabel lbl_user, lbl_pass, lbl_not_member;
    static Cursor cur;
    static ImageIcon icon_boiyer_pata;

    private static final String DATABASE_URL = "jdbc:sqlite:boiyer_pata_users.db";

    public Main() {
        initLoginComponents();
    }

    private void initLoginComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(167, 199, 231));
        icon_boiyer_pata = new ImageIcon(getClass().getResource("Boiyer_Pata_logo.png"));
        setIconImage(Main.icon_boiyer_pata.getImage());
        cur = new Cursor(Cursor.HAND_CURSOR);
        
        //labels and text fields
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

        //buttons
        btn_login = new JButton("Login");
        btn_login.setBounds(190, 180, 90, 30);
        btn_login.setBackground(new Color(19, 189, 19));
        btn_login.setForeground(Color.white);
        btn_login.setCursor(cur);
        add(btn_login);
        
        btn_clear = new JButton("Clear");
        btn_clear.setBounds(300, 180, 90, 30);
        btn_clear.setBackground(new Color(232, 60, 95));
        btn_clear.setForeground(Color.white);
        btn_clear.setCursor(cur);
        add(btn_clear);

        // "Not a member?" section
        lbl_not_member = new JLabel("Not a member?");
        lbl_not_member.setBounds(150, 230, 100, 30);
        add(lbl_not_member);

        btn_signup = new JButton("Sign Up");
        btn_signup.setBounds(260, 230, 85, 25);
        btn_signup.setCursor(cur);
        add(btn_signup);

        // Actions
        btn_clear.addActionListener(e -> clearFields());
        btn_login.addActionListener(e -> loginUser());
        btn_signup.addActionListener(e -> {
            this.dispose();
            new userRegister();
        });

        setTitle("Boiyer Pata - Login");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
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

    private void clearFields() {
		tf_username.setText("");
		tf_password.setText("");
	}
    
    public static void main(String[] args) {
        new Main();
    }
}
