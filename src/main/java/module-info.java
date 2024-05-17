module com.example.textfieldfx{
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;

    opens com.example.textfieldfx to javafx.fxml, com.google.gson;
    exports com.example.textfieldfx;
}

