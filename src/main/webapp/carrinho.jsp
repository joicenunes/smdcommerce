<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="modelo.produto.Produto" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Base64" %>

<%
    List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");
    if (carrinho == null) {
        carrinho = new ArrayList<>();
        session.setAttribute("carrinho", carrinho);
    }

    BigDecimal total = BigDecimal.ZERO;
    Map<Produto, Integer> produtosAgrupados = new LinkedHashMap<>();
    for (Produto p : carrinho) {
        produtosAgrupados.put(p, produtosAgrupados.getOrDefault(p, 0) + 1);
    }

    for(Map.Entry<Produto, Integer> entry : produtosAgrupados.entrySet()){
        Produto p = entry.getKey();
        Integer quantidade = entry.getValue();
        if(p.getPreco() != null){
            total = total.add(p.getPreco().multiply(new BigDecimal(quantidade)));
        }
    }

    session.setAttribute("produtosAgrupados", produtosAgrupados);
    request.setAttribute("produtosAgrupados", produtosAgrupados);
    request.setAttribute("totalCarrinho", total);
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Carrinho de Compras - Loja Virtual</title>
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600;700&family=Roboto:wght@300;400;500&display=swap" rel="stylesheet"/>
    <style>
        :root { --primary-color: #2c3e50; --secondary-color: #3498db; --accent-color: #e74c3c; --light-color: #ecf0f1; --dark-color: #2c3e50; --success-color: #27ae60; --warning-color: #f39c12; --error-color: #e74c3c; --text-color: #333; --text-light: #7f8c8d; --border-radius: 8px; --box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); --transition: all 0.3s ease; }
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: "Open Sans", sans-serif; line-height: 1.6; color: var(--text-color); background: #f9f9f9; margin: 0; padding: 0; min-height: 100vh; display: flex; flex-direction: column; }
        header { background: var(--primary-color); color: #fff; padding: 1.5rem 2rem; text-align: center; box-shadow: var(--box-shadow); }
        header h1 { font-size: 2.2rem; margin: 0; font-weight: 700; letter-spacing: 1px; }
        main { flex: 1; display: flex; flex-direction: column; justify-content: center; padding: 2rem 0; }
        .container { max-width: 900px; width: 90%; margin: 0 auto; padding: 2rem; background: #fff; border-radius: var(--border-radius); box-shadow: var(--box-shadow); }
        h2 { color: var(--primary-color); text-align: center; margin-bottom: 2rem; font-size: 1.8rem; position: relative; padding-bottom: 0.5rem; }
        h2::after { content: ""; position: absolute; bottom: 0; left: 50%; transform: translateX(-50%); width: 60px; height: 3px; background: var(--secondary-color); }
        .cart-table { width: 100%; border-collapse: collapse; margin-bottom: 2rem; }
        .cart-table th, .cart-table td { padding: 1rem; text-align: left; border-bottom: 1px solid #eee; vertical-align: middle; }
        .cart-table th { background: var(--light-color); font-weight: 600; color: var(--dark-color); }
        .cart-table td.product-info { display: flex; align-items: center; gap: 15px; }
        .cart-table td.product-info img { width: 60px; height: 60px; object-fit: cover; border-radius: var(--border-radius); }
        .cart-table td.quantity input { width: 70px; padding: 0.5rem; text-align: center; border: 1px solid #ddd; border-radius: var(--border-radius); font-size: 1rem; }
        .cart-table td.quantity input:focus { outline: none; border-color: var(--secondary-color); }
        .cart-table td.actions .remove-btn { padding: 0.5rem 1rem; color: #fff; border: none; border-radius: var(--border-radius); cursor: pointer; font-size: 0.9rem; transition: var(--transition); background: var(--error-color); }
        .cart-table td.actions .remove-btn:hover { background: #c0392b; transform: translateY(-2px); }
        .cart-total { text-align: right; margin-bottom: 2rem; padding: 1rem; background: var(--light-color); border-radius: var(--border-radius); }
        .cart-total h3 { margin: 0; font-size: 1.4rem; color: var(--dark-color); }
        .cart-total h3 span { color: var(--accent-color); font-weight: 700; }
        .cart-actions { display: flex; justify-content: space-between; align-items: center; gap: 1rem; flex-wrap: wrap; }
        .cart-actions a, .cart-actions button { padding: 0.8rem 1.5rem; text-decoration: none; border-radius: var(--border-radius); cursor: pointer; font-size: 1rem; font-weight: 600; transition: var(--transition); text-align: center; flex: 1; min-width: 200px; }
        .continue-shopping { background: var(--warning-color); color: #fff; border: none; }
        .continue-shopping:hover { background: #e67e22; transform: translateY(-2px); }
        .checkout-button { background: var(--success-color); color: #fff; border: none; }
        .checkout-button:hover { background: #219653; transform: translateY(-2px); }
        .empty-cart { text-align: center; color: var(--text-light); padding: 3rem; font-size: 1.2rem; }
        .empty-cart a { display: inline-block; margin-top: 1rem; }
        footer { background: var(--dark-color); color: #fff; text-align: center; padding: 1.5rem; font-size: 0.9rem; margin-top: auto; }
        tr.removing { opacity: 0; transition: opacity 0.5s ease-out; }
    </style>
</head>
<body>
<header>
    <h1>Meu Carrinho de Compras</h1>
</header>
<main>
    <div class="container">
        <h2>Itens no seu Carrinho</h2>
        <div id="cart-container">
            <c:choose>
                <c:when test="${empty produtosAgrupados}">
                    <div class="empty-cart">
                        <p>Seu carrinho está vazio.</p>
                        <a href="${pageContext.request.contextPath}/principal" class="continue-shopping">Voltar para a loja</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <form id="cart-form" action="${pageContext.request.contextPath}/finalizar-compra" method="post">
                        <table class="cart-table">
                            <thead>
                            <tr>
                                <th>Produto</th>
                                <th>Preço</th>
                                <th>Quantidade</th>
                                <th>Subtotal</th>
                                <th>Ações</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${produtosAgrupados}" var="entry">
                                <tr data-produto-id-row="${entry.key.id}">
                                    <td class="product-info">
                                        <c:choose>
                                            <c:when test="${not empty entry.key.fotoBytes}">
                                                <%-- Acessa a variável do loop JSTL 'entry' dentro do scriptlet --%>
                                                <%
                                                    Produto p = (Produto) ((Map.Entry) pageContext.getAttribute("entry")).getKey();
                                                    String fotoBase64 = Base64.getEncoder().encodeToString(p.getFotoBytes());
                                                    pageContext.setAttribute("fotoBase64", fotoBase64);
                                                %>
                                                <img src="data:image/jpeg;base64,${fotoBase64}" alt="${entry.key.nome}">
                                            </c:when>
                                            <c:otherwise>
                                                <%-- Exibe uma imagem padrão se não houver foto --%>
                                                <img src="https://via.placeholder.com/60" alt="Sem imagem">
                                            </c:otherwise>
                                        </c:choose>
                                        <span>${entry.key.nome}</span>
                                    </td>
                                    <td><fmt:formatNumber value="${entry.key.preco}" type="currency" currencySymbol="R$ "/></td>
                                    <td class="quantity">
                                        <input type="number" class="quantidade-input" name="produto_${entry.key.id}" value="${entry.value}" min="0"
                                               data-produto-id="${entry.key.id}" data-old-value="${entry.value}" onchange="atualizarCarrinho(this)" onkeyup="atualizarCarrinho(this)">
                                    </td>
                                    <td class="subtotal">
                                        <fmt:formatNumber value="${entry.key.preco * entry.value}" type="currency" currencySymbol="R$ "/>
                                    </td>
                                    <td class="actions">
                                        <button type="button" class="remove-btn" onclick="removerItem(${entry.key.id})">Remover</button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <div class="cart-total">
                            <h3>Total: <span id="cart-total-value"><fmt:formatNumber value="${totalCarrinho}" type="currency" currencySymbol="R$ "/></span></h3>
                        </div>
                        <div class="cart-actions">
                            <a href="${pageContext.request.contextPath}/principal" class="continue-shopping">Continuar Comprando</a>
                            <button type="submit" class="checkout-button">Finalizar Compra</button>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>
<footer>
    <p>&copy; 2025 Minha Loja Virtual - Todos os direitos reservados</p>
</footer>

<script>
    function formatCurrency(value) {
        const numberValue = Number(value);
        if (isNaN(numberValue)) {
            return "R$ 0,00";
        }
        return numberValue.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    }

    let debounceTimer;
    function atualizarCarrinho(inputAlterado) {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            const form = document.getElementById('cart-form');
            if (!form) return;

            const quantidadeInputs = document.querySelectorAll('.quantidade-input');
            const payload = new URLSearchParams();
            quantidadeInputs.forEach(input => {
                const produtoId = input.dataset.produtoId;
                const quantidade = input.value;
                payload.append('produto_' + produtoId, quantidade);
            });

            fetch('${pageContext.request.contextPath}/atualizar-carrinho', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: payload.toString()
            })
            .then(response => {
                return response.text().then(text => {
                    if (!response.ok) {
                        try {
                            const errorData = JSON.parse(text);
                            throw new Error(errorData.error || 'Erro desconhecido do servidor.');
                        } catch (e) {
                            throw new Error(text || 'Erro ao processar a resposta do servidor.');
                        }
                    }
                    return JSON.parse(text);
                });
            })
            .then(data => {
                document.getElementById('cart-total-value').innerText = formatCurrency(data.novoTotal);

                quantidadeInputs.forEach(input => {
                    const produtoId = input.dataset.produtoId;
                    const row = document.querySelector(`tr[data-produto-id-row='${produtoId}']`);
                    if (!row) return;

                    input.setAttribute('data-old-value', input.value);

                    if (parseInt(input.value, 10) <= 0) {
                        row.classList.add('removing');
                        setTimeout(() => row.remove(), 500);
                    } else {
                        const subtotalCell = row.querySelector('.subtotal');
                        if (subtotalCell && data.subtotais[produtoId] !== undefined) {
                            subtotalCell.innerText = formatCurrency(data.subtotais[produtoId]);
                        }
                    }
                });

                if (data.carrinhoVazio) {
                    const cartContainer = document.getElementById('cart-container');
                    setTimeout(() => {
                         cartContainer.innerHTML = `
                        <div class="empty-cart">
                            <p>Seu carrinho está vazio.</p>
                            <a href="${pageContext.request.contextPath}/principal" class="continue-shopping">Voltar para a loja</a>
                        </div>`;
                    }, 500);
                }
            })
            .catch(error => {
                console.error('Erro na requisição:', error);
                alert('Erro ao atualizar o carrinho: ' + error.message);
                if (inputAlterado) {
                    inputAlterado.value = inputAlterado.getAttribute('data-old-value');
                }
            });
        }, 300);
    }

    function removerItem(produtoId) {
        const inputQuantidade = document.querySelector(`.quantidade-input[data-produto-id='${produtoId}']`);
        if (inputQuantidade) {
            inputQuantidade.value = 0;
            atualizarCarrinho(inputQuantidade);
        } else {
            console.error(`Não foi possível encontrar o input para o produto ID: ${produtoId}`);
            alert('Erro: Não foi possível encontrar o item para remover.');
        }
    }
</script>

</body>
</html>