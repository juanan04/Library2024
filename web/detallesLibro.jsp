<%-- 
    Document   : detallesLibro
    Created on : 1 mar 2024, 9:11:26
    Author     : Juan Antonio
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<fmt:setBundle basename="resources.language"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="title.details.library"/></title>
        <!-- Enlace al CSS de Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <!-- Enlace al archivo CSS personalizado -->
    </head>
    <body>

        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="Inicio"><fmt:message key="nav.brand.library"/></a>
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
                                    <span class="nav-link"><fmt:message key="msg.simplewelcome"/>, ${usuario.nombre}</span>
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
        <div class="container mt-5">
            <div class="row">
                <div class="col-md-6">
                    <img src="img/<c:out value="${libro.imagenPortada}"/>" class="img-fluid" alt="Portada del libro" width="350" height="450">
                </div>
                <div class="col-md-6">
                    <h2 class="mb-4">${libro.titulo}</h2>
                    <p><strong><fmt:message key="table.label.authors"/>: </strong>
                        <c:forEach var="autor" items="${libro.autores}" varStatus="status">
                            ${autor.nombre}
                            <c:if test="${not status.last}">
                                , 
                            </c:if>
                        </c:forEach>
                    </p>
                    <p><strong><fmt:message key="table.label.genders"/>: </strong>
                        <c:forEach var="genero" items="${libro.generos}" varStatus="status">
                            ${genero.nombre}
                            <c:if test="${not status.last}">
                                , 
                            </c:if>
                        </c:forEach>
                    </p>
                    <p><strong><fmt:message key="table.label.editiondate"/>: </strong> <fmt:formatDate value="${libro.fechaEdicion}" type="date" /> </p>
                    <p><strong><fmt:message key="table.label.availablemodels"/>: </strong><c:out value="${libro.ejemplaresDisponibles}"/></p>
                    <p><strong>ISBN: </strong><c:out value="${libro.isbn}"/></p>
                    <%-- El botón aparecera si estás registrado como socio --%>
                    <c:choose>
                        <c:when test="${not empty usuario}">
                            <form class="mb-4" action="PedirPrestamo">
                                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#prestarModal" data-bs-whatever="${libro.id}">
                                    <fmt:message key="label.btn.lending"/>
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p class="lead"><fmt:message key="msg.howtolean"/>.</p>
                        </c:otherwise>
                    </c:choose>
                    <a class="btn btn-info" href="BuscarLibros"><fmt:message key="btn.label.back"/></a>
                </div>
            </div>
            <div class="row mt-5">
                <div class="col-md-12">
                    <h3>Info</h3>
                    <p><fmt:message key="msg.moreinfo"/>.</p>
                </div>
            </div>
        </div>

        <!-- Modal para prestar un libro -->
        <div class="modal fade" id="prestarModal" tabindex="-1" aria-labelledby="prestarModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="prestarModalLabel"><fmt:message key="modal.loan.title"/></h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form>
                            <!-- Campo oculto para almacenar el id_libro -->
                            <input type="hidden" id="idLibro" name="libro_id" value="${libro.id}">
                            <!-- Select para seleccionar la cantidad de meses -->
                            <div class="mb-3">
                                <label for="mesesSelect" class="form-label"><fmt:message key="modal.loan.label"/></label>
                                <select class="form-select" id="mesesSelect" name="meses">
                                    <!-- Opciones de 1 a 12 meses -->
                                    <c:forEach var="i" begin="1" end="12">
                                        <option value="${i}">${i}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <!-- Botón de enviar -->
                            <button type="submit" class="btn btn-primary"><fmt:message key="modal.loan.send"/></button>
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
