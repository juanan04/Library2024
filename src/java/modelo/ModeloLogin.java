/*
 * Clase para controlar el Login del usuario
 */
package modelo;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.dao.UsuarioJpaController;
import modelo.entidades.Usuario;

/**
 *
 * @author Juan Antonio
 */
public class ModeloLogin {
    public static Usuario validarUsuario(String email, String password) {
        Usuario u = null;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        UsuarioJpaController ujc = new UsuarioJpaController(emf);
        List<Usuario> usuarios = ujc.findUsuarioEntities();
        boolean encontrado = false;
        for (int i = 0; i < usuarios.size() && !encontrado; i++) {
            Usuario actual = usuarios.get(i);
            if (actual.getEmail().equals(email)) {
                encontrado = true;
                if (actual.getPassword().equals(password)) {
                    u = actual;
                }
            }
        }
        emf.close();
        return u;
    }
}
