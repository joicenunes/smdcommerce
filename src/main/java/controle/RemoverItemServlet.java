package controle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.produto.Produto;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/remover-item")
public class RemoverItemServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");

        if (carrinho != null) {
            try {
                int produtoId = Integer.parseInt(request.getParameter("produtoId"));

                // Cria um produto temporário apenas com o ID para a comparação.
                // Isso funciona porque Produto.equals() compara apenas pelo ID.
                Produto produtoParaRemover = new Produto();
                produtoParaRemover.setId(produtoId);

                // CORREÇÃO: Usa removeAll para remover TODAS as ocorrências do produto.
                // O método Collections.singletonList() cria uma pequena lista contendo
                // apenas o nosso produto temporário, que será usada como referência para a remoção.
                boolean removido = carrinho.removeAll(Collections.singletonList(produtoParaRemover));

                if (removido) {
                    session.setAttribute("carrinho", carrinho);
                    response.setStatus(HttpServletResponse.SC_OK); // Responde com sucesso
                } else {
                    // Isso pode acontecer se o item já foi removido em outra aba, por exemplo.
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }

            } catch (NumberFormatException e) {
                System.err.println("ID do produto inválido: " + request.getParameter("produtoId"));
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}