module eu.innorenew.smartfloorfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.commons.codec;
    requires com.google.gson;

    opens eu.innorenew.smartfloorfx to javafx.fxml;
    exports eu.innorenew.smartfloorfx;

}