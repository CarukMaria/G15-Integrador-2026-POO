package com.poo;

import com.poo.modelo.Mascota;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sistema de gestión veterinaria iniciado");
        
        Mascota mascota = new Mascota();
        
        System.out.println("Mascota creada: " + mascota);
    }
}