<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmação de Compra</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
        .container { border: 1px solid #d4edda; background-color: #f0fff4; color: #155724; padding: 30px; border-radius: 8px; display: inline-block; }
        h1 { color: #155724; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>

    <div class="container">
        <h1>Compra Realizada com Sucesso!</h1>
        <p>Obrigado por sua preferência.</p>

        <%-- Este bloco exibe os detalhes da venda se o objeto 'venda' for passado pelo servlet --%>
        <c:if test="${not empty venda}">
            <hr>
            <p><strong>ID da Venda:</strong> ${venda.id}</p>
            <p><strong>Valor Total:</strong>
                <fmt:setLocale value="pt_BR"/>
                <%-- Alteração aqui: de 'venda.valorTotal' para 'venda.valor' --%>
                <fmt:formatNumber value="${venda.valor}" type="currency"/>
            </p>
        </c:if>

        <br>
        <p><a href="${pageContext.request.contextPath}">Voltar para a Página Inicial</a></p>
    </div>

</body>
</html>