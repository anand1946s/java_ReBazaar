package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import database.ItemDAO;
import model.Product;

public class PostProduct extends JDialog {
    // --- UI Component Palette (Matching Dashboard Theme) ---
    private static final Color COLOR_MAIN_BG = new Color(50, 59, 67);
    private static final Color COLOR_ACCENT_GREEN = new Color(70, 181, 149);
    private static final Color COLOR_TEXT_FIELD_BG = new Color(60, 70, 80);
    private static final Color COLOR_TEXT_LIGHT = new Color(240, 240, 240);
    private static final Color COLOR_TEXT_SECONDARY_LIGHT = new Color(170, 170, 170);

    // --- NEW: Constant for corner radius ---
    private static final int CORNER_RADIUS = 15; // You can change this value

    private Runnable onPost;

    public PostProduct(Frame owner, Runnable onPost) {
        super(owner, "Post Product", true);
        this.onPost = onPost;
        initUI();
        setSize(480, 520);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_MAIN_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().setBackground(COLOR_MAIN_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        // --- Title ---
        JLabel nameLbl = new JLabel("Title:");
        nameLbl.setForeground(COLOR_TEXT_LIGHT);
        panel.add(nameLbl, gbc);

        gbc.gridx = 1;
        // Use the new RoundedJTextField
        JTextField nameField = new RoundedJTextField();
        panel.add(nameField, gbc);

        // --- Category ---
        gbc.gridx = 0; gbc.gridy++;
        JLabel categoryLbl = new JLabel("Category:");
        categoryLbl.setForeground(COLOR_TEXT_LIGHT);
        panel.add(categoryLbl, gbc);

        gbc.gridx = 1;
        JComboBox<String> categoryBox = new JComboBox<>(new String[] {"Furnitures", "Electronics", "Books", "Clothing", "Other"});
        categoryBox.setBackground(COLOR_TEXT_FIELD_BG);
        categoryBox.setForeground(COLOR_TEXT_LIGHT);
        panel.add(categoryBox, gbc);

        // --- Price ---
        gbc.gridx = 0; gbc.gridy++;
        JLabel priceLbl = new JLabel("Price:");
        priceLbl.setForeground(COLOR_TEXT_LIGHT);
        panel.add(priceLbl, gbc);

        gbc.gridx = 1;
        // Use the new RoundedJTextField
        JTextField priceField = new RoundedJTextField();
        panel.add(priceField, gbc);

        // --- Description ---
        gbc.gridx = 0; gbc.gridy++;
        JLabel descLbl = new JLabel("Description:");
        descLbl.setForeground(COLOR_TEXT_LIGHT);
        panel.add(descLbl, gbc);

        gbc.gridx = 1;
        // Use a RoundedPanel to contain the JTextArea
        RoundedPanel descPanel = new RoundedPanel(new BorderLayout());
        JTextArea descArea = new JTextArea(6, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(COLOR_TEXT_FIELD_BG);
        descArea.setForeground(COLOR_TEXT_LIGHT);
        descArea.setCaretColor(COLOR_TEXT_LIGHT);
        descArea.setBorder(new EmptyBorder(5, 8, 5, 8));
        descPanel.add(descArea);
        panel.add(descPanel, gbc);


        // --- Image Chooser ---
        gbc.gridx = 0; gbc.gridy++;
        JLabel imageLbl = new JLabel("Image:");
        imageLbl.setForeground(COLOR_TEXT_LIGHT);
        panel.add(imageLbl, gbc);

        gbc.gridx = 1;
        JPanel imgChooserPanel = new JPanel(new BorderLayout(6, 6));
        imgChooserPanel.setOpaque(false);
        JLabel chosenPathLabel = new JLabel("No image selected");
        chosenPathLabel.setForeground(COLOR_TEXT_SECONDARY_LIGHT);
        // Use the new RoundedJButton
        JButton chooseBtn = new RoundedJButton("Choose...");
        styleButton(chooseBtn, false);

        imgChooserPanel.add(chosenPathLabel, BorderLayout.CENTER);
        imgChooserPanel.add(chooseBtn, BorderLayout.EAST);
        panel.add(imgChooserPanel, gbc);

        final String[] selectedRelativePath = new String[1];
        chooseBtn.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            int ret = fc.showOpenDialog(PostProduct.this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File sel = fc.getSelectedFile();
                try {
                    Path imagesDir = Paths.get(System.getProperty("user.dir"), "images");
                    if (!Files.exists(imagesDir)) Files.createDirectories(imagesDir);
                    String baseName = sel.getName();
                    Path dest = imagesDir.resolve(baseName);
                    if (Files.exists(dest)) {
                        String name = baseName;
                        int dot = name.lastIndexOf('.');
                        String prefix = (dot > 0) ? name.substring(0, dot) : name;
                        String ext = (dot > 0) ? name.substring(dot) : "";
                        dest = imagesDir.resolve(prefix + "_" + System.currentTimeMillis() + ext);
                    }
                    Files.copy(sel.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                    selectedRelativePath[0] = "images/" + dest.getFileName().toString();
                    chosenPathLabel.setText(dest.getFileName().toString());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(PostProduct.this, "Failed to copy image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Post Button ---
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        // Use the new RoundedJButton
        JButton postBtn = new RoundedJButton("Post Product");
        styleButton(postBtn, true);
        panel.add(postBtn, gbc);

        postBtn.addActionListener((ActionEvent e) -> {
            String name = nameField.getText().trim();
            String category = (String) categoryBox.getSelectedItem();
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
            if (selectedRelativePath[0] != null) p.setImagePath(selectedRelativePath[0]);

            ItemDAO.addProduct(p);

            if (onPost != null) onPost.run();
            JOptionPane.showMessageDialog(this, "Product posted successfully.");
            dispose();
        });
        getContentPane().add(panel);
    }

    private void styleButton(JButton button, boolean isPrimary) {
        if (isPrimary) {
             button.setBackground(COLOR_ACCENT_GREEN);
             button.setForeground(Color.WHITE);
        } else {
             button.setBackground(COLOR_TEXT_FIELD_BG);
             button.setForeground(COLOR_TEXT_LIGHT);
        }
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
    }
    
    // --- HELPER CLASSES FOR ROUNDED COMPONENTS ---

    // A custom JPanel that paints a rounded background
    private static class RoundedPanel extends JPanel {
        public RoundedPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(COLOR_TEXT_FIELD_BG);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
            g2d.setColor(COLOR_TEXT_SECONDARY_LIGHT.darker());
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
            g2d.dispose();
        }
    }

    // A custom JTextField with a rounded border
    private static class RoundedJTextField extends JTextField {
        public RoundedJTextField() {
            super();
            setOpaque(false); // Make the component transparent
            setBackground(new Color(0,0,0,0)); // Transparent background
            setForeground(COLOR_TEXT_LIGHT);
            setCaretColor(COLOR_TEXT_LIGHT);
            setBorder(new EmptyBorder(5, 8, 5, 8)); // Padding
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Paint background
            g2.setColor(COLOR_TEXT_FIELD_BG);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
            // Paint the text on top
            super.paintComponent(g);
            g2.dispose();
        }
        
        @Override
        protected void paintBorder(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             g2.setColor(COLOR_TEXT_SECONDARY_LIGHT.darker());
             g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
             g2.dispose();
        }
    }

    // A custom JButton with rounded corners
    private static class RoundedJButton extends JButton {
        public RoundedJButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Use the button's background color for the rounded rectangle
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
            // Paint the button's text and icon
            super.paintComponent(g);
            g2.dispose();
        }
    }
}
```

