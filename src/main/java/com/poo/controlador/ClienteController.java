package com.poo.controlador;

import com.poo.modelo.Cliente;
import com.poo.modelo.Especie;
import com.poo.modelo.Mascota;
import com.poo.servicio.ClienteService;
import com.poo.servicio.MascotaService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class ClienteController {


    private ClienteService servicio;
    private MascotaService mascotaService;


    private Cliente clienteSeleccionado;
    private Mascota mascotaSeleccionada;


    private ObservableList<Cliente> listaObservableClientes;



    // BUSCADOR

    @FXML private TextField txtBuscarCliente;
    @FXML private Button btnBuscar;
    @FXML private Button btnNuevoCliente;



    // TABLA CLIENTES

    @FXML private TableView<Cliente> tablaClientes;

    @FXML private TableColumn<Cliente,String> colDni;
    @FXML private TableColumn<Cliente,String> colApellido;
    @FXML private TableColumn<Cliente,String> colNombre;
    @FXML private TableColumn<Cliente,String> colTelefono;



    // CLIENTE

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;


    @FXML private Button btnGuardarCliente;
    @FXML private Button btnBajaCliente;
    @FXML private Button btnCancelar;



    // MASCOTAS

    @FXML private TableView<Mascota> tablaMascotas;

    @FXML private TableColumn<Mascota,String> colMascotaNumFicha;
    @FXML private TableColumn<Mascota,String> colMascotaNombre;
    @FXML private TableColumn<Mascota,String> colMascotaRaza;
    @FXML private TableColumn<Mascota,LocalDate> colMascotaFechaNac;
    @FXML private TableColumn<Mascota,String> colMascotaEspecie;



    @FXML private Button btnAgregarMascota;
    @FXML private Button btnEditarMascota;



    // FORMULARIO MASCOTA

    @FXML private TextField txtMascotaNombre;
    @FXML private TextField txtMascotaRaza;
    @FXML private DatePicker dpMascotaFecha;
    @FXML private ComboBox<Especie> cbMascotaEspecie;


    @FXML private Button btnGuardarMascota;
    @FXML private Button btnCancelarMascota;



    public ClienteController(){

        servicio = new ClienteService();
        mascotaService = new MascotaService();

    }



    @FXML
    public void initialize(){


        // CLIENTES

        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));



        // MASCOTAS

        colMascotaNumFicha.setCellValueFactory(
                new PropertyValueFactory<>("numeroFicha")
        );

        colMascotaNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colMascotaRaza.setCellValueFactory(
                new PropertyValueFactory<>("raza")
        );

        colMascotaFechaNac.setCellValueFactory(
                new PropertyValueFactory<>("fechaNacimiento")
        );

        colMascotaEspecie.setCellValueFactory(
                new PropertyValueFactory<>("especie")
        );



        cbMascotaEspecie.setItems(
                FXCollections.observableArrayList(
                        Especie.values()
                )
        );



        cargarTablaClientes();



        tablaClientes.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, viejo, nuevo)->{

                    if(nuevo != null){

                        mostrarClienteEnFormulario(nuevo);

                    }

                });



        tablaMascotas.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs,viejo,nuevo)->{

                    mascotaSeleccionada = nuevo;

                });



        btnBuscar.setOnAction(e->buscarCliente());

        btnNuevoCliente.setOnAction(e->limpiarFormulario());



        btnAgregarMascota.setDisable(true);
        btnEditarMascota.setDisable(true);

        btnGuardarMascota.setDisable(true);
        btnCancelarMascota.setDisable(true);


    }




    private void cargarTablaClientes(){

        List<Cliente> clientes = servicio.listar();

        listaObservableClientes =
                FXCollections.observableArrayList(clientes);

        tablaClientes.setItems(listaObservableClientes);

    }





    private void mostrarClienteEnFormulario(Cliente cliente){


        clienteSeleccionado = cliente;


        txtDni.setText(cliente.getDni());
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtTelefono.setText(cliente.getTelefono());



        // evita problema LazyInitialization

        Cliente actualizado =
                servicio.buscarPorId(cliente.getIdCliente());


        tablaMascotas.setItems(
                FXCollections.observableArrayList(
                        actualizado.getMascotas()
                )
        );


        btnAgregarMascota.setDisable(false);
        btnEditarMascota.setDisable(false);


    }





    // ======================
    // MASCOTAS
    // ======================


    @FXML
    private void agregarMascotaAccion(ActionEvent e){

        limpiarFormularioMascota();

        mascotaSeleccionada = new Mascota();

        btnGuardarMascota.setDisable(false);
        btnCancelarMascota.setDisable(false);

    }





    @FXML
    private void editarMascotaAccion(ActionEvent e){


        if(mascotaSeleccionada == null){

            mostrarAlerta(
                    "Atención",
                    "Seleccione una mascota.",
                    Alert.AlertType.WARNING
            );

            return;
        }


        txtMascotaNombre.setText(
                mascotaSeleccionada.getNombre()
        );

        txtMascotaRaza.setText(
                mascotaSeleccionada.getRaza()
        );

        dpMascotaFecha.setValue(
                mascotaSeleccionada.getFechaNacimiento()
        );

        cbMascotaEspecie.setValue(
                mascotaSeleccionada.getEspecie()
        );


        btnGuardarMascota.setDisable(false);
        btnCancelarMascota.setDisable(false);

    }





    @FXML
    private void guardarMascotaAccion(ActionEvent e){


        if(mascotaSeleccionada == null){

            mascotaSeleccionada = new Mascota();

        }



        mascotaSeleccionada.setNombre(
                txtMascotaNombre.getText()
        );


        mascotaSeleccionada.setRaza(
                txtMascotaRaza.getText()
        );


        mascotaSeleccionada.setFechaNacimiento(
                dpMascotaFecha.getValue()
        );


        mascotaSeleccionada.setEspecie(
                cbMascotaEspecie.getValue()
        );



        if(mascotaSeleccionada.getNumeroFicha()==null){

            mascotaSeleccionada.setNumeroFicha(
                    "F-" + System.currentTimeMillis()%10000
            );

        }



        mascotaSeleccionada.setCliente(
                clienteSeleccionado
        );


        clienteSeleccionado.agregarMascota(
                mascotaSeleccionada
        );



        mascotaService.guardar(
                mascotaSeleccionada
        );



        mostrarClienteEnFormulario(
                clienteSeleccionado
        );


        limpiarFormularioMascota();


        mostrarAlerta(
                "Éxito",
                "Mascota guardada correctamente.",
                Alert.AlertType.INFORMATION
        );


    }






    @FXML
    private void cancelarMascotaAccion(ActionEvent e){

        limpiarFormularioMascota();

    }





    private void limpiarFormularioMascota(){

        txtMascotaNombre.clear();
        txtMascotaRaza.clear();
        dpMascotaFecha.setValue(null);
        cbMascotaEspecie.setValue(null);


        mascotaSeleccionada=null;


        btnGuardarMascota.setDisable(true);
        btnCancelarMascota.setDisable(true);

    }





    // ======================
    // CLIENTES
    // ======================


    @FXML
    public void guardarClienteAccion(ActionEvent e){


        if(clienteSeleccionado==null){

            clienteSeleccionado = new Cliente();

        }


        clienteSeleccionado.setDni(txtDni.getText());
        clienteSeleccionado.setNombre(txtNombre.getText());
        clienteSeleccionado.setApellido(txtApellido.getText());
        clienteSeleccionado.setTelefono(txtTelefono.getText());


        servicio.guardar(clienteSeleccionado);


        cargarTablaClientes();


    }





    @FXML
    public void darDeBajaClienteAccion(ActionEvent e){

        if(clienteSeleccionado!=null){

            servicio.eliminar(clienteSeleccionado);

            cargarTablaClientes();

            limpiarFormulario();

        }

    }





    @FXML
    public void limpiarFormulario(ActionEvent e){

        limpiarFormulario();

    }





    private void limpiarFormulario(){


        clienteSeleccionado=null;


        txtDni.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();


        tablaMascotas.getItems().clear();


        btnAgregarMascota.setDisable(true);
        btnEditarMascota.setDisable(true);

    }





    private void buscarCliente(){


        String texto =
                txtBuscarCliente.getText()
                        .toLowerCase();



        List<Cliente> filtrados =
                listaObservableClientes.stream()
                .filter(c ->
                        c.getDni().toLowerCase().contains(texto)
                        ||
                        c.getApellido().toLowerCase().contains(texto)
                )
                .collect(Collectors.toList());



        tablaClientes.setItems(
                FXCollections.observableArrayList(filtrados)
        );

    }




    private void mostrarAlerta(
            String titulo,
            String mensaje,
            Alert.AlertType tipo
    ){

        Alert a = new Alert(tipo);

        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);

        a.showAndWait();

    }

}