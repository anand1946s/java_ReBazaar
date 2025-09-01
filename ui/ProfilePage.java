package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ProfilePage {

    public static VBox showProfile() {
        VBox profileLayout = new VBox(15);
        profileLayout.setPadding(new Insets(20));
        profileLayout.setStyle("-fx-background-color: #121212;");

        Label title = new Label("Profile");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        ImageView photo = new ImageView(new Image("https://via.placeholder.com/150"));
        photo.setFitWidth(150);
        photo.setFitHeight(150);

        Label name = new Label("Name: John Doe");
        name.setStyle("-fx-text-fill: #cccccc;");
        Label email = new Label("Email: johndoe@example.com");
        email.setStyle("-fx-text-fill: #cccccc;");
        Label phone = new Label("Phone: +91 9876543210");
        phone.setStyle("-fx-text-fill: #cccccc;");

        profileLayout.getChildren().addAll(title, photo, name, email, phone);
        return profileLayout;
    }
}

// opens user profile .shows name,profpic,address,email etc.
