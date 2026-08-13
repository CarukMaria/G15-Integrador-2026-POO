package com.poo.controlador;

import com.poo.modelo.ServicioPrestado;
import com.poo.modelo.Turno;
import com.poo.servicio.TurnoServicio;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HistorialControlador {

    private final TurnoServicio turnoServicio;
    private ObservableList<Turno> listaObservableTurnos;

    // --- ELEMENTOS FXML ---
    @FXML
    private TextField txtBuscarFicha;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private TableView<Turno> tablaHistorial;

    @FXML
    private TableColumn<Turno, String> colFecha;

    @FXML
    private TableColumn<Turno, String> colHora;

    @FXML
    private TableColumn<Turno, String> colMascota;

    @FXML
    private TableColumn<Turno, String> colVeterinario;

    @FXML
    private TableColumn<Turno, String> colEstado;

    @FXML
    private TableColumn<Turno, String> colServicios;

    public HistorialControlador() {
        this.turnoServicio = new TurnoServicio();
    }

    @FXML
    public void initialize() {

        DateTimeFormatter fechaFormatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        DateTimeFormatter horaFormatter =
                DateTimeFormatter.ofPattern("HH:mm");

        // FECHA
        colFecha.setCellValueFactory(cellData -> {

            Turno turno = cellData.getValue();

            if (turno.getFechaHora() != null) {
                return new SimpleStringProperty(
                        turno.getFechaHora().format(fechaFormatter)
                );
            }

            return new SimpleStringProperty("");
        });

        // HORA
        colHora.setCellValueFactory(cellData -> {

            Turno turno = cellData.getValue();

            if (turno.getFechaHora() != null) {
                return new SimpleStringProperty(
                        turno.getFechaHora().format(horaFormatter)
                );
            }

            return new SimpleStringProperty("");
        });

        // MASCOTA
        colMascota.setCellValueFactory(cellData -> {

            Turno turno = cellData.getValue();

            if (turno.getMascota() != null) {
                return new SimpleStringProperty(
                        turno.getMascota().getNombre()
                );
            }

            return new SimpleStringProperty("Sin Mascota");
        });

        // VETERINARIO
        colVeterinario.setCellValueFactory(cellData -> {

            Turno turno = cellData.getValue();

            if (turno.getVeterinario() != null) {
                return new SimpleStringProperty(
                        turno.getVeterinario().getNombre()
                );
            }

            return new SimpleStringProperty("Sin Veterinario");
        });

        // ESTADO
        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getEstado() != null
                                ? cellData.getValue().getEstado().toString()
                                : ""
                )
        );

        // SERVICIOS
        colServicios.setCellValueFactory(cellData -> {

            List<ServicioPrestado> serviciosPrestados =
                    cellData.getValue().getServiciosPrestados();

            if (serviciosPrestados != null
                    && !serviciosPrestados.isEmpty()) {

                String nombresServicios = serviciosPrestados.stream()
                        .filter(sp -> sp.getServicio() != null)
                        .map(sp -> sp.getServicio().getNombre())
                        .collect(Collectors.joining(", "));

                if (!nombresServicios.isEmpty()) {
                    return new SimpleStringProperty(nombresServicios);
                }
            }

            return new SimpleStringProperty("Sin servicios registrados");
        });

        // Al abrir el historial se muestran todos los turnos.
        cargarHistorial(null);
    }

    private void cargarHistorial(String ficha) {

        List<Turno> turnos;

        if (ficha == null || ficha.trim().isEmpty()) {

            turnos = turnoServicio.listar();

        } else {

            turnos = turnoServicio.buscarPorFicha(
                    ficha.trim()
            );
        }

        listaObservableTurnos =
                FXCollections.observableArrayList(turnos);

        tablaHistorial.setItems(listaObservableTurnos);
    }

    @FXML
    public void buscarHistorialAccion(ActionEvent event) {

        cargarHistorial(txtBuscarFicha.getText());
    }

    @FXML
    public void limpiarBusquedaAccion(ActionEvent event) {

        txtBuscarFicha.clear();

        cargarHistorial(null);
    }


    public void recibirFichaDesdeAfuera(String numeroFicha) {

        txtBuscarFicha.setText(numeroFicha);
        
        cargarHistorial(numeroFicha);
    }

    
}