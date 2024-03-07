/*
 * Detalles Libro 
 */
package controladores;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ModeloLibros;
import modelo.ModeloPrestamos;
import modelo.entidades.EstadoPrestamo;
import modelo.entidades.Libro;
import modelo.entidades.Prestamo;
import modelo.entidades.Usuario;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "DetallesLibro", urlPatterns = {"/DetallesLibro"})
public class DetallesLibro extends HttpServlet {

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
        String vista = "/detallesLibro.jsp";
        // Obtenemos el id del libro recibido
        String idLibroVer = request.getParameter("libro_id");
        System.out.println(idLibroVer);
        ModeloLibros ml = new ModeloLibros();
        Libro libro = null;
        if (idLibroVer != null) {
            libro = ml.consultarLibro(Long.parseLong(idLibroVer));
        }

        if (libro != null) {
            request.setAttribute("libro", libro);
        }
        String mesesStr = request.getParameter("meses");

        if (idLibroVer != null && !idLibroVer.trim().isEmpty()
                && mesesStr != null && !mesesStr.trim().isEmpty()) {
            // Obtenemos fecha actual y sumamos meses para obtener fechaPrestamo y fechaDevolucion
            Calendar calendar = Calendar.getInstance();
            Date fechaPrestamo = calendar.getTime(); // Fecha en el momento que se pide el préstamo

            int meses = Integer.parseInt(mesesStr);
            calendar.add(Calendar.MONTH, meses);
            Date fechaDevolucion = calendar.getTime();

            // Obtenemos el usuario actual de la sesion para asociarle el préstamo
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

            if (usuario != null && fechaPrestamo != null && fechaDevolucion != null) {
                // Llamamos al modelo de Préstamos y creamos el nuevo préstamo
                ModeloPrestamos mp = new ModeloPrestamos();
                Prestamo nuevoPrestamo = new Prestamo();
                nuevoPrestamo.setLibro(libro);
                nuevoPrestamo.setUsuario(usuario);
                nuevoPrestamo.setFechaPrestamo(fechaPrestamo);
                nuevoPrestamo.setFechaDevolucion(fechaDevolucion);
                nuevoPrestamo.setEstadoPrestamo(EstadoPrestamo.CURRENTE);

                String error = mp.crearPrestamo(nuevoPrestamo);

                if (error != null) {
                    request.setAttribute("error", error);
                } else {
                    response.sendRedirect("PerfilUsuario");
                    return;
                }
            } else {
                request.setAttribute("error", "Error a la hora de crear el prestamo");
            }
        }

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
