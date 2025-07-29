package controle.admin;

import modelo.produto.Produto;
import modelo.produto.ProdutoDAO;
import modelo.usuario.Usuario;
import modelo.usuario.UsuarioDAO;
import modelo.venda.VendaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/relatorios")
public class AdminRelatorioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        modelo.usuario.Usuario usuario = (session != null) ? (modelo.usuario.Usuario) session.getAttribute("usuario") : null;
        if (usuario == null || !usuario.isAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String tipo = request.getParameter("tipo");
        if (tipo == null) {
            // Página principal de relatórios
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/relatorios.jsp");
            rd.forward(request, response);
            return;
        }

        switch (tipo) {
            case "produtos":
                getProdutosMaisVendidos(request, response);
                break;
            case "estoque":
                getProdutosPorEstoque(request, response);
                break;
            case "clientes":
                getClientesMaisCompraram(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void getProdutosMaisVendidos(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        VendaDAO vendaDAO = new VendaDAO();
        List<Produto> produtosMaisVendidos = vendaDAO.listarProdutosMaisVendidos(10);
        request.setAttribute("produtosMaisVendidos", produtosMaisVendidos);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/relatorio-produtos.jsp");
        rd.forward(request, response);
    }

    private void getProdutosPorEstoque(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtosEstoque = produtoDAO.listarPorEstoqueDesc();
        request.setAttribute("produtosEstoque", produtosEstoque);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/relatorio-estoque.jsp");
        rd.forward(request, response);
    }

    private void getClientesMaisCompraram(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> clientesMaisCompraram = usuarioDAO.listarTop20ClientesMaisCompraram();
        System.out.println(clientesMaisCompraram.size());
        request.setAttribute("clientesMaisCompraram", clientesMaisCompraram);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/relatorio-clientes.jsp");
        rd.forward(request, response);
    }
}
