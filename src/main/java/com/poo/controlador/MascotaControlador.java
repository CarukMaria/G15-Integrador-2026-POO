package com.poo.controlador;

import com.poo.modelo.Cliente;
import com.poo.modelo.Especie;
import com.poo.modelo.Mascota;
import com.poo.servicio.MascotaService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//import java.time.LocalDate;

public class MascotaControlador {

    private MascotaService servicio;
    
    // Variables para conectar la mascota con su dueño y saber si estamos editando
    private Cliente clienteDuenio;
    private Mascota mascotaSeleccionada;

    // --- ELEMENTOS FXML ---
    @FXML private TextField txtNombre;
    @FXML private TextField txtRaza;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<Especie> cbEspecie;
    
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    public MascotaControlador() {
        servicio = new MascotaService();
    }

    @FXML
    public void initialize() {
        // Cargar los valores del Enum Especie en el ComboBox
        cbEspecie.setItems(FXCollections.observableArrayList(Especie.values()));
    }

    /**
     * Este método será llamado desde ClienteController justo después de abrir la ventana.
     * Sirve para pasarle el dueño y, si es una edición, los datos de la mascota.
     */
    public void inicializarDatos(Cliente dueño, Mascota mascota) {
        this.clienteDuenio = dueño;
        this.mascotaSeleccionada = mascota;

        // Si recibimos una mascota, es porque estamos editando. Cargamos sus datos:
        if (mascota != null) {
            txtNombre.setText(mascota.getNombre());
            txtRaza.setText(mascota.getRaza());
            dpFecha.setValue(mascota.getFechaNacimiento());
            cbEspecie.setValue(mascota.getEspecie());
        }
    }

    @FXML
    public void guardarMascotaAccion(ActionEvent event) {
   
        if (txtNombre.getText().isEmpty() || cbEspecie.getValue() == null || dpFecha.getValue() == null) {
            mostrarAlerta("Campos obligatorios", "El nombre, la fecha de nacimiento y la especie son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        // Si es una mascota nueva, la instanciamos
        if (mascotaSeleccionada == null) {
            mascotaSeleccionada = new Mascota();
            
            // Generar un número de ficha automático para mascotas nuevas
            String numeroFichaGenerado = "F-" + (System.currentTimeMillis() % 10000);
            mascotaSeleccionada.setNumeroFicha(numeroFichaGenerado);
            
            // Asociar la mascota al cliente usando el método de negocio de la clase Cliente
            if (clienteDuenio != null) {
                clienteDuenio.agregarMascota(mascotaSeleccionada);
            }
        }

        mascotaSeleccionada.setNombre(txtNombre.getText());
        mascotaSeleccionada.setRaza(txtRaza.getText());
        mascotaSeleccionada.setFechaNacimiento(dpFecha.getValue());
        mascotaSeleccionada.setEspecie(cbEspecie.getValue());

        // Guardar en base de datos
        try {
            servicio.guardar(mascotaSeleccionada);
            mostrarAlerta("Éxito", "La mascota se guardó correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana(event);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al intentar guardar la mascota.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void cancelarAccion(ActionEvent event) {
        cerrarVentana(event);
    }

    // --- MÉTODOS AUXILIARES ---

    private void cerrarVentana(ActionEvent event) {
        // Obtenemos la ventana (Stage) actual a partir del botón que disparó el evento y la cerramos
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}