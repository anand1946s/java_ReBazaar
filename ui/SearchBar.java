import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

/**
 * A simple Swing application to search for products in a MySQL database.
 */
public class SearchBar {

    // Main application frame and components
    private JFrame frame;
    private JTextField searchField;
    private JTable productTable;
    private DefaultTableModel tableModel;

    // Components for switching between views
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JLabel noResultsLabel;

    // Database credentials and table information
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecommerce_db";
    private static final String USER = "your_username"; // <-- Replace with your MySQL username
    private static final String PASS = "your_password"; // <-- Replace with your MySQL password

    public ProductSearchApp() {
        // Create the main window
        frame = new JFrame("Product Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Create the search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search Item:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Create the panel with CardLayout to switch between views
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create the table to display search results
        String[] columnNames = {"ID", "Name", "Description", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Create the "no results" label
        noResultsLabel = new JLabel("ITEM NOT FOUND!", SwingConstants.CENTER);
        noResultsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        noResultsLabel.setForeground(Color.RED);

        // Add the table and the label as cards
        cardPanel.add(scrollPane, "TableCard");
        cardPanel.add(noResultsLabel, "NoResultsCard");

        // Add components to the frame
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(cardPanel, BorderLayout.CENTER);

        // Add action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                searchProducts(searchTerm);
            }
        });

        // Initially load all products
        searchProducts("");

        // Make the window visible
        frame.setVisible(true);
    }

    /**
     * Searches the products table in the database for a given search term.
     * @param searchTerm The term to search for (empty string to show all).
     */
    private void searchProducts(String searchTerm) {
        // Clear previous results
        tableModel.setRowCount(0);

        String sql;
        if (searchTerm.isEmpty()) {
            sql = "SELECT * FROM products";
        } else {
            sql = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ?";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement if a search term exists
            if (!searchTerm.isEmpty()) {
                pstmt.setString(1, "%" + searchTerm + "%");
                pstmt.setString(2, "%" + searchTerm + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                // Check if the result set is empty
                if (!rs.isBeforeFirst()) {
                    cardLayout.show(cardPanel, "NoResultsCard");
                } else {
                    while (rs.next()) {
                        Vector<Object> row = new Vector<>();
                        row.add(rs.getInt("id"));
                        row.add(rs.getString("name"));
                        row.add(rs.getString("description"));
                        row.add(rs.getDouble("price"));
                        tableModel.addRow(row);
                    }
                    cardLayout.show(cardPanel, "TableCard");
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            cardLayout.show(cardPanel, "NoResultsCard");
        }
    }

    public static void main(String[] args) {
        // Use a thread-safe way to create the GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProductSearchApp();
            }
        });
    }
}
