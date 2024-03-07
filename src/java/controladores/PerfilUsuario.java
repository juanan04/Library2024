/*
 * Controlador del Perfil del usuario (SOCIO)
 */
package controladores;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ModeloPrestamos;
import modelo.ModeloUsuarios;
import modelo.entidades.EstadoPrestamo;
import modelo.entidades.Prestamo;
import modelo.entidades.Usuario;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "PerfilUsuario", urlPatterns = {"/PerfilUsuario"})
public class PerfilUsuario extends HttpServlet {

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
        String vista = "/usuario/perfilUsuario.jsp";
        ModeloPrestamos mp = new ModeloPrestamos();

        // Obtenemos el usuario actual de la sesion para asociarle el préstamo
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        List<Prestamo> prestamos = mp.getPrestamos();
        List<Prestamo> prestamosUsuario = mp.filtrarPrestamosPorUsuario(prestamos, usuario);

        request.setAttribute("prestamos", prestamosUsuario);

        // Lógica para editar el perfil del usuario de la sesion
        if (request.getMethod().equals("POST")) {
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String domicilio = request.getParameter("domicilio");

            if (nombre != null && !nombre.trim().isEmpty()
                    && apellidos != null && !apellidos.trim().isEmpty()
                    && email != null && !email.trim().isEmpty()
                    && password != null && !password.trim().isEmpty()) {
                ModeloUsuarios mu = new ModeloUsuarios();
                String error = mu.actualizarUsuario(usuario.getId(), nombre, apellidos, email,
                        password, domicilio, usuario.getTipoUsuario());

                if (error != null) {
                    request.setAttribute("error", error);
                    System.out.println("Error al actualizar");
                } else {
                    Usuario usuarioActualizado = mu.consultarUsuario(usuario.getId());
                    request.getSession().setAttribute("usuario", usuarioActualizado);
                    response.sendRedirect("PerfilUsuario");
                    return;
                }
            }

        }

        // Lógica para devolver un libro
        String prestamoDevolverId = request.getParameter("prestamoDevolverId");
        if (prestamoDevolverId != null && !prestamoDevolverId.trim().isEmpty()) {
            ModeloUsuarios mu = new ModeloUsuarios();
            Prestamo prestamo = mp.consultarPrestamo(Long.parseLong(prestamoDevolverId));
            String error = mp.actualizarPrestamo(Long.parseLong(prestamoDevolverId), prestamo.getLibro().getId(),
                    usuario.getId(), prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion(), EstadoPrestamo.DEVUELTO);
            if (error != null) {
                request.setAttribute("error", error);
            } else {
                
                Usuario usuarioActualizado = mu.consultarUsuario(usuario.getId());
                request.getSession().setAttribute("usuario", usuarioActualizado);
                response.sendRedirect("PerfilUsuario");
                return;
            }
        }
        mp.closeEntityManagerFactory();
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
