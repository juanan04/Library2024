/*
 * Servlet Gestión Prestamos
 */
package controladores.empleado;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
@WebServlet(name = "GestionPrestamos", urlPatterns = {"/empleado/GestionPrestamos"})
public class GestionPrestamos extends HttpServlet {

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
        String vista = "/usuario/empleado/gestionPrestamos.jsp";
        //Lógica para mostrar listado de Préstamos
        ModeloPrestamos mp = new ModeloPrestamos();
        List<Prestamo> prestamos = mp.getPrestamos();
        mp.actualizarEstadoPrestamos(prestamos);
        request.setAttribute("prestamos", prestamos);

        // Lógica para listar los usuarios SOCIOS y libros
        ModeloLibros ml = new ModeloLibros();
        ModeloUsuarios mu = new ModeloUsuarios();
        List<Libro> libros = ml.getLibros();
        List<Usuario> usuarios = mu.getUsuarios();
        request.setAttribute("libros", libros);
        request.setAttribute("usuarios", usuarios);

        // Lógica para crear un préstamo
        String idLibro = request.getParameter("setNombrelibro");
        String idUsuario = request.getParameter("setNombreusuario");
        String fechaPrestamoStr = request.getParameter("setFechaPrestamo");
        String fechaDevolucionStr = request.getParameter("setFechaDevolucion");

        if (idLibro != null && !idLibro.trim().isEmpty()
                && idUsuario != null && !idUsuario.trim().isEmpty()
                && fechaPrestamoStr != null && !fechaPrestamoStr.trim().isEmpty()
                && fechaDevolucionStr != null && !fechaDevolucionStr.trim().isEmpty()) {
            // Pasamos fechas de String a Date
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date fechaPrestamo = formato.parse(fechaPrestamoStr);
                Date fechaDevolucion = formato.parse(fechaDevolucionStr);

                // Creamos un objeto Prestamo con los datos obtenidos
                Prestamo nuevoPrestamo = new Prestamo();
                //Buscamos el libro y el usuario seleccionado
                Libro libro = ml.consultarLibro(Long.parseLong(idLibro));
                Usuario usuario = mu.consultarUsuario(Long.parseLong(idUsuario));

                // Asignamos libro y usuario al prestamo
                nuevoPrestamo.setLibro(libro);
                nuevoPrestamo.setUsuario(usuario);
                nuevoPrestamo.setFechaPrestamo(fechaPrestamo);
                nuevoPrestamo.setFechaDevolucion(fechaDevolucion);

                // Llamamos al método para crear el préstamo en el modelo
                String error = mp.crearPrestamo(nuevoPrestamo);
                if (error != null) {
                    // Manejar el error si ocurre durante la creación del préstamo
                    request.setAttribute("error", error);
                } else {
                    response.sendRedirect("GestionPrestamos");
                    request.setAttribute("error", "Préstamo creado correctamente");
                    return;
                }
            } catch (ParseException e) {
                request.setAttribute("error", "Error al parsear las fechas");
            }

        }

        // Lógica para eliminar un préstamo
        String idPrestamoEliminar = request.getParameter("idPrestamo");
        if (idPrestamoEliminar != null && !idPrestamoEliminar.trim().isEmpty()) {
            String error = mp.eliminarPrestamo(Long.parseLong(idPrestamoEliminar));
            if (error != null) {
                request.setAttribute("error", error);
                System.out.println("Error al eliminar Prestamo");
            } else {
                response.sendRedirect("GestionPrestamos");
                request.setAttribute("error", "El préstamo se ha eliminado Correctamente");
                return;
            }
        }

        // Lógica para editar un préstamo
        if (request.getMethod().equals("POST")) {
            String idPrestamoEdit = request.getParameter("idPrestamoEditar");
            String editNombrelibro = request.getParameter("editNombrelibro");
            String editNombreusuario = request.getParameter("editNombreusuario");
            String editFechaPrestamoStr = request.getParameter("editFechaPrestamo");
            String editFechaDevolucionStr = request.getParameter("editFechaDevolucion");

            if (idPrestamoEdit != null && !idPrestamoEdit.trim().isEmpty()
                    && editNombrelibro != null && !editNombrelibro.trim().isEmpty()
                    && editNombreusuario != null && !editNombreusuario.trim().isEmpty()
                    && editFechaPrestamoStr != null && !editFechaPrestamoStr.trim().isEmpty()
                    && editFechaDevolucionStr != null && !editFechaDevolucionStr.trim().isEmpty()) {

                // Pasamos fechas de String a Date
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date editFechaPrestamo = formato.parse(editFechaPrestamoStr);
                    Date editFechaDevolucion = formato.parse(editFechaDevolucionStr);

                    String error = mp.actualizarPrestamo(Long.parseLong(idPrestamoEdit), Long.parseLong(editNombrelibro),
                            Long.parseLong(editNombreusuario), editFechaPrestamo, editFechaDevolucion, 
                            mp.consultarPrestamo(Long.parseLong(idPrestamoEdit)).getEstadoPrestamo());
                    if (error != null) {
                        request.setAttribute("error", error);
                    } else {
                        response.sendRedirect("GestionPrestamos");
                        request.setAttribute("error", "Préstamo Actualizado");
                        return;
                    }
                } catch (ParseException e) {
                    request.setAttribute("error", "Error al parsear las fechas");
                }
            } else {
                request.setAttribute("error", "Todos los campos son obligatorios.");
            }
        }

        mp.closeEntityManagerFactory();
        ml.closeEntityManagerFactory();
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
