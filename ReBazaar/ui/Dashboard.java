package ui;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Dashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    // --- UI Component Palette ---
    private static final Color COLOR_MAIN_BG = new Color(248, 250, 248);
    private static final Color COLOR_ACCENT_GREEN = new Color(137, 207, 189);
    private static final Color COLOR_TEXT_FIELD_BG = new Color(230, 230, 230);
    private static final Color COLOR_BUTTON_GREEN = new Color(89, 179, 130);
    private static final Color COLOR_TEXT_DARK = new Color(50, 50, 50);
    private static final Color COLOR_TEXT_SECONDARY = new Color(150, 150, 150);

    private JPanel contentPanel;
    private JTextField searchField;
    private JWindow searchResultsWindow;
    private JList<String> searchResultsList;
    private DefaultListModel<String> searchResultsModel;

    private String loggedInUser;

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

        // --- Left Sidebar Panel ---
        JPanel sidebar = createSidebar();
        splitPane.setLeftComponent(sidebar);

        // --- Right Content Area ---
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(COLOR_MAIN_BG);
        splitPane.setRightComponent(contentPanel);

        splitPane.setDividerLocation(250);

        mainContentPane.add(splitPane, BorderLayout.CENTER);

        initSearchResultsWindow();
        displayCategory("Furnitures");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Color.WHITE);
        sidebar.setLayout(new GridBagLayout());
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // 1. ReBazaar Logo Panel
        JPanel logoPanel = new JPanel(new BorderLayout(5, 5));
        logoPanel.setBackground(COLOR_ACCENT_GREEN);
        logoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        logoPanel.setPreferredSize(new Dimension(200, 120));
        logoPanel.setMaximumSize(new Dimension(200, 120));
        logoPanel.setMinimumSize(new Dimension(200, 120));

        JLabel cartIcon = new JLabel("🛒", SwingConstants.CENTER);
        cartIcon.setFont(new Font("SansSerif", Font.BOLD, 40));
        cartIcon.setForeground(COLOR_TEXT_DARK);
        logoPanel.add(cartIcon, BorderLayout.NORTH);

        JLabel logoText = new JLabel("<html><b>ReBazaar</b><br><font size='3'>REBUY | RESALE</font></html>", SwingConstants.CENTER);
        logoText.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoText.setForeground(COLOR_TEXT_DARK);
        logoPanel.add(logoText, BorderLayout.CENTER);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.anchor = GridBagConstraints.NORTH;
        sidebar.add(logoPanel, gbc);
        
        // 2. Navigation Links
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        
        String[] navItems = {"Home", "Sell Items", "Messages", "Favourites", "Notifications", "Settings", "Logout"};
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
        button.setForeground(COLOR_TEXT_DARK);
        button.setBackground(Color.WHITE);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
        return button;
    }

    private void handleNavigation(String item) {
        switch (item) {
        case "Home":
            displayCategory("Furnitures");
            break;

        // MODIFIED PART STARTS HERE
        case "Settings":
            // This will open your new UserProfile frame without closing the dashboard
            SwingUtilities.invokeLater(() -> new UserProfile(loggedInUser).setVisible(true));
            break;
        // MODIFIED PART ENDS HERE

        case "Logout":
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginPage().setVisible(true); // Ensure LoginPage.java is in the ui package
                dispose();
            }
            break;
        default:
            JOptionPane.showMessageDialog(this, "Navigating to " + item);
            break;
        }
    }

    private void displayCategory(String categoryName) {
        contentPanel.removeAll();

        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(COLOR_MAIN_BG);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbcHeader = new GridBagConstraints();
        gbcHeader.insets = new Insets(0, 0, 0, 15);
        gbcHeader.anchor = GridBagConstraints.WEST;
        gbcHeader.weightx = 0;

        JLabel categoryTitle = new JLabel(categoryName);
        categoryTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        categoryTitle.setForeground(COLOR_TEXT_DARK);
        gbcHeader.gridx = 0;
        gbcHeader.gridy = 0;
        headerPanel.add(categoryTitle, gbcHeader);

        searchField = new JTextField(30);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchField.setBackground(COLOR_TEXT_FIELD_BG);
        searchField.setForeground(COLOR_TEXT_DARK);
        searchField.setCaretColor(COLOR_TEXT_DARK);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TEXT_SECONDARY.brighter(), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));

        gbcHeader.gridx = 1;
        gbcHeader.weightx = 1.0;
        gbcHeader.fill = GridBagConstraints.HORIZONTAL;
        gbcHeader.anchor = GridBagConstraints.EAST;
        headerPanel.add(searchField, gbcHeader);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel productGrid = new JPanel(new GridLayout(0, 4, 25, 25));
        productGrid.setBackground(COLOR_MAIN_BG);
        productGrid.setBorder(new EmptyBorder(0, 30, 30, 30));

        List<String> productNames = new ArrayList<>();
        productNames.add("Wooden Bed Frame");
        productNames.add("Modern Sofa Set");
        productNames.add("Dining Table & Chairs");
        productNames.add("Office Desk Chair");
        productNames.add("Bookshelf Unit");
        productNames.add("Coffee Table");
        productNames.add("Bedroom Wardrobe");
        productNames.add("Outdoor Patio Set");

        for (String productName : productNames) {
            productGrid.add(createProductCard(productName));
        }

        JScrollPane scrollPane = new JScrollPane(productGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { showSearchResults(searchField.getText(), productNames); }
            public void removeUpdate(DocumentEvent e) { showSearchResults(searchField.getText(), productNames); }
            public void changedUpdate(DocumentEvent e) { }
        });
        
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
           // Delay hiding to allow clicks on search results
           SwingUtilities.invokeLater(() -> {
             Component oppositeComponent = e.getOppositeComponent();
        // Hide the pop-up ONLY IF the focus did not go to the pop-up window itself or one of its children
               if (oppositeComponent != null && !SwingUtilities.isDescendingFrom(oppositeComponent, searchResultsWindow)) {
                  hideSearchResults();
               }
             });
            }
            @Override
            public void focusGained(FocusEvent e) {
                if(!searchField.getText().trim().isEmpty()){
                    showSearchResults(searchField.getText(), productNames);
                }
            }
        });
    }

    private JPanel createProductCard(String productName) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(12, 12, 12, 12)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setPreferredSize(new Dimension(200, 180));
        imagePanel.setBackground(COLOR_TEXT_FIELD_BG);
        JLabel imageLabel = new JLabel("<html><center>Product<br>Image</center></html>");
        imageLabel.setForeground(COLOR_TEXT_SECONDARY);
        imagePanel.add(imageLabel);
        card.add(imagePanel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(productName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        nameLabel.setForeground(COLOR_TEXT_DARK);
        card.add(nameLabel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BUTTON_GREEN, 1),
                    new EmptyBorder(12, 12, 12, 12)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    new EmptyBorder(12, 12, 12, 12)
                ));
            }
        });
        return card;
    }

    private void initSearchResultsWindow() {
        searchResultsWindow = new JWindow(this);
        searchResultsModel = new DefaultListModel<>();
        searchResultsList = new JList<>(searchResultsModel);
        searchResultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultsList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchResultsList.setBackground(Color.WHITE);
        searchResultsList.setForeground(COLOR_TEXT_DARK);
        searchResultsList.setFixedCellHeight(28);

        searchResultsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selectedProduct = searchResultsList.getSelectedValue();
                if (selectedProduct != null && !selectedProduct.startsWith("No results")) {
                    searchField.setText(selectedProduct);
                    JOptionPane.showMessageDialog(Dashboard.this, "Searching for: " + selectedProduct);
                    hideSearchResults();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(searchResultsList);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BUTTON_GREEN, 1));
        searchResultsWindow.add(scrollPane);
    }

    private void showSearchResults(String query, List<String> allProducts) {
        searchResultsModel.clear();
        if (query.trim().isEmpty()) {
            hideSearchResults();
            return;
        }

        List<String> filteredProducts = allProducts.stream()
            .filter(product -> product.toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());

        if (filteredProducts.isEmpty()) {
            searchResultsModel.addElement("No results found for '" + query + "'");
        } else {
            filteredProducts.forEach(searchResultsModel::addElement);
        }
        
        Point location = searchField.getLocationOnScreen();
        searchResultsWindow.setLocation(location.x, location.y + searchField.getHeight());
        int preferredHeight = Math.min(200, searchResultsModel.size() * searchResultsList.getFixedCellHeight());
        searchResultsWindow.setSize(searchField.getWidth(), preferredHeight);
        
        searchResultsWindow.setVisible(true);
    }

    private void hideSearchResults() {
        searchResultsWindow.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("GuestUser").setVisible(true));
    }
}

