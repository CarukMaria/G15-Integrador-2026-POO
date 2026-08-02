package com.poo.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
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

    
    // 1. Constructor vacío requerido por JPA (protected para que no se use libremente)
    protected Cliente() {
    }

    // 2. Constructor con parámetros
    public Cliente(String dni, String apellido, String nombre, String telefono) {
        setDni(dni);
        setApellido(apellido);
        setNombre(nombre);
        setTelefono(telefono);
    }

    // --- MÉTODOS DE NEGOCIO ---

    public void agregarMascota(Mascota m) {
        if (m == null) {
            throw new IllegalArgumentException("No se puede agregar una mascota nula.");
        }
        
        // Evitamos duplicados
        if (!this.mascotas.contains(m)) {
            this.mascotas.add(m);
            m.setCliente(this); // Mantenemos la relación bidireccional consistente
        }
    }

    public void darDeBajaMascota(Mascota m) {
        if (m != null && this.mascotas.remove(m)) {
            m.setCliente(null); // Rompemos la relación
        }
    }

    // --- VALIDACIONES INTERNAS PRIVADAS ---

    private String validarCadenaNoVacia(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " no puede ser nulo o estar vacío.");
        }
        return valor.trim();
    }


    // --- GETTERS Y SETTERS PROTEGIDOS ---

    public Long getIdCliente() {
        return idCliente;
    }
    
    // No agregamos setIdCliente() porque el ID lo debe generar exclusivamente la base de datos (JPA).

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        String dniLimpio = validarCadenaNoVacia(dni, "DNI");
        
        // Verifica que tenga entre 7 y 8 dígitos numéricos
        if (!dniLimpio.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("El DNI debe contener 7 u 8 números válidos, sin puntos ni letras.");
        }
        this.dni = dniLimpio;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = validarCadenaNoVacia(apellido, "Apellido");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarCadenaNoVacia(nombre, "Nombre");
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        // El teléfono puede ser opcional, pero si lo mandan, que no sean espacios en blanco
        if (telefono != null && telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("Si se ingresa un teléfono, no puede estar en blanco.");
        }
        this.telefono = (telefono != null) ? telefono.trim() : null;
    }

    public List<Mascota> getMascotas() {
        // Devuelve una vista INMODIFICABLE de la lista. 
        // Esto obliga a que cualquier cambio pase sí o sí por agregarMascota() o darDeBajaMascota()
        return Collections.unmodifiableList(mascotas);
    }
}