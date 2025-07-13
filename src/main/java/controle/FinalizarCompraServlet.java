package controle;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime; // 1. Importar LocalDateTime
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.produto.Produto;
import modelo.usuario.Usuario;
import modelo.venda.Venda;
import modelo.venda.VendaDAO;
import modelo.venda_produto.VendaProduto; // 2. Corrigir o pacote se necessário

@WebServlet("/finalizar-compra")
public class FinalizarCompraServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");

        if (usuario == null) {
            response.sendRedirect("login.jsp?destino=carrinho.jsp");
            return;
        }

        if (carrinho == null || carrinho.isEmpty()) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            BigDecimal valorTotal = carrinho.stream()
                    .map(Produto::getPreco)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Venda venda = new Venda();
            // 3. CORREÇÃO: Usar os métodos que realmente existem na classe Venda
            venda.setUsuario(usuario); // Passa o objeto Usuario inteiro
            venda.setData_hora(LocalDateTime.now()); // Usa LocalDateTime
            venda.setValorTotal(valorTotal);

            VendaDAO vendaDAO = new VendaDAO();
            // Assume que o método inserir retorna a Venda com o ID preenchido
            venda = vendaDAO.inserir(venda);

            // 4. CORREÇÃO: Passar os objetos Venda e Produto para VendaProduto
            for (Produto produto : carrinho) {
                VendaProduto vp = new VendaProduto();
                vp.setVenda(venda); // Passa o objeto Venda
                vp.setProduto(produto); // Passa o objeto Produto
                vp.setQuantidade(1);
                vp.setPreco(produto.getPreco()); // Usa o método setPreco
                vendaDAO.inserirVendaProduto(vp); // Assume que existe um método para isso
            }

            session.removeAttribute("carrinho");
            request.setAttribute("mensagem", "Compra finalizada com sucesso! ID da Venda: " + venda.getId());
            request.getRequestDispatcher("/WEB-INF/views/confirmacao-compra.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao finalizar a compra: " + e.getMessage());
            request.getRequestDispatcher("carrinho.jsp").forward(request, response);
        }
    }
}