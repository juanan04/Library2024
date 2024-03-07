/*
 * Clase Modelo Préstamos
 */
package modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.dao.LibroJpaController;
import modelo.dao.PrestamoJpaController;
import modelo.dao.UsuarioJpaController;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.EstadoPrestamo;
import modelo.entidades.Libro;
import modelo.entidades.Prestamo;
import modelo.entidades.Usuario;

/**
 *
 * @author Juan Antonio
 */
public class ModeloPrestamos {

    private final PrestamoJpaController prestamoController;
    private final LibroJpaController libroController;
    private final UsuarioJpaController usuarioController;
    private final EntityManagerFactory emf;

    public ModeloPrestamos() {
        emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        prestamoController = new PrestamoJpaController(emf);
        libroController = new LibroJpaController(emf);
        usuarioController = new UsuarioJpaController(emf);
    }

    public List<Prestamo> getPrestamos() {
        return prestamoController.findPrestamoEntities();
    }

    public List<String> getCorreosUsuariosMorosos(List<Prestamo> prestamos) {
        List<String> correosMorosos = new ArrayList<>();

        // Iteramos sobre los préstamos para encontrar los usuarios morosos
        for (Prestamo prestamo : prestamos) {
            // Verificamos si el estado del préstamo es PENDIENTE
            if (prestamo.getEstadoPrestamo().equals(EstadoPrestamo.PENDIENTE)) {
                Usuario usuario = prestamo.getUsuario();
                // Verificamos si el correo del usuario ya está en la lista
                if (!correosMorosos.contains(usuario.getEmail())) {
                    // Agregamos el correo del usuario a la lista de morosos
                    correosMorosos.add(usuario.getEmail());
                }
            }
        }

        return correosMorosos;
    }

    // Método para obtener prestamos actuales
    public List<Prestamo> filtrarPrestamosActuales(List<Prestamo> prestamos) {
        List<Prestamo> prestamosFiltrados = new ArrayList<>();

        // Recogemos los préstamos con EstadoPrestamo.CURRENTE y los almacenamos en la nueva lista
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getEstadoPrestamo().equals(EstadoPrestamo.CURRENTE)
                    || prestamo.getEstadoPrestamo().equals(EstadoPrestamo.PENDIENTE)) {
                prestamosFiltrados.add(prestamo);
            }
        }
        return prestamosFiltrados; // Devolvemos la lista de préstamos filtrados
    }

    // Método para filtrar préstamos por rango de fechas
    public List<Prestamo> filtrarPrestamosPorFecha(List<Prestamo> prestamos, String fechaIniStr, String fechaFinStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha
        List<Prestamo> prestamosFiltrados = new ArrayList<>();
        try {
            Date fechaIni = sdf.parse(fechaIniStr);
            Date fechaFin = sdf.parse(fechaFinStr);

            for (Prestamo prestamo : prestamos) {
                Date fechaPrestamo = prestamo.getFechaPrestamo();

                if (fechaPrestamo.after(fechaIni) && fechaPrestamo.before(fechaFin)) {
                    prestamosFiltrados.add(prestamo);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error al filtrar por fechas");
        }
        System.out.println(prestamosFiltrados);
        return prestamosFiltrados;
    }

    // Método para filtrar los préstamos por un usuario
    public List<Prestamo> filtrarPrestamosPorUsuario(List<Prestamo> prestamos, Usuario usuario) {
        List<Prestamo> prestamosFiltrados = new ArrayList<>();

        for (Prestamo prestamo : prestamos) {

            if (prestamo.getUsuario().getId().equals(usuario.getId())) {
                prestamosFiltrados.add(prestamo);
            }
        }
        return prestamosFiltrados;
    }

    // Método para filtrar préstamos por libro
    public List<Prestamo> filtrarPrestamosPorLibro(List<Prestamo> prestamos, Libro libro) {
        List<Prestamo> prestamosFiltrados = new ArrayList<>();
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getLibro().getId().equals(libro.getId())) {
                prestamosFiltrados.add(prestamo);
            }
        }
        return prestamosFiltrados;
    }

    // Método para actualizar el estado de los préstamos
    public void actualizarEstadoPrestamos(List<Prestamo> prestamos) {
        Date hoy = new Date(); // Obtener la fecha actual

        for (Prestamo prestamo : prestamos) {
            if (prestamo.getEstadoPrestamo() != EstadoPrestamo.DEVUELTO) {
                if (prestamo.getFechaDevolucion().before(hoy)) {
                    prestamo.setEstadoPrestamo(EstadoPrestamo.PENDIENTE);
                } else {
                    prestamo.setEstadoPrestamo(EstadoPrestamo.CURRENTE);
                }
            }
            try {
                prestamoController.edit(prestamo);
            } catch(Exception e) {
                System.out.println("Error al actualizar estado préstamo " + e.getMessage());
            }
        }
    }

    public String crearPrestamo(Prestamo prestamo) {
        String error = null;
        try {
            // Verificar si hay ejemplares disponibles del libro
            Libro libro = prestamo.getLibro(); // Obtener el libro del préstamo
            int ejemplaresDisponibles = libro.getEjemplaresDisponibles();
            if (ejemplaresDisponibles > 0) {
                // Si hay ejemplares disponibles, crear el préstamo y actualizar la cantidad de ejemplares del libro
                prestamoController.create(prestamo);
                libro.setEjemplaresDisponibles(ejemplaresDisponibles - 1); // Restar 1 al total de ejemplares disponibles
                libroController.edit(libro); // Actualizar el libro en la base de datos
            } else {
                error = "No hay ejemplares disponibles del libro.";
            }
        } catch (Exception e) {
            error = "Error al crear el préstamo: " + e.getMessage();
        }
        return error;
    }

    public Prestamo consultarPrestamo(Long id) {
        return prestamoController.findPrestamo(id);
    }

    public String eliminarPrestamo(Long id) {
        String error = null;
        try {
            // Obtener el préstamo
            Prestamo prestamo = prestamoController.findPrestamo(id);
            // Obtener el libro asociado al préstamo
            Libro libro = prestamo.getLibro();
            // Actualizar la cantidad de ejemplares del libro al eliminar el préstamo (si no estaba devuelto)
            if (prestamo.getEstadoPrestamo().equals(EstadoPrestamo.PENDIENTE)
                    || prestamo.getEstadoPrestamo().equals(EstadoPrestamo.CURRENTE)) {
                libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1); // Sumar 1 al total de ejemplares disponibles
                libroController.edit(libro); // Actualizar el libro en la base de datos
            }
            // Eliminar el préstamo
            prestamoController.destroy(id);
        } catch (NonexistentEntityException e) {
            error = "No se encontró el préstamo con el ID proporcionado";
        } catch (Exception e) {
            error = "Error al eliminar el préstamo: " + e.getMessage();
        }
        return error;
    }

    public String actualizarPrestamo(Long idPrestamo, Long idLibro, Long idUsuario, Date fechaPrestamo, Date fechaDevolucion, EstadoPrestamo estadoPrestamo) {
        String error = null;
        try {
            // Obtener el préstamo actual de la base de datos
            Prestamo prestamoDB = prestamoController.findPrestamo(idPrestamo);

            // Verificar si el préstamo existe
            if (prestamoDB != null) {
                // Obtener el libro y usuario asociados al préstamo
                Libro libro = libroController.findLibro(idLibro);
                Usuario usuario = usuarioController.findUsuario(idUsuario);

                // Actualizar los campos del préstamo
                prestamoDB.setLibro(libro);
                prestamoDB.setUsuario(usuario);
                prestamoDB.setFechaPrestamo(fechaPrestamo);
                prestamoDB.setFechaDevolucion(fechaDevolucion);
                prestamoDB.setEstadoPrestamo(estadoPrestamo);

                if (prestamoDB.getEstadoPrestamo().equals(EstadoPrestamo.DEVUELTO)) {
                    libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1); // Sumar 1 al total de ejemplares disponibles
                    libroController.edit(libro);
                    // Actualizamos la fecha del préstamo a hoy
                    Date hoy = new Date();
                    prestamoDB.setFechaDevolucion(hoy);
                }

                // Guardar los cambios en la base de datos
                prestamoController.edit(prestamoDB);
            } else {
                error = "No se encontró el préstamo con el ID proporcionado";
            }
        } catch (Exception e) {
            error = "Error al actualizar el préstamo: " + e.getMessage();
        }
        return error;
    }

    public void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
