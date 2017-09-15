package com.xie.train;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * Created by xie on 17/9/12.
 */
@SpringBootApplication
public class Booter extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Booter.class);
    public static ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        CompletableFuture.supplyAsync(() -> {
            ConfigurableApplicationContext ctx = SpringApplication.run(this.getClass());
            return ctx;
        }).thenAccept(this::launchApplicationView);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("fxml/MainUI.fxml");
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Hello World");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    private void launchApplicationView(ConfigurableApplicationContext ctx) {
        Booter.applicationContext = ctx;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
