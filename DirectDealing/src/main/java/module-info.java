module eu.telecomnancy.labfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.calendarfx.view;


    requires org.slf4j;

    opens eu.telecomnancy.labfx to javafx.fxml;
    exports eu.telecomnancy.labfx;
}