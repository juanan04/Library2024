<%-- 
    Document   : historialPrestamos
    Created on : 1 mar 2024, 9:24:23
    Author     : Juan Antonio
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%-- Obtener el usuario de la sesión --%>
<c:set var="usuario" value="${sessionScope.usuario}" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Biblioteca - Historial de Prestamos</title>
        <!-- Enlace al CSS de Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <!-- Enlace al archivo CSS personalizado -->
    </head>
    <body>

        <!-- Barra de navegación -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="MenuEmpleado">Biblioteca - Empleado</a>
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

        <!-- Filtro de rango fechas -->
        <div class="container mt-5">
            <h3>Filtrar por Rango de Fechas</h3>
            <form method="POST">
                <div class="row mt-3">
                    <div class="col-md-6">
                        <label for="fechaInicio" class="form-label">Fecha de Inicio:</label>
                        <input type="date" class="form-control" id="fechaInicio" name="fechaInicio">
                    </div>
                    <div class="col-md-6">
                        <label for="fechaFin" class="form-label">Fecha de Fin:</label>
                        <input type="date" class="form-control" id="fechaFin" name="fechaFin">
                    </div>
                </div>
                <c:if test="${usuario.tipoUsuario eq 'ADMINISTRADOR'}">
                    <div class="row mt3">
                        <div class="col-md-12">
                            <label for="prestamosLibro">Seleccione el libro para ver sus préstamos</label>
                            <select class="form-control" id="prestamosLibro" name="prestamosLibro">
                                <option value="" selected disabled>Seleccione el libro</option>
                                <c:forEach var="libro" items="${libros}">
                                    <option value="${libro.id}">${libro.titulo}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </c:if>
                <div class="row mt-3">
                    <div class="col-md-12">
                        <button type="submit" class="btn btn-primary">Filtrar</button>
                    </div>
                </div>
            </form>
        </div>
        <c:if test="${usuario.tipoUsuario eq 'ADMINISTRADOR'}">
            <div class="container mt-2">
                <div class="row">
                    <form method="POST" action="EnviarEmail">
                        <input type="hidden" value="sanciones" name="sanciones"/>
                        <p>Enviar avisos a Socios con préstamos en PENDIENTE: <button type="submit" class="btn btn-danger">Enviar Avisos</button></p>
                    </form>
                </div>
                <div class="row">
                    <button id="botonMostrarOcultar" class="btn btn-info">Mostrar Prestamos x Libro</button>

                    <div class="col-md-12" id="divOculto" style="display: none;">
                        <h2 class="mb-2">Cantidad Prestamos por Libro Actuales</h2>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Título Libro</th>
                                    <th>Cantidad de Préstamos</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="entry" items="${prestamosPorLibro}">
                                    <tr>
                                        <td>${entry.key}</td>
                                        <td>${entry.value}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:if> 

        <!-- Contenido de la página -->
        <div class="container mt-5">
            <div class="row">
                <div class="col-md-12">
                    <h2 class="mb-4">Historial de Préstamos</h2>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Libro</th>
                                <th>Usuario</th>
                                <th>Fecha de Préstamo</th>
                                <th>Fecha de Devolución</th>
                                <th>Estado Actual</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="prestamo" items="${prestamos}">
                                <tr>
                                    <td>${prestamo.libro.titulo}</td>
                                    <td>${prestamo.usuario.nombre}</td>
                                    <td><fmt:formatDate value="${prestamo.fechaPrestamo}" type="date" /></td>
                                    <td><fmt:formatDate value="${prestamo.fechaDevolucion}" type="date" /></td>
                                    <td>${prestamo.estadoPrestamo}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <figure class="highcharts-figure">
                <div id="container"></div>
                <div id="sliders">
                    <table>
                        <tr>
                            <td><label for="alpha">Alpha Angle</label></td>
                            <td><input id="alpha" type="range" min="0" max="45" value="15"/> <span id="alpha-value" class="value"></span></td>
                        </tr>
                        <tr>
                            <td><label for="beta">Beta Angle</label></td>
                            <td><input id="beta" type="range" min="-45" max="45" value="15"/> <span id="beta-value" class="value"></span></td>
                        </tr>
                        <tr>
                            <td><label for="depth">Depth</label></td>
                            <td><input id="depth" type="range" min="20" max="100" value="50"/> <span id="depth-value" class="value"></span></td>
                        </tr>
                    </table>
                </div>
            </figure>
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
        <!-- Scripts para gráfica -->
        <script src="https://code.highcharts.com/highcharts.js"></script>
        <script src="https://code.highcharts.com/highcharts-3d.js"></script>
        <script src="https://code.highcharts.com/modules/exporting.js"></script>
        <script src="https://code.highcharts.com/modules/export-data.js"></script>
        <script src="https://code.highcharts.com/modules/accessibility.js"></script>
        <script>
            // Script para los datos de la grafica
            var categorias = [
            <c:forEach items="${titulosLibros}" var="tituloLibro" varStatus="status">
            '${tituloLibro}'
                <c:if test="${!status.last}">
            ,
                </c:if>
            </c:forEach>
            ];
            var datos = [
            <c:forEach items="${cantidadPrestamos}" var="cantidadPrestamo" varStatus="status">
                ${cantidadPrestamo}
                <c:if test="${!status.last}">
            ,
                </c:if>

            </c:forEach>
            ];
            console.log(categorias);
            console.log(datos);

            // Set up the chart
            const chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'container',
                    type: 'column',
                    options3d: {
                        enabled: true,
                        alpha: 15,
                        beta: 15,
                        depth: 50,
                        viewDistance: 25
                    }
                },
                xAxis: {
                    categories: categorias
                },
                yAxis: {
                    title: {
                        enabled: false
                    }
                },
                tooltip: {
                    headerFormat: '<b>{point.key}</b><br>',
                    pointFormat: 'Notas: {point.y}'
                },
                title: {
                    text: 'Cantidad de libros prestados en el rango de fechas seleccionado',
                    align: 'left'
                },
                subtitle: {
                    text: 'Cantidad de libros prestados',
                    align: 'left'
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        depth: 25
                    }
                },
                series: [{
                        data: datos,
                        colorByPoint: true
                    }]
            });

            function showValues() {
                document.getElementById('alpha-value').innerHTML = chart.options.chart.options3d.alpha;
                document.getElementById('beta-value').innerHTML = chart.options.chart.options3d.beta;
                document.getElementById('depth-value').innerHTML = chart.options.chart.options3d.depth;
            }

            // Activate the sliders
            document.querySelectorAll('#sliders input').forEach(input => input.addEventListener('input', e => {
                    chart.options.chart.options3d[e.target.id] = parseFloat(e.target.value);
                    showValues();
                    chart.redraw(false);
                }));

            showValues();

            // Obtener el botón y el div con el texto
            const botonMostrarOcultar = document.getElementById('botonMostrarOcultar');
            const divOculto = document.getElementById('divOculto');

            // Agregar un evento de clic al botón
            botonMostrarOcultar.addEventListener('click', function () {
                // Alternar la visibilidad del div de texto
                if (divOculto.style.display === 'none') {
                    divOculto.style.display = 'block';
                } else {
                    divOculto.style.display = 'none';
                }
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
