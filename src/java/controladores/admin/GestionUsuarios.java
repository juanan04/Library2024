/*
 * Servlet Gestion Usuarios
 */
package controladores.admin;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ModeloUsuarios;
import modelo.entidades.TipoUsuario;
import modelo.entidades.Usuario;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "GestionUsuarios", urlPatterns = {"/admin/GestionUsuarios"})
public class GestionUsuarios extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista = "/usuario/admin/gestionUsuarios.jsp";
        //Lógica para mostrar listado de Usuarios
        ModeloUsuarios mu = new ModeloUsuarios();
        List<Usuario> usuarios = mu.getUsuarios();
        List<Usuario> empleados = mu.getUsuariosEmpleados(usuarios);
        request.setAttribute("usuarios", empleados);

        // Lógica para crear un nuevo usuario
        String nombre = request.getParameter("setNombre");
        String apellidos = request.getParameter("setApellidos");
        String email = request.getParameter("setEmail");
        String password = request.getParameter("setPassword");
        String domicilio = request.getParameter("setDomicilio");
        String tipoUsuario = request.getParameter("setTipo");
        if (nombre != null && !nombre.trim().isEmpty()
                && apellidos != null && !apellidos.trim().isEmpty()
                && email != null && !email.trim().isEmpty()
                && password != null && !password.trim().isEmpty()
                && tipoUsuario != null && !tipoUsuario.trim().isEmpty()) {
            String error = mu.crearUsuario(nombre, apellidos, email, password, domicilio, TipoUsuario.valueOf(tipoUsuario));
            if (error != null) {
                request.setAttribute("error", error);
                request.setAttribute("nombre", nombre);
                request.setAttribute("apellidos", apellidos);
                request.setAttribute("email", email);
                request.setAttribute("domicilio", domicilio);
            } else {
                response.sendRedirect("GestionUsuarios");
                request.setAttribute("error", "Empleado creado correctamente");
                return;
            }
        }

        // Lógica para eliminar un Libro
        String idUsuarioEliminar = request.getParameter("idUsuario");
        if (idUsuarioEliminar != null && !idUsuarioEliminar.trim().isEmpty()) {
            String error = mu.eliminarUsuario(Long.parseLong(idUsuarioEliminar));
            if (error != null) {
                request.setAttribute("error", error);
            } else {
                response.sendRedirect("GestionUsuarios");
                request.setAttribute("error", "Empleado eliminado correctamente");
                return;
            }
        }

        // Lógica para editar un usuario
        if (request.getMethod().equals("POST")) {
            String idUsuarioEdit = request.getParameter("idUsuarioEditar");
            String nuevoNombre = request.getParameter("editNombre");
            String nuevoApellidos = request.getParameter("editApellidos");
            String nuevoEmail = request.getParameter("editEmail");
            String nuevoPassword = request.getParameter("editPassword");
            String nuevoDomicilio = request.getParameter("editDomicilio");

            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()
                    && nuevoApellidos != null && !nuevoApellidos.trim().isEmpty()
                    && nuevoEmail != null && !nuevoEmail.trim().isEmpty()) {
                if (nuevoPassword == null || nuevoPassword.trim().isEmpty()) {
                    // Si la contraseña se ha dejado en blanco obtenemos su contraseña original
                    Usuario e = mu.consultarUsuario(Long.parseLong(idUsuarioEdit));
                    nuevoPassword = e.getPassword();
                }
                String error = mu.actualizarUsuario(Long.parseLong(idUsuarioEdit), nuevoNombre, nuevoApellidos,
                        nuevoEmail, nuevoPassword, nuevoDomicilio, TipoUsuario.EMPLEADO);

                if (error != null) {
                    request.setAttribute("error", error);
                } else {
                    response.sendRedirect("GestionUsuarios");
                    request.setAttribute("error", "Empleado actualizado correctamente");
                    return;
                }

            } else {
                request.setAttribute("error", "Se deben rellenar todos los campos");
            }
        }

        mu.closeEntityManagerFactory();
        getServletContext().getRequestDispatcher(vista).forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
