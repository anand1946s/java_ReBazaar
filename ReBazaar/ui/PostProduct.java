package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import database.ItemDAO;
import model.Product;

public class PostProduct extends JDialog {
	private Runnable onPost;

	public PostProduct(Frame owner, Runnable onPost) {
		super(owner, "Post Product", true);
		this.onPost = onPost;
		initUI();
		setSize(480, 520); // slightly larger for image chooser
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

		// NEW: Image chooser
		gbc.gridx = 0; gbc.gridy++;
		JLabel imageLbl = new JLabel("Image:");
		panel.add(imageLbl, gbc);
		gbc.gridx = 1;
		JPanel imgChooserPanel = new JPanel(new BorderLayout(6, 6));
		JLabel chosenPathLabel = new JLabel("No image selected");
		JButton chooseBtn = new JButton("Choose Image...");
		imgChooserPanel.add(chosenPathLabel, BorderLayout.CENTER);
		imgChooserPanel.add(chooseBtn, BorderLayout.EAST);
		panel.add(imgChooserPanel, gbc);

		final String[] selectedRelativePath = new String[1];

		chooseBtn.addActionListener(ev -> {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int ret = fc.showOpenDialog(PostProduct.this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File sel = fc.getSelectedFile();
				// copy to project images folder
				try {
					Path imagesDir = Paths.get(System.getProperty("user.dir"), "images");
					if (!Files.exists(imagesDir)) Files.createDirectories(imagesDir);
					String baseName = sel.getName();
					Path dest = imagesDir.resolve(baseName);
					// if exists, append timestamp to avoid overwrite
					if (Files.exists(dest)) {
						String name = baseName;
						int dot = name.lastIndexOf('.');
						String prefix = (dot > 0) ? name.substring(0, dot) : name;
						String ext = (dot > 0) ? name.substring(dot) : "";
						dest = imagesDir.resolve(prefix + "_" + System.currentTimeMillis() + ext);
					}
					Files.copy(sel.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
					selectedRelativePath[0] = "images/" + dest.getFileName().toString();
					chosenPathLabel.setText(selectedRelativePath[0]);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(PostProduct.this, "Failed to copy image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

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
			// attach image path if chosen
			if (selectedRelativePath[0] != null) p.setImagePath(selectedRelativePath[0]);

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
