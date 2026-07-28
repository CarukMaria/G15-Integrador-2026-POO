package com.poo.controlador;

import com.poo.modelo.Cliente;
import com.poo.modelo.Mascota;
import com.poo.servicio.ClienteService;

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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteController {

    private ClienteService servicio;
    private Cliente clienteSeleccionado;
    private ObservableList<Cliente> listaObservableClientes;

    // --- ELEMENTOS FXML: BUSCADOR ---
    @FXML private TextField txtBuscarCliente;
    @FXML private Button btnBuscar;
    @FXML private Button btnNuevoCliente;

    // --- ELEMENTOS FXML: TABLA CLIENTES ---
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colDni;
    @FXML private TableColumn<Cliente, String> colApellido;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;

    // --- ELEMENTOS FXML: FORMULARIO CLIENTE ---
    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private Button btnGuardarCliente;
    @FXML private Button btnBajaCliente;
    @FXML private Button btnCancelar;

    // --- ELEMENTOS FXML: TABLA MASCOTAS (Preparada para la fase 2) ---
    @FXML private TableView<Mascota> tablaMascotas;
    @FXML private TableColumn<Mascota, String> colMascotaNumFicha;
    @FXML private TableColumn<Mascota, String> colMascotaNombre;
    @FXML private TableColumn<Mascota, String> colMascotaRaza;
    @FXML private TableColumn<Mascota, LocalDate> colMascotaFechaNac;
    @FXML private TableColumn<Mascota, String> colMascotaEspecie;
    
    @FXML private Button btnAgregarMascota;
    @FXML private Button btnEditarMascota;

    public ClienteController() {
        servicio = new ClienteService();
    }

    @FXML
    public void initialize() {
        // 1. Configurar columnas de la tabla Clientes
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // 2. Configurar columnas de la tabla Mascotas (listas para cuando agregues mascotas)
        // Nota: Los nombres entre comillas deben coincidir exactamente con los atributos de tu clase Mascota
        colMascotaNumFicha.setCellValueFactory(new PropertyValueFactory<>("numeroFicha"));
        colMascotaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMascotaRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colMascotaFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colMascotaEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));

        // 3. Cargar la tabla de clientes al iniciar
        cargarTablaClientes();

        // 4. Escuchar clics en la tabla de clientes para cargar el formulario
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarClienteEnFormulario(newSelection);
            }
        });

        // 5. Asignar eventos a los botones superiores (que no tienen onAction en el FXML)
        btnBuscar.setOnAction(e -> buscarCliente());
        btnNuevoCliente.setOnAction(e -> limpiarFormulario());
        
        // Botones de mascotas desactivados por ahora (Fase 2)
        btnAgregarMascota.setDisable(true);
        btnEditarMascota.setDisable(true);
    }

    // --- MÉTODOS DE LA LÓGICA DE CLIENTES ---

    private void cargarTablaClientes() {
        List<Cliente> clientes = servicio.listar();
        listaObservableClientes = FXCollections.observableArrayList(clientes);
        tablaClientes.setItems(listaObservableClientes);
    }

    private void mostrarClienteEnFormulario(Cliente cliente) {
        this.clienteSeleccionado = cliente;
        txtDni.setText(cliente.getDni());
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtTelefono.setText(cliente.getTelefono() != null ? cliente.getTelefono() : "");
        
        // Cargar las mascotas de este cliente en la tabla inferior
        if (cliente.getMascotas() != null) {
            tablaMascotas.setItems(FXCollections.observableArrayList(cliente.getMascotas()));
        } else {
            tablaMascotas.getItems().clear();
        }

        // Cuando habilites la fase 2, acá podés activar los botones de mascotas:
        // btnAgregarMascota.setDisable(false);
    }

    // Método conectado directamente desde el FXML (onAction="#guardarClienteAccion")
    @FXML
    public void guardarClienteAccion(ActionEvent event) {
        if (txtDni.getText().isEmpty() || txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()) {
            mostrarAlerta("Campos incompletos", "El DNI, Nombre y Apellido son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        if (clienteSeleccionado == null) {
            clienteSeleccionado = new Cliente();
        }

        clienteSeleccionado.setDni(txtDni.getText());
        clienteSeleccionado.setNombre(txtNombre.getText());
        clienteSeleccionado.setApellido(txtApellido.getText());
        clienteSeleccionado.setTelefono(txtTelefono.getText());

        try {
            servicio.guardar(clienteSeleccionado);
            cargarTablaClientes();
            limpiarFormulario();
            mostrarAlerta("Éxito", "Cliente guardado correctamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el cliente. Verifique que el DNI no esté duplicado.", Alert.AlertType.ERROR);
        }
    }

    // Método conectado directamente desde el FXML (onAction="#darDeBajaClienteAccion")
    @FXML
    public void darDeBajaClienteAccion(ActionEvent event) {
        if (clienteSeleccionado != null) {
            try {
                servicio.eliminar(clienteSeleccionado);
                cargarTablaClientes();
                limpiarFormulario();
                mostrarAlerta("Éxito", "Cliente eliminado correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "No se puede eliminar el cliente. Es posible que tenga mascotas o turnos asociados.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Atención", "Debe seleccionar un cliente de la tabla primero.", Alert.AlertType.WARNING);
        }
    }

    // Método conectado directamente desde el FXML (onAction="#limpiarFormulario")
    @FXML
    public void limpiarFormulario(ActionEvent event) {
        limpiarFormulario();
    }

    // Sobrecarga para usarlo internamente sin el ActionEvent
    private void limpiarFormulario() {
        clienteSeleccionado = null;
        txtDni.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        tablaClientes.getSelectionModel().clearSelection();
        tablaMascotas.getItems().clear();
        
        // Bloquear botones de mascota si no hay cliente seleccionado
        btnAgregarMascota.setDisable(true);
        btnEditarMascota.setDisable(true);
    }

    private void buscarCliente() {
        String textoBusqueda = txtBuscarCliente.getText().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            tablaClientes.setItems(listaObservableClientes);
        } else {
            List<Cliente> filtrados = listaObservableClientes.stream()
                    .filter(c -> c.getDni().toLowerCase().contains(textoBusqueda) || 
                                 c.getApellido().toLowerCase().contains(textoBusqueda))
                    .collect(Collectors.toList());
            tablaClientes.setItems(FXCollections.observableArrayList(filtrados));
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