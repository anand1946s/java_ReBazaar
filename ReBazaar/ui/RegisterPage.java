package ui;

import database.UserDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class RegisterPage extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField confirmField;
	private JButton registerButton;
	private JButton backButton;
	private JLabel titleLabel;

	public RegisterPage() {
		super("Register");
		initUI();
	}

	private void initUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		GradientPanel panel = new GradientPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		titleLabel = new JLabel("Create an account");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(titleLabel, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		JLabel userLabel = new JLabel("Username:");
		userLabel.setForeground(Color.WHITE);
		userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		panel.add(userLabel, gbc);

		usernameField = new JTextField(18);
		usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		panel.add(usernameField, gbc);

		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		JLabel passLabel = new JLabel("Password:");
		passLabel.setForeground(Color.WHITE);
		passLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		panel.add(passLabel, gbc);

		passwordField = new JPasswordField(18);
		passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		panel.add(passwordField, gbc);

		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		JLabel confLabel = new JLabel("Confirm:");
		confLabel.setForeground(Color.WHITE);
		confLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		panel.add(confLabel, gbc);

		confirmField = new JPasswordField(18);
		confirmField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		panel.add(confirmField, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonPanel.setOpaque(false);

		registerButton = new JButton("register");
		backButton = new JButton("back to login");

		Color greenPrimary = new Color(46, 204, 113);
		Color greenDark = new Color(39, 174, 96);

		registerButton.setBackground(greenPrimary);
		registerButton.setForeground(Color.WHITE);
		registerButton.setFocusPainted(false);
		registerButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
		registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
		registerButton.setOpaque(true);

		backButton.setBackground(greenDark);
		backButton.setForeground(Color.WHITE);
		backButton.setFocusPainted(false);
		backButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
		backButton.setOpaque(true);

		buttonPanel.add(backButton);
		buttonPanel.add(registerButton);

		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(buttonPanel, gbc);

		getContentPane().add(panel);

		// Actions
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
						JOptionPane.showMessageDialog(RegisterPage.this, "Signup complete", "Success", JOptionPane.INFORMATION_MESSAGE);
						// return to login
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								new LoginPage().setVisible(true);
							}
						});
						dispose();
					} else {
						// unique constraint detected by insertUser
						JOptionPane.showMessageDialog(RegisterPage.this, "Signup failed: username already exists", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException ex) {
					// show actual DB error to help debugging (permission/path/driver issues)
					JOptionPane.showMessageDialog(RegisterPage.this, "Database error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new LoginPage().setVisible(true);
					}
				});
				dispose();
			}
		});

		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(true);
	}

	// reuse the same gradient background style
	private static class GradientPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			int w = getWidth();
			int h = getHeight();
			Color c1 = new Color(24, 160, 95);
			Color c2 = new Color(46, 204, 113);
			GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, w, h);
			g2d.dispose();
		}
	}
}
