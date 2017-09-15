package com.xie.train.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by xie on 17/9/12.
 */
public class MainUIController extends BaseFXController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private Button btn_Test;

    @FXML
    public void onTestClick(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("test");
        alert.show();
    }
}
