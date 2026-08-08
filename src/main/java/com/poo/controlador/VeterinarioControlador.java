package com.poo.controlador;

import com.poo.modelo.Especialidad;
import com.poo.modelo.Veterinario;
import com.poo.servicio.VeterinarioServicio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class VeterinarioControlador {

    private VeterinarioServicio servicio;
    private Veterinario veterinarioSeleccionado;
    private ObservableList<Veterinario> listaObservableVeterinarios;

    // --- ELEMENTOS FXML: BUSCADOR ---
    @FXML private TextField txtBuscarVeterinario;
    @FXML private Button btnBuscar;
    @FXML private Button btnNuevoVeterinario;

    // --- ELEMENTOS FXML: TABLA VETERINARIOS ---
    @FXML private TableView<Veterinario> tablaVeterinarios;
    @FXML private TableColumn<Veterinario, String> colMatricula;
    @FXML private TableColumn<Veterinario, String> colNombre;
    @FXML private TableColumn<Veterinario, String> colApellido;

    // --- ELEMENTOS FXML: FORMULARIO VETERINARIO ---
    @FXML private TextField txtMatricula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private Button btnGuardarVeterinario;
    @FXML private Button btnBajaVeterinario;
    @FXML private Button btnCancelar;

    // --- ELEMENTOS FXML: ESPECIALIDADES ---
    @FXML private ListView<Especialidad> listaEspecialidades;
    @FXML private ComboBox<Especialidad> cbEspecialidades;
    @FXML private Button btnAgregarEspecialidad;
    @FXML private Button btnQuitarEspecialidad;

    public VeterinarioControlador() {
        servicio = new VeterinarioServicio();
    }

    @FXML
    public void initialize() {
        //  Configurar columnas de la tabla Veterinarios
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        // Cargar el ComboBox con los valores del Enum Especialidad
        cbEspecialidades.setItems(FXCollections.observableArrayList(Especialidad.values()));

        // Cargar la tabla de veterinarios al iniciar
        cargarTablaVeterinarios();

        // Escuchar clics en la tabla para cargar el formulario
        tablaVeterinarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarVeterinarioEnFormulario(newSelection);
            }
        });

        // Asignar eventos a los botones que no tienen onAction en el FXML
        btnBuscar.setOnAction(e -> buscarVeterinario());
        btnNuevoVeterinario.setOnAction(e -> limpiarFormulario());
        btnCancelar.setOnAction(e -> limpiarFormulario());
    }

    // --- MÉTODOS DE LA LÓGICA DE VETERINARIOS ---

    private void cargarTablaVeterinarios() {
        List<Veterinario> veterinarios = servicio.listar();
        listaObservableVeterinarios = FXCollections.observableArrayList(veterinarios);
        tablaVeterinarios.setItems(listaObservableVeterinarios);
    }

    private void mostrarVeterinarioEnFormulario(Veterinario veterinario) {
        this.veterinarioSeleccionado = veterinario;
        txtMatricula.setText(veterinario.getMatricula());
        txtNombre.setText(veterinario.getNombre());
        txtApellido.setText(veterinario.getApellido());
        
        // Cargar las especialidades en el ListView
        actualizarListaEspecialidades();
    }

    // Conectado desde el FXML: onAction="#guardarVeterinarioAccion"
    // Conectado desde el FXML: onAction="#guardarVeterinarioAccion"
    @FXML
    public void guardarVeterinarioAccion(ActionEvent event) {
        try {
            if (veterinarioSeleccionado == null) {
                // Usamos el constructor del modelo rico
                veterinarioSeleccionado = new Veterinario(
                    txtMatricula.getText(),
                    txtNombre.getText(),
                    txtApellido.getText()
                );
            } else {
                // Si es edición, usamos los setters que también están blindados
                veterinarioSeleccionado.setMatricula(txtMatricula.getText());
                veterinarioSeleccionado.setNombre(txtNombre.getText());
                veterinarioSeleccionado.setApellido(txtApellido.getText());
            }

            servicio.guardar(veterinarioSeleccionado);
            cargarTablaVeterinarios();
            limpiarFormulario();
            mostrarAlerta("Éxito", "Veterinario guardado correctamente.", Alert.AlertType.INFORMATION);

        } catch (IllegalArgumentException ex) {
            // Atrapa nombres vacíos, nulos, etc.
            mostrarAlerta("Campos inválidos", ex.getMessage(), Alert.AlertType.WARNING);
            
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el veterinario. Verifique que la matrícula no esté duplicada.", Alert.AlertType.ERROR);
        }
    }

    // Conectado desde el FXML: onAction="#eliminarVeterinarioAccion"
    @FXML
    public void eliminarVeterinarioAccion(ActionEvent event) {
        if (veterinarioSeleccionado != null) {
            try {
                servicio.eliminar(veterinarioSeleccionado);
                cargarTablaVeterinarios();
                limpiarFormulario();
                mostrarAlerta("Éxito", "Veterinario dado de baja correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "No se puede eliminar el veterinario. Es posible que tenga turnos asignados.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Atención", "Debe seleccionar un veterinario de la tabla primero.", Alert.AlertType.WARNING);
        }
    }

    // --- MÉTODOS DE ESPECIALIDADES ---

    // Conectado desde el FXML: onAction="#agregarEspecialidadAccion"
    @FXML
    public void agregarEspecialidadAccion(ActionEvent event) {
        Especialidad seleccionada = cbEspecialidades.getValue();
        
        if (seleccionada == null) {
            mostrarAlerta("Atención", "Seleccione una especialidad de la lista desplegable.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Si es un veterinario nuevo, lo instanciamos con los datos de los campos de texto
            if (veterinarioSeleccionado == null) {
                veterinarioSeleccionado = new Veterinario(
                    txtMatricula.getText(),
                    txtNombre.getText(),
                    txtApellido.getText()
                );
            }
            
            // Verificamos si ya la tiene usando el método inteligente que creamos
            if (!veterinarioSeleccionado.tieneEspecialidad(seleccionada)) {
                veterinarioSeleccionado.agregarEspecialidad(seleccionada);
                actualizarListaEspecialidades();
            } else {
                mostrarAlerta("Atención", "El veterinario ya posee esta especialidad.", Alert.AlertType.WARNING);
            }

        } catch (IllegalArgumentException ex) {
            // Si los textos estaban vacíos, el constructor del Veterinario lanza el error
            mostrarAlerta("Faltan datos", "Complete la Matrícula, Nombre y Apellido antes de agregar especialidades.", Alert.AlertType.WARNING);
        }
    }

    // Conectado desde el FXML: onAction="#quitarEspecialidadAccion"
    @FXML
    public void quitarEspecialidadAccion(ActionEvent event) {
        Especialidad seleccionada = listaEspecialidades.getSelectionModel().getSelectedItem();
        
        if (seleccionada != null && veterinarioSeleccionado != null) {
            // Usamos el método de negocio de nuestra clase para no romper el encapsulamiento
            veterinarioSeleccionado.removerEspecialidad(seleccionada);
            actualizarListaEspecialidades();
        } else {
            mostrarAlerta("Atención", "Seleccione una especialidad de la lista para quitarla.", Alert.AlertType.WARNING);
        }
    }

    private void actualizarListaEspecialidades() {
        if (veterinarioSeleccionado != null && veterinarioSeleccionado.getEspecialidades() != null) {
            listaEspecialidades.setItems(FXCollections.observableArrayList(veterinarioSeleccionado.getEspecialidades()));
        } else {
            listaEspecialidades.getItems().clear();
        }
    }

    // --- UTILIDADES ---

    private void limpiarFormulario() {
        veterinarioSeleccionado = null;
        txtMatricula.clear();
        txtNombre.clear();
        txtApellido.clear();
        cbEspecialidades.getSelectionModel().clearSelection();
        tablaVeterinarios.getSelectionModel().clearSelection();
        listaEspecialidades.getItems().clear();
    }

    private void buscarVeterinario() {
        String textoBusqueda = txtBuscarVeterinario.getText().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            tablaVeterinarios.setItems(listaObservableVeterinarios);
        } else {
            List<Veterinario> filtrados = listaObservableVeterinarios.stream()
                    .filter(v -> v.getMatricula().toLowerCase().contains(textoBusqueda) || 
                                 v.getApellido().toLowerCase().contains(textoBusqueda))
                    .collect(Collectors.toList());
            tablaVeterinarios.setItems(FXCollections.observableArrayList(filtrados));
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