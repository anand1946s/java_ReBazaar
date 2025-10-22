package utils;

import database.UserDAO;

public class Validater {
	
	public static boolean login(String username, String password) {
		if (username == null || username.trim().isEmpty()) return false;
		if (password == null || password.isEmpty()) return false;
		return UserDAO.validateUser(username.trim(), password);
	}
}
