package controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.produto.Produto;
import modelo.produto.ProdutoDAO;

@WebServlet("/adicionar-carrinho")
public class AdicionarCarrinhoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int produtoId = Integer.parseInt(request.getParameter("id"));
            HttpSession session = request.getSession();

            List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");
            if (carrinho == null) {
                carrinho = new ArrayList<>();
            }

            ProdutoDAO produtoDAO = new ProdutoDAO();
            // CORREÇÃO: Usando o método correto "obterPorId" que existe em ProdutoDAO
            Produto produto = produtoDAO.obterPorId(produtoId);

            if (produto != null) {
                carrinho.add(produto);
                session.setAttribute("carrinho", carrinho);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                System.err.println("Produto com ID " + produtoId + " não encontrado no banco de dados.");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Produto não encontrado.");
            }

        } catch (Exception e) {
            System.err.println("Erro em AdicionarCarrinhoServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao adicionar produto ao carrinho.");
        }
    }
}