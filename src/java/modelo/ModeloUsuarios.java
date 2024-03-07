/*
 * Clase Modelo Usuarios
 */
package modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.dao.UsuarioJpaController;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.TipoUsuario;
import modelo.entidades.Usuario;

/**
 *
 * @author Juan Antonio
 */
public class ModeloUsuarios {
    
    private final UsuarioJpaController usuarioController;
    private final EntityManagerFactory emf;

    public ModeloUsuarios() {
        emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        usuarioController = new UsuarioJpaController(emf);
    }

    public List<Usuario> getUsuarios() {
        return usuarioController.findUsuarioEntities();
    }

    // Método para obtener un listado de todos los usuarios que son EMPLEADOS
    public List<Usuario> getUsuariosEmpleados(List<Usuario> usuarios) {
        List<Usuario> empleados = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            if(usuario.getTipoUsuario().equals(TipoUsuario.EMPLEADO)) {
                empleados.add(usuario);
            }
        }
        return empleados;
    }
    
    public String crearUsuario(String nombre, String apellidos, String email, String password, String domicilio, TipoUsuario tipoUsuario) {
        String error = null;
        Usuario usuario = new Usuario(nombre, apellidos, email, password, domicilio, tipoUsuario);
        try {
            usuarioController.create(usuario);
        } catch (Exception e) {
            error = "Error al crear el usuario: " + e.getMessage();
        }
        return error;
    }

    public Usuario consultarUsuario(Long id) {
        return usuarioController.findUsuario(id);
    }

    public String actualizarUsuario(Long id, String nombre, String apellidos, String email, String password, String domicilio, TipoUsuario tipoUsuario) {
        String error = null;
        Usuario usuario = usuarioController.findUsuario(id);
        if (usuario != null) {
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuario.setPassword(password);
            usuario.setDomicilio(domicilio);
            usuario.setTipoUsuario(tipoUsuario);
            try {
                usuarioController.edit(usuario);
            } catch (Exception e) {
                error = "Error al actualizar el usuario: " + e.getMessage();
            }
        } else {
            error = "No se encontró el usuario con el ID proporcionado";
        }
        return error;
    }

    public String eliminarUsuario(Long id) {
        String error = null;
        try {
            usuarioController.destroy(id);
        } catch (NonexistentEntityException e) {
            error = "No se encontró el usuario con el ID proporcionado";
        } catch (Exception e) {
            if (e.getMessage().contains("parent row")) {
                error = "El Usuario ya está asociado a un Préstamo";
            } else {
                error = "Error al eliminar el Usuario: " + e.getMessage();
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
