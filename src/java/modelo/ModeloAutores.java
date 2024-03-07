/*
 * Clase Modelo Autores
 */
package modelo;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.dao.AutorJpaController;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.Autor;

/**
 *
 * @author Juan Antonio
 */
public class ModeloAutores {

    public static List<Autor> getAutores() {
        System.out.println("Ejecutando método getAutores()");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        AutorJpaController ajc = new AutorJpaController(emf);
        List<Autor> autores = ajc.findAutorEntities();
        emf.close();
        return autores;
    }

    public static String crearAutor(String nombre) {
        String error = null;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        AutorJpaController ajc = new AutorJpaController(emf);
        Autor autor = new Autor();
        autor.setNombre(nombre);
        try {
            ajc.create(autor);
        } catch (Exception e) {
            error = "Error al crear el autor: " + e.getMessage();
        }
        emf.close();
        return error;
    }

    public static Autor consultarAutor(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        AutorJpaController ajc = new AutorJpaController(emf);
        Autor a = ajc.findAutor(id);
        emf.close();
        return a;
    }

    public static String actualizarAutor(Long id, String nombre) {
        String error = null;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        AutorJpaController ajc = new AutorJpaController(emf);
        Autor autor = ajc.findAutor(id);
        if (autor != null) {
            autor.setNombre(nombre);
            try {
                ajc.edit(autor);
            } catch (Exception e) {
                error = "Error al actualizar el Autor " + e.getMessage();
            }
        } else {
            error = "No se encontró el autor con el ID proporcionado";
        }
        emf.close();
        return error;
    }

    public static String eliminarAutor(Long id) {
        String error = null;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        AutorJpaController ajc = new AutorJpaController(emf);
        try {
            ajc.destroy(id);
        } catch (NonexistentEntityException ex) {
            error = "No se encontró el autor con el ID proporcionado";
        } catch (Exception e) {
            if (e.getMessage().contains("parent row")) {
                error = "El autor ya está asociado a un libro";
            } else {
                error = "Error al eliminar el autor: " + e.getMessage();
            }
        }
        emf.close();
        return error;
    }
}
