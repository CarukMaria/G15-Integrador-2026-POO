package com.poo.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    @Column(unique = true, nullable = false)
    private String dni;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String telefono; 

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Mascota> mascotas = new ArrayList<>();

    
    public Cliente() {
    }

    // 2. Constructor con parámetros (incluyendo teléfono)
    public Cliente(String dni, String apellido, String nombre, String telefono) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    // --- MÉTODOS DE NEGOCIO ---

    public void agregarMascota(Mascota m) {
        this.mascotas.add(m);
        m.setCliente(this);
    }

    public void darDeBajaMascota(Mascota m) {
        this.mascotas.remove(m);
        m.setCliente(null);
    }

    // --- GETTERS Y SETTERS ---

    public Long getIdCliente() {
        return idCliente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas;
    }
}