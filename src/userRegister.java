import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.regex.*;

public class userRegister extends JFrame {
    JTextField tf_fn, tf_mob, tf_email, tf_un, tf_adr;
    JPasswordField tf_pass;
    JButton sign_up, clear;
    Container c1;

    private static final String DATABASE_URL = "jdbc:sqlite:boiyer_pata_users.db";

    public userRegister() {
        initComponents();
    }

    public void initComponents() {
        c1 = this.getContentPane();
        c1.setLayout(null);
        c1.setBackground(new Color(167, 199, 231));

        JLabel be_a_member = new JLabel("Be a Member");
        be_a_member.setBounds(460, 100, 220, 30);
        be_a_member.setFont(new Font("Arial", Font.BOLD, 20));
        c1.add(be_a_member);

        // Labels
        JLabel full_name = new JLabel("Full Name:");
        full_name.setBounds(350, 150, 100, 30);
        c1.add(full_name);

        JLabel mob = new JLabel("Mobile:");
        mob.setBounds(350, 190, 100, 30);
        c1.add(mob);

        JLabel email = new JLabel("Email:");
        email.setBounds(350, 230, 100, 30);
        c1.add(email);

        JLabel user_name = new JLabel("Username:");
        user_name.setBounds(350, 270, 100, 30);
        c1.add(user_name);

        JLabel pass = new JLabel("Password:");
        pass.setBounds(350, 310, 100, 30);
        c1.add(pass);

        JLabel address = new JLabel("Address:");
        address.setBounds(350, 350, 100, 30);
        c1.add(address);

        // Text fields
        tf_fn = new JTextField(); 
        tf_fn.setBounds(450, 150, 220, 30); 
        c1.add(tf_fn);
        
        tf_mob = new JTextField(); 
        tf_mob.setBounds(450, 190, 220, 30); 
        c1.add(tf_mob);
        
        tf_email = new JTextField(); 
        tf_email.setBounds(450, 230, 220, 30); 
        c1.add(tf_email);
        
        tf_un = new JTextField(); 
        tf_un.setBounds(450, 270, 220, 30); 
        c1.add(tf_un);
        
        tf_pass = new JPasswordField(); 
        tf_pass.setBounds(450, 310, 220, 30); 
        c1.add(tf_pass);
        
        tf_adr = new JTextField(); 
        tf_adr.setBounds(450, 350, 220, 30); 
        c1.add(tf_adr);

        // Buttons
        sign_up = new JButton("Sign Up");
        sign_up.setBounds(400, 420, 100, 30);
        sign_up.setBackground(Color.green);
        sign_up.setCursor(Main.cur);
        c1.add(sign_up);

        clear = new JButton("Clear");
        clear.setBounds(520, 420, 100, 30);
        clear.setBackground(Color.red);
        clear.setForeground(Color.white);
        clear.setCursor(Main.cur);
        c1.add(clear);

        // Actions
        clear.addActionListener(e -> clearFields());
        sign_up.addActionListener(e -> registerUser());

        setSize(1024, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Boiyer Pata - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void registerUser() {
        String name = tf_fn.getText();
        String mobile = tf_mob.getText();
        String email = tf_email.getText();
        String username = tf_un.getText();
        String password = new String(tf_pass.getPassword());
        String address = tf_adr.getText();

        // Validation
        if (name.isEmpty() || mobile.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields");
            return;
        }

        if (!mobile.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Mobile number must be 11 digits");
            return;
        }

        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        if (!emailPattern.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email format");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            //check if username exists
            PreparedStatement checkUser = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            checkUser.setString(1, username);
            ResultSet rs = checkUser.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists, choose another.");
                return;
            }

            // Insert new user
            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO users(full_name, mobile, email, username, password, address) VALUES(?,?,?,?,?,?)");
            pstmt.setString(1, name);
            pstmt.setString(2, mobile);
            pstmt.setString(3, email);
            pstmt.setString(4, username);
            pstmt.setString(5, password);
            pstmt.setString(6, address);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!");
            this.dispose();
            new Main();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        tf_fn.setText("");
        tf_mob.setText("");
        tf_email.setText("");
        tf_un.setText("");
        tf_pass.setText("");
        tf_adr.setText("");
    }
}
