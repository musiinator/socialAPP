package com.example.reteadesocializare123;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.domain.Friendship;
import src.domain.User;
import src.domain.validators.UserValidator;
import src.repository.database.FriendshipDataBase;
import src.repository.database.UserDataBase;
import src.repository.memory.InMemoryRepository;
import src.service.ServiceUser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class MainFX extends Application {

    private static Stage stg;

    public void openMenu() throws IOException{
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("afterLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 550);
        stage.setTitle("LoyalChainApp");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stg = primaryStage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        primaryStage.setTitle("LoyalChainApp");
        primaryStage.setScene(new Scene(root, 630, 400));
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load((Objects.requireNonNull(getClass().getResource(fxml))));
        stg.getScene().setRoot(pane);
    }


    public static void main (String[] args){
        System.out.println(LocalDateTime.now());
        launch(args);
    }
}
