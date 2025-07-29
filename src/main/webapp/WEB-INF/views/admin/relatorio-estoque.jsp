<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Estoque dos Produtos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="admin-container">
        <h1>Estoque dos Produtos</h1>
        <a href="${pageContext.request.contextPath}/admin/relatorios" class="back-link">Voltar</a>
        <table class="relatorio-table">
            <thead>
                <tr>
                    <th>Produto</th>
                    <th>Quantidade em Estoque</th>
                    <th>Categoria</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="produto" items="${produtosEstoque}">
                    <tr>
                        <td>${produto.descricao}</td>
                        <td>${produto.quantidade}</td>
                        <td>${produto.categoria.descricao}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>