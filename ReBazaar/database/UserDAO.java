package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
	// ...existing code...

	static {
		try {
			init();
		} catch (SQLException e) {
			// print stack for debugging; in production log appropriately
			e.printStackTrace();
		}
	}

	public static void init() throws SQLException {
		try (Connection conn = DBConnection.getConnection();
			 Statement st = conn.createStatement()) {
			st.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "username TEXT UNIQUE NOT NULL, "
					+ "password TEXT NOT NULL, "
					+ "created_at DATETIME DEFAULT CURRENT_TIMESTAMP"
					+ ")");
		}
	}

	/**
	 * Inserts a new user.
	 * Returns true if inserted, false if username already exists.
	 * Throws SQLException for unexpected DB errors.
	 */
	public static boolean insertUser(String username, String password) throws SQLException {
		String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password);
			int r = ps.executeUpdate();
			return r > 0;
		} catch (SQLException e) {
			String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
			// unique constraint -> username already exists
			if (msg.contains("unique") || msg.contains("constraint")) {
				return false;
			}
			// rethrow other SQL exceptions so caller can handle/log them
			throw e;
		}
	}

	/**
	 * Verifies credentials. Returns true if a user with the given username and password exists.
	 */
	public static boolean validateUser(String username, String password) {
		String sql = "SELECT 1 FROM users WHERE username = ? AND password = ? LIMIT 1";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns all users as a list of String arrays: {username, password, created_at}
	 * Throws SQLException on DB errors so callers can present useful messages.
	 */
	public static java.util.List<String[]> getAllUsers() throws SQLException {
		String sql = "SELECT username, password, created_at FROM users ORDER BY id";
		java.util.List<String[]> list = new java.util.ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(new String[] {
					rs.getString("username"),
					rs.getString("password"),
					rs.getString("created_at")
				});
			}
		}
		return list;
	}
}
