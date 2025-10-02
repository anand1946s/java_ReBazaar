package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.sql.SQLException;

import database.UserDAO;

public class UserProfile extends JFrame {

    private static final long serialVersionUID = 1L;

    // --- UI Component Palette (consistent with Dashboard) ---
    private static final Color COLOR_MAIN_BG = new Color(248, 250, 248);
    private static final Color COLOR_BUTTON_GREEN = new Color(89, 179, 130);
    private static final Color COLOR_TEXT_DARK = new Color(50, 50, 50);
    private static final Color COLOR_TEXT_SECONDARY = new Color(150, 150, 150);
    private static final Color COLOR_BORDER = new Color(220, 220, 220);

    private String loggedInUser;
    private JTextField firstNameField, lastNameField;
    private JTextField usernameField;
    private JPasswordField newPasswordField, confirmPasswordField;

    public UserProfile(String user) {
        super("User Profile");
        this.loggedInUser = user;
        initUI();
    }

    private void initUI() {
        // Use DISPOSE_ON_CLOSE so it doesn't exit the main dashboard
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(COLOR_MAIN_BG);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // --- Title ---
        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(COLOR_TEXT_DARK);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(titleLabel, gbc);

        // --- Profile Header Panel ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(createProfileHeaderPanel(), gbc);
        
        // --- General Information Panel ---
        gbc.gridy++;
        mainPanel.add(createGeneralInfoPanel(), gbc);

        // --- Security Panel ---
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(createSecurityPanel(), gbc);
        
        // --- Spacer to push content up ---
        gbc.gridy++;
        gbc.weighty = 1.0;
        mainPanel.add(Box.createVerticalGlue(), gbc);
    }

    private JPanel createProfileHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panel.setOpaque(false);

        // Profile picture placeholder
        ProfilePicturePanel picPanel = new ProfilePicturePanel();
        panel.add(picPanel);

        // User details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(loggedInUser);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameLabel.setForeground(COLOR_TEXT_DARK);
        
        JLabel phoneLabel = new JLabel("+91 9847358174"); // Placeholder phone number
        phoneLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        phoneLabel.setForeground(COLOR_TEXT_SECONDARY);

        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(phoneLabel);

        panel.add(detailsPanel);
        return panel;
    }
    
    private JPanel createGeneralInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDER), 
            " General Information ", 
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
            javax.swing.border.TitledBorder.DEFAULT_POSITION, 
            new Font("SansSerif", Font.BOLD, 14), 
            COLOR_TEXT_DARK
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.weightx = 1.0;

        // First Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        panel.add(createStyledLabel("First Name"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        firstNameField = createStyledTextField();
        panel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createStyledLabel("Last Name"), gbc);
        gbc.gridx = 1;
        lastNameField = createStyledTextField();
        panel.add(lastNameField, gbc);

        // Update Button
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JButton updateButton = createStyledButton("Update");
        updateButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "General information updated!"));
        panel.add(updateButton, gbc);
        
        return panel;
    }
    
    private JPanel createSecurityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDER), 
            " Security ", 
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
            javax.swing.border.TitledBorder.DEFAULT_POSITION, 
            new Font("SansSerif", Font.BOLD, 14), 
            COLOR_TEXT_DARK
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.weightx = 1.0;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        panel.add(createStyledLabel("Username"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        usernameField = createStyledTextField();
        usernameField.setText(loggedInUser);
        usernameField.setEditable(false);
        usernameField.setBackground(new Color(235, 235, 235));
        panel.add(usernameField, gbc);
        
        // New Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createStyledLabel("New Password"), gbc);
        gbc.gridx = 1;
        newPasswordField = createStyledPasswordField();
        panel.add(newPasswordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createStyledLabel("Confirm Password"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = createStyledPasswordField();
        panel.add(confirmPasswordField, gbc);
        
        // Change Password Button
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JButton changePassButton = createStyledButton("Change Password");
        changePassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] newPass = newPasswordField.getPassword();
                char[] confirmPass = confirmPasswordField.getPassword();
                try {
                    if (newPass.length == 0 || confirmPass.length == 0) {
                        JOptionPane.showMessageDialog(UserProfile.this, "Password fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (!Arrays.equals(newPass, confirmPass)) {
                        JOptionPane.showMessageDialog(UserProfile.this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String newPasswordStr = new String(newPass);
                        try {
                            boolean changed = UserDAO.changePassword(loggedInUser, newPasswordStr);
                            if (changed) {
                                JOptionPane.showMessageDialog(UserProfile.this, "Password changed successfully!");
                                newPasswordField.setText("");
                                confirmPasswordField.setText("");
                            } else {
                                JOptionPane.showMessageDialog(UserProfile.this, "Failed to change password: user not found.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(UserProfile.this, "Database error while changing password.", "Error", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            // zero-out the temporary String reference by overwriting char arrays
                            Arrays.fill(newPass, '\0');
                            Arrays.fill(confirmPass, '\0');
                        }
                    }
                } finally {
                    // ensure sensitive char arrays are cleared even if earlier checks failed
                    Arrays.fill(newPass, '\0');
                    Arrays.fill(confirmPass, '\0');
                }
            }
        });
        panel.add(changePassButton, gbc);
        
        return panel;
    }
    
    // --- Helper Methods for Styling Components ---
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(COLOR_TEXT_DARK);
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return textField;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return passField;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(COLOR_BUTTON_GREEN);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // --- Custom Panel to draw a circular profile picture icon ---
    private static class ProfilePicturePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        ProfilePicturePanel() {
            setPreferredSize(new Dimension(80, 80));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw gray circle background
            g2d.setColor(new Color(220, 220, 220));
            g2d.fill(new Ellipse2D.Float(0, 0, getWidth(), getHeight()));
            
            // Draw a simple user icon inside the circle
            g2d.setColor(Color.WHITE);
            g2d.fill(new Ellipse2D.Float(getWidth() * 0.25f, getHeight() * 0.2f, getWidth() * 0.5f, getHeight() * 0.4f)); // Head
            g2d.fill(new Ellipse2D.Float(getWidth() * 0.1f, getHeight() * 0.65f, getWidth() * 0.8f, getHeight() * 0.5f)); // Body
            
            g2d.dispose();
        }
    }
}