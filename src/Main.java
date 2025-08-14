import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main extends JFrame{
	JLabel be_a_member, full_name, mob, email, user_name, pass, address, alr_a_mem; 
	JTextField tf_fn, tf_mob, tf_email, tf_un, tf_adr;
	JPasswordField tf_pass;
	Font font_head;
	ImageIcon icon_bp;
	Container c1;
	JButton sign_up, clear, sign_in;
	Cursor handcur;
	
	private static final String DATABASE_URL = "jdbc:sqlite:boiyer_pata_users.db";
	Main(){
		initDatabase();
		initComponents();
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 700);
		setLocationRelativeTo(null);
		setTitle("Boiyer Pata");
		setResizable(false);
		
	}
	
	private void initDatabase() {
		try(Connection conn = DriverManager.getConnection(DATABASE_URL)) {
			System.out.println("Database initialized successfully");
			
		}
		catch(Exception e) {
			System.out.println("Database initialized failed");
		}
		
	}
	
	public void initComponents() {
		//set icon
		icon_bp = new ImageIcon(getClass().getResource("Boiyer_Pata_logo.png"));
		this.setIconImage(icon_bp.getImage());
		
		//set container
		c1 = this.getContentPane();
		c1.setLayout(null);
		c1.setBackground(new Color (167, 199, 231));
		
		font_head = new Font("Arial", Font.BOLD, 20);
		
		be_a_member = new JLabel("Be a Member");
		be_a_member.setBounds(460, 140, 220, 30);
		be_a_member.setFont(font_head);
		c1.add(be_a_member);
		
		full_name = new JLabel("Full Name: ");
		full_name.setBounds(350, 190, 70, 30);
		c1.add(full_name);
		
		mob = new JLabel("Mobile Number: ");
		mob.setBounds(350, 225, 100, 30);
		c1.add(mob);
		
		email = new JLabel("Email: ");
		email.setBounds(350, 260, 100, 30);
		c1.add(email);
		
		user_name = new JLabel("User Name: ");
		user_name.setBounds(350, 295, 100, 30);
		c1.add(user_name);
		
		pass = new JLabel("Password: ");
		pass.setBounds(350, 330, 100, 30);
		c1.add(pass);
		
		address = new JLabel("Address: ");
		address.setBounds(350, 365, 100, 30);
		c1.add(address);
		///-------------------------------
		tf_fn = new JTextField();
		tf_fn.setBounds(450, 189, 220, 32);
		c1.add(tf_fn);
		
		tf_mob = new JTextField();
		tf_mob.setBounds(450, 224, 220, 32);
		c1.add(tf_mob);
		
		tf_email = new JTextField();
		tf_email.setBounds(450, 259, 220, 32);
		c1.add(tf_email);
		
		tf_un = new JTextField();
		tf_un.setBounds(450, 294, 220, 32);
		c1.add(tf_un);
		
		tf_pass = new JPasswordField();
		tf_pass.setBounds(450, 329, 220, 32);
		c1.add(tf_pass); 
		
		tf_adr = new JTextField();
		tf_adr.setBounds(450, 364, 220, 32);
		c1.add(tf_adr); 
		//-----------------------------------
		handcur = new Cursor(Cursor.HAND_CURSOR);
		
		sign_up = new JButton("Sign Up");
		sign_up.setBounds(400, 430, 100, 30);
		sign_up.setBackground(Color.green);
		sign_up.setCursor(handcur);
		c1.add(sign_up);
		
		clear = new JButton("Clear");
		clear.setBounds(520, 430, 100, 30);
		clear.setBackground(Color.red);
		clear.setForeground(Color.white);
		clear.setCursor(handcur);
		c1.add(clear);
		//--------------------------------------------
		alr_a_mem = new JLabel("Already a Member?");
		alr_a_mem.setBounds(400, 480, 150, 30);
		c1.add(alr_a_mem);
		
		sign_in = new JButton("sign in");
		sign_in.setBounds(535, 483, 75, 25);
		sign_in.setCursor(handcur);
		c1.add(sign_in);
		
		//Action Listener for Clear
		clear.addActionListener(e -> {
				tf_fn.setText(""); tf_mob.setText(""); tf_email.setText(""); tf_un.setText(""); tf_pass.setText(""); tf_adr.setText("");
		});
		
		sign_up.addActionListener(e -> {
			String name = tf_fn.getText();
	        String mobile = tf_mob.getText();
	        String email = tf_email.getText();
	        String username = tf_un.getText();
	        String password = new String(tf_pass.getPassword());
	        String address = tf_adr.getText();
	        
	        if(name.isEmpty() || mobile.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || address.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Please fill all the fields"); 
	            return; 
	        }
	        if(userRegister.registerUser(name, mobile, email, username, password, address)) {
	        	JOptionPane.showMessageDialog(this, "Thank you for Registration");
	        	tf_fn.setText(""); tf_mob.setText(""); tf_email.setText(""); tf_un.setText(""); tf_pass.setText(""); tf_adr.setText("");
	        }
		});
		
		sign_in.addActionListener(e -> {
			this.dispose();
			new LoginWindow();
		});
	}
	

	public static void main(String[] args) {
		
		
		
		Main frame1 = new Main();
		
	}
}