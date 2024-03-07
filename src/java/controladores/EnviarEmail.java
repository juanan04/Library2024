/*
 * Servlet EnviarEmail.
 * Controlador para enviar email.
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
import modelo.entidades.Prestamo;
import modelo.utilidades.Email;
import modelo.utilidades.Utilidades;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "EnviarEmail", urlPatterns = {"/empleado/EnviarEmail"})
public class EnviarEmail extends HttpServlet {

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
        // Lógica para enviar email a todos los SOCIOS con un préstamo en PENDIENTE
        ModeloPrestamos mp = new ModeloPrestamos();
        List<Prestamo> prestamos = mp.getPrestamos();
        String enviarCorreos = request.getParameter("sanciones");
        List<String> correosSancionar = mp.getCorreosUsuariosMorosos(prestamos);
        System.out.println(correosSancionar);
        if (request.getMethod().equals("POST") && enviarCorreos != null && !correosSancionar.isEmpty()) {
            for (String correo : correosSancionar) {
                String to = correo.trim();
                String subject = "Prestamo Libro Expirado";
                String text = "Querido Socio desde la Biblioteca le recordamos que tiene un préstamo expirado.\n "
                        + "Porfavor realice la devolución lo tan pronto posible que pueda.\n "
                        + "Saludos Cordiales, el Administrador de la biblioteca";
                String from = "aragon.andrades.juan.antonio@iescamas.es";
                String password = "FPjuanAncurso2023/2024";
                Email email = new Email();
                email.setTo(to);
                email.setSubject(subject);
                email.setText(text);
                email.setFrom(from);
                Utilidades u = new Utilidades();
                String error = "El email se ha enviado correctamente";
                try {
                    u.enviarEmail(email, password);
                } catch (Throwable e) {
                    error = "Error al enviar e-mail: <br>" + e.getClass().getName() + ":"
                            + e.getMessage();
                    
                }
                request.setAttribute("error", error);
            }
            response.sendRedirect("HistorialPrestamos");
        } else {
            System.out.println("Valores no recibidos");
            response.sendRedirect("HistorialPrestamos");
        }
        mp.closeEntityManagerFactory();
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
