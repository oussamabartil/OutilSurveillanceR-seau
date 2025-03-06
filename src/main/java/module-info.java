module com.example.networkprojectgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires jnetpcap;

    opens com.example.networkprojectgui.DataTypes to javafx.base;
    opens com.example.networkprojectgui to javafx.fxml;
    exports com.example.networkprojectgui.DataTypes;
    exports com.example.networkprojectgui;
}