package ui;

import database.UserDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;

public class RegisterPage extends JFrame {

    private static final long serialVersionUID = 1L;

    
    private static final Color COLOR_BACKGROUND = new Color(22, 160, 133);
    private static final Color COLOR_PANEL_DARK = new Color(44, 62, 80);
    private static final Color COLOR_TEXT_FIELD = new Color(52, 73, 94);
    private static final Color COLOR_BUTTON = new Color(26, 188, 156);
    private static final Color COLOR_TEXT_PRIMARY = Color.WHITE;
    private static final Color COLOR_TEXT_SECONDARY = new Color(149, 165, 166);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;

    public RegisterPage() {
        super("Register");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setResizable(true);

        
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(COLOR_BACKGROUND);
        setContentPane(backgroundPanel);

        
        RoundedPanel formPanel = new RoundedPanel(50, COLOR_PANEL_DARK);
        formPanel.setLayout(new GridBagLayout());
    
        formPanel.setPreferredSize(new Dimension(380, 580)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        
        
        JLabel profileIcon = new JLabel("👤"); 
        profileIcon.setFont(new Font("SansSerif", Font.BOLD, 50));
        profileIcon.setForeground(COLOR_TEXT_SECONDARY);
        profileIcon.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(10, 15, 10, 15); 
        formPanel.add(profileIcon, gbc);
        
        
        gbc.gridy++;
        JLabel welcomeLabel = createStyledLabel("Welcome to ReBazaar", 22, COLOR_TEXT_PRIMARY);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(5, 15, 10, 15); 
        formPanel.add(welcomeLabel, gbc);

        
        gbc.gridy++;
        JLabel titleLabel = createStyledLabel("Create Account", 20, COLOR_TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(5, 15, 20, 15); 
        formPanel.add(titleLabel, gbc);

        
        gbc.insets = new Insets(10, 25, 5, 25);

        
        gbc.gridy++;
        formPanel.add(createStyledLabel("USERNAME", 12, COLOR_TEXT_SECONDARY), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 25, 10, 25);
        usernameField = createStyledTextField();
        formPanel.add(usernameField, gbc);

        
        gbc.gridy++;
        gbc.insets = new Insets(5, 25, 5, 25);
        formPanel.add(createStyledLabel("PASSWORD", 12, COLOR_TEXT_SECONDARY), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 25, 10, 25);
        passwordField = createStyledPasswordField();
        formPanel.add(passwordField, gbc);

        
        gbc.gridy++;
        gbc.insets = new Insets(5, 25, 5, 25);
        formPanel.add(createStyledLabel("CONFIRM PASSWORD", 12, COLOR_TEXT_SECONDARY), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 25, 10, 25);
        confirmField = createStyledPasswordField();
        formPanel.add(confirmField, gbc);

        
        gbc.gridy++;
        gbc.insets = new Insets(20, 25, 20, 25);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        JButton registerButton = createStyledButton("Register");
        JButton backButton = createStyledButton("Back to Login");

        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);
        formPanel.add(buttonPanel, gbc);
        
        
        backgroundPanel.add(formPanel, new GridBagConstraints());
        
        
        getRootPane().setDefaultButton(registerButton);

        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText().trim();
                String pass = new String(passwordField.getPassword());
                String conf = new String(confirmField.getPassword());
                if (user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPage.this, "Please fill all fields", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!pass.equals(conf)) {
                    JOptionPane.showMessageDialog(RegisterPage.this, "Passwords do not match", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    boolean ok = UserDAO.insertUser(user, pass);
                    if (ok) {
                        JOptionPane.showMessageDialog(RegisterPage.this, "Signup complete!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(RegisterPage.this, "Signup failed: username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(RegisterPage.this, "Database error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
                dispose();
            }
        });
    }



    private JLabel createStyledLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        label.setForeground(color);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setBackground(COLOR_TEXT_FIELD);
        textField.setForeground(COLOR_TEXT_PRIMARY);
        textField.setCaretColor(COLOR_TEXT_PRIMARY);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(new EmptyBorder(10, 10, 10, 10));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passField = new JPasswordField(20);
        passField.setBackground(COLOR_TEXT_FIELD);
        passField.setForeground(COLOR_TEXT_PRIMARY);
        passField.setCaretColor(COLOR_TEXT_PRIMARY);
        passField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passField.setBorder(new EmptyBorder(10, 10, 10, 10));
        return passField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(COLOR_BUTTON);
        button.setForeground(COLOR_TEXT_PRIMARY);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 12, 12, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    

    private static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(backgroundColor);
            g2d.fill(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, arcs.width, arcs.height));
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterPage().setVisible(true));
    }
}
