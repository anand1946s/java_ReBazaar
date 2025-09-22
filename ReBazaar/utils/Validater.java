package utils;

import database.UserDAO;

public class Validater {
	/**
	 * Validate credentials: returns true only when username/password are non-empty
	 * and match a record in the database.
	 */
	public static boolean login(String username, String password) {
		if (username == null || username.trim().isEmpty()) return false;
		if (password == null || password.isEmpty()) return false;
		return UserDAO.validateUser(username.trim(), password);
	}
}
