module com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx {

    requires javafx.controls;
    requires javafx.fxml;

    opens com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Controllers to javafx.fxml;

    exports com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx;
}