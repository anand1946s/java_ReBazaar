package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import database.ItemDAO;
import model.Product;

public class Dashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    
    private static final Color COLOR_MAIN_BG = new Color(50, 59, 67);
    private static final Color COLOR_SIDEBAR_GREEN = new Color(25, 118, 109);
    private static final Color COLOR_ACCENT_GREEN = new Color(70, 181, 149);
    private static final Color COLOR_TEXT_FIELD_BG = new Color(60, 70, 80);
    private static final Color COLOR_TEXT_LIGHT = new Color(240, 240, 240);
    private static final Color COLOR_TEXT_SECONDARY_LIGHT = new Color(170, 170, 170);
    private static final Color COLOR_CARD_BG = Color.WHITE;
    private static final Color COLOR_CARD_TEXT = new Color(50, 50, 50);
    private static final Color COLOR_CARD_TEXT_SECONDARY = new Color(150, 150, 150);

    private JPanel contentPanel;
    private String loggedInUser;
    private String currentCategory;
    private favourites favouritesWindow;

    public Dashboard(String user) {
        super("ReBazaar Dashboard");
        this.loggedInUser = user;
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainContentPane = new JPanel(new BorderLayout());
        mainContentPane.setBackground(COLOR_MAIN_BG);
        setContentPane(mainContentPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);

        
        JPanel sidebar = createSidebar();
        splitPane.setLeftComponent(sidebar);

        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(COLOR_MAIN_BG);
        splitPane.setRightComponent(contentPanel);

        splitPane.setDividerLocation(250);
        mainContentPane.add(splitPane, BorderLayout.CENTER);

        displayCategory("Welcome to ReBazaar");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(COLOR_SIDEBAR_GREEN);
        sidebar.setLayout(new GridBagLayout());
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        
        JPanel logoPanel = new JPanel(new BorderLayout(5, 5));
        logoPanel.setBackground(COLOR_ACCENT_GREEN);
        logoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        logoPanel.setPreferredSize(new Dimension(200, 120));

        JLabel cartIcon = new JLabel("🛒", SwingConstants.CENTER);
        cartIcon.setFont(new Font("SansSerif", Font.BOLD, 40));
        cartIcon.setForeground(COLOR_CARD_TEXT);
        logoPanel.add(cartIcon, BorderLayout.NORTH);

        JLabel logoText = new JLabel("<html><b>ReBazaar</b><br><font size='3'>REBUY | RESALE</font></html>", SwingConstants.CENTER);
        logoText.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoText.setForeground(COLOR_CARD_TEXT);
        logoPanel.add(logoText, BorderLayout.CENTER);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.anchor = GridBagConstraints.NORTH;
        sidebar.add(logoPanel, gbc);

        
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;

    
        String[] navItems = {"Sell Items", "Favourites", "Settings", "Logout"};
        for (int i = 0; i < navItems.length; i++) {
            String item = navItems[i];
            JButton navButton = createSidebarNavItem(item);
            navButton.addActionListener(e -> handleNavigation(item));
            gbc.gridy = i + 1;
            sidebar.add(navButton, gbc);
        }

        gbc.gridy = navItems.length + 1;
        gbc.weighty = 1.0;
        sidebar.add(Box.createVerticalGlue(), gbc);

        return sidebar;
    }

    private JButton createSidebarNavItem(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setForeground(COLOR_TEXT_LIGHT);
        button.setBackground(COLOR_SIDEBAR_GREEN);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_ACCENT_GREEN.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_SIDEBAR_GREEN);
            }
        });
        return button;
    }

    private void handleNavigation(String item) {
        switch (item) {
            
            case "Sell Items":
                SwingUtilities.invokeLater(() -> {
                    PostProduct dlg = new PostProduct(this, loggedInUser, () -> displayCategory(currentCategory == null ? "Furnitures" : currentCategory));
                    dlg.setVisible(true);
                });
                break;

            case "Favourites":
                SwingUtilities.invokeLater(() -> {
                    if (favouritesWindow == null || !favouritesWindow.isDisplayable()) {
                        favouritesWindow = new favourites();
                    }
                    favouritesWindow.setVisible(true);
                    favouritesWindow.refresh();
                });
                break;

            case "Settings":
                SwingUtilities.invokeLater(() -> new UserProfile(loggedInUser).setVisible(true));
                break;

            case "Logout":
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    new LoginPage().setVisible(true);
                    dispose();
                }
                break;

            default:
                JOptionPane.showMessageDialog(this, "Navigating to " + item);
                break;
        }
    }

    private void displayCategory(String categoryName) {
        this.currentCategory = categoryName;
        contentPanel.removeAll();

        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(COLOR_MAIN_BG);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbcHeader = new GridBagConstraints();
        gbcHeader.insets = new Insets(0, 0, 0, 15);
        gbcHeader.anchor = GridBagConstraints.WEST;

        JLabel categoryTitle = new JLabel(categoryName);
        categoryTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        categoryTitle.setForeground(COLOR_TEXT_LIGHT);
        gbcHeader.gridx = 0;
        gbcHeader.gridy = 0;
        headerPanel.add(categoryTitle, gbcHeader);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel productGrid = new JPanel(new GridLayout(0, 5, 20, 20));
        productGrid.setBackground(COLOR_MAIN_BG);
        productGrid.setBorder(new EmptyBorder(20, 30, 30, 30));

        List<Product> posted = ItemDAO.getAllProducts();
        for (Product p : posted) {
            productGrid.add(createProductCard(p));
        }

        JScrollPane scrollPane = new JScrollPane(productGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    
    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setPreferredSize(new Dimension(220, 300));
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setPreferredSize(new Dimension(200, 160));
        imagePanel.setBackground(new Color(240, 240, 240));
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1));

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
                    imageLabel = new JLabel("No Image", SwingConstants.CENTER);
                    imageLabel.setForeground(COLOR_CARD_TEXT_SECONDARY);
                }
            } else {
                imageLabel = new JLabel("Product", SwingConstants.CENTER);
                imageLabel.setForeground(COLOR_CARD_TEXT_SECONDARY);
            }
        } catch (Exception ex) {
            imageLabel = new JLabel("Image Error", SwingConstants.CENTER);
            imageLabel.setForeground(COLOR_CARD_TEXT_SECONDARY);
        }
        imagePanel.add(imageLabel);
        card.add(imagePanel, BorderLayout.CENTER);

        
        JPanel infoPanel = new JPanel(new BorderLayout(0, 6));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(p.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(COLOR_CARD_TEXT);

        JLabel priceLabel = new JLabel("₱ " + String.format("%.2f", p.getPrice()), SwingConstants.CENTER);
        priceLabel.setForeground(COLOR_ACCENT_GREEN);
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(priceLabel, BorderLayout.CENTER);

        
        boolean isFav = ItemDAO.isFavourite(p.getId());
        String starChar = isFav ? "★" : "☆";
        JButton favBtn = new JButton(starChar);
        favBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        favBtn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        favBtn.setFocusPainted(false);
        favBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        favBtn.setContentAreaFilled(false);
        favBtn.setOpaque(false);
        favBtn.setToolTipText(isFav ? "Remove from favourites" : "Add to favourites");
        favBtn.setForeground(isFav ? new Color(255, 215, 0) : COLOR_TEXT_LIGHT); // gold colr

        favBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                
                if (ItemDAO.isFavourite(p.getId())) {
                    favBtn.setForeground(new Color(230, 190, 0));
                } else {
                    favBtn.setForeground(COLOR_ACCENT_GREEN);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                favBtn.setForeground(ItemDAO.isFavourite(p.getId()) ? new Color(255, 215, 0) : COLOR_TEXT_LIGHT);
            }
        });

        favBtn.addActionListener(ae -> {
            if (ItemDAO.isFavourite(p.getId())) {
                ItemDAO.removeFromFavourites(p.getId());
                favBtn.setText("☆");
                favBtn.setToolTipText("Add to favourites");
                favBtn.setForeground(COLOR_TEXT_LIGHT);
                JOptionPane.showMessageDialog(Dashboard.this, p.getName() + " removed from favourites.");
            } else {
                ItemDAO.addToFavourites(p.getId());
                favBtn.setText("★");
                favBtn.setToolTipText("Remove from favourites");
                favBtn.setForeground(new Color(255, 215, 0));
                JOptionPane.showMessageDialog(Dashboard.this, p.getName() + " added to favourites.");
            }
            if (favouritesWindow != null && favouritesWindow.isVisible()) {
                favouritesWindow.refresh();
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        bottomPanel.add(favBtn, BorderLayout.EAST);

        card.add(bottomPanel, BorderLayout.SOUTH);

        // hover glow
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_ACCENT_GREEN, 2),
                        new EmptyBorder(10, 10, 10, 10)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(Dashboard.this,
                        p.getName() + "\n\nPrice: ₱ " + String.format("%.2f", p.getPrice()) + "\n\n" + p.getDescription(),
                        "Product Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return card;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("GuestUser").setVisible(true));
    }
}
