package com.poo.modelo;

import java.time.LocalDate;

public class SeguimientoVacuna {

    private String numeroFicha;
    private String dniCliente;
    private String nombreVacuna;
    private LocalDate fechaVencimiento;
    private long dias;
    private String estado;

    public SeguimientoVacuna(
            String numeroFicha,
            String dniCliente,
            String nombreVacuna,
            LocalDate fechaVencimiento,
            long dias,
            String estado) {

        this.numeroFicha = numeroFicha;
        this.dniCliente = dniCliente;
        this.nombreVacuna = nombreVacuna;
        this.fechaVencimiento = fechaVencimiento;
        this.dias = dias;
        this.estado = estado;
    }

    public String getNumeroFicha() {
        return numeroFicha;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public String getNombreVacuna() {
        return nombreVacuna;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public long getDias() {
        return dias;
    }

    public String getEstado() {
        return estado;
    }
}