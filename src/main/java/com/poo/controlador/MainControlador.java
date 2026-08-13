package com.poo.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainControlador {

   
    private static MainControlador instancia;

    @FXML
    private StackPane panelCentral;

    public MainControlador() {
        instancia = this;
    }

    public static MainControlador getInstancia() {
        return instancia;
    }
    // -----------------------------------------------------------------------------------------

    @FXML
    public void initialize() {
        abrirTurnos(null);
    }

    @FXML
    public void abrirTurnos(ActionEvent event) {
        cargarVista("TurnosVista.fxml");
    }

    @FXML
    public void abrirClientes(ActionEvent event) {
        cargarVista("ClientesVista.fxml");
    }

    @FXML
    public void abrirVacunas(ActionEvent event) {
        cargarVista("VacunasVista.fxml");
    }

    @FXML
    public void abrirVeterinarios(ActionEvent event) {
        cargarVista("VeterinariosVista.fxml");
    }

    @FXML
    public void abrirServicios(ActionEvent event) {
        cargarVista("ServiciosVista.fxml");
    }

    @FXML
    public void abrirHistorial(ActionEvent event) {
        cargarVista("HistorialVista.fxml");
    }

    public void abrirHistorialFiltrado(String numeroFicha) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/HistorialVista.fxml"));
            Parent vista = loader.load();
            
 
            HistorialControlador controlador = loader.getController();
            controlador.recibirFichaDesdeAfuera(numeroFicha);
            
            panelCentral.getChildren().clear();
            panelCentral.getChildren().add(vista);
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("No se pudo cargar la pantalla prefiltrada de Historial.");
        }
    }
    // ---------------------------------------------------------------------------

    private void cargarVista(String archivoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/" + archivoFxml));
            Parent vista = loader.load();
            
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