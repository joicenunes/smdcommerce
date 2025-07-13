package controle;

import modelo.produto.Produto;
import modelo.produto.ProdutoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/atualizar-carrinho")
public class AtualizarCarrinhoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<Produto> novoCarrinho = new ArrayList<>();
            ProdutoDAO produtoDAO = new ProdutoDAO();
            Map<Integer, BigDecimal> subtotais = new HashMap<>();
            BigDecimal novoTotal = BigDecimal.ZERO;

            // Itera sobre os parâmetros da requisição para encontrar os produtos e suas quantidades
            for (String paramName : Collections.list(request.getParameterNames())) {
                if (paramName.startsWith("produto_")) {
                    int produtoId = Integer.parseInt(paramName.substring("produto_".length()));
                    int novaQuantidade = Integer.parseInt(request.getParameter(paramName));

                    if (novaQuantidade < 0) {
                        continue; // Ignora quantidades negativas
                    }

                    Produto produto = produtoDAO.obterPorId(produtoId);
                    if (produto == null) {
                        continue; // Se o produto não existe, pula para o próximo
                    }

                    // Verifica se a quantidade desejada está disponível no estoque
                    if (novaQuantidade > produto.getQuantidade()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        // Constrói a resposta de erro JSON manualmente
                        out.print("{\"error\": \"Estoque insuficiente para o produto '" + produto.getNome().replace("\"", "\\\"") + "'. Disponível: " + produto.getQuantidade() + "\"}");
                        out.flush();
                        return; // Interrompe a execução
                    }

                    // Adiciona o produto ao novo carrinho a quantidade de vezes especificada
                    for (int i = 0; i < novaQuantidade; i++) {
                        novoCarrinho.add(produto);
                    }

                    // Calcula o subtotal para este produto e o adiciona ao total geral
                    if (novaQuantidade > 0) {
                        BigDecimal subtotalItem = produto.getPreco().multiply(new BigDecimal(novaQuantidade));
                        subtotais.put(produtoId, subtotalItem);
                        novoTotal = novoTotal.add(subtotalItem);
                    }
                }
            }

            session.setAttribute("carrinho", novoCarrinho);

            // Constrói a resposta JSON de sucesso manualmente
            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("{");
            jsonResponse.append("\"novoTotal\": ").append(novoTotal).append(",");
            jsonResponse.append("\"carrinhoVazio\": ").append(novoCarrinho.isEmpty()).append(",");

            // Constrói o objeto de subtotais
            String subtotaisJson = subtotais.entrySet()
                    .stream()
                    .map(entry -> "\"" + entry.getKey() + "\": " + entry.getValue())
                    .collect(Collectors.joining(","));
            jsonResponse.append("\"subtotais\": {").append(subtotaisJson).append("}");

            jsonResponse.append("}");

            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonResponse.toString());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // Constrói a resposta de erro JSON manualmente
            out.print("{\"error\": \"Ocorreu um erro interno no servidor: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        } finally {
            out.flush();
        }
    }
}