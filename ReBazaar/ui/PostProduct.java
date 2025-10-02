package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import database.ItemDAO;
import model.Product;

public class PostProduct extends JDialog {
	private Runnable onPost;

	public PostProduct(Frame owner, Runnable onPost) {
		super(owner, "Post Product", true);
		this.onPost = onPost;
		initUI();
		setSize(420, 420);
		setLocationRelativeTo(owner);
	}

	private void initUI() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0; gbc.gridy = 0;

		JLabel nameLbl = new JLabel("Title:");
		panel.add(nameLbl, gbc);
		gbc.gridx = 1;
		JTextField nameField = new JTextField();
		panel.add(nameField, gbc);

		gbc.gridx = 0; gbc.gridy++;
		JLabel categoryLbl = new JLabel("Category:");
		panel.add(categoryLbl, gbc);
		gbc.gridx = 1;
		JComboBox<String> categoryBox = new JComboBox<>(new String[] {"Furnitures", "Electronics", "Books", "Clothing", "Other"});
		panel.add(categoryBox, gbc);

		gbc.gridx = 0; gbc.gridy++;
		JLabel priceLbl = new JLabel("Price:");
		panel.add(priceLbl, gbc);
		gbc.gridx = 1;
		JTextField priceField = new JTextField();
		panel.add(priceField, gbc);

		gbc.gridx = 0; gbc.gridy++;
		JLabel descLbl = new JLabel("Description:");
		panel.add(descLbl, gbc);
		gbc.gridx = 1;
		JTextArea descArea = new JTextArea(6, 20);
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		panel.add(new JScrollPane(descArea), gbc);

		gbc.gridx = 0; gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JButton postBtn = new JButton("Post");
		panel.add(postBtn, gbc);

		postBtn.addActionListener((ActionEvent e) -> {
			String name = nameField.getText().trim();
			String category = (String)categoryBox.getSelectedItem();
			String desc = descArea.getText().trim();
			String priceText = priceField.getText().trim();

			if (name.isEmpty() || priceText.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please provide at least Title and Price.", "Validation", JOptionPane.WARNING_MESSAGE);
				return;
			}
			double price;
			try {
				price = Double.parseDouble(priceText);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Invalid price.", "Validation", JOptionPane.WARNING_MESSAGE);
				return;
			}

			Product p = new Product(name, desc, category, price);
			ItemDAO.addProduct(p);

			if (onPost != null) {
				onPost.run();
			}
			JOptionPane.showMessageDialog(this, "Product posted successfully.");
			dispose();
		});

		getContentPane().add(panel);
	}
}
