/*
 * Servlet Registro
 */
package controladores;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.ModeloLogin;
import modelo.ModeloUsuarios;
import modelo.entidades.TipoUsuario;
import modelo.entidades.Usuario;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "Registro", urlPatterns = {"/Registro"})
public class Registro extends HttpServlet {

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
        String vista = "/registro.jsp";
        if (request.getMethod().equals("POST")) {
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String domicilio = request.getParameter("domicilio");

            if (nombre != null && !nombre.trim().isEmpty()
                    && apellidos != null && !apellidos.trim().isEmpty()
                    && email != null && !email.trim().isEmpty()
                    && password != null && !password.trim().isEmpty()
                    && domicilio != null && !domicilio.trim().isEmpty()) {
                ModeloUsuarios mu = new ModeloUsuarios();
                String error = mu.crearUsuario(nombre, apellidos, email, password, domicilio, TipoUsuario.SOCIO);

                if (error != null) {
                    request.setAttribute("error", error);
                } else {
                    Usuario u = ModeloLogin.validarUsuario(email, password);
                    if (u != null) {
                        HttpSession sesion = request.getSession();
                        try {
                            sesion.setAttribute("usuario", u);
                            response.sendRedirect("Inicio");
                        }catch(Exception e) {
                            request.setAttribute("error", e.getMessage());
                        }
                        return;
                    }
                }
                mu.closeEntityManagerFactory();
            }
        }
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
