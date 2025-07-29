<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Clientes que Mais Compraram</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="admin-container">
        <h1>Clientes que Mais Compraram</h1>
        <a href="${pageContext.request.contextPath}/admin/relatorios" class="back-link">Voltar</a>
        <table class="relatorio-table">
            <thead>
                <tr>
                    <th>Cliente</th>
                    <th>E-mail</th>
                    <th>Pedidos Realizados</th>
                    <th>Valor Total Comprado</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cliente" items="${clientesMaisCompraram}">
                    <tr>
                        <td>${cliente.nome}</td>
                        <td>${cliente.email}</td>
                        <td>${cliente.quantidadePedidos}</td>
                        <td>
                            <fmt:setLocale value="pt_BR"/>
                            <fmt:formatNumber value="${cliente.valorTotalComprado}" type="currency"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
