// In your RegisterPage.java, update the registerUser method:

private void registerUser() {
    String username = usernameField.getText().trim();
    String email = emailField.getText().trim();
    String phone = phoneField.getText().trim();
    String password = new String(passwordField.getPassword());
    String confirmPassword = new String(confirmPasswordField.getPassword());
    
    // Validate inputs with more specific error messages
    if (!Validator.isValidUsername(username)) {
        JOptionPane.showMessageDialog(this, 
            "Username must be 5-20 characters and can only contain letters, numbers, and underscores", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (!Validator.isValidEmail(email)) {
        JOptionPane.showMessageDialog(this, 
            "Please enter a valid email address (e.g., user@example.com)", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (!Validator.isValidPhone(phone)) {
        JOptionPane.showMessageDialog(this, 
            "Please enter a valid 10-digit phone number", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (!Validator.isStrongPassword(password)) {
        JOptionPane.showMessageDialog(this, 
            "Password must be at least 8 characters long and contain both letters and numbers", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (!password.equals(confirmPassword)) {
        JOptionPane.showMessageDialog(this, 
            "Passwords do not match", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Rest of your registration logic...
}