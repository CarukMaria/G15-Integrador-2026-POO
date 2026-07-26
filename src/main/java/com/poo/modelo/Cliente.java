package com.poo.modelo; // Asegurate de que el paquete coincida con el tuyo

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
    private String dni; // Es buena práctica que el DNI sea único y no nulo

    @Column(nullable = false)
    private String apellido; // Nota: En Java los atributos van con minúscula inicial

    @Column(nullable = false)
    private String nombre;

    /*
     * RELACIÓN UNO A MUCHOS:
     * mappedBy = "cliente": Indica que la clase Mascota es dueña de la relación (debe tener un atributo 'cliente').
     * cascade = CascadeType.ALL: Si borras al cliente, se borran sus mascotas.
     * orphanRemoval = true: Si sacas una mascota de esta lista, se borra de la base de datos.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mascota> mascotas = new ArrayList<>(); // Inicializamos la lista vacía

    // 1. Constructor vacío (OBLIGATORIO para JPA)
    public Cliente() {
    }

    // 2. Constructor con parámetros
    // No incluimos el idCliente (se genera solo) ni la lista de mascotas (arranca vacía)
    public Cliente(String dni, String apellido, String nombre) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
    }

    // --- MÉTODOS DE NEGOCIO (Los que me pasaste) ---

    public void agregarMascota(Mascota m) {
        this.mascotas.add(m);
        m.setCliente(this); // ¡Clave! Sincronizamos la relación bidireccional
    }

    public void darDeBajaMascota(Mascota m) {
        this.mascotas.remove(m);
        m.setCliente(null); // Rompemos el enlace del lado de la mascota
    }

    // --- GETTERS Y SETTERS ---

    public Long getIdCliente() {
        return idCliente;
    }

    // No solemos poner setIdCliente porque la base de datos lo maneja automáticamente

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

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas;
    }
}