/*
 * Clase Modelo Libros
 */
package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import modelo.dao.LibroJpaController;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.Autor;
import modelo.entidades.Genero;
import modelo.entidades.Libro;

/**
 *
 * @author Juan Antonio
 */
public class ModeloLibros {

    private final LibroJpaController libroController;
    private final EntityManagerFactory emf;

    public ModeloLibros() {
        emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        libroController = new LibroJpaController(emf);
    }

    // Método para devolver un listado de libros filtrados
    public List<Libro> filtrarLibros(List<Libro> libros, String autor, String genero, String busqueda) {
        List<Libro> librosFiltrados = new ArrayList<>();

        for (Libro libro : libros) {
            boolean autorCoincide = false;
            boolean generoCoincide = false;
            boolean busquedaCoincide = false;

            // Filtrar por autor
            if (!autor.trim().isEmpty()) {
                for (Autor autorLibro : libro.getAutores()) {
                    if (autorLibro.getId() == Integer.parseInt(autor)) {
                        System.out.println("Autor coincide");
                        autorCoincide = true;
                        break; // Paramos la iteración si se encontró un resultado
                    }
                }
            }

            // Filtrar por género
            if (!genero.trim().isEmpty()) {
                for (Genero generoLibro : libro.getGeneros()) {
                    if (generoLibro.getId() == Integer.parseInt(genero)) {
                        generoCoincide = true;
                        break; // Paramos la iteración si se encontró un resultado
                    }
                }
            }

            // Filtrar por cadena de búsqueda
            if (!busqueda.trim().isEmpty()) {
                if (libro.getTitulo().contains(busqueda) || libro.getIsbn().contains(busqueda) || libro.getFechaEdicion().toString().contains(busqueda)) {
                    busquedaCoincide = true;
                }
            }

            // Si algunas de las condiciones coinciden, agregar el libro a la lista filtrada
            if (autorCoincide || generoCoincide || busquedaCoincide) {
                librosFiltrados.add(libro);
            }
        }
        return librosFiltrados;
    }

    public List<Libro> getLibros() {
        return libroController.findLibroEntities();
    }

    public String crearLibro(String isbn, String titulo, List<Autor> autores, List<Genero> generos,
            Date fechaEdicion, String imagenPortada, int ejemplaresDisponibles) {
        String error = null;
        try {
            Libro libro = new Libro(isbn, titulo, autores, generos, fechaEdicion, imagenPortada, ejemplaresDisponibles);
            libroController.create(libro);
        } catch (RollbackException e) {
            if (e.getMessage().contains("Duplicate")) {
                error = "Ya existe un libro con el ISBN: " + isbn + "\n" + e.getMessage();
            } else {
                error = "Se ha producido un error inesperado al crear el libro\n" + e.getMessage();
            }
        }
        return error;
    }

    public Libro consultarLibro(Long id) {
        return libroController.findLibro(id);
    }

    public String actulizarLibro(Libro libro) {
        String error = null;
        try {
            libroController.edit(libro);
        } catch (NonexistentEntityException ex) {
            error = "El libro no existe o ha sido eliminado.\n" + ex.getMessage();
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate")) {
                error = "Ya existe un libro con el ISBN: " + libro.getIsbn() + "\n" + e.getMessage();
            } else {
                error = "Se ha producido un error al actualizar el libro: " + libro.getTitulo() + "\n" + e.getMessage();
            }
        }
        return error;
    }

    public String eliminarLibro(Long id) {
        String error = null;
        try {
            libroController.destroy(id);;
        } catch (NonexistentEntityException ex) {
            error = "El libro no se puede eliminar. No existe.\n" + ex.getMessage();
        } catch (Exception e) {
            if (e.getMessage().contains("parent row")) {
                error = "El libro ya está asociado a un prestamo";
            } else {
                error = "Error al eliminar el libro: " + e.getMessage();
            }
        }
        return error;
    }

    public void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
