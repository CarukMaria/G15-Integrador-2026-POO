package com.poo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/MainVista.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1060, 600);
        
        primaryStage.setTitle("Sistema de Gestión Veterinaria");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Cerrando la interfaz visual y desconectando la base de datos...");
        com.poo.util.JPAUtil.close();
        super.stop();
    }

    public static void main(String[] args) {
        System.out.println("Iniciando sistema visual...");
        launch(args); 
    }
}