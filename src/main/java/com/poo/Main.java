package com.poo;

import com.poo.controlador.*;
import com.poo.modelo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        System.out.println("Iniciando sistema...");


        // Controllers
        ClienteController clienteController = new ClienteController();
        MascotaController mascotaController = new MascotaController();
        VeterinarioController veterinarioController = new VeterinarioController();
        TurnoController turnoController = new TurnoController();
        ServicioController servicioController = new ServicioController();
        ServicioPrestadoController servicioPrestadoController = new ServicioPrestadoController();



        // =========================
        // CLIENTE
        // =========================

        Cliente cliente = new Cliente(
                "45678902",
                "Carla",
                "Gomez"
        );

        clienteController.guardar(cliente);

        System.out.println("Cliente guardado");



        // =========================
        // MASCOTA
        // =========================

        Mascota mascota = new Mascota(
                "F001",
                "Firulais",
                "Labrador",
                LocalDate.of(2020, 5, 10),
                Especie.PERRO
        );

        mascota.setCliente(cliente);

        mascotaController.guardar(mascota);

        System.out.println("Mascota guardada");



        // =========================
        // VETERINARIO
        // =========================

        Veterinario veterinario = new Veterinario(
                "MAT123",
                "Laura",
                "Gomez"
        );

        veterinario.agregarEspecialidad(
                Especialidad.CLINICA_GENERAL
        );

        veterinarioController.guardar(veterinario);

        System.out.println("Veterinario guardado");



        // =========================
        // TURNO
        // =========================

        Turno turno = new Turno(
                LocalDateTime.of(2026, 7, 27, 17, 0),
                mascota,
                veterinario
        );

        turnoController.guardar(turno);

        System.out.println("Turno guardado");



        // =========================
        // SERVICIO (VACUNACION)
        // =========================

        Vacunacion vacunacion = new Vacunacion(
                "Vacunación",
                5000,
                20,
                "Antirrábica",
                "Zoetis"
        );

        servicioController.guardar(vacunacion);

        System.out.println("Vacunación guardada");



        // =========================
        // SERVICIO PRESTADO
        // =========================

        ServicioPrestado servicioPrestado = new ServicioPrestado(
                vacunacion,
                turno
        );

        turno.agregarServicioPrestado(servicioPrestado);

        servicioPrestadoController.guardar(servicioPrestado);

        System.out.println("Servicio prestado guardado");



        // =========================
        // LISTADO FINAL
        // =========================

        System.out.println("\nLista de turnos:");

        turnoController.listar()
                .forEach(t ->
                        System.out.println(
                                t.getIdTurno()
                                + " - "
                                + t.getFechaHora()
                                + " - "
                                + t.getMascota().getNombre()
                                + " - "
                                + t.getVeterinario().getNombre()
                                + " - "
                                + t.getEstado()
                        )
                );



        System.out.println("\nLista servicios:");

        servicioController.listar()
                .forEach(System.out::println);



        System.out.println("\nLista servicios prestados:");

        servicioPrestadoController.listar()
                .forEach(sp ->
                        System.out.println(
                                sp.getIdServicioPrestado()
                                + " - "
                                + sp.getServicio().getNombre()
                                + " - $"
                                + sp.getPrecioServicioPrestado()
                        )
                );



        System.out.println("\nFin del sistema.");

        com.poo.util.JPAUtil.close();
    }
}