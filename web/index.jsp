<%-- 
    Document   : index
    Created on : 29 feb 2024, 18:58:43
    Author     : Juan Antonio
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<fmt:setBundle basename="resources.language"/>
<%-- Obtener el usuario de la sesión --%>
<c:set var="usuario" value="${sessionScope.usuario}" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="title.home.library"/></title>
        <!-- Enlace al CSS de Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <!-- Enlace al archivo CSS personalizado -->
    </head>
    <body>
        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="#"><fmt:message key="nav.brand.library"/></a>
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
        <div class="container mt-5">
            <div class="row">
                <div class="col-md-6">
                    <h1 class="display-4"><fmt:message key="msg.welcome"/>, ${usuario.nombre}</h1>
                    <p class="lead"><fmt:message key="msg.subwelcome"/></p>
                    <a href="BuscarLibros" class="btn btn-primary"><fmt:message key="btn.label.find"/></a>
                </div>
            </div>
        </div>

        <!-- Scripts de Bootstrap -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    </body>
</html>
