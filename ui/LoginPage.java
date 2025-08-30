package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {

    public JTextField username;
    public JPasswordField passwordField;
    public JButton loginButton;
    public JButton RegisterButton;

public LoginPage(){
    setTitle("Welcome Back");
    setSize(500, 400);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    getContentPane().setBackground(new Color(17, 46, 35));

    setExtendedState(JFrame.MAXIMIZED_BOTH);

    username = new JTextField(15);
    passwordField = new JPasswordField(15);
    loginButton = new JButton("Login");
    RegisterButton = new JButton("SignUP");

    // Use GridLayout for proper structure - 5 rows, 2 columns
    JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10)); // rows, cols, hgap, vgap
    panel.setBackground(new Color(17, 46, 35));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

    // Welcome label (centered and styled)
    JLabel welcomeLabel = new JLabel("Welcome Back!!!", SwingConstants.CENTER);
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
    welcomeLabel.setForeground(new Color(255, 215, 0)); // Gold color

    // Username row
    JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    userPanel.setBackground(new Color(17, 46, 35));
    JLabel userLabel = new JLabel("Username:");
    userLabel.setForeground(Color.WHITE);
    userPanel.add(userLabel);
    userPanel.add(username);

    // Password row
    JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    passPanel.setBackground(new Color(17, 46, 35));
    JLabel passLabel = new JLabel("Password:");
    passLabel.setForeground(Color.WHITE);
    passPanel.add(passLabel);
    passPanel.add(passwordField);

    // Button row
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // hgap=20
    buttonPanel.setBackground(new Color(17, 46, 35));
    
    // Style buttons
    loginButton.setBackground(new Color(76, 175, 80));
    loginButton.setForeground(Color.WHITE);
    loginButton.setOpaque(true);
    loginButton.setBorderPainted(false);
    
    RegisterButton.setBackground(new Color(57, 73, 171));
    RegisterButton.setForeground(Color.WHITE);
    RegisterButton.setOpaque(true);
    RegisterButton.setBorderPainted(false);
    
    buttonPanel.add(loginButton);
    buttonPanel.add(RegisterButton);

    // Add all panels in order
    panel.add(welcomeLabel);
    panel.add(userPanel);
    panel.add(passPanel);
    panel.add(buttonPanel);
    // Empty panel for spacing (5th row)
    panel.add(new JPanel()); 

    add(panel);

    loginButton.addActionListener(e -> {
        JOptionPane.showMessageDialog(this, "Login clicked!");
    });
    
    RegisterButton.addActionListener(e -> {
        RegisterPage regpg = new RegisterPage();
        regpg.setVisible(true);
        
    });
}
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}