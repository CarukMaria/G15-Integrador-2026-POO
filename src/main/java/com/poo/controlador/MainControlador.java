package com.poo.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainControlador {

    @FXML
    private StackPane panelCentral;

    @FXML
    public void initialize() {
        // Opcional: Cargar la vista de Turnos por defecto al abrir la aplicación
        abrirTurnos(null);
    }

    @FXML
    public void abrirTurnos(ActionEvent event) {
        // Asegurate de que el nombre del archivo coincida exactamente con tu FXML
        cargarVista("TurnosView.fxml");
    }

    @FXML
    public void abrirClientes(ActionEvent event) {
        cargarVista("ClientesView.fxml");
    }

    @FXML
    public void abrirVacunas(ActionEvent event) {
        cargarVista("VacunasView.fxml");
    }

    @FXML
    public void abrirVeterinarios(ActionEvent event) {
        cargarVista("VeterinariosView.fxml");
    }

    @FXML
    public void abrirServicios(ActionEvent event) {
        cargarVista("ServiciosView.fxml");
    }

    @FXML
    public void abrirHistorial(ActionEvent event) {
        cargarVista("HistorialView.fxml");
    }

    /**
     * Método auxiliar que se encarga de leer el archivo FXML y colocarlo en el centro de la pantalla.
     */
    private void cargarVista(String archivoFxml) {
        try {
            // La ruta asume que tus archivos .fxml están dentro de src/main/resources/vistas/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/" + archivoFxml));
            Parent vista = loader.load();
            
            // Limpiamos lo que haya en el panel central y agregamos la nueva vista
            panelCentral.getChildren().clear();
            panelCentral.getChildren().add(vista);
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaError("No se pudo cargar la pantalla: " + archivoFxml);
        } catch (NullPointerException e) {
            e.printStackTrace();
            mostrarAlertaError("No se encontró el archivo FXML. Verificá que esté en la carpeta resources/vistas/ y se llame " + archivoFxml);
        }
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de navegación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}