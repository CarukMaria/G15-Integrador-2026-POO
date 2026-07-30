package com.poo.controlador;

import com.poo.modelo.Servicio;
import com.poo.servicio.ServicioService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ServicioController {

    private ServicioService servicioService;
    private ObservableList<Servicio> listaObservableServicios;
    private Servicio servicioSeleccionado;

    // --- ELEMENTOS FXML: TABLA ---
    @FXML private TableView<Servicio> tablaServicios;
    @FXML private TableColumn<Servicio, String> colNombre;
    @FXML private TableColumn<Servicio, Double> colPrecio;
    @FXML private TableColumn<Servicio, Integer> colDuracion;

    // --- ELEMENTOS FXML: FORMULARIO ---
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtDuracion;
    
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    public ServicioController() {
        servicioService = new ServicioService();
    }

    @FXML
    public void initialize() {
        // Configurar las columnas para que lean los atributos de la clase Servicio
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        // Cargar los datos en la tabla
        cargarTablaServicios();

        // Escuchar los clics en la tabla para pasar los datos al formulario
        tablaServicios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarServicioEnFormulario(newSelection);
            }
        });

        // Asignar el evento de limpiar al botón cancelar
        btnCancelar.setOnAction(e -> limpiarFormulario());
    }

    private void cargarTablaServicios() {
        List<Servicio> servicios = servicioService.listar();
        listaObservableServicios = FXCollections.observableArrayList(servicios);
        tablaServicios.setItems(listaObservableServicios);
    }

    private void mostrarServicioEnFormulario(Servicio servicio) {
        this.servicioSeleccionado = servicio;
        
        // Cargamos los datos en los campos de texto (convirtiendo los números a String)
        txtNombre.setText(servicio.getNombre());
        txtPrecio.setText(String.valueOf(servicio.getPrecio()));
        txtDuracion.setText(String.valueOf(servicio.getDuracion()));
    }

    // Método conectado directamente al FXML (onAction="#guardarCambiosAccion")
    @FXML
    public void guardarCambiosAccion(ActionEvent event) {
        // Validar que haya un servicio seleccionado
        if (servicioSeleccionado == null) {
            mostrarAlerta("Atención", "Debe seleccionar un servicio de la tabla para modificarlo.", Alert.AlertType.WARNING);
            return;
        }

        // Validar que los campos no estén vacíos
        if (txtPrecio.getText().isEmpty() || txtDuracion.getText().isEmpty()) {
            mostrarAlerta("Campos incompletos", "El precio y la duración no pueden estar vacíos.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Convertir el texto ingresado a números
            double nuevoPrecio = Double.parseDouble(txtPrecio.getText());
            int nuevaDuracion = Integer.parseInt(txtDuracion.getText());

            // Actualizar el objeto (usamos tu método actualizarPrecio si lo tenés, o los setters)
            servicioSeleccionado.setPrecio(nuevoPrecio);
            servicioSeleccionado.setDuracion(nuevaDuracion);

            // Guardar en la base de datos a través del Service
            servicioService.guardar(servicioSeleccionado);
            
            // Refrescar la tabla y limpiar
            cargarTablaServicios();
            limpiarFormulario();
            
            mostrarAlerta("Éxito", "Los datos del servicio se actualizaron correctamente.", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            // Si el usuario escribió letras en lugar de números, capturamos el error para que el programa no se rompa
            mostrarAlerta("Error de formato", "Por favor, ingrese valores numéricos válidos para el precio y la duración.", Alert.AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            // Captura las validaciones de tu ServicioService (ej: precio <= 0)
            mostrarAlerta("Error de validación", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error inesperado al guardar los cambios en la base de datos.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarFormulario() {
        servicioSeleccionado = null;
        txtNombre.clear();
        txtPrecio.clear();
        txtDuracion.clear();
        tablaServicios.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}