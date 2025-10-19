module org.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Added this line
    requires java.desktop; // Added for image handling

    opens org.javafx to javafx.fxml;
    exports org.javafx;
}
