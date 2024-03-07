/*
 * Servlet de Gestion de Autores
 */
package controladores.admin;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ModeloAutores;
import modelo.entidades.Autor;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "GestionAutores", urlPatterns = {"/admin/GestionAutores"})
public class GestionAutores extends HttpServlet {

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

        String vista = "/usuario/admin/gestionAutores.jsp";
        // Obtener la lista de autores del modelo
        List<Autor> autores = ModeloAutores.getAutores();
        request.setAttribute("autores", autores);

        // Lógica para crear un nuevo autor
        String nombre = request.getParameter("setNombre");
        if (nombre != null && !nombre.trim().isEmpty()) {
            String error = ModeloAutores.crearAutor(nombre);
            if (error != null) {
                request.setAttribute("error", error);
                request.setAttribute("nombre", nombre);
                System.out.println("No se ha podido crear el autor");
            } else {
                response.sendRedirect("GestionAutores");
                request.setAttribute("error", "Autor creado correctamente");
                return;
            }
        }
        
        // Lógica para eliminar un Autor
        String idAutorEliminar = request.getParameter("idAutor");
        if (idAutorEliminar != null && !idAutorEliminar.trim().isEmpty()) {
            String error = ModeloAutores.eliminarAutor(Long.parseLong(idAutorEliminar));
            if (error != null) {
                request.setAttribute("error", error);
            } else {
                response.sendRedirect("GestionAutores");
                request.setAttribute("error", "Autor eliminado correctamente");
                return;
            }
        }
        
        // Lógica para editar un autor
        String idAutorEditar = request.getParameter("idAutorEditar");
        if (idAutorEditar != null && !idAutorEditar.trim().isEmpty()) {
            String nombreEditar = request.getParameter("editNombre");
            if (nombreEditar != null && !nombreEditar.trim().isEmpty()) {
                String error = ModeloAutores.actualizarAutor(Long.parseLong(idAutorEditar), nombreEditar);
                if (error != null) {
                    request.setAttribute("error", error);
                    System.out.println("Error al editar el Autor");
                } else {
                    response.sendRedirect("GestionAutores");
                    request.setAttribute("error", "Autor actualizado correctamente");
                    return;
                }
            }
        } else {
            request.setAttribute("error", "No se recibió el identificador del autor");
        }
        
        // Reenviar la solicitud a la vista
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
