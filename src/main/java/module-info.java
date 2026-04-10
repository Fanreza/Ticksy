module com.ticksy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires atlantafx.base;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    requires org.slf4j;

    requires com.sun.jna;
    requires com.sun.jna.platform;

    opens com.ticksy to javafx.fxml;
    opens com.ticksy.controller to javafx.fxml;
    opens com.ticksy.controller.master to javafx.fxml;
    opens com.ticksy.controller.transaction to javafx.fxml;
    opens com.ticksy.controller.report to javafx.fxml;
    opens com.ticksy.controller.dashboard to javafx.fxml;
    opens com.ticksy.model to org.hibernate.orm.core, javafx.base;

    exports com.ticksy;
    exports com.ticksy.controller;
    exports com.ticksy.controller.master;
    exports com.ticksy.controller.transaction;
    exports com.ticksy.controller.report;
    exports com.ticksy.controller.dashboard;
    exports com.ticksy.model;
    exports com.ticksy.service;
    exports com.ticksy.repository;
    exports com.ticksy.config;
    exports com.ticksy.util;
}
