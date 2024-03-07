<%-- 
    Document   : gestionGeneros
    Created on : 1 mar 2024, 12:10:16
    Author     : Juan Antonio
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%-- Obtener el usuario de la sesión --%>
<c:set var="usuario" value="${sessionScope.usuario}" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Biblioteca - Gestion Generos</title>
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
            <h2 class="mb-4">Gestión de Generos</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Nombre</th>
                        <th>Descripcion</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="genero" items="${generos}">
                        <tr id="row_${genero.id}">
                            <td>${genero.id}</td>
                            <td id="genero_${genero.id}">${genero.nombre}</td>
                            <td id="descripcion_${genero.id}">${genero.descripcion}</td>
                            <td>
                                <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#editarGeneroModal" data-bs-whatever="${genero.id}">
                                    Editar
                                </button>
                                <button type="button" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#eliminarGeneroModal" data-bs-whatever="${genero.id}">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#crearGeneroModal">Crear Nuevo Género</button>

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


        <!-- Modales de Crear, Editar y Eliminar Genero -->
        <div class="modal fade" id="crearGeneroModal" tabindex="-1" aria-labelledby="crearGeneroModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="crearGeneroModalLabel">Crear Género</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Aquí va el formulario de creación del Género -->
                        <form method="post">
                            <div class="mb-3">
                                <label for="setNombre" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="setNombre" name="setNombre" value="<c:out value="${nombre}"/>">
                            </div>
                            <div class="mb-3">
                                <label for="setApellidos" class="form-label">Descripción</label>
                                <textarea type="text" class="form-control" id="setDescripcion" name="setDescripcion" rows="5"><c:out value="${descripcion}"/></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="editarGeneroModal" tabindex="-1" aria-labelledby="editarGeneroModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editarGeneroModalLabel">Editar Género</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Aquí va el formulario de edición del género -->
                        <form action="GestionGeneros" method="POST">
                            <input type="hidden" id="idGeneroEditar" name="idGeneroEditar">
                            <div class="mb-3">
                                <label for="editNombre" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="editNombre" name="editNombre">
                            </div>
                            <div class="mb-3">
                                <label for="editDescripcion" class="form-label">Descripción</label>
                                <textarea class="form-control" id="editDescripcion" name="editDescripcion" rows="5"></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="eliminarGeneroModal" tabindex="-1" aria-labelledby="eliminarGeneroModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="eliminarGeneroModalLabel">Eliminar Género</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <p>¿Está seguro de que desea eliminar este género?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <form action="GestionGeneros">
                            <input type="hidden" id="idGeneroEliminar" name="idGenero">
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
            const modalEliminar = document.getElementById('eliminarGeneroModal');
            modalEliminar.addEventListener('show.bs.modal', event => {
                const button = event.relatedTarget;
                const recipient = button.getAttribute('data-bs-whatever');
                const modalInput = document.getElementById('idGeneroEliminar');

                modalInput.value = recipient;
            });

            const modalEditar = document.getElementById('editarGeneroModal');
            modalEditar.addEventListener('show.bs.modal', event => {
                const button = event.relatedTarget;
                const idGenero = button.getAttribute('data-bs-whatever');
                const genero = document.getElementById('genero_' + idGenero).innerText.trim();
                const descripcion = document.getElementById('descripcion_' + idGenero).innerText.trim();

                document.getElementById('idGeneroEditar').value = idGenero;
                document.getElementById('editNombre').value = genero;
                document.getElementById('editDescripcion').value = descripcion;
            });
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
