<%-- 
    Document   : gestionUsuarios
    Created on : 1 mar 2024, 9:30:35
    Author     : Juan Antonio
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%-- Obtener el usuario de la sesión --%>
<c:set var="usuario" value="${sessionScope.usuario}" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Biblioteca - Gestion Usuarios</title>
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
            <h2 class="mb-4">Gestión de Empleados</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Nombre</th>
                        <th>Apellidos</th>
                        <th>Correo Electrónico</th>
                        <th>Contraseña</th>
                        <th>Domicilio</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="usuario" items="${usuarios}">
                        <tr id="row_${usuario.id}">
                            <td>${usuario.id}</td>
                            <td id="nombre_${usuario.id}">${usuario.nombre}</td>
                            <td id="apellidos_${usuario.id}">${usuario.apellidos}</td>
                            <td id="email_${usuario.id}">${usuario.email}</td>
                            <td>******</td>
                            <td id="domicilio_${usuario.id}">${usuario.domicilio}</td>
                            <td>
                                <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#editarUsuarioModal" data-bs-whatever="${usuario.id}">
                                    Editar
                                </button>
                                <button type="button" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#eliminarUsuarioModal" data-bs-whatever="${usuario.id}">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#crearUsuarioModal">Crear Usuario</button>

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

        <!-- Modales de Crear, Editar y Eliminar Usuario -->
        <div class="modal fade" id="crearUsuarioModal" tabindex="-1" aria-labelledby="crearUsuarioModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="crearUsuarioModalLabel">Crear Empleado</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Aquí va el formulario de creación del usuario -->
                        <form>
                            <div class="mb-3">
                                <label for="setNombre" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="setNombre" name="setNombre" value="<c:out value="${nombre}"/>">
                            </div>
                            <div class="mb-3">
                                <label for="setApellidos" class="form-label">Apellidos</label>
                                <input type="text" class="form-control" id="setApellidos" name="setApellidos" value="<c:out value="${apellidos}"/>">
                            </div>
                            <div class="mb-3">
                                <label for="setEmail" class="form-label">Correo Electrónico</label>
                                <input type="email" class="form-control" id="setEmail" name="setEmail" value="<c:out value="${email}"/>">
                            </div>
                            <div class="mb-3">
                                <label for="setPassword" class="form-label">Contraseña</label>
                                <input type="password" class="form-control" id="setPassword" name="setPassword">
                            </div>
                            <div class="mb-3">
                                <label for="setDomicilio" class="form-label">Domicilio</label>
                                <input type="text" class="form-control" id="setDomicilio" name="setDomicilio" value="<c:out value="${domicilio}"/>">
                            </div>
                            <div class="mb-3">
                                <label for="setTipo" class="form-label">Tipo Usuario</label>
                                <select id="setTipo" name="setTipo" class="form-control">
                                    <option value="EMPLEADO" selected disabled>EMPLEADO</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <c:if test="${not empty error}">
                            <br>
                            <div class="error">
                                <c:out value="${error}" />
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="editarUsuarioModal" tabindex="-1" aria-labelledby="editarUsuarioModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editarUsuarioModalLabel">Editar Empleado</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Aquí va el formulario de edición del usuario -->
                        <form method="POST">
                            <input type="hidden" id="idUsuarioEditar" name="idUsuarioEditar">
                            <div class="mb-3">
                                <label for="editNombre" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="editNombre" name="editNombre">
                            </div>
                            <div class="mb-3">
                                <label for="editApellidos" class="form-label">Apellidos</label>
                                <input type="text" class="form-control" id="editApellidos" name="editApellidos">
                            </div>
                            <div class="mb-3">
                                <label for="editEmail" class="form-label">Correo Electrónico</label>
                                <input type="email" class="form-control" id="editEmail" name="editEmail">
                            </div>
                            <div class="mb-3">
                                <label for="editPassword" class="form-label">Nueva Contraseña</label>
                                <input type="password" class="form-control" id="editPassword" name="editPassword">
                                <small class="text-muted">Dejar en blanco para no cambiar.</small>
                            </div>
                            <div class="mb-3">
                                <label for="editDomicilio" class="form-label">Domicilio</label>
                                <input type="text" class="form-control" id="editDomicilio" name="editDomicilio">
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="eliminarUsuarioModal" tabindex="-1" aria-labelledby="eliminarUsuarioModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="eliminarUsuarioModalLabel">Eliminar Usuario</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <p>¿Está seguro de que desea eliminar este usuario?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <form action="GestionUsuarios">
                            <input type="hidden" id="idUsuarioEliminar" name="idUsuario">
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
            const modalCrear = document.getElementById('eliminarUsuarioModal');
            modalCrear.addEventListener('show.bs.modal', event => {
                const button = event.relatedTarget;
                const recipient = button.getAttribute('data-bs-whatever');
                const modalInput = document.getElementById('idUsuarioEliminar');

                modalInput.value = recipient;
            });

            const modalEditar = document.getElementById('editarUsuarioModal');
            modalEditar.addEventListener('show.bs.modal', event => {
                const button = event.relatedTarget;
                const idUsuario = button.getAttribute('data-bs-whatever');
                const nombre = document.getElementById('nombre_' + idUsuario).innerText.trim();
                const apellidos = document.getElementById('apellidos_' + idUsuario).innerText.trim();
                const email = document.getElementById('email_' + idUsuario).innerText.trim();
                const domicilio = document.getElementById('domicilio_' + idUsuario).innerText.trim();

                document.getElementById('idUsuarioEditar').value = idUsuario;
                document.getElementById('editNombre').value = nombre;
                document.getElementById('editApellidos').value = apellidos;
                document.getElementById('editEmail').value = email;
                document.getElementById('editDomicilio').value = domicilio;
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
