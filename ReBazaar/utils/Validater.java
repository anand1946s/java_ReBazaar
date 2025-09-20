package utils;

import database.UserDAO;

public class Validater {
	// Validates login credentials via the UserDAO
	public static boolean login(String username, String password) {
		if (username == null || password == null) return false;
		username = username.trim();
		if (username.isEmpty() || password.isEmpty()) return false;
		return UserDAO.validateUser(username, password);
	}
}
