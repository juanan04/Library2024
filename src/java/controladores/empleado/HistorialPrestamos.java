/*
 * Servlet HistorialPrestamos
 */
package controladores.empleado;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ModeloLibros;
import modelo.ModeloPrestamos;
import modelo.ModeloUsuarios;
import modelo.entidades.Libro;
import modelo.entidades.Prestamo;
import modelo.entidades.Usuario;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "HistorialPrestamos", urlPatterns = {"/empleado/HistorialPrestamos"})
public class HistorialPrestamos extends HttpServlet {

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
        String vista = "/usuario/historialPrestamos.jsp";

        //Lógica para mostrar listado de Préstamos
        ModeloPrestamos mp = new ModeloPrestamos();
        List<Prestamo> prestamos = mp.getPrestamos();
        mp.actualizarEstadoPrestamos(prestamos);

        // Lógica para listar los usuarios SOCIOS y libros del préstamo
        ModeloLibros ml = new ModeloLibros();
        ModeloUsuarios mu = new ModeloUsuarios();
        List<Libro> libros = ml.getLibros();
        List<Usuario> usuarios = mu.getUsuarios();
        request.setAttribute("libros", libros);
        request.setAttribute("usuarios", usuarios);

        // Lógica para mostrar los prestamos de x fecha a x fecha
        String fechaIniStr = request.getParameter("fechaInicio");
        String fechaFinStr = request.getParameter("fechaFin");
        if (fechaIniStr != null && fechaFinStr != null) {
            if (!fechaIniStr.trim().isEmpty() && !fechaFinStr.trim().isEmpty()) {
                List<Prestamo> prestamosFiltrados = mp.filtrarPrestamosPorFecha(prestamos, fechaIniStr, fechaFinStr);
                request.setAttribute("prestamos", prestamosFiltrados);

                // Procedemos a recopilar datos de prestamos por libro para la gráfica
                Map<String, Integer> prestamosPorLibro = new HashMap<>();

                for (Prestamo prestamo : prestamosFiltrados) {
                    String tituloLibro = prestamo.getLibro().getTitulo();
                    prestamosPorLibro.put(tituloLibro, prestamosPorLibro.getOrDefault(tituloLibro, 0) + 1);
                }

                // Extraer los títulos de los libros y la cantidad de préstamos
                List<String> titulosLibros = new ArrayList<>(prestamosPorLibro.keySet());
                List<Integer> cantidadPrestamos = new ArrayList<>(prestamosPorLibro.values());
                // Pasar los datos al atributo del request para que la vista JSP pueda acceder a ellos
                request.setAttribute("titulosLibros", titulosLibros);
                request.setAttribute("cantidadPrestamos", cantidadPrestamos);

            } else {
                // Lógica para mostrar los préstamos asociados a un libro
                String idLibroPrestamos = request.getParameter("prestamosLibro");
                if (idLibroPrestamos != null) {
                    // Declaramos el libro encontrado por el id
                    Libro libro = ml.consultarLibro(Long.parseLong(idLibroPrestamos));
                    List<Prestamo> prestamosFiltradosPorLibro = mp.filtrarPrestamosPorLibro(prestamos, libro);

                    if (prestamosFiltradosPorLibro != null && !prestamosFiltradosPorLibro.isEmpty()) {
                        // Enviamos la lista a la vista
                        request.setAttribute("prestamos", prestamosFiltradosPorLibro);
                    } else {
                        // Enviamos mensaje de error
                        request.setAttribute("error", "No se ha encontrado ningún préstamo para ese libro");
                        // Si no mostramos todos los préstamos
                        request.setAttribute("prestamos", prestamos);
                    }
                } else {
                    request.setAttribute("prestamos", prestamos);
                }
            }
        } else {
            request.setAttribute("prestamos", prestamos);
        }

        // Lógica para listar titulos y cantidad de préstamos actuales
        List<Prestamo> prestamosActuales = mp.filtrarPrestamosActuales(prestamos);

        Map<String, Integer> prestamosPorLibro = new HashMap<>(); // Creamos mapa hash para la cantidad de préstamos por cada libro

        for (Prestamo prestamo : prestamosActuales) {
            String tituloLibro = prestamo.getLibro().getTitulo();
            prestamosPorLibro.put(tituloLibro, prestamosPorLibro.getOrDefault(tituloLibro, 0) + 1);
        }
        request.setAttribute("prestamosPorLibro", prestamosPorLibro);
        
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
