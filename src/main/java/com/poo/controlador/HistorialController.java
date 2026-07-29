package com.poo.controlador;

import com.poo.modelo.Turno;
import com.poo.servicio.TurnoService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HistorialController {

    private final TurnoService turnoService;
    private ObservableList<Turno> listaObservableTurnos;

    // --- ELEMENTOS FXML ---
    @FXML private TextField txtBuscarMascota;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Turno> tablaHistorial;
    @FXML private TableColumn<Turno, String> colFecha;
    @FXML private TableColumn<Turno, String> colHora;
    @FXML private TableColumn<Turno, String> colMascota;
    @FXML private TableColumn<Turno, String> colVeterinario;
    @FXML private TableColumn<Turno, String> colEstado;
    @FXML private TableColumn<Turno, String> colServicios;

    public HistorialController() {
        this.turnoService = new TurnoService();
    }

    @FXML
    public void initialize() {
        // 1. Mapeo de columnas con formato personalizado
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Fecha y Hora formateadas desde el LocalDateTime del Turno
        colFecha.setCellValueFactory(cellData -> {
            if (cellData.getValue().getFechaHora() != null) {
                return new SimpleStringProperty(cellData.getValue().getFechaHora().format(fechaFormatter));
            }
            return new SimpleStringProperty("");
        });

        colHora.setCellValueFactory(cellData -> {
            if (cellData.getValue().getFechaHora() != null) {
                return new SimpleStringProperty(cellData.getValue().getFechaHora().format(horaFormatter));
            }
            return new SimpleStringProperty("");
        });

        // Mascota, Veterinario y Estado
        colMascota.setCellValueFactory(cellData -> {
            if (cellData.getValue().getMascota() != null) {
                return new SimpleStringProperty(cellData.getValue().getMascota().getNombre());
            }
            return new SimpleStringProperty("Sin Mascota");
        });

        colVeterinario.setCellValueFactory(cellData -> {
            if (cellData.getValue().getVeterinario() != null) {
                return new SimpleStringProperty(cellData.getValue().getVeterinario().getNombre());
            }
            return new SimpleStringProperty("Sin Veterinario");
        });

        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Servicios Realizados: junta los nombres de todos los servicios prestados del turno
        colServicios.setCellValueFactory(cellData -> {
            // 1. Usamos el getter correcto que vimos en tu clase Turno
            List<com.poo.modelo.ServicioPrestado> serviciosPrestados = cellData.getValue().getServiciosPrestados();
            
            if (serviciosPrestados != null && !serviciosPrestados.isEmpty()) {
                String nombresServicios = serviciosPrestados.stream()
                        .map(sp -> sp.getServicio().getNombre())
                        .collect(Collectors.joining(", "));
                return new SimpleStringProperty(nombresServicios);
            }
            return new SimpleStringProperty("Sin servicios registrados");
        });
    }

    private void cargarHistorial(String filtroMascota) {
        List<Turno> todosLosTurnos = turnoService.listar();

        // Si hay un texto en el buscador, filtramos ignorando mayúsculas/minúsculas
        if (filtroMascota != null && !filtroMascota.trim().isEmpty()) {
            String busqueda = filtroMascota.trim().toLowerCase();
            todosLosTurnos = todosLosTurnos.stream()
                    .filter(t -> t.getMascota() != null && t.getMascota().getNombre().toLowerCase().contains(busqueda))
                    .collect(Collectors.toList());
        }

        listaObservableTurnos = FXCollections.observableArrayList(todosLosTurnos);
        tablaHistorial.setItems(listaObservableTurnos);
    }

    @FXML
    public void buscarHistorialAccion(ActionEvent event) {
        cargarHistorial(txtBuscarMascota.getText());
    }

    @FXML
    public void limpiarBusquedaAccion(ActionEvent event) {
        txtBuscarMascota.clear();
        cargarHistorial(null);
    }
}