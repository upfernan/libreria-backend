package com.libreria;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoPrestamoEntidad;

import java.util.List;

public class TestConexion {

    public static void main(final String[] args) {
        System.out.println("=== Probando conexion con LibreriaDB ===");

        final DAOFactory factory = DAOFactory.getFactory();

        try {
            factory.iniciarTransaccion();

            final List<EstadoPrestamoEntidad> estados = factory.getEstadoPrestamoDAO().consultarTodos();

            System.out.println("Conexion exitosa. Estados de prestamo encontrados: " + estados.size());
            for (final EstadoPrestamoEntidad estado : estados) {
                System.out.println("  - " + estado.getId() + " | " + estado.getNombre());
            }

            factory.confirmarTransaccion();

        } catch (final Exception e) {
            factory.cancelarTransaccion();
            System.err.println("Error en la conexion: " + e.getMessage());
            e.printStackTrace();
        } finally {
            factory.cerrarConexion();
        }

        System.out.println("=== Fin del test ===");
    }

}
