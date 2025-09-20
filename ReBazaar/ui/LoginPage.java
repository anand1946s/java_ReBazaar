package ui;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import utils.Validater;
import database.UserDAO;
import java.sql.SQLException;

public class LoginPage extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton signupButton;
	// { changed code } add showButton
	private JButton showButton;
	private JLabel titleLabel; // added

	public LoginPage() {
		super("Login");
		initUI();
	}

	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// remove fixed preferred size so frame can expand
		// setPreferredSize(new Dimension(800, 500));
		setLocationRelativeTo(null); // center on screen

		// Custom gradient background panel
		GradientPanel panel = new GradientPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Title
		titleLabel = new JLabel("Welcome to ReBazaar");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(titleLabel, gbc);

		// Username label + field
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

		// Password label + field
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

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonPanel.setOpaque(false);

		loginButton = new JButton("login");
		signupButton = new JButton("register");
		// { changed code } create and style show button
		showButton = new JButton("show");
		showButton.setBackground(new Color(52, 152, 219)); // blue-ish for contrast
		showButton.setForeground(Color.WHITE);
		showButton.setFocusPainted(false);
		showButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		showButton.setFont(new Font("SansSerif", Font.BOLD, 14));
		showButton.setOpaque(true);

		// Button styling
		Color greenPrimary = new Color(46, 204, 113);
		Color greenDark = new Color(39, 174, 96);
		loginButton.setBackground(greenPrimary);
		loginButton.setForeground(Color.WHITE);
		loginButton.setFocusPainted(false);
		loginButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
		loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
		loginButton.setOpaque(true);

		signupButton.setBackground(greenDark);
		signupButton.setForeground(Color.WHITE);
		signupButton.setFocusPainted(false);
		signupButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		signupButton.setFont(new Font("SansSerif", Font.BOLD, 14));
		signupButton.setOpaque(true);

		// { changed code } add show button to panel (left-most)
		buttonPanel.add(showButton);
		buttonPanel.add(signupButton);
		buttonPanel.add(loginButton);

		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(buttonPanel, gbc);

		getContentPane().add(panel);

		// Make login the default button so Enter triggers it
		getRootPane().setDefaultButton(loginButton);

		// Action listeners (changed login behavior)
		loginButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String user = usernameField.getText();
					String pass = new String(passwordField.getPassword());

					// validate against database
					boolean ok = Validater.login(user, pass);
					if (ok) {
						// { changed code } open dashboard and close login
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								new Dashboard(user).setVisible(true);
							}
						});
						dispose();
					} else {
						JOptionPane.showMessageDialog(LoginPage.this,
								"Invalid username or password",
								"Login failed",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

		// { changed code } show button action to display all users
		showButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					java.util.List<String[]> users = UserDAO.getAllUsers();
					if (users.isEmpty()) {
						JOptionPane.showMessageDialog(LoginPage.this, "No users found", "Users", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					String[] cols = {"Username", "Password", "Created At"};
					Object[][] data = new Object[users.size()][3];
					for (int i = 0; i < users.size(); i++) {
						String[] row = users.get(i);
						data[i][0] = row[0];
						data[i][1] = row[1];
						data[i][2] = row[2];
					}
					JTable table = new JTable(data, cols);
					table.setFillsViewportHeight(true);
					JScrollPane scroll = new JScrollPane(table);
					scroll.setPreferredSize(new Dimension(700, Math.min(400, users.size() * 22 + 40)));
					JOptionPane.showMessageDialog(LoginPage.this, scroll, "Registered users", JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(LoginPage.this, "Database error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});

		// open RegisterPage and close this LoginPage
		signupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// open register page and close login
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new RegisterPage().setVisible(true);
					}
				});
				dispose();
			}
		});

		// final layout adjustments
		pack();
		// maximize the window when it is shown
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(true);
	}

	// Gradient background panel
	private static class GradientPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			int w = getWidth();
			int h = getHeight();
			Color c1 = new Color(24, 160, 95); // darker green
			Color c2 = new Color(46, 204, 113); // lighter green
			GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, w, h);
			g2d.dispose();
		}
	}
}
