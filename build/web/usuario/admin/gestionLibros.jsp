<%-- 
    Document   : gestionLibros
    Created on : 1 mar 2024, 12:10:43
    Author     : Juan Antonio
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<fmt:setLocale value="${pageContext.request.locale}" />
<%-- Obtener el usuario de la sesión --%>
<c:set var="usuario" value="${sessionScope.usuario}" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Biblioteca - Gestion Libros</title>
        <!-- Enlace al CSS de Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <!-- Enlace al archivo CSS personalizado -->
    </head>
    <body>

        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="MenuAdmin">Biblioteca - Admin</a>
                <%-- Verificar si hay un usuario logeado --%>
                <c:if test="${not empty usuario}">
                    <%-- Si hay un usuario logeado, mostrar su nombre --%>
                    <!-- Botón de hamburguesa para dispositivos móviles -->
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="navbar-nav ms-auto mb-2 mb-lg-0">

                            <li class="nav-item">
                                <span class="nav-link">Bienvenido/a, ${usuario.nombre}</span>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="../CerrarSesion">Cerrar Sesión</a>
                            </li>

                        </ul>
                    </div>
                </c:if>
            </div>
        </nav>

        <!-- Contenido de la página -->
        <div class="container mt-5">
            <h2 class="mb-4">Gestión de Libros</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>ISBN</th>
                        <th>Título</th>
                        <th>Autores</th>
                        <th>Géneros</th>
                        <th>Fecha Edición</th>
                        <th>Imagen de portada</th>
                        <th>Modelos Disponibles</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="libro" items="${libros}">
                        <tr id="row_${libro.id}" data-autores-id="${libro.autores}" data-generos-id="${libro.generos}">
                            <td>${libro.id}</td>
                            <td id="isbn_${libro.id}">${libro.isbn}</td>
                            <td id="titulo_${libro.id}">${libro.titulo}</td>
                            <td id="autores_${libro.id}">
                                <c:forEach var="autor" items="${libro.autores}" varStatus="status">
                                    ${autor.nombre}
                                    <c:if test="${not status.last}">
                                        , 
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td id="generos_${libro.id}">
                                <c:forEach var="genero" items="${libro.generos}" varStatus="status">
                                    ${genero.nombre}
                                    <c:if test="${not status.last}">
                                        , 
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td id="fechaEdicion_${libro.id}"><fmt:formatDate value="${libro.fechaEdicion}" type="date" /></td>
                            <td><img src="../img/${libro.imagenPortada}" alt="imgLibro"
                                     width="100" height="150"/></td>
                            <td id="modelosDispo_${libro.id}">${libro.ejemplaresDisponibles}</td>
                            <td>
                                <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#editarLibroModal" data-bs-whatever="${libro.id}">
                                    Editar
                                </button>
                                <button type="button" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#eliminarLibroModal" data-bs-whatever="${libro.id}">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#crearLibroModal">Crear Nuevo Libro</button>

            <a href="MenuAdmin" class="btn btn-primary">Volver al Menú Principal</a>
        </div>

        <div class="container mt-5">
            <div class="row">
                <c:if test="${not empty errorCrear}">
                    <br>
                    <div class="error">
                        <c:out value="${errorCrear}" />
                    </div>
                </c:if>
            </div>
            <div class="row">
                <c:if test="${not empty errorEliminar}">
                    <br>
                    <div class="error">
                        <c:out value="${errorEliminar}" />
                    </div>
                </c:if>
            </div>
            <div class="row">
                <c:if test="${not empty errorEditar}">
                    <br>
                    <div class="error">
                        <c:out value="${errorEditar}" />
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Modales de Crear, Editar y Eliminar Libro -->
        <div class="modal fade" id="crearLibroModal" tabindex="-1" aria-labelledby="crearLibroModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="crearLibroModalLabel">Crear Libro</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Aquí va el formulario de creación del Libro -->
                        <form method="POST" enctype="multipart/form-data">
                            <div class="mb-3">
                                <label for="setISBN" cl ass="form-label">ISBN del Libro</label>
                                <input type="number" class="form-control" id="setISBN" name="setISBN" required>
                            </div>
                            <div class="mb-3">
                                <label for="setTitulo" class="form-label">Título del Libro</label>
                                <input type="text" class="form-control" id="setTitulo" name="setTitulo" required>
                            </div>
                            <div class="mb-3">
                                <label for="setAutor" class="form-label">Autor/es del Libro</label>
                                <select class="form-control" id="setAutor" name="setAutor[]" multiple>
                                    <c:forEach var="autor" items="${autores}">
                                        <option value="${autor.id}">${autor.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="setGenero" class="form-label">Género/s del Libro</label>
                                <select class="form-control" id="setGenero" name="setGenero[]" multiple>
                                    <c:forEach var="genero" items="${generos}">
                                        <option value="${genero.id}">${genero.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="setFechaEdicion" class="form-label">Fecha de Edición</label>
                                <input type="date" class="form-control" id="setFechaEdicion" name="setFechaEdicion">
                            </div>
                            <div class="mb-3">
                                <label for="setImagenPortada" class="form-label">Imagen de Portada</label>
                                <input type="file" class="form-control" id="setImagenPortada" name="setImagenPortada" required>
                                <small class="form-text text-muted">Solo se permiten archivos .jpg</small>
                            </div>
                            <div class="mb-3">
                                <label for="setModelosDispo" class="form-label">Modelos Disponibles</label>
                                <input type="number" class="form-control" id="setModelosDispo" name="setModelosDispo" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                        </form>
                    </div>

                    <div class="modal-footer">
                        <c:if test="${not empty error or not empty errorFile}">
                            <br>
                            <div class="error">
                                <c:out value="${error}" />
                                <c:out value="${errorFile}" />
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="editarLibroModal" tabindex="-1" aria-labelledby="editarLibroModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editarLibroModalLabel">Editar Libro</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    <!-- Aquí va el formulario de edición del libro -->
                    <form action="GestionLibros" method="POST" enctype="multipart/form-data">
                        <input type="hidden" id="idLibroEditar" name="idLibroEditar">
                        <div class="mb-3">
                            <label for="editISBN" class="form-label">ISBN del Libro</label>
                            <input type="number" class="form-control" id="editISBN" name="editISBN">
                        </div>
                        <div class="mb-3">
                            <label for="editTitulo" class="form-label">Título del Libro</label>
                            <input type="text" class="form-control" id="editTitulo" name="editTitulo">
                        </div>
                        <div class="mb-3">
                            <label for="editAutores" class="form-label">Autores</label>
                            <select multiple class="form-control" id="editAutores" name="editAutores[]">
                                <c:forEach var="autor" items="${autores}">
                                    <c:choose>
                                        <c:when test="${libro.autores.contains(autor)}">
                                            <option value="${autor.id}" selected>${autor.nombre}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${autor.id}">${autor.nombre}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                            <small class="text-muted">No seleccione ninguno para no modificar</small>
                        </div>

                        <div class="mb-3">
                            <label for="editGeneros" class="form-label">Géneros</label>
                            <select multiple class="form-control" id="editGeneros" name="editGeneros[]">
                                <c:forEach var="genero" items="${generos}">
                                    <c:choose>
                                        <c:when test="${libro.generos.contains(genero)}">
                                            <option value="${genero.id}" selected>${genero.nombre}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${genero.id}">${genero.nombre}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                            <small class="text-muted">No seleccione ninguno para no modificar</small>
                        </div>
                        <div class="mb-3">
                            <label for="editFechaEdicion" class="form-label">Fecha de Edición</label>
                            <input type="date" class="form-control" id="editFechaEdicion" name="editFechaEdicion">
                        </div>
                        <div class="mb-3">
                            <label for="editImagenPortada" class="form-label">Imagen de Portada</label>
                            <input type="file" class="form-control" id="editImagenPortada" name="editImagenPortada">
                        </div>
                        <div class="mb-3">
                            <label for="editModelosDispo" class="form-label">Modelos Disponibles</label>
                            <input type="number" class="form-control" id="editModelosDispo" name="editModelosDispo">
                        </div>
                        <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="eliminarLibroModal" tabindex="-1" aria-labelledby="eliminarLibroModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="eliminarLibroModalLabel">Eliminar Libro</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    <p>¿Está seguro de que desea eliminar este libro?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <form action="GestionLibros">
                        <input type="hidden" id="idLibroEliminar" name="idLibro">
                        <button type="submit" class="btn btn-danger">Eliminar</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%-- Toast para errores --%>
    <div class="toast-container position-fixed bottom-0 end-0 p-3">
        <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <svg class="bd-placeholder-img rounded me-2" width="20" height="20" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" preserveAspectRatio="xMidYMid slice" focusable="false"><rect width="100%" height="100%" fill="#ff0000"></rect></svg>

                <strong class="me-auto">Page Message</strong>
                <small id="minutesElapsed"></small>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                ${error}
            </div>
        </div>
    </div>

    <!-- Scripts de Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    <script>
        const modalEliminar = document.getElementById('eliminarLibroModal');
        modalEliminar.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            const recipient = button.getAttribute('data-bs-whatever');
            const modalInput = document.getElementById('idLibroEliminar');

            modalInput.value = recipient;
        });

        const modalEditar = document.getElementById('editarLibroModal');
        modalEditar.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            const idLibro = button.getAttribute('data-bs-whatever');
            const isbn = document.getElementById('isbn_' + idLibro).innerText.trim();
            const titulo = document.getElementById('titulo_' + idLibro).innerText.trim();
            const fechaEdicion = document.getElementById('fechaEdicion_' + idLibro).innerText.trim();
            const modelosDispo = document.getElementById('modelosDispo_' + idLibro).innerText.trim();
            console.log(fechaEdicion);
            document.getElementById('idLibroEditar').value = idLibro;

            // Introducir valores del libro en campos de Edición
            document.getElementById('editISBN').value = isbn;
            document.getElementById('editTitulo').value = titulo;
            document.getElementById('editModelosDispo').value = modelosDispo;

            // Formatear la fecha del préstamo y devolución en el formato YYYY-MM-DD
            const formattedFechaEdicion = convertirFecha(fechaEdicion);
            console.log(formattedFechaEdicion);
            // Establecer la fecha formateada como valor por defecto en los inputs date
            document.getElementById('editFechaEdicion').value = formattedFechaEdicion;
        });

        // Función para convertir fecha al formato YYYY-MM-DD
        function convertirFecha(fechaTexto) {
            // Objeto con los meses en español
            const meses = {
                'ene': '01', 'feb': '02', 'mar': '03', 'abr': '04',
                'may': '05', 'jun': '06', 'jul': '07', 'ago': '08',
                'sept': '09', 'oct': '10', 'nov': '11', 'dic': '12'
            };

            // Separar la fecha en día, mes y año
            const partesFecha = fechaTexto.split(' ');
            const dia = partesFecha[0].length === 1 ? '0' + partesFecha[0] : partesFecha[0]; // Agregar cero si el día no tiene decenas
            const mes = meses[partesFecha[1].toLowerCase()];
            const año = partesFecha[2];
            console.log(mes);

            // Devolver la fecha en formato "YYYY-MM-DD"
            return año + '-' + mes + '-' + dia;
        }
    </script>
    <%-- Verificar si hay un mensaje de Toast para mostrar --%>
    <script>
        const toastError = document.getElementById('liveToast');
        <c:if test="${error ne null}">
        const toast = new bootstrap.Toast(toastError);
        var minutesElapsed = document.getElementById('minutesElapsed');
        // Actualizar los minutos transcurridos cada segundo
        var start = new Date().getTime(); // Obtener el tiempo actual en milisegundos
        var interval = setInterval(function () {
            var now = new Date().getTime(); // Obtener el tiempo actual en milisegundos
            var elapsed = Math.floor((now - start) / (1000 * 60)); // Calcular los minutos transcurridos
            minutesElapsed.textContent = "Hace " + elapsed + " minutos"; // Actualizar el contenido del elemento
        }, 1000); // Intervalo de actualización: 1 segundo

        toast.show();
        </c:if>
    </script>
</body>
</html>
