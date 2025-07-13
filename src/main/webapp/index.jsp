<%@page import="modelo.usuario.Usuario"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Beleza Radiante</title>
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600;700&family=Roboto:wght@300;400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="styles/index.css">
    <style>
        .product-list {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
        }
        .product-card {
            border: 1px solid #ddd;
            padding: 15px;
            text-align: center;
        }
        .product-card img {
            max-width: 100%;
            height: 150px;
            object-fit: cover;
            margin-bottom: 10px;
        }
        .product-card h3 {
            font-size: 1.1em;
            margin: 10px 0;
        }
        .product-card .price {
            font-size: 1.2em;
            font-weight: bold;
            color: #333;
        }
    </style>
</head>
<body>
    <header>
        <h1>Beleza Radiante</h1>
    </header>

    <nav>
        <a href="${pageContext.request.contextPath}/carrinho.jsp">Meu Carrinho</a>
    </nav>

    <div class="container">
        <div class="main-content">
            <h2>Produtos Disponíveis</h2>
            <div class="product-list">
                <c:if test="${empty listaProdutos}">
                    <p>Não há produtos disponíveis no momento.</p>
                </c:if>
                <c:forEach var="produto" items="${listaProdutos}">
                    <div class="product-card">
                        <c:if test="${not empty produto.fotoBytes}">
                            <%-- Correção: trocado 'produto.descricao' por 'produto.nome' --%>
                            <img src="${pageContext.request.contextPath}/imagem?id=${produto.id}" alt="<c:out value='${produto.nome}'/>">
                        </c:if>
                        <%-- Correção: trocado 'produto.descricao' por 'produto.nome' --%>
                        <h3><c:out value="${produto.nome}" /></h3>
                        <p class="price">
                            <fmt:setLocale value="pt_BR"/>
                            <fmt:formatNumber value="${produto.preco}" type="currency"/>
                        </p>
                        <button onclick="adicionarAoCarrinho(${produto.id})">Adicionar ao Carrinho</button>
                    </div>
                </c:forEach>
            </div>
        </div>

        <aside class="sidebar">
        <%
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
        %>
            <div id="login-area">
                <h3>Login</h3>
                <form id="login-form" action="<%= request.getContextPath() %>/Login" method="post">
                    <label for="login">E-mail ou usuário:</label>
                    <input type="text" id="login" name="login" required placeholder="Digite seu e-mail ou usuário">

                    <label for="senha">Senha:</label>
                    <input type="password" id="senha" name="senha" required placeholder="Digite sua senha">

                     <div>
                         <input type="checkbox" id="lembrar" name="lembrar">
                         <label for="lembrar">Lembrar-se de mim</label>
                     </div>


                    <button type="submit" value="">Entrar</button>
                    <a href="<%= request.getContextPath() %>/cadastro.jsp">Não tem conta? Cadastre-se</a>
                </form>
                <div id="mensagem"></div>
            </div>
        <%
            } else {
        %>
                <jsp:include page="/WEB-INF/views/menu-usuario.jsp" />
        <% } %>

        </aside>
    </div>

    <footer>
        <p>&copy; 2025 Beleza Radiante - Todos os direitos reservados</p>
    </footer>

    <script>
        // CORREÇÃO: Função implementada para enviar o ID do produto para o servlet
        function adicionarAoCarrinho(id) {
            const button = event.target;
            const originalText = button.textContent;
            button.disabled = true;
            button.textContent = 'Adicionando...';

            fetch('${pageContext.request.contextPath}/adicionar-carrinho', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'id=' + id
            })
            .then(response => {
                if (response.ok) {
                    button.textContent = 'Adicionado!';
                    setTimeout(() => {
                        button.textContent = originalText;
                        button.disabled = false;
                    }, 1500);
                } else {
                    alert('Erro ao adicionar o produto ao carrinho.');
                    button.textContent = originalText;
                    button.disabled = false;
                }
            })
            .catch(error => {
                console.error('Erro na requisição:', error);
                alert('Ocorreu um erro de comunicação.');
                button.textContent = originalText;
                button.disabled = false;
            });
        }

        if (document.getElementById('login-form')) {
            document.getElementById('login-form').addEventListener('submit', function(event) {
                event.preventDefault();

                var form = event.target;
                var formData = new FormData(form);
                var btn = form.querySelector('button[type="submit"]');
                var originalText = btn.textContent;
                btn.disabled = true;
                btn.textContent = 'Entrando...';
                document.getElementById('mensagem').innerHTML = '';

                fetch('<%= request.getContextPath() %>/Login', {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    }
                })
                .then(response => response.text())
                .then(html => {
                    if (html.includes('id="menu-usuario"')) {
                        const loginArea = document.getElementById('login-area');
                        if (loginArea) loginArea.remove();

                        const oldMenu = document.getElementById('menu-area') || document.getElementById('menu-usuario');
                        if (oldMenu) oldMenu.remove();

                        const aside = document.querySelector('.sidebar');
                        const div = document.createElement('div');
                        div.id = 'menu-area';
                        div.innerHTML = html;
                        aside.appendChild(div);
                    } else {
                        document.getElementById('mensagem').innerHTML = html;
                        btn.disabled = false;
                        btn.textContent = originalText;
                    }
                })
                .catch(err => {
                    console.log(err);
                    btn.disabled = false;
                    btn.textContent = originalText;
                });
            });
        }

        function logout(e) {
            e.preventDefault();
            fetch('${pageContext.request.contextPath}/logout')
                .then(() => {
                    window.location.reload(true);
                });
        }
    </script>

</body>
</html>