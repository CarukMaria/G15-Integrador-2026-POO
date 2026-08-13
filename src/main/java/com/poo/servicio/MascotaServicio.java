package com.poo.servicio;

import com.poo.modelo.Mascota;
import com.poo.modelo.SeguimientoVacuna;
import com.poo.modelo.Turno;
import com.poo.modelo.ServicioPrestado;
import com.poo.modelo.Vacuna;
import com.poo.modelo.Vacunacion;
import com.poo.repositorio.MascotaRepositorio;
import com.poo.modelo.EstadoTurno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MascotaServicio {

    private final MascotaRepositorio mascotaRepositorio;

    public MascotaServicio() {
        this.mascotaRepositorio = new MascotaRepositorio();
    }

    public void guardar(Mascota mascota) {
        mascotaRepositorio.guardar(mascota);
    }

    public List<Mascota> listar() {
        return mascotaRepositorio.listarTodos();
    }

    public Mascota buscarPorId(Long id) {
        return mascotaRepositorio.buscarPorId(id);
    }

    public void eliminar(Mascota mascota) {
        mascotaRepositorio.eliminar(mascota);
    }

    public List<SeguimientoVacuna> obtenerSeguimientoVacunas() {

        List<SeguimientoVacuna> seguimiento = new ArrayList<>();

        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(30);

        for (Mascota mascota : listar()) {

            Map<String, Turno> ultimaVacunacionPorVacuna =
                    new HashMap<>();

            for (Turno turno : mascota.getTurnos()) {

                if (turno.getEstado() != EstadoTurno.ATENDIDO) {
                    continue;
                }

                for (ServicioPrestado servicio :
                        turno.getServiciosPrestados()) {

                    if (!(servicio.getServicio()
                            instanceof Vacunacion vacunacion)) {
                        continue;
                    }

                    Vacuna vacuna = vacunacion.getVacuna();

                    String nombreVacuna = vacuna.getNombre();

                    Turno turnoAnterior =
                            ultimaVacunacionPorVacuna.get(nombreVacuna);

                    if (turnoAnterior == null
                            || turno.getFechaHora()
                                    .isAfter(turnoAnterior.getFechaHora())) {

                        ultimaVacunacionPorVacuna.put(
                                nombreVacuna,
                                turno
                        );
                    }
                }
            }

            for (Map.Entry<String, Turno> entrada :
                    ultimaVacunacionPorVacuna.entrySet()) {

                Turno turno = entrada.getValue();

                Vacuna vacuna = null;

                for (ServicioPrestado servicio :
                        turno.getServiciosPrestados()) {

                    if (servicio.getServicio()
                            instanceof Vacunacion vacunacion) {

                        Vacuna posibleVacuna =
                                vacunacion.getVacuna();

                        if (posibleVacuna.getNombre()
                                .equals(entrada.getKey())) {

                            vacuna = posibleVacuna;
                            break;
                        }
                    }
                }

                if (vacuna == null) {
                    continue;
                }

                LocalDate fechaAplicacion =
                        turno.getFechaHora().toLocalDate();

                LocalDate fechaVencimiento =
                        fechaAplicacion.plusMonths(
                                vacuna.getPeriodicidad()
                        );

                if (fechaVencimiento.isAfter(limite)) {
                    continue;
                }

                long dias =
                        java.time.temporal.ChronoUnit.DAYS.between(
                                hoy,
                                fechaVencimiento
                        );

                String estado;

                if (fechaVencimiento.isBefore(hoy)) {
                    estado = "VENCIDA";
                } else {
                    estado = "VENCE EN " + dias + " DÍAS";
                }

                seguimiento.add(
                        new SeguimientoVacuna(
                                mascota.getNumeroFicha(),
                                mascota.getCliente().getDni(),
                                vacuna.getNombre(),
                                fechaVencimiento,
                                dias,
                                estado
                        )
                );
            }
        }

        return seguimiento;
    }
}