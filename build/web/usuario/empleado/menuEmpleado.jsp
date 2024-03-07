<%-- 
    Document   : menuEmpleado
    Created on : 4 mar 2024, 12:02:00
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
        <title>Biblioteca - Empleado</title>
        <!-- Enlace al CSS de Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <!-- Enlace al archivo CSS personalizado -->
    </head>
    <body>
        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="#">Biblioteca - Empleado</a>
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
            <h2>Seleccione una opción de gestión:</h2>
            <div class="list-group mt-3">
                <a href="GestionPrestamos" class="list-group-item list-group-item-action">Gestionar Préstamos</a>
                <a href="HistorialPrestamos" class="list-group-item list-group-item-action">Consultar Historial de Préstamos</a>
                <c:if test="${usuario.tipoUsuario eq 'ADMINISTRADOR'}">
                    <a href="../admin/MenuAdmin" class="list-group-item list-group-item-action">Volver al menú de Administrador</a>
                </c:if>
            </div>
        </div>

        <!-- Scripts de Bootstrap -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>

    </body>
</html>
