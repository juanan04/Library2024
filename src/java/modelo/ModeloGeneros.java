/*
 * Clase Modelo Géneros
 */
package modelo;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.dao.GeneroJpaController;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.Genero;

/**
 *
 * @author Juan Antonio
 */
public class ModeloGeneros {

    private final GeneroJpaController generoController;
    private final EntityManagerFactory emf;

    public ModeloGeneros() {
        emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        generoController = new GeneroJpaController(emf);
    }

    public List<Genero> getGeneros() {
        return generoController.findGeneroEntities();
    }

    public String crearGenero(String nombre, String descripcion) {
        String error = null;
        Genero genero = new Genero();
        genero.setNombre(nombre);
        genero.setDescripcion(descripcion);
        try {
            generoController.create(genero);
        } catch (Exception e) {
            error = "Error al crear el género: " + e.getMessage();
        }
        return error;
    }

    public Genero consultarGenero(Long id) {
        return generoController.findGenero(id);
    }

    public String actualizarGenero(Long id, String nombre, String descripcion) {
        String error = null;
        Genero genero = generoController.findGenero(id);
        if (genero != null) {
            genero.setNombre(nombre);
            genero.setDescripcion(descripcion);
            try {
                generoController.edit(genero);
            } catch (Exception e) {
                error = "Error al actualizar el género: " + e.getMessage();
            }
        } else {
            error = "No se encontró el género con el ID proporcionado";
        }
        return error;
    }

    public String eliminarGenero(Long id) {
        String error = null;
        try {
            generoController.destroy(id);
        } catch (NonexistentEntityException e) {
            error = "No se encontró el género con el ID proporcionado";
        } catch (Exception e) {
            if (e.getMessage().contains("parent row")) {
                error = "El género ya está asociado a un libro";
            } else {
                error = "Error al eliminar el género: " + e.getMessage();
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
