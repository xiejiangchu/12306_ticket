package com.xie.train.controller;

/**
 * FXML User Interface enum
 * <p>
 * Created by Owen on 6/20/16.
 */
public enum FXMLPage {

    Login("fxml/Login.fxml"),
    Main_UI("fxml/MainUI.fxml");

    private String fxml;

    FXMLPage(String fxml) {
        this.fxml = fxml;
    }

    public String getFxml() {
        return this.fxml;
    }


}
