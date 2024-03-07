<%-- 
    Document   : gestionPrestamos
    Created on : 1 mar 2024, 12:10:34
    Author     : Juan Antonio
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<fmt:setLocale value="${pageContext.request.locale}" />
<%-- Obtener el usuario de la sesión --%>
<c:set var="usuario" value="${sessionScope.usuario}" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Biblioteca - Gestion Prestamos</title>
        <!-- Enlace al CSS de Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <!-- Enlace al archivo CSS personalizado -->
    </head>
    <body>

        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="MenuEmpleado">Biblioteca - Empleado</a>
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
            <h2 class="mb-4">Gestión de Prestamos</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Nombre Libro</th>
                        <th>Nombre Usuario</th>
                        <th>Fecha del Préstamo</th>
                        <th>Fecha de devolución</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="prestamo" items="${prestamos}">
                        <tr id="row_${prestamo.id}" data-user-id="${prestamo.usuario.id}" data-book-id="${prestamo.libro.id}">
                            <td>${prestamo.id}</td>
                            <td id="nombreLibro_${prestamo.id}">${prestamo.libro.titulo}</td>
                            <td id="nombreUsuario_${prestamo.id}">${prestamo.usuario.nombre}</td>
                            <td id="fechaPrestamo_${prestamo.id}"><fmt:formatDate value="${prestamo.fechaPrestamo}" type="date" /></td>
                            <td id="fechaDevolucion_${prestamo.id}"><fmt:formatDate value="${prestamo.fechaDevolucion}" type="date" /></td>
                            <td id="estadoPrestamo_${prestamo.id}">${prestamo.estadoPrestamo}</td>
                            <td>
                                <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#editarPrestamoModal" data-bs-whatever="${prestamo.id}">
                                    Editar
                                </button>
                                <button type="button" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#eliminarPrestamoModal" data-bs-whatever="${prestamo.id}">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#crearPrestamoModal">Crear Nuevo Préstamo</button>

            <a href="MenuEmpleado" class="btn btn-primary">Volver al Menú Principal</a>
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


        <!-- Modales de Crear, Editar y Eliminar Préstamo -->
        <div class="modal fade" id="crearPrestamoModal" tabindex="-1" aria-labelledby="crearPrestamoModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="crearPrestamoModalLabel">Crear Préstamo</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Aquí va el formulario de creación del Préstamo -->
                        <form action="GestionPrestamos" method="POST">
                            <div class="mb-3">
                                <label for="setNombrelibro" class="form-label">Nombre del Libro</label>
                                <select class="form-control" id="setNombrelibro" name="setNombrelibro">
                                    <option value="">Seleccione Libro</option>
                                    <c:forEach var="libro" items="${libros}">
                                        <option value="${libro.id}">${libro.titulo}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="setNombreusuario" class="form-label">Nombre del Usuario</label>
                                <select class="form-control" id="setNombreusuario" name="setNombreusuario" rows="5">
                                    <option value="">Seleccione Socio</option>
                                    <c:forEach var="usuario" items="${usuarios}">
                                        <c:if test="${usuario.tipoUsuario eq 'SOCIO'}">
                                            <option value="${usuario.id}">${usuario.nombre}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="setFechaPrestamo" class="form-label">Fecha del Préstamo</label>
                                <input type="date" class="form-control" id="setFechaPrestamo" name="setFechaPrestamo" rows="5">
                            </div>
                            <div class="mb-3">
                                <label for="setFechaDevolucion" class="form-label">Fecha de Devolución</label>
                                <input type="date" class="form-control" id="setFechaDevolucion" name="setFechaDevolucion" rows="5">
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                        </form>
                    </div>
                    <div class="modal-footer">

                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="editarPrestamoModal" tabindex="-1" aria-labelledby="editarPrestamoModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editarPrestamoModalLabel">Editar Préstamo</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Aquí va el formulario de edición del préstamo -->
                        <form action="GestionPrestamos" method="POST">
                            <input type="hidden" id="idPrestamoEditar" name="idPrestamoEditar">
                            <div class="mb-3">
                                <label for="editNombrelibro" class="form-label">Nombre del Libro</label>
                                <select class="form-control" id="editNombrelibro" name="editNombrelibro">
                                    <option value="">Seleccione Libro</option>
                                    <c:forEach var="libro" items="${libros}">
                                        <option id="libro_${libro.id}" value="${libro.id}">${libro.titulo}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="editNombreusuario" class="form-label">Nombre del Usuario</label>
                                <select class="form-control" id="editNombreusuario" name="editNombreusuario" rows="5">
                                    <option value="">Seleccione Socio</option>
                                    <c:forEach var="usuario" items="${usuarios}">
                                        <c:if test="${usuario.tipoUsuario eq 'SOCIO'}">
                                            <option id="usuario_${usuario.id}" value="${usuario.id}">${usuario.nombre}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="editFechaPrestamo" class="form-label">Fecha del Préstamo</label>
                                <input type="date" class="form-control" id="editFechaPrestamo" name="editFechaPrestamo">
                            </div>
                            <div class="mb-3">
                                <label for="editFechaDevolucion" class="form-label">Fecha de Devolución</label>
                                <input type="date" class="form-control" id="editFechaDevolucion" name="editFechaDevolucion">
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="eliminarPrestamoModal" tabindex="-1" aria-labelledby="eliminarPrestamoModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="eliminarPrestamoModalLabel">Eliminar Préstamo</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <p>¿Está seguro de que desea eliminar este préstamo?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <form action="GestionPrestamos">
                            <input type="hidden" id="idPrestamoEliminar" name="idPrestamo">
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
            const exampleModal = document.getElementById('eliminarPrestamoModal');
            exampleModal.addEventListener('show.bs.modal', event => {
                const button = event.relatedTarget;
                const recipient = button.getAttribute('data-bs-whatever');
                const modalInput = document.getElementById('idPrestamoEliminar');

                modalInput.value = recipient;
            });

            const modalEditar = document.getElementById('editarPrestamoModal');
            modalEditar.addEventListener('show.bs.modal', event => {
                const button = event.relatedTarget;
                const idPrestamo = button.getAttribute('data-bs-whatever');
                const nombreLibro = document.getElementById('nombreLibro_' + idPrestamo).innerText.trim();
                const nombreUsuario = document.getElementById('nombreUsuario_' + idPrestamo).innerText.trim();
                const fechaPrestamo = document.getElementById('fechaPrestamo_' + idPrestamo).innerText.trim();
                const fechaDevolucion = document.getElementById('fechaDevolucion_' + idPrestamo).innerText.trim();

                document.getElementById('idPrestamoEditar').value = idPrestamo;

                const selectLibro = document.getElementById('editNombrelibro');
                const selectUsuario = document.getElementById('editNombreusuario');

                // Recuperar los IDs de usuario y libro de la fila seleccionada
                const userId = document.getElementById('row_' + idPrestamo).getAttribute('data-user-id');
                const bookId = document.getElementById('row_' + idPrestamo).getAttribute('data-book-id');

                // Marcar por defecto las opciones correspondientes a los IDs de usuario y libro
                for (let option of selectLibro.options) {
                    if (option.value === bookId) {
                        option.selected = true;
                        break;
                    }
                }

                for (let option of selectUsuario.options) {
                    if (option.value === userId) {
                        option.selected = true;
                        break;
                    }
                }

                // Formatear la fecha del préstamo y devolución en el formato YYYY-MM-DD
                const formattedFechaPrestamo = convertirFecha(fechaPrestamo);
                const formattedFechaDevolucion = convertirFecha(fechaDevolucion);
                // Establecer la fecha formateada como valor por defecto en los inputs date
                document.getElementById('editFechaPrestamo').value = formattedFechaPrestamo;
                document.getElementById('editFechaDevolucion').value = formattedFechaDevolucion;
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
