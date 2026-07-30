package com.poo.servicio;

import com.poo.modelo.EstadoTurno;
import com.poo.modelo.ServicioPrestado;
import com.poo.modelo.Turno;
import com.poo.modelo.Vacunacion;
import com.poo.repositorio.TurnoRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TurnoService {


    private final TurnoRepository turnoRepository;


    public TurnoService() {

        turnoRepository = new TurnoRepository();
    }



    /*
     * Guarda un turno aplicando reglas que necesitan consultar BD
     */
    public void guardar(Turno turno) {


        validarDatosBasicos(turno);


        validarSolapamiento(turno);


        validarVacunas(turno);


        turnoRepository.guardar(turno);
    }





    private void validarDatosBasicos(Turno turno) {


        if (turno.getFechaHora() == null) {

            throw new IllegalArgumentException(
                "La fecha del turno no puede estar vacía"
            );
        }



        if (turno.getMascota() == null) {

            throw new IllegalArgumentException(
                "El turno debe tener una mascota"
            );
        }



        if (turno.getVeterinario() == null) {

            throw new IllegalArgumentException(
                "El turno debe tener un veterinario"
            );
        }
    }





    /*
     * Regla: un veterinario y una mascota no pueden tener
     * dos turnos que se pisen.
     */
    private void validarSolapamiento(Turno nuevoTurno) {


        LocalDateTime inicioNuevo =
                nuevoTurno.getFechaHora();


        int duracionNueva =
                nuevoTurno.calcularDuracionTotal();



        if (duracionNueva == 0) {

            duracionNueva = 30;
        }



        LocalDateTime finNuevo =
                inicioNuevo.plusMinutes(duracionNueva);




        for (Turno existente : listar()) {


            // Ignorar el mismo turno al editar
            if (nuevoTurno.getIdTurno() != null
                    &&
                nuevoTurno.getIdTurno()
                .equals(existente.getIdTurno())) {

                continue;
            }



            if (existente.getEstado()
                    == EstadoTurno.CANCELADO) {

                continue;
            }



            LocalDateTime inicioExistente =
                    existente.getFechaHora();



            int duracionExistente =
                    existente.calcularDuracionTotal();



            if (duracionExistente == 0) {

                duracionExistente = 30;
            }



            LocalDateTime finExistente =
                    inicioExistente.plusMinutes(
                            duracionExistente
                    );



            boolean seSolapan =
                    inicioNuevo.isBefore(finExistente)
                    &&
                    finNuevo.isAfter(inicioExistente);



            if (!seSolapan) {

                continue;
            }



            if (existente.getMascota()
                    .equals(nuevoTurno.getMascota())) {


                throw new IllegalArgumentException(
                    "La mascota ya tiene un turno en ese horario."
                );
            }



            if (existente.getVeterinario()
                    .equals(nuevoTurno.getVeterinario())) {


                throw new IllegalArgumentException(
                    "El veterinario ya tiene un turno en ese horario."
                );
            }
        }
    }







    /*
     * Regla: una mascota no puede recibir la misma vacuna
     * si ya la recibió dentro del último mes.
     */
    private void validarVacunas(Turno turno) {


        for (ServicioPrestado servicio :
                turno.getServiciosPrestados()) {



            if (!(servicio.getServicio()
                    instanceof Vacunacion vacuna)) {

                continue;
            }




            LocalDateTime fechaLimite =
                    turno.getFechaHora()
                    .minusMonths(1);





            for (Turno existente : listar()) {



                // No comparar consigo mismo
                if (turno.getIdTurno() != null
                        &&
                    turno.getIdTurno()
                    .equals(existente.getIdTurno())) {

                    continue;
                }




                if (!existente.getMascota()
                        .equals(turno.getMascota())) {

                    continue;
                }




                if (existente.getFechaHora()
                        .isBefore(fechaLimite)) {

                    continue;
                }





                for (ServicioPrestado realizado :
                        existente.getServiciosPrestados()) {



                    if (realizado.getServicio()
                            instanceof Vacunacion vacunaRealizada) {



                        if (vacunaRealizada
                                .getNombreVacuna()
                                .equalsIgnoreCase(
                                    vacuna.getNombreVacuna()
                                )) {



                            throw new IllegalArgumentException(
                                "La mascota ya recibió esta vacuna dentro del último mes."
                            );
                        }
                    }
                }
            }
        }
    }







    public void cambiarEstado(
            Turno turno,
            EstadoTurno nuevoEstado) {


        turno.cambiarEstado(nuevoEstado);

        turnoRepository.guardar(turno);
    }







    public List<Turno> listar() {

        return turnoRepository.listarTodos();
    }






    public Turno buscarPorId(Long id) {


        if (id == null) {

            throw new IllegalArgumentException(
                "El id no puede ser nulo"
            );
        }


        return turnoRepository.buscarPorId(id);
    }






    public void eliminar(Turno turno) {

        turnoRepository.eliminar(turno);
    }

}