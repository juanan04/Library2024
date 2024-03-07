<%-- 
    Document   : perfilUsuario
    Created on : 1 mar 2024, 9:20:38
    Author     : Juan Antonio
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<fmt:setBundle basename="resources.language"/>
<%-- Obtener el usuario de la sesión --%>
<c:set var="usuario" value="${sessionScope.usuario}" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Biblioteca - Perfil de Usuario</title>
        <!-- Enlace al CSS de Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <!-- Enlace al archivo CSS personalizado -->
    </head>
    <body>

        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="Inicio">Biblioteca</a>
                <!-- Botón de hamburguesa para dispositivos móviles -->
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                        <%-- Verificar si hay un usuario logeado --%>
                        <c:choose>
                            <c:when test="${not empty usuario}">
                                <%-- Si hay un usuario logeado, mostrar su nombre --%>
                                <li class="nav-item">
                                    <a class="nav-link" href="PerfilUsuario"><fmt:message key="msg.simplewelcome"/>, ${usuario.nombre}</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="CerrarSesion"><fmt:message key="msg.closesesion"/></a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <%-- Si no hay un usuario logeado, mostrar las opciones para iniciar sesión y registrarse --%>
                                <li class="nav-item">
                                    <a class="nav-link" href="Registro"><fmt:message key="nav.menu.register"/></a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="Login"><fmt:message key="nav.menu.login"/></a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Contenido de la página -->
        <div class="container mt-5 mb-5">
            <div class="row">
                <div class="col-md-6 offset-md-3">
                    <h2 class="mb-4">Perfil de Usuario</h2>
                    <form method="post">
                        <input type="hidden" name="idUsuario" value="${usuario.id}">
                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" class="form-control" id="nombre" name="nombre" value="<c:out value="${usuario.nombre}"/>">
                        </div>
                        <div class="mb-3">
                            <label for="apellidos" class="form-label">Apellidos</label>
                            <input type="text" class="form-control" id="apellidos" name="apellidos" value="<c:out value="${usuario.apellidos}"/>">
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Correo Electrónico</label>
                            <input type="email" class="form-control" id="email" name="email" value="<c:out value="${usuario.email}"/>">
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Contraseña</label>
                            <input type="password" class="form-control" id="password" name="password" value="<c:out value="${usuario.password}"/>">
                        </div>
                        <div class="mb-3">
                            <label for="domicilio" class="form-label">Domicilio</label>
                            <input type="text" class="form-control" id="domicilio" name="domicilio" value="<c:out value="${usuario.domicilio}"/>">
                        </div>
                        <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                    </form>
                </div>
            </div>
            <div class="row">
                <h2 class="mb-4">Mis Préstamos</h2>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Nombre Libro</th>
                            <th>Fecha del Préstamo</th>
                            <th>Fecha de devolución</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="prestamo" items="${prestamos}">
                            <tr>
                                <td>${prestamo.libro.titulo}</td>
                                <td><fmt:formatDate value="${prestamo.fechaPrestamo}" type="date" /></td>
                                <td><fmt:formatDate value="${prestamo.fechaDevolucion}" type="date" /></td>
                                <td>${prestamo.estadoPrestamo}</td>
                                <td>
                                    <form>
                                        <input type="hidden" value="${prestamo.id}" name="prestamoDevolverId">
                                        <button type="submit" class="btn btn-primary btn-sm"
                                                <c:if test="${prestamo.estadoPrestamo eq 'DEVUELTO'}">
                                                    disabled
                                                </c:if>>
                                            Devolver Libro
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
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
