import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class userRegister {
	private static final String DATABSASE_URL = "jdbc:sqlite:boiyer_pata_users.db";
	
	public static boolean registerUser(String name, String mobile, String email, String username, String password, String address) {
		String sql = "INSERT INTO users(full_name, mobile, email, username, password, address) VALUES(?,?,?,?,?,?)";
		
		try(Connection conn = DriverManager.getConnection(DATABSASE_URL)){
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, mobile);
            pstmt.setString(3, email);
            pstmt.setString(4, username);
            pstmt.setString(5, password);
            pstmt.setString(6, address);
            
            pstmt.executeUpdate();
          
            return true;
            
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while registaring");
			return false;
		} 
	}
}
