package org.example.javafx_db_translation;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.PASSWORD;
import static sun.net.ftp.FtpDirEntry.Permission.USER;

public class LocalizationController {
    @FXML
    private ComboBox<String> languageSelector;

    @FXML
    private ListView<String> employeeList;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_translation_jfx";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Test12";

    @FXML
    public void initialize() {
        languageSelector.getItems().addAll("English", "French", "Spanish");
        languageSelector.setValue("English");

        languageSelector.setOnAction(event -> changeLanguage());

        // Call fetchLocalizedData with default language "en"
        fetchLocalizedData("en");
    }

    @FXML
    private void changeLanguage() {
        String languageCode = getLanguageCode(languageSelector.getValue());
        // Call fetchLocalizedData with the selected language code
        fetchLocalizedData(languageCode);
    }

    // Modify the method to accept a String parameter for language code
    @FXML
    public void fetchLocalizedData(String languageCode) {
        System.out.println("Connecting to database...");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected successfully!");

            String query = "SELECT * FROM translations WHERE language_code = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, languageCode);  // Pass the language code to the query
            ResultSet rs = stmt.executeQuery();

            // Clear any previous data in the ListView
            employeeList.getItems().clear();

            while (rs.next()) {
                // Assuming the column name for the translation is 'translation_text'
                String translation = rs.getString("translation_text"); // Replace with the actual column name
                employeeList.getItems().add(translation); // Add translation to ListView
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not fetch localized data.");
        }
    }


    private String getLanguageCode(String language) {
        return switch (language) {
            case "French" -> "fr";
            case "Spanish" -> "es";
            default -> "en";
        };
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
