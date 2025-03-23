module org.example.javafx_db_translation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens org.example.javafx_db_translation to javafx.fxml;
    exports org.example.javafx_db_translation;
}