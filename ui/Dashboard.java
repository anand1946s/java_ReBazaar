package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard extends Application {

    private BorderPane root;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new BorderPane();
        root.setStyle("-fx-background-color: #121212;");

        // --- Top: Search Bar ---
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search items...");
        searchBar.setPrefWidth(400);
        searchBar.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: white; -fx-prompt-text-fill: #aaaaaa;");
        HBox topBar = new HBox(10, searchBar);
        topBar.setPadding(new Insets(10));
        root.setTop(topBar);

        // --- Left: Sidebar ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #1E1E1E;");

        Button homeBtn = makeSidebarButton("Home");
        Button profileBtn = makeSidebarButton("Profile");
        Button settingsBtn = makeSidebarButton("Settings");
        Button sellBtn = makeSidebarButton("Sell Item");
        Button logoutBtn = makeSidebarButton("Log Out");

        homeBtn.setOnAction(e -> showHomePage());
        profileBtn.setOnAction(e -> root.setCenter(ProfilePage.showProfile()));
        settingsBtn.setOnAction(e -> navigateTo("SettingsPage"));
        sellBtn.setOnAction(e -> navigateTo("SellPage"));
        logoutBtn.setOnAction(e -> logout());

        sidebar.getChildren().addAll(homeBtn, profileBtn, settingsBtn, sellBtn, logoutBtn);
        root.setLeft(sidebar);

        // --- Initial center content ---
        showHomePage();

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Resale App Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper for sidebar button styling
    private Button makeSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold;");
        btn.setPrefWidth(120);
        return btn;
    }

    // --- Home Screen with clickable items ---
    private void showHomePage() {
        TilePane productGrid = new TilePane();
        productGrid.setPadding(new Insets(20));
        productGrid.setHgap(20);
        productGrid.setVgap(20);

        for (int i = 1; i <= 6; i++) {
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: #1E1E1E; -fx-border-color: #333333; -fx-border-radius: 8; -fx-background-radius: 8;");
            card.setPadding(new Insets(10));

            ImageView img = new ImageView(new Image("https://via.placeholder.com/150"));
            img.setFitWidth(150);
            img.setFitHeight(150);

            Label label = new Label("Item " + i);
            label.setStyle("-fx-text-fill: white;");

            card.getChildren().addAll(img, label);

            final int productId = i;
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                VBox productDetailUI = ProductDetail.showProductDetail(productId, "Item " + productId);
                root.setCenter(productDetailUI);
            });

            productGrid.getChildren().add(card);
        }

        root.setCenter(productGrid);
    }

    private void navigateTo(String pageName) {
        VBox page = new VBox(15);
        page.setPadding(new Insets(20));
        Label label = new Label(pageName + " Page");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        page.getChildren().add(label);
        root.setCenter(page);
    }

    private void logout() {
        new LoginPage().start(primaryStage);
    }
}
//if login successful. contains elemnts as in the pdf(refer group),navigation to other pages
