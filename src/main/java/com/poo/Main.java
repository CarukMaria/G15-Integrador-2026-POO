package com.poo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carga la vista principal. 
        // IMPORTANTE: Asegurate de que la ruta sea correcta. Si guardaste MainView.fxml 
        // adentro de resources/vistas/, la ruta debe ser "/vistas/MainView.fxml".
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/MainView.fxml"));
        Parent root = loader.load();

        // Creamos la escena y le damos un tamaño inicial
        Scene scene = new Scene(root, 900, 600);
        
        primaryStage.setTitle("Sistema de Gestión Veterinaria");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Este método se ejecuta automáticamente cuando el usuario cierra la ventana.
        // Es el lugar perfecto para cerrar la conexión de la base de datos de forma segura.
        System.out.println("Cerrando la interfaz visual y desconectando la base de datos...");
        com.poo.util.JPAUtil.close();
        super.stop();
    }

    public static void main(String[] args) {
        System.out.println("Iniciando sistema visual...");
        // launch arranca todo el motor gráfico de JavaFX y llama al método start()
        launch(args); 
    }
}