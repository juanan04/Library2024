/*
 * Servlet Login
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
import modelo.entidades.Usuario;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

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
        String vista = "/login.jsp";
        String error = null;
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email != null && password != null && !email.trim().isEmpty()
                && !password.trim().isEmpty()) {
            Usuario u = ModeloLogin.validarUsuario(email, password);
            if (u != null) {
                HttpSession sesion = request.getSession();
                sesion.setAttribute("usuario", u);
                switch (u.getTipoUsuario()) {
                    case ADMINISTRADOR:
                        response.sendRedirect("admin/MenuAdmin");
                        break;
                    case EMPLEADO:
                        response.sendRedirect("empleado/MenuEmpleado");
                        break;
                    case SOCIO:
                        response.sendRedirect("Inicio");
                        break;
                }
                return;
            } else {
                error = "Email y/o contraseñas incorrectos";
            }
        }

        // Manejo de errores
        request.setAttribute("error", error);

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
