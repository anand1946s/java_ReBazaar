package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import utils.Validater; // Assuming you have this package

public class LoginPage extends JFrame {

    private static final long serialVersionUID = 1L;

    // --- UI Component Palette ---
    private static final Color COLOR_BACKGROUND = new Color(22, 160, 133);
    private static final Color COLOR_PANEL_DARK = new Color(44, 62, 80);
    private static final Color COLOR_TEXT_FIELD = new Color(52, 73, 94);
    private static final Color COLOR_BUTTON = new Color(26, 188, 156);
    private static final Color COLOR_TEXT_PRIMARY = Color.WHITE;
    private static final Color COLOR_TEXT_SECONDARY = new Color(149, 165, 166);

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        super("Login");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(900, 600));
        setLocationRelativeTo(null); // Center the frame
        setResizable(true);

        // Main background panel
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(COLOR_BACKGROUND);
        setContentPane(backgroundPanel);

        // The central, dark, rounded login form
        RoundedPanel formPanel = new RoundedPanel(50, COLOR_PANEL_DARK);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(380, 500));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // --- Components inside the form panel ---

        // 1. Profile Icon
        JLabel profileIcon = new JLabel("👤");
        profileIcon.setFont(new Font("SansSerif", Font.BOLD, 50));
        profileIcon.setForeground(COLOR_TEXT_SECONDARY);
        profileIcon.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(10, 15, 10, 15);
        formPanel.add(profileIcon, gbc);
        
        // 2. Welcome Message
        gbc.gridy++;
        JLabel welcomeLabel = createStyledLabel("Welcome to ReBazaar", 22, COLOR_TEXT_PRIMARY);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(welcomeLabel, gbc);

        // 3. Login Title
        gbc.gridy++;
        JLabel loginTitle = createStyledLabel("LOGIN", 18, COLOR_TEXT_PRIMARY);
        loginTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_BUTTON));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 15, 10, 15);
        formPanel.add(loginTitle, gbc);
        
        // Reset constraints for fields
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 25, 5, 25);

        // 4. Username Field
        gbc.gridy++;
        formPanel.add(createStyledLabel("USERNAME", 12, COLOR_TEXT_SECONDARY), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 25, 10, 25);
        usernameField = createStyledTextField();
        formPanel.add(usernameField, gbc);

        // 5. Password Field
        gbc.gridy++;
        gbc.insets = new Insets(5, 25, 5, 25);
        formPanel.add(createStyledLabel("PASSWORD", 12, COLOR_TEXT_SECONDARY), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 25, 10, 25);
        passwordField = createStyledPasswordField();
        formPanel.add(passwordField, gbc);

        // 6. Submit (Login) and Sign Up Buttons Panel
        gbc.gridy++;
        gbc.insets = new Insets(20, 25, 5, 25);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        JButton loginButton = createStyledButton("Submit");
        JButton signupButton = createStyledButton("Sign Up");

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        formPanel.add(buttonPanel, gbc);
        
        // 7. Forgot Password Link
        gbc.gridy++;
        gbc.insets = new Insets(10, 25, 15, 25);
        JLabel forgotPassword = new JLabel("Forgot Your Password?");
        forgotPassword.setForeground(COLOR_TEXT_SECONDARY);
        forgotPassword.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgotPassword.setHorizontalAlignment(SwingConstants.CENTER);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(forgotPassword, gbc);

        // Add the finished form panel to the background
        backgroundPanel.add(formPanel, new GridBagConstraints());
        
        // Make login button the default for 'Enter' key
        getRootPane().setDefaultButton(loginButton);

        // --- Action Listeners ---
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText(); 
                String pass = new String(passwordField.getPassword());

                // Validate against database
                if (Validater.login(user, pass)) {
                    // Open dashboard and close login
                    SwingUtilities.invokeLater(() -> new Dashboard(user).setVisible(true));
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this,
                            "Invalid username or password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open register page and close login
                SwingUtilities.invokeLater(() -> new RegisterPage().setVisible(true));
                dispose();
            }
        });
    }

    // --- Helper methods for creating styled components ---

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
    
    // --- Custom JPanel class for rounded corners ---

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
        // Run the UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
