module com.example.reteadesocializare123 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.reteadesocializare123 to javafx.fxml;
    exports com.example.reteadesocializare123;

    opens src.domain to javafx.base;
}