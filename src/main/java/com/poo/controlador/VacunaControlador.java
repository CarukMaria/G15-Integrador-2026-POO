package com.poo.controlador;

import com.poo.modelo.Vacuna;
import com.poo.servicio.VacunaServicio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class VacunaControlador {

    private VacunaServicio servicio;
    private Vacuna vacunaSeleccionada; // Para saber si estamos editando o creando una nueva
    private ObservableList<Vacuna> listaObservableVacunas;

    // --- ELEMENTOS DEL FXML ---
    @FXML private TextField txtBuscar;
    @FXML private Button btnBuscar;
    @FXML private Button btnNuevaVacuna;
    
    @FXML private TableView<Vacuna> tablaVacunas;
    @FXML private TableColumn<Vacuna, String> colNombre;
    @FXML private TableColumn<Vacuna, String> colEnfermedad;
    @FXML private TableColumn<Vacuna, Integer> colPeriodicidad;
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtEnfermedad;
    @FXML private Spinner<Integer> spPeriodicidad;
    
    @FXML private Button btnGuardarVacuna;
    @FXML private Button btnEliminar;
    @FXML private Button btnCancelar;

    public VacunaControlador() {
        servicio = new VacunaServicio();
    }

    @FXML
    public void initialize() {
        // Configurar las columnas de la tabla para que lean los atributos de la clase Vacuna
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEnfermedad.setCellValueFactory(new PropertyValueFactory<>("enfermedad"));
        colPeriodicidad.setCellValueFactory(new PropertyValueFactory<>("periodicidad"));

        // Configurar el Spinner de periodicidad (valores de 1 a 120 meses, valor inicial 12)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, 12);
        spPeriodicidad.setValueFactory(valueFactory);

        // Cargar los datos iniciales en la tabla
        cargarTabla();

        // Escuchar cuando se hace clic en una fila de la tabla para editarla
        tablaVacunas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarVacunaEnFormulario(newSelection);
            }
        });

        // Asignar acciones a los botones
        btnGuardarVacuna.setOnAction(e -> guardarVacuna());
        btnEliminar.setOnAction(e -> eliminarVacuna());
        btnCancelar.setOnAction(e -> limpiarFormulario());
        btnNuevaVacuna.setOnAction(e -> limpiarFormulario());
        btnBuscar.setOnAction(e -> buscarVacuna());
    }

    // --- MÉTODOS DE ACCIÓN ---

    private void cargarTabla() {
        List<Vacuna> vacunas = servicio.listar();
        listaObservableVacunas = FXCollections.observableArrayList(vacunas);
        tablaVacunas.setItems(listaObservableVacunas);
    }

    private void mostrarVacunaEnFormulario(Vacuna vacuna) {
        this.vacunaSeleccionada = vacuna;
        txtNombre.setText(vacuna.getNombre());
        txtEnfermedad.setText(vacuna.getEnfermedad());
        spPeriodicidad.getValueFactory().setValue(vacuna.getPeriodicidad());
    }

    private void guardarVacuna() {
        // Validar que los campos no estén vacíos
        if (txtNombre.getText().isEmpty() || txtEnfermedad.getText().isEmpty()) {
            mostrarAlerta("Error", "Los campos Nombre y Enfermedad son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        // Si no hay vacuna seleccionada, creamos una nueva
        if (vacunaSeleccionada == null) {
            vacunaSeleccionada = new Vacuna();
        }

        // Actualizamos los datos
        vacunaSeleccionada.setNombre(txtNombre.getText());
        vacunaSeleccionada.setEnfermedad(txtEnfermedad.getText());
        vacunaSeleccionada.setPeriodicidad(spPeriodicidad.getValue());

        // Guardamos en la base de datos
        servicio.guardar(vacunaSeleccionada);
        
        cargarTabla();
        limpiarFormulario();
        mostrarAlerta("Éxito", "La vacuna se guardó correctamente.", Alert.AlertType.INFORMATION);
    }

    private void eliminarVacuna() {
        if (vacunaSeleccionada != null) {
            try {
                servicio.eliminar(vacunaSeleccionada);
                cargarTabla();
                limpiarFormulario();
            } catch (Exception e) {
                // Capturamos el error por si la vacuna ya está registrada en el historial de alguna mascota
                mostrarAlerta("Error al eliminar", "No se puede eliminar la vacuna porque ya está asociada a vacunaciones registradas.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Atención", "Debe seleccionar una vacuna de la tabla para eliminarla.", Alert.AlertType.WARNING);
        }
    }

    private void limpiarFormulario() {
        vacunaSeleccionada = null;
        txtNombre.clear();
        txtEnfermedad.clear();
        spPeriodicidad.getValueFactory().setValue(12);
        tablaVacunas.getSelectionModel().clearSelection();
    }

    private void buscarVacuna() {
        String textoBusqueda = txtBuscar.getText().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            tablaVacunas.setItems(listaObservableVacunas);
        } else {
            List<Vacuna> filtradas = listaObservableVacunas.stream()
                    .filter(v -> v.getNombre().toLowerCase().contains(textoBusqueda) || 
                                 v.getEnfermedad().toLowerCase().contains(textoBusqueda))
                    .collect(Collectors.toList());
            tablaVacunas.setItems(FXCollections.observableArrayList(filtradas));
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}