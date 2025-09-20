package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {
	private static final long serialVersionUID = 1L;
	private final String username;

	public Dashboard(String username) {
		super("Dashboard - ReBazaar");
		this.username = username != null ? username : "User";
		initUI();
	}

	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLocationRelativeTo(null);

		// Top bar with greeting and logout
		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(new Color(34, 139, 34));
		top.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

		JLabel title = new JLabel("Welcome, " + username);
		title.setForeground(Color.WHITE);
		title.setFont(new Font("SansSerif", Font.BOLD, 20));
		top.add(title, BorderLayout.WEST);

		JButton logout = new JButton("Logout");
		logout.setBackground(new Color(220, 53, 69));
		logout.setForeground(Color.WHITE);
		logout.setFocusPainted(false);
		top.add(logout, BorderLayout.EAST);

		getContentPane().add(top, BorderLayout.NORTH);

		// Sample product data
		Object[][] items = new Object[][] {
			{"Wireless Headphones", "$59.99"},
			{"Smartphone Stand", "$12.49"},
			{"Bluetooth Speaker", "$29.00"},
			{"USB-C Charger", "$15.75"},
			{"Gaming Mouse", "$39.99"},
			{"Mechanical Keyboard", "$89.50"},
			{"HD Webcam", "$49.99"},
			{"External SSD 1TB", "$119.00"},
			{"Fitness Tracker", "$69.95"}
		};

		// Grid panel for product cards
		JPanel grid = new JPanel(new GridLayout(0, 3, 12, 12));
		grid.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		for (Object[] it : items) {
			grid.add(createProductCard((String) it[0], (String) it[1]));
		}

		JScrollPane scroll = new JScrollPane(grid);
		getContentPane().add(scroll, BorderLayout.CENTER);

		// Logout action
		logout.addActionListener(new ActionListener() {
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

		setVisible(true);
	}

	private JPanel createProductCard(String name, String price) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
		p.setBackground(Color.WHITE);

		// placeholder image area
		JLabel img = new JLabel();
		img.setPreferredSize(new Dimension(200, 120));
		img.setHorizontalAlignment(SwingConstants.CENTER);
		img.setText("<html><center>Image<br>Placeholder</center></html>");
		img.setForeground(Color.DARK_GRAY);
		p.add(img, BorderLayout.NORTH);

		// name + price
		JPanel info = new JPanel(new BorderLayout());
		info.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
		JLabel nm = new JLabel(name);
		nm.setFont(new Font("SansSerif", Font.BOLD, 14));
		info.add(nm, BorderLayout.NORTH);
		JLabel pr = new JLabel(price);
		pr.setForeground(new Color(34,139,34));
		pr.setFont(new Font("SansSerif", Font.BOLD, 13));
		info.add(pr, BorderLayout.CENTER);

		// buy button
		JButton buy = new JButton("Buy");
		buy.setBackground(new Color(46, 204, 113));
		buy.setForeground(Color.WHITE);
		buy.setFocusPainted(false);
		buy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Dashboard.this,
						"Added to cart: " + name,
						"Buy",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		south.setOpaque(false);
		south.add(buy);

		p.add(info, BorderLayout.CENTER);
		p.add(south, BorderLayout.SOUTH);

		return p;
	}
}
