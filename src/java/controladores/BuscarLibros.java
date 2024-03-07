/*
 * Servlet BuscarLibros
 */
package controladores;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ModeloAutores;
import modelo.ModeloGeneros;
import modelo.ModeloLibros;
import modelo.entidades.Autor;
import modelo.entidades.Genero;
import modelo.entidades.Libro;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "BuscarLibros", urlPatterns = {"/BuscarLibros"})
public class BuscarLibros extends HttpServlet {

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
        String vista = "/buscarLibros.jsp";
        //Listado de autores y generos
        ModeloGeneros mg = new ModeloGeneros();
        List<Autor> autores = ModeloAutores.getAutores();
        List<Genero> generos = mg.getGeneros();
        request.setAttribute("autores", autores);
        request.setAttribute("generos", generos);

        //Lógica para mostrar listado de Libros con o sin filtros
        ModeloLibros ml = new ModeloLibros();
        List<Libro> libros = ml.getLibros();
        // Recogemos los filtros que haya puesto si no hay mostramos listado al completo
        String filtroTexto = request.getParameter("searchFor");
        String filtroIdGenero = request.getParameter("generoFiltro");
        String filtroIdAutor = request.getParameter("autorFiltro");
        if (request.getMethod().equals("POST")) {
            if (!filtroTexto.trim().isEmpty() || !filtroIdGenero.trim().isEmpty() || !filtroIdAutor.trim().isEmpty()) {
                List<Libro> librosFiltrado = ml.filtrarLibros(libros, filtroIdAutor, filtroIdGenero, filtroTexto);
                request.setAttribute("libros", librosFiltrado);
            } else { // Si no hay filtro alguno mostramos los libros al completo
                request.setAttribute("libros", libros);
            }
        } else { // Si no hay filtro alguno mostramos los libros al completo
            request.setAttribute("libros", libros);
        }

        mg.closeEntityManagerFactory();
        ml.closeEntityManagerFactory();
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
