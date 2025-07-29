<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Relatórios - Administração</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="admin-container">
        <h1>Relatórios do E-commerce</h1>
        <a href="${pageContext.request.contextPath}/" class="back-link">Voltar para a Página Inicial</a>
        <ul style="margin-top:2rem;">
            <li><a href="${pageContext.request.contextPath}/admin/relatorios?tipo=produtos">Produtos Mais Vendidos</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/relatorios?tipo=clientes">Clientes que Mais Compraram</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/relatorios?tipo=estoque">Estoque de Produtos</a></li>
        </ul>
    </div>
</body>
</html>