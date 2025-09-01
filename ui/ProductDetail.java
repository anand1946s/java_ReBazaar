import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RebazaarDarkMode extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel;

    public RebazaarDarkMode() {
        setTitle("Rebazaar - Dark Mode");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Dark theme colors
        Color bgColor = new Color(30, 30, 30);
        Color cardColor = new Color(50, 50, 50);
        Color textColor = Color.WHITE;

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // ---------------- HOME SCREEN ----------------
        JPanel homePanel = new JPanel(new GridLayout(2, 3, 20, 20));
        homePanel.setBackground(bgColor);
        homePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create category buttons
        homePanel.add(createCategoryButton("CARS", cardColor, textColor, "Cars"));
        homePanel.add(createCategoryButton("BIKES", cardColor, textColor, "Bikes"));
        homePanel.add(createCategoryButton("MOBILE PHONES", cardColor, textColor, "Mobiles"));
        homePanel.add(createCategoryButton("PC", cardColor, textColor, "PCs"));
        homePanel.add(createCategoryButton("FURNITURE", cardColor, textColor, "Furniture"));

        mainPanel.add(homePanel, "Home");

        // ---------------- CATEGORY PAGES ----------------
        mainPanel.add(createCategoryPanel("Cars", 6, bgColor, textColor, cardColor), "Cars");
        mainPanel.add(createCategoryPanel("Bikes", 6, bgColor, textColor, cardColor), "Bikes");
        mainPanel.add(createCategoryPanel("Mobile Phones", 6, bgColor, textColor, cardColor), "Mobiles");
        mainPanel.add(createCategoryPanel("PCs", 6, bgColor, textColor, cardColor), "PCs");
        mainPanel.add(createCategoryPanel("Furniture", 6, bgColor, textColor, cardColor), "Furniture");

        add(mainPanel);
        cardLayout.show(mainPanel, "Home"); // start with home screen
    }

    // Create category button for home screen
    private JButton createCategoryButton(String text, Color bg, Color fg, String pageName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setPreferredSize(new Dimension(200, 100));

        // Action to switch page
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, pageName);
            }
        });

        return button;
    }

    // Create category product page
    private JPanel createCategoryPanel(String categoryName, int count, Color bg, Color fg, Color cardColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bg);

        // Title
        JLabel title = new JLabel(categoryName, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(fg);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panel.add(title, BorderLayout.NORTH);

        // Product Grid
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setBackground(bg);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        for (int i = 1; i <= count; i++) {
            gridPanel.add(createProductCard(categoryName + " " + i, cardColor, fg));
        }

        panel.add(gridPanel, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(cardColor);
        backButton.setForeground(fg);
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Home");
            }
        });

        JPanel bottom = new JPanel();
        bottom.setBackground(bg);
        bottom.add(backButton);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // Create each product card
    private JPanel createProductCard(String productName, Color bg, Color fg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        JLabel imageLabel = new JLabel("Image", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 150));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(70, 70, 70));

        JLabel nameLabel = new JLabel(productName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setForeground(fg);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(nameLabel, BorderLayout.SOUTH);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RebazaarDarkMode().setVisible(true);
        });
    }
}
