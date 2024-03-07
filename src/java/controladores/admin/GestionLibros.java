/*
 * Gestión Libros
 */
package controladores.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
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
@WebServlet(name = "GestionLibros", urlPatterns = {"/admin/GestionLibros"})
@MultipartConfig
public class GestionLibros extends HttpServlet {

    public static final int TAM_BUFFER = 4 * 1024;

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
        String vista = "/usuario/admin/gestionLibros.jsp";

        //Listado de autores y generos
        ModeloGeneros mg = new ModeloGeneros();
        List<Autor> autores = ModeloAutores.getAutores();
        List<Genero> generos = mg.getGeneros();
        request.setAttribute("autores", autores);
        request.setAttribute("generos", generos);
        //Lógica para mostrar listado de Libros
        ModeloLibros ml = new ModeloLibros();
        List<Libro> libros = ml.getLibros();
        request.setAttribute("libros", libros);

        if (request.getMethod().equals("POST")) {
            String isbn = request.getParameter("setISBN");
            String titulo = request.getParameter("setTitulo");
            String[] autoresSeleccionados = request.getParameterValues("setAutor[]");
            String[] generosSeleccionados = request.getParameterValues("setGenero[]");
            String fechaEdicionStr = request.getParameter("setFechaEdicion");
            String modelosDisponiblesStr = request.getParameter("setModelosDispo");
            if (isbn != null && !isbn.trim().isEmpty()
                    && titulo != null && !titulo.trim().isEmpty()
                    && fechaEdicionStr != null && !fechaEdicionStr.trim().isEmpty()
                    && modelosDisponiblesStr != null && !modelosDisponiblesStr.trim().isEmpty()) {

                // Procesar los autores seleccionados
                List<Autor> listaAutores = new ArrayList<>();
                for (String idAutor : autoresSeleccionados) {
                    Autor autor = ModeloAutores.consultarAutor(Long.parseLong(idAutor));
                    if (autor != null) {
                        listaAutores.add(autor);
                    }
                }

                // Procesar los géneros seleccionados
                List<Genero> listaGeneros = new ArrayList<>();
                for (String idGenero : generosSeleccionados) {
                    Genero genero = mg.consultarGenero(Long.parseLong(idGenero));
                    if (genero != null) {
                        listaGeneros.add(genero);
                    }
                }

                // Manejar el archivo imagen
                Part parte = request.getPart("setImagenPortada");
                String nombreFichero = parte.getSubmittedFileName();
                if (nombreFichero.endsWith(".jpg")) {
                    InputStream entrada = parte.getInputStream();
                    String ruta = getServletContext().getRealPath("/img/") + isbn + ".jpg";
                    FileOutputStream salida = new FileOutputStream(ruta);
                    byte[] buffer = new byte[TAM_BUFFER];
                    while (entrada.available() > 0) {
                        int tam = entrada.read(buffer);
                        salida.write(buffer, 0, tam);
                    }
                    salida.close();
                    entrada.close();
                    nombreFichero = isbn + ".jpg";
                }
                
                // Pasamos fechaStr a Date y modelosDisponibles a int
                int modelosDisponibles = Integer.parseInt(modelosDisponiblesStr);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaEdicion = new Date();
                try {
                    fechaEdicion = dateFormat.parse(fechaEdicionStr);
                } catch (ParseException e) {
                    System.err.println("Error al convertir la fecha: " + e.getMessage());
                    request.setAttribute("error", "Error al convertir la fecha: " + e.getMessage());
                }

                // Llamamos a la funcion create para crear el libro
                String error = ml.crearLibro(isbn, titulo, listaAutores, listaGeneros, fechaEdicion, nombreFichero, modelosDisponibles);

                if (error != null) {
                    request.setAttribute("error", error);
                    request.setAttribute("isbn", isbn);
                    request.setAttribute("titulo", titulo);
                    request.setAttribute("modelosDisponibles", modelosDisponibles);
                } else {
                    response.sendRedirect("GestionLibros");
                    request.setAttribute("error", "El libro se ha creado correctamente");
                    return;
                }
            }
        }

        // Lógica para eliminar un libro
        String idLibroEliminar = request.getParameter("idLibro");
        if (idLibroEliminar != null && !idLibroEliminar.trim().isEmpty()) {
            String error = ml.eliminarLibro(Long.parseLong(idLibroEliminar));
            if (error != null) {
                request.setAttribute("error", error);
            } else {
                response.sendRedirect("GestionLibros");
                request.setAttribute("error", "El libro se ha eliminado correctamente");
                return;
            }
        }

        // Lógica para editar un libro
        if (request.getMethod().equals("POST")) {
            String idLibroEdit = request.getParameter("idLibroEditar");
            String isbnEdit = request.getParameter("editISBN");
            String tituloEdit = request.getParameter("editTitulo");
            String[] autoresSeleccionadosEdit = request.getParameterValues("editAutor[]");
            String[] generosSeleccionadosEdit = request.getParameterValues("editGenero[]");
            String fechaEdicionEditStr = request.getParameter("editFechaEdicion");
            String modelosDisponiblesEditStr = request.getParameter("editModelosDispo");
            Part parteImagen = request.getPart("editImagenPortada");
            String nombreFicheroEdit = null;
            // Consultamos libro seleccionado
            Libro l = ml.consultarLibro(Long.parseLong(idLibroEdit));

            // Procesar los autores seleccionados
            List<Autor> listaAutoresEdit = new ArrayList<>();
            if (autoresSeleccionadosEdit != null && autoresSeleccionadosEdit.length == 0) {
                for (String idAutor : autoresSeleccionadosEdit) {
                    Autor autor = ModeloAutores.consultarAutor(Long.parseLong(idAutor));
                    if (autor != null) {
                        listaAutoresEdit.add(autor);
                    }
                }
            } else {
                listaAutoresEdit = l.getAutores();
            }

            // Procesar los géneros seleccionados
            List<Genero> listaGenerosEdit = new ArrayList<>();
            if (generosSeleccionadosEdit != null && generosSeleccionadosEdit.length == 0) {
                for (String idGenero : generosSeleccionadosEdit) {
                    Genero genero = mg.consultarGenero(Long.parseLong(idGenero));
                    if (genero != null) {
                        listaGenerosEdit.add(genero);
                    }
                }
            } else {
                listaGenerosEdit = l.getGeneros();
            }

            // Verificar si el ISBN ha cambiado y si se proporciona una nueva imagen
            if (!isbnEdit.equals(l.getIsbn())) {
                // Cambiar el nombre de la imagen existente (si existe) basado en el nuevo ISBN
                File imagenAntigua = new File(getServletContext().getRealPath("/img/") + l.getIsbn() + ".jpg");
                File nuevaImagen = new File(getServletContext().getRealPath("/img/") + isbnEdit + ".jpg");
                if (imagenAntigua.exists()) {
                    imagenAntigua.renameTo(nuevaImagen);
                }
                nombreFicheroEdit = isbnEdit + ".jpg";
            } else {
                nombreFicheroEdit = l.getIsbn() + ".jpg";
            }

            
            // Verificar si se proporciona una nueva imagen y eliminar la imagen anterior si existe
            if (parteImagen != null && parteImagen.getSize() > 0) {
                File imagenAnterior = new File(getServletContext().getRealPath("/img/") + isbnEdit + ".jpg");
                if (imagenAnterior.exists()) {
                    imagenAnterior.delete();
                }
                
                if (nombreFicheroEdit.endsWith(".jpg")) {
                    InputStream entrada = parteImagen.getInputStream();
                    String ruta = getServletContext().getRealPath("/img/") + isbnEdit + ".jpg";
                    FileOutputStream salida = new FileOutputStream(ruta);
                    byte[] buffer = new byte[TAM_BUFFER];
                    while (entrada.available() > 0) {
                        int tam = entrada.read(buffer);
                        salida.write(buffer, 0, tam);
                    }
                    salida.close();
                    entrada.close();
                    System.out.println(nombreFicheroEdit);
                    nombreFicheroEdit = isbnEdit + ".jpg";
                }
            }

            // Pasamos fechaStr a Date y modelosDisponibles a int
            int modelosDisponiblesEdit = Integer.parseInt(modelosDisponiblesEditStr);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaEdicionEdit = new Date();
            try {
                fechaEdicionEdit = dateFormat.parse(fechaEdicionEditStr);
            } catch (ParseException e) {
                request.setAttribute("error", "Error al parsear la fecha");
            }

            // Crear un nuevo objeto Libro con los datos actualizados
            Libro libroActualizado = new Libro();
            libroActualizado.setId(l.getId());
            libroActualizado.setIsbn(isbnEdit);
            libroActualizado.setTitulo(tituloEdit);
            libroActualizado.setAutores(listaAutoresEdit);
            libroActualizado.setGeneros(listaGenerosEdit);
            libroActualizado.setFechaEdicion(fechaEdicionEdit);
            libroActualizado.setEjemplaresDisponibles(modelosDisponiblesEdit);
            libroActualizado.setImagenPortada(nombreFicheroEdit);
            System.out.println(libroActualizado);
            if (idLibroEdit != null && !idLibroEdit.isEmpty()
                    && libroActualizado != null){
                // Llamamos al método para actualizar el libro
                String error = ml.actulizarLibro(libroActualizado);
                System.out.println(error);
                if (error != null) {
                    request.setAttribute("error", error);
                } else {
                    response.sendRedirect("GestionLibros");
                    request.setAttribute("error", "El libro se ha actualizado correctamente");
                    return;
                }
            } else {
                System.out.println("Faltan datos para editar libro");
            }
        }

        ml.closeEntityManagerFactory();
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
