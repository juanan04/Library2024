    /*
 * Servlet Gestión Géneros
 */
package controladores.admin;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ModeloGeneros;
import modelo.entidades.Genero;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "GestionGeneros", urlPatterns = {"/admin/GestionGeneros"})
public class GestionGeneros extends HttpServlet {

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
        String vista = "/usuario/admin/gestionGeneros.jsp";

        //Lógica para mostrar listado de Géneros
        ModeloGeneros mg = new ModeloGeneros();
        List<Genero> generos = mg.getGeneros();
        request.setAttribute("generos", generos);

        // Lógica para crear un Género
        String nombre = request.getParameter("setNombre");
        String descripcion = request.getParameter("setDescripcion");
        if (nombre != null && !nombre.trim().isEmpty()) {
            String error = mg.crearGenero(nombre, descripcion);
            if (error != null) {
                request.setAttribute("error", error);
                request.setAttribute("nombre", nombre);
                request.setAttribute("descripcion", descripcion);
            } else {
                response.sendRedirect("GestionGeneros");
                request.setAttribute("error", "Género creado correctamente");
                return;
            }
        }

        // Lógica para eliminar un género
        String idGeneroEliminar = request.getParameter("idGenero");
        if (idGeneroEliminar != null && !idGeneroEliminar.trim().isEmpty()) {
            String error = mg.eliminarGenero(Long.parseLong(idGeneroEliminar));
            if (error != null) {
                request.setAttribute("error", error);
            } else {
                response.sendRedirect("GestionGeneros");
                request.setAttribute("error", "Género eliminado correctamente");
                return;
            }
        }

        // Lógica para editar un género
        String idGeneroEditar = request.getParameter("idGeneroEditar");
        if (idGeneroEditar != null && !idGeneroEditar.trim().isEmpty()) {
            String nombreEditar = request.getParameter("editNombre");
            String descripcionEditar = request.getParameter("editDescripcion");
            if (nombreEditar != null && !nombreEditar.trim().isEmpty()) {
                if (descripcionEditar == null) {
                    descripcionEditar = "";
                }
                String error = mg.actualizarGenero(Long.parseLong(idGeneroEditar), nombreEditar, descripcionEditar);
                if (error != null) {
                    request.setAttribute("error", error);
                } else {
                    response.sendRedirect("GestionGeneros");
                    request.setAttribute("error", "Genero actualizado correctamente");
                    return;
                }
            } else {
                request.setAttribute("error", "El nombre y/o la descripción están vacios");
            }
        }

        mg.closeEntityManagerFactory();
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
