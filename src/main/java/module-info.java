module com.imc.imc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.imc.imc to javafx.fxml;
    exports com.imc.View;
    exports com.imc.Model;
    exports com.imc.Util;
}