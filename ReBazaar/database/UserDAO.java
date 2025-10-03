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

			// ensure columns for general info exist; ALTER may fail if column already exists, so ignore errors
			try { st.executeUpdate("ALTER TABLE users ADD COLUMN first_name TEXT"); } catch (SQLException ex) { /* ignore */ }
			try { st.executeUpdate("ALTER TABLE users ADD COLUMN last_name TEXT"); } catch (SQLException ex) { /* ignore */ }
			try { st.executeUpdate("ALTER TABLE users ADD COLUMN contact TEXT"); } catch (SQLException ex) { /* ignore */ }
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

	/**
	 * Changes the password for the given username.
	 * Returns true if the password was updated (user existed), false if no rows were affected.
	 * Throws SQLException on DB errors.
	 */
	public static boolean changePassword(String username, String newPassword) throws SQLException {
		String sql = "UPDATE users SET password = ? WHERE username = ?";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, newPassword);
			ps.setString(2, username);
			int updated = ps.executeUpdate();
			return updated > 0;
		}
	}

	// --- new helpers for general info and contact ---

	/**
	 * Update first name, last name and contact for a user.
	 * Returns true if updated (user existed).
	 */
	public static boolean updateGeneralInfo(String username, String firstName, String lastName, String contact) throws SQLException {
		String sql = "UPDATE users SET first_name = ?, last_name = ?, contact = ? WHERE username = ?";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, contact);
			ps.setString(4, username);
			int updated = ps.executeUpdate();
			return updated > 0;
		}
	}

	/**
	 * Return contact number for username or null if not found.
	 */
	public static String getContact(String username) {
		String sql = "SELECT contact FROM users WHERE username = ? LIMIT 1";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return rs.getString("contact");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return {first_name, last_name, contact} for username, or null if not found.
	 */
	public static String[] getUserInfo(String username) throws SQLException {
		String sql = "SELECT first_name, last_name, contact FROM users WHERE username = ? LIMIT 1";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new String[] {
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("contact")
					};
				}
			}
		}
		return null;
	}
}
