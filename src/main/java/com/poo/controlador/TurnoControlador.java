package com.poo.controlador;

import com.poo.modelo.Consulta;
import com.poo.modelo.EstadoTurno;
import com.poo.modelo.Mascota;
import com.poo.modelo.Servicio;
import com.poo.modelo.ServicioPrestado;
import com.poo.modelo.Turno;
import com.poo.modelo.Vacuna;
import com.poo.modelo.Vacunacion;
import com.poo.modelo.Veterinario;
import com.poo.servicio.MascotaServicio;
import com.poo.servicio.ServicioServicio;
import com.poo.servicio.TurnoServicio;
import com.poo.servicio.VacunaServicio;
import com.poo.servicio.VeterinarioServicio;

import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class TurnoControlador {

    private TurnoServicio turnoService;
    private MascotaServicio mascotaService;
    private VeterinarioServicio veterinarioService;
    private VacunaServicio vacunaService;
    private ServicioServicio servicioServicio;

    private Turno turnoSeleccionado;
    private ObservableList<Turno> listaObservableTurnos;
    private ObservableList<ServicioPrestado> listaServiciosDelTurno;

    @FXML private TextField txtBuscarTurno;
    @FXML private Button btnBuscarTurno;
    @FXML private Button btnNuevoTurno;

    @FXML private TextArea txtDiagnostico;
    @FXML private TextArea txtTratamiento;
    @FXML private GridPane panelDatosConsulta;
    
    @FXML private TableView<Turno> tablaTurnos;
    @FXML private TableColumn<Turno, LocalDate> colFecha;
    @FXML private TableColumn<Turno, LocalTime> colHora;
    @FXML private TableColumn<Turno, String> colMascota;
    @FXML private TableColumn<Turno, EstadoTurno> colEstado;

    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbHora;
    @FXML private ComboBox<String> cbMinutos;
    @FXML private ComboBox<Mascota> cbMascota;
    @FXML private ComboBox<Veterinario> cbVeterinario;
    @FXML private ComboBox<EstadoTurno> cbEstado;

    @FXML private Button btnGuardarTurno;
    @FXML private Button btnEliminarTurno;
    @FXML private Button btnCancelar;

    @FXML private TableView<ServicioPrestado> tablaServiciosTurno;
    @FXML private TableColumn<ServicioPrestado, String> colNombreServicio;
    @FXML private TableColumn<ServicioPrestado, Double> colPrecioServicio;
    @FXML private TableColumn<ServicioPrestado, Integer> colDuracionServicio;

    @FXML private ComboBox<Servicio> cbServicios;
    @FXML private ComboBox<Vacuna> cbVacunas;

    @FXML private Button btnAgregarServicio;
    @FXML private Button btnQuitarServicio;

    @FXML private Label lblDiagnostico;
    @FXML private Label lblTratamiento;
    @FXML private Label lblDuracion;
    @FXML private Label lblTotal;

    public TurnoControlador() {
        turnoService = new TurnoServicio();
        mascotaService = new MascotaServicio();
        veterinarioService = new VeterinarioServicio();
        vacunaService = new VacunaServicio();
        servicioServicio = new ServicioServicio();
    }

    @FXML
    public void initialize() {

        listaServiciosDelTurno = FXCollections.observableArrayList();

        cbVacunas.setVisible(false);
        cbVacunas.setManaged(false);


        configurarTablas();
        cargarCombos();
        cargarTablaTurnos();

        tablaTurnos.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        mostrarTurnoEnFormulario(newVal);
                    }
                });

        cbServicios.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {

                    boolean esVacunacion =
                            newVal instanceof Vacunacion;

                    cbVacunas.setVisible(esVacunacion);
                    cbVacunas.setManaged(esVacunacion);

                    if (!esVacunacion) {
                        cbVacunas.getSelectionModel().clearSelection();
                    }

                    actualizarCamposConsulta();
                });

                cbEstado.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldVal, newVal) -> actualizarCamposConsulta());

        btnNuevoTurno.setOnAction(e -> limpiarFormulario());
        btnCancelar.setOnAction(e -> limpiarFormulario());
        btnBuscarTurno.setOnAction(e -> buscarTurno());
        btnAgregarServicio.setOnAction(e -> agregarServicioAccion());
        btnQuitarServicio.setOnAction(e -> quitarServicioAccion());
    }

    private void configurarTablas() {

        colFecha.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getFechaHora().toLocalDate()
                ));

        colHora.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getFechaHora().toLocalTime()
                ));

        colMascota.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getMascota().getNombre()
                ));

        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado")
        );

        colNombreServicio.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getServicio().getNombre()
                ));

        colPrecioServicio.setCellValueFactory(
                new PropertyValueFactory<>("precioServicioPrestado")
        );

        colDuracionServicio.setCellValueFactory(
                new PropertyValueFactory<>("duracionServicioPrestado")
        );

        tablaServiciosTurno.setItems(listaServiciosDelTurno);
    }

    private void cargarCombos() {

        cbHora.setItems(FXCollections.observableArrayList(
                "08", "09", "10", "11", "12", "13",
                "14", "15", "16", "17", "18", "19", "20"
        ));

        cbMinutos.setItems(FXCollections.observableArrayList(
                "00", "15", "30", "45"
        ));

        cbMascota.setItems(
                FXCollections.observableArrayList(mascotaService.listar())
        );

        cbVeterinario.setItems(
                FXCollections.observableArrayList(veterinarioService.listar())
        );

        cbEstado.setItems(
                FXCollections.observableArrayList(EstadoTurno.values())
        );

        cbVacunas.setItems(
                FXCollections.observableArrayList(vacunaService.listar())
        );

        cbServicios.setItems(
                FXCollections.observableArrayList(servicioServicio.listar())
        );
    }

    private void agregarServicioAccion() {

        Servicio servicio = cbServicios.getValue();

        if (servicio == null) {
            mostrarAlerta(
                    "Atención",
                    "Debe seleccionar un servicio.",
                    Alert.AlertType.WARNING
            );
            return;
        }

        if (servicio instanceof Vacunacion vacunacion) {

            Mascota mascota = cbMascota.getValue();
            Vacuna vacuna = cbVacunas.getValue();
            LocalDate fecha = dpFecha.getValue();

            if (mascota == null) {
                mostrarAlerta(
                        "Atención",
                        "Seleccione una mascota.",
                        Alert.AlertType.WARNING
                );
                return;
            }

            if (vacuna == null) {
                mostrarAlerta(
                        "Atención",
                        "Debe seleccionar una vacuna.",
                        Alert.AlertType.WARNING
                );
                return;
            }

            if (fecha == null) {
                mostrarAlerta(
                        "Atención",
                        "Seleccione la fecha del turno.",
                        Alert.AlertType.WARNING
                );
                return;
            }

            if (!mascota.puedeRecibirVacuna(vacuna, fecha)) {
                mostrarAlerta(
                        "Vacuna no permitida",
                        "La mascota todavía tiene vigente esta vacuna para la fecha seleccionada.",
                        Alert.AlertType.WARNING
                );
                return;
            }

            vacunacion.setVacuna(vacuna);
        }

        ServicioPrestado nuevo =
                new ServicioPrestado(servicio, turnoSeleccionado);

        listaServiciosDelTurno.add(nuevo);
        actualizarTotales();
    }

    private void quitarServicioAccion() {

        ServicioPrestado seleccionado =
                tablaServiciosTurno.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(
                    "Atención",
                    "Seleccione un servicio de la tabla.",
                    Alert.AlertType.WARNING
            );
            return;
        }

        listaServiciosDelTurno.remove(seleccionado);
        actualizarTotales();
    }

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

        LocalDateTime fechaHora =
                LocalDateTime.of(
                        fecha,
                        LocalTime.of(
                                Integer.parseInt(hora),
                                Integer.parseInt(minutos)
                        )
                );

        if (turnoSeleccionado == null) {
            turnoSeleccionado =
                    new Turno(fechaHora, mascota, veterinario);
        } else {
            turnoSeleccionado.setFechaHora(fechaHora);
            turnoSeleccionado.setMascota(mascota);
            turnoSeleccionado.setVeterinario(veterinario);
        }

        turnoSeleccionado.getServiciosPrestados().clear();

        for (ServicioPrestado servicio : listaServiciosDelTurno) {
            turnoSeleccionado.agregarServicioPrestado(servicio);
        }

        try {

            guardarDatosConsulta();

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

        } catch (Exception e) {

            mostrarAlerta(
                    "Error",
                    e.getMessage(),
                    Alert.AlertType.ERROR
            );
        }
    }

    @FXML
    public void eliminarTurnoAccion(ActionEvent event) {

        if (turnoSeleccionado == null) {
            mostrarAlerta(
                    "Atención",
                    "Debe seleccionar un turno de la tabla.",
                    Alert.AlertType.WARNING
            );
            return;
        }

        try {
            turnoService.eliminar(turnoSeleccionado);
            cargarTablaTurnos();
            limpiarFormulario();

            mostrarAlerta(
                    "Éxito",
                    "Turno eliminado.",
                    Alert.AlertType.INFORMATION
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "Error",
                    "No se pudo eliminar el turno.",
                    Alert.AlertType.ERROR
            );
        }
    }

    private void cargarTablaTurnos() {

        listaObservableTurnos =
                FXCollections.observableArrayList(
                        turnoService.listar()
                );

        tablaTurnos.setItems(listaObservableTurnos);
    }

    private void mostrarTurnoEnFormulario(Turno turno) {

        turnoSeleccionado = turno;

        dpFecha.setValue(
                turno.getFechaHora().toLocalDate()
        );

        cbHora.setValue(
                String.format(
                        "%02d",
                        turno.getFechaHora().getHour()
                )
        );

        cbMinutos.setValue(
                String.format(
                        "%02d",
                        turno.getFechaHora().getMinute()
                )
        );

        cbMascota.setValue(turno.getMascota());
        cbVeterinario.setValue(turno.getVeterinario());
        cbEstado.setValue(turno.getEstado());

        listaServiciosDelTurno.setAll(
            turno.getServiciosPrestados()
        );

        cargarDatosConsulta();
        actualizarCamposConsulta();
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
        cbVacunas.setManaged(false);

        txtDiagnostico.clear();
        txtTratamiento.clear();

        panelDatosConsulta.setVisible(false);
        panelDatosConsulta.setManaged(false);

        listaServiciosDelTurno.clear();
        actualizarTotales();

        tablaTurnos.getSelectionModel().clearSelection();
    }

    private void actualizarTotales() {

        double total = 0;
        int duracion = 0;

        for (ServicioPrestado servicio : listaServiciosDelTurno) {
            total += servicio.getPrecioServicioPrestado();
            duracion += servicio.getDuracionServicioPrestado();
        }

        lblTotal.setText("Total: $" + total);
        lblDuracion.setText(
                "Duración estimada: " + duracion + " min"
        );
    }

    private void cargarDatosConsulta() {

        txtDiagnostico.clear();
        txtTratamiento.clear();

        for (ServicioPrestado servicio : listaServiciosDelTurno) {

            if (servicio.getServicio() instanceof Consulta) {

                txtDiagnostico.setText(
                        servicio.getDiagnostico() != null
                                ? servicio.getDiagnostico()
                                : ""
                );

                txtTratamiento.setText(
                        servicio.getTratamiento() != null
                                ? servicio.getTratamiento()
                                : ""
                );

                break;
            }
        }
    }

   private void actualizarCamposConsulta() {

        boolean tieneConsulta = listaServiciosDelTurno.stream()
                .anyMatch(servicio ->
                        servicio.getServicio() instanceof Consulta
                );

        boolean habilitar = tieneConsulta
                && cbEstado.getValue() == EstadoTurno.CONFIRMADO;

        panelDatosConsulta.setVisible(habilitar);
        panelDatosConsulta.setManaged(habilitar);
    }

    private void guardarDatosConsulta() {

        for (ServicioPrestado servicio : listaServiciosDelTurno) {

            if (servicio.getServicio() instanceof Consulta) {

                servicio.registrarConsulta(
                        txtDiagnostico.getText(),
                        txtTratamiento.getText()
                );
            }
        }
    }

    private void buscarTurno() {

        String texto =
                txtBuscarTurno.getText().trim().toLowerCase();

        if (texto.isEmpty()) {
            tablaTurnos.setItems(listaObservableTurnos);
            return;
        }

        List<Turno> filtrados =
                listaObservableTurnos.stream()
                        .filter(t ->
                                t.getEstado().name()
                                        .toLowerCase()
                                        .contains(texto)
                                ||
                                t.getFechaHora()
                                        .toLocalDate()
                                        .toString()
                                        .contains(texto)
                        )
                        .collect(Collectors.toList());

        tablaTurnos.setItems(
                FXCollections.observableArrayList(filtrados)
        );
    }

    private void mostrarAlerta(
            String titulo,
            String mensaje,
            Alert.AlertType tipo) {

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}