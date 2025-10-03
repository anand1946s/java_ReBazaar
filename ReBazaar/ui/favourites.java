package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import database.ItemDAO;
import model.Product;

public class favourites extends JFrame {

    private static final Color COLOR_MAIN_BG = new Color(50, 59, 67);
    private static final Color COLOR_ACCENT_GREEN = new Color(70, 181, 149);
    private static final Color COLOR_TEXT_LIGHT = new Color(240, 240, 240);
    private static final Color COLOR_CARD_BG = Color.WHITE;
    private static final Color COLOR_CARD_TEXT = new Color(50, 50, 50);
    private static final Color COLOR_CARD_TEXT_SECONDARY = new Color(150, 150, 150);

    // --- new fields for refresh ---
    private JPanel productGrid;

    public favourites() {
        super("Favourites");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(COLOR_MAIN_BG);

        JLabel title = new JLabel("Favourites");
        title.setForeground(COLOR_TEXT_LIGHT);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(16, 16, 16, 16));
        main.add(title, BorderLayout.NORTH);

        productGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        productGrid.setBackground(COLOR_MAIN_BG);
        productGrid.setBorder(new EmptyBorder(20, 20, 20, 20));

        // load favourites
        List<Product> products = ItemDAO.getFavouriteProducts();
        for (Product p : products) {
            productGrid.add(createProductCard(p));
        }

        JScrollPane scroll = new JScrollPane(productGrid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        main.add(scroll, BorderLayout.CENTER);

        setContentPane(main);
    }

    // call this to refresh the displayed favourites
    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            productGrid.removeAll();
            List<Product> products = ItemDAO.getFavouriteProducts();
            for (Product p : products) productGrid.add(createProductCard(p));
            productGrid.revalidate();
            productGrid.repaint();
        });
    }

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setPreferredSize(new Dimension(220, 280));
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setPreferredSize(new Dimension(200, 160));
        imagePanel.setBackground(new Color(230, 230, 230));

        JLabel imageLabel;
        try {
            String imgPath = p.getImagePath();
            if (imgPath != null && !imgPath.trim().isEmpty()) {
                File f = new File(System.getProperty("user.dir"), imgPath);
                if (f.exists()) {
                    ImageIcon raw = new ImageIcon(f.getAbsolutePath());
                    Image scaled = raw.getImage().getScaledInstance(200, 160, Image.SCALE_SMOOTH);
                    imageLabel = new JLabel(new ImageIcon(scaled));
                } else {
                    imageLabel = new JLabel("<html><center>Image<br>not found</center></html>");
                    imageLabel.setForeground(COLOR_CARD_TEXT_SECONDARY);
                }
            } else {
                imageLabel = new JLabel("<html><center>Product<br>Image</center></html>");
                imageLabel.setForeground(COLOR_CARD_TEXT_SECONDARY);
            }
        } catch (Exception ex) {
            imageLabel = new JLabel("<html><center>Image Error</center></html>");
            imageLabel.setForeground(COLOR_CARD_TEXT_SECONDARY);
        }

        imagePanel.add(imageLabel);
        card.add(imagePanel, BorderLayout.CENTER);

        JPanel info = new JPanel(new BorderLayout(0, 4));
        info.setOpaque(false);

        JLabel nameLabel = new JLabel(p.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        nameLabel.setForeground(COLOR_CARD_TEXT);
        info.add(nameLabel, BorderLayout.NORTH);

        JLabel priceLabel = new JLabel("₱ " + String.format("%.2f", p.getPrice()), SwingConstants.CENTER);
        priceLabel.setForeground(COLOR_ACCENT_GREEN);
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        info.add(priceLabel, BorderLayout.CENTER);

        card.add(info, BorderLayout.SOUTH);
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(favourites.this,
                        p.getName() + "\n\nPrice: ₱ " + String.format("%.2f", p.getPrice()) + "\n\n" + p.getDescription(),
                        "Product Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return card;
    }
}
