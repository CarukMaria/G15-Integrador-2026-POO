package com.poo.controlador;

import com.poo.modelo.EstadoTurno;
import com.poo.modelo.Mascota;
import com.poo.modelo.Servicio;
import com.poo.modelo.ServicioPrestado;
import com.poo.modelo.Turno;
import com.poo.modelo.Vacuna;
import com.poo.modelo.Vacunacion;
import com.poo.modelo.Veterinario;
import com.poo.servicio.MascotaService;
import com.poo.servicio.ServicioService;
import com.poo.servicio.TurnoService;
import com.poo.servicio.VacunaService;
import com.poo.servicio.VeterinarioService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class TurnoController {

    // --- SERVICIOS DE BD ---
    private TurnoService turnoService;
    private MascotaService mascotaService;
    private VeterinarioService veterinarioService;
    private VacunaService vacunaService;
    private ServicioService servicioService; 

    private Turno turnoSeleccionado;
    private ObservableList<Turno> listaObservableTurnos;
    private ObservableList<ServicioPrestado> listaServiciosDelTurno;

    // --- FXML: BUSCADOR ---
    @FXML private TextField txtBuscarTurno;
    @FXML private Button btnBuscarTurno;
    @FXML private Button btnNuevoTurno;

    // --- FXML: TABLA PRINCIPAL ---
    @FXML private TableView<Turno> tablaTurnos;
    @FXML private TableColumn<Turno, LocalDate> colFecha;
    @FXML private TableColumn<Turno, LocalTime> colHora;
    @FXML private TableColumn<Turno, String> colMascota;
    @FXML private TableColumn<Turno, EstadoTurno> colEstado;

    // --- FXML: FORMULARIO ---
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbHora;
    @FXML private ComboBox<String> cbMinutos;
    @FXML private ComboBox<Mascota> cbMascota;
    @FXML private ComboBox<Veterinario> cbVeterinario;
    @FXML private ComboBox<EstadoTurno> cbEstado;

    @FXML private Button btnGuardarTurno;
    @FXML private Button btnEliminarTurno;
    @FXML private Button btnCancelar;

    // --- FXML: SERVICIOS Y VACUNAS ---
    @FXML private TableView<ServicioPrestado> tablaServiciosTurno;
    @FXML private TableColumn<ServicioPrestado, String> colNombreServicio;
    @FXML private TableColumn<ServicioPrestado, Double> colPrecioServicio;
    @FXML private TableColumn<ServicioPrestado, Integer> colDuracionServicio;

    @FXML private ComboBox<Servicio> cbServicios; 
    @FXML private ComboBox<Vacuna> cbVacunas;
    
    @FXML private Button btnAgregarServicio;
    @FXML private Button btnQuitarServicio;
    
    @FXML private Label lblDuracion;
    @FXML private Label lblTotal;

    public TurnoController() {
        turnoService = new TurnoService();
        mascotaService = new MascotaService();
        veterinarioService = new VeterinarioService();
        vacunaService = new VacunaService();
        servicioService = new ServicioService(); 
    }

    @FXML
    public void initialize() {
        listaServiciosDelTurno = FXCollections.observableArrayList();
        cbVacunas.setVisible(false);

        // Configurar columnas de la tabla de Turnos (Principal)
        colFecha.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getFechaHora().toLocalDate()));
        colHora.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getFechaHora().toLocalTime()));
        colMascota.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMascota().getNombre()));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // 2. Configurar columnas de la tabla de Servicios Prestados
        colNombreServicio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServicio().getNombre()));
        colPrecioServicio.setCellValueFactory(new PropertyValueFactory<>("precioServicioPrestado"));
        colDuracionServicio.setCellValueFactory(new PropertyValueFactory<>("duracionServicioPrestado"));
        
        tablaServiciosTurno.setItems(listaServiciosDelTurno);

        // Cargar opciones en los ComboBox de horarios
        cbHora.setItems(FXCollections.observableArrayList("08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"));
        cbMinutos.setItems(FXCollections.observableArrayList("00", "15", "30", "45"));

        // Cargar datos desde la base de datos
        cbMascota.setItems(FXCollections.observableArrayList(mascotaService.listar()));
        cbVeterinario.setItems(FXCollections.observableArrayList(veterinarioService.listar()));
        cbEstado.setItems(FXCollections.observableArrayList(EstadoTurno.values()));
        cbVacunas.setItems(FXCollections.observableArrayList(vacunaService.listar()));
        cbServicios.setItems(FXCollections.observableArrayList(servicioService.listar()));

        cargarTablaTurnos();

        // Escuchar clics en la tabla principal
        tablaTurnos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) mostrarTurnoEnFormulario(newVal);
        });

        // Lógica para mostrar/ocultar el ComboBox de vacunas
        cbServicios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && (newVal instanceof Vacunacion || newVal.getNombre().toLowerCase().contains("vacun"))) {
                cbVacunas.setVisible(true);
            } else {
                cbVacunas.setVisible(false);
                cbVacunas.getSelectionModel().clearSelection();
            }
        });

        // Eventos de botones que no estaban enlazados desde el FXML
        btnNuevoTurno.setOnAction(e -> limpiarFormulario());
        btnCancelar.setOnAction(e -> limpiarFormulario());
        btnBuscarTurno.setOnAction(e -> buscarTurno());
        btnAgregarServicio.setOnAction(e -> agregarServicioAccion());
        btnQuitarServicio.setOnAction(e -> quitarServicioAccion());
    }

    // --- ACCIONES DE SERVICIOS ---

    private void agregarServicioAccion() {

        Servicio servicioSeleccionado = cbServicios.getValue();

        if (servicioSeleccionado == null) {
            mostrarAlerta(
                "Atención",
                "Debe seleccionar un servicio.",
                Alert.AlertType.WARNING
            );
            return;
        }


        if (servicioSeleccionado instanceof Vacunacion) {

            Vacuna vacunaSeleccionada = cbVacunas.getValue();

            if (vacunaSeleccionada == null) {
                mostrarAlerta(
                    "Atención",
                    "Debe seleccionar una vacuna.",
                    Alert.AlertType.WARNING
                );
                return;
            }


            Mascota mascota = cbMascota.getValue();

            if (mascota == null) {
                mostrarAlerta(
                   "Error",
                    "Seleccione una mascota antes.",
                    Alert.AlertType.WARNING
                );
                return;
            }
            
            LocalDate fechaSeleccionada = dpFecha.getValue();
            
            if (fechaSeleccionada == null) {
                mostrarAlerta(
                   "Error",
                    "Seleccione una fecha para el turno en el calendario antes de agregar la vacuna.",
                    Alert.AlertType.WARNING
                );
                return;
            }

            // ACÁ ESTÁ EL CAMBIO CLAVE: Le pasamos la fechaSeleccionada a la mascota
            if (!mascota.puedeRecibirVacuna(vacunaSeleccionada, fechaSeleccionada)) {

                mostrarAlerta(
                    "Vacuna no permitida",
                    "La mascota todavía tiene vigente esta vacuna para esa fecha.",
                    Alert.AlertType.WARNING
                );

                return;
            }
        }

        if (servicioSeleccionado instanceof Vacunacion vacunacion) {
            Vacuna vacunaSeleccionada = cbVacunas.getValue();
            vacunacion.setVacuna(vacunaSeleccionada);
        }
        
        ServicioPrestado nuevo =
                new ServicioPrestado(servicioSeleccionado, null);


        listaServiciosDelTurno.add(nuevo);

        actualizarTotales();
    }

    private void quitarServicioAccion() {
        ServicioPrestado seleccionado = tablaServiciosTurno.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaServiciosDelTurno.remove(seleccionado);
            actualizarTotales();
        } else {
            mostrarAlerta("Atención", "Seleccione un servicio de la tabla inferior para quitarlo.", Alert.AlertType.WARNING);
        }
    }

    // --- ACCIONES PRINCIPALES (Conectadas desde FXML) ---

    @FXML
    public void guardarTurnoAccion(ActionEvent event) {

        LocalDate fecha = dpFecha.getValue();
        String hora = cbHora.getValue();
        String minutos = cbMinutos.getValue();
        Mascota mascota = cbMascota.getValue();
        Veterinario veterinario = cbVeterinario.getValue();


        if (fecha == null || hora == null || minutos == null 
                || mascota == null || veterinario == null) {

            mostrarAlerta(
                "Error",
                "Faltan datos básicos del turno.",
                Alert.AlertType.WARNING
            );

            return;
        }


        if (listaServiciosDelTurno.isEmpty()) {

            mostrarAlerta(
                "Error",
                "El turno debe tener al menos un servicio.",
                Alert.AlertType.WARNING
            );

            return;
        }


        LocalDateTime fechaHoraTurno =
                LocalDateTime.of(
                        fecha,
                        LocalTime.of(
                            Integer.parseInt(hora),
                            Integer.parseInt(minutos)
                        )
                );


        if (turnoSeleccionado == null) {

           turnoSeleccionado =
                   new Turno(
                        fechaHoraTurno,
                        mascota,
                        veterinario
                    );

        } else {

            turnoSeleccionado.setFechaHora(fechaHoraTurno);
            turnoSeleccionado.setMascota(mascota);
            turnoSeleccionado.setVeterinario(veterinario);
        }



        turnoSeleccionado.getServiciosPrestados().clear();


        for (ServicioPrestado sp : listaServiciosDelTurno) {
            turnoSeleccionado.agregarServicioPrestado(sp);
        }



        try {


                if (turnoSeleccionado.getIdTurno() != null 
                    && cbEstado.getValue() != null) {

                    turnoService.cambiarEstado(
                    turnoSeleccionado,
                    cbEstado.getValue()
                    );
                }

                turnoService.guardar(turnoSeleccionado);



            cargarTablaTurnos();
            limpiarFormulario();


            mostrarAlerta(
                "Éxito",
                "Turno guardado correctamente.",
                Alert.AlertType.INFORMATION
            );



        } catch(Exception e) {


            mostrarAlerta(
                "Error",
                e.getMessage(),
                Alert.AlertType.ERROR
            );
        }
    }


    @FXML
    public void eliminarTurnoAccion(ActionEvent event) {
        if (turnoSeleccionado != null) {
            try {
                turnoService.eliminar(turnoSeleccionado);
                cargarTablaTurnos();
                limpiarFormulario();
                mostrarAlerta("Éxito", "Turno eliminado.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo eliminar el turno.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Atención", "Debe seleccionar un turno de la tabla.", Alert.AlertType.WARNING);
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private void cargarTablaTurnos() {
        listaObservableTurnos = FXCollections.observableArrayList(turnoService.listar());
        tablaTurnos.setItems(listaObservableTurnos);
    }

    private void mostrarTurnoEnFormulario(Turno turno) {
        this.turnoSeleccionado = turno;
        dpFecha.setValue(turno.getFechaHora().toLocalDate());
        
        // Formatear horas y minutos para que coincidan con el ComboBox ("09", "15", etc.)
        String horaStr = String.format("%02d", turno.getFechaHora().getHour());
        String minStr = String.format("%02d", turno.getFechaHora().getMinute());
        cbHora.setValue(horaStr);
        cbMinutos.setValue(minStr);
        
        cbMascota.setValue(turno.getMascota());
        cbVeterinario.setValue(turno.getVeterinario());
        cbEstado.setValue(turno.getEstado());

        // Cargar los servicios de este turno
        listaServiciosDelTurno.setAll(turno.getServiciosPrestados());
        actualizarTotales();
    }

    private void limpiarFormulario() {
        turnoSeleccionado = null;
        dpFecha.setValue(null);
        cbHora.getSelectionModel().clearSelection();
        cbMinutos.getSelectionModel().clearSelection();
        cbMascota.getSelectionModel().clearSelection();
        cbVeterinario.getSelectionModel().clearSelection();
        cbEstado.setValue(EstadoTurno.PENDIENTE);
        cbServicios.getSelectionModel().clearSelection();
        cbVacunas.getSelectionModel().clearSelection();
        cbVacunas.setVisible(false);
        
        listaServiciosDelTurno.clear();
        actualizarTotales();
        tablaTurnos.getSelectionModel().clearSelection();
    }

    private void actualizarTotales() {
        double total = 0.0;
        int duracion = 0;
        for (ServicioPrestado sp : listaServiciosDelTurno) {
            total += sp.getPrecioServicioPrestado();
            duracion += sp.getDuracionServicioPrestado();
        }
        lblTotal.setText("Total: $" + total);
        lblDuracion.setText("Duración estimada: " + duracion + " min");
    }

    private void buscarTurno() {
        String textoBusqueda = txtBuscarTurno.getText().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            tablaTurnos.setItems(listaObservableTurnos);
        } else {
            List<Turno> filtrados = listaObservableTurnos.stream()
                    .filter(t -> t.getMascota().getNombre().toLowerCase().contains(textoBusqueda) || 
                                 t.getFechaHora().toLocalDate().toString().contains(textoBusqueda))
                    .collect(Collectors.toList());
            tablaTurnos.setItems(FXCollections.observableArrayList(filtrados));
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