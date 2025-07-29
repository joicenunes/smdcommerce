<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Produtos Mais Vendidos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="admin-container">
        <h1>Produtos Mais Vendidos</h1>
        <a href="${pageContext.request.contextPath}/admin/relatorios" class="back-link">Voltar</a>
        <table class="relatorio-table">
            <thead>
                <tr>
                    <th>Produto</th>
                    <th>Quantidade Vendida</th>
                    <th>Receita Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="produto" items="${produtosMaisVendidos}">
                    <tr>
                        <td>${produto.descricao}</td>
                        <td>${produto.quantidade}</td>
                        <td>
                            <fmt:setLocale value="pt_BR"/>
                            <fmt:formatNumber value="${produto.preco}" type="currency"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
