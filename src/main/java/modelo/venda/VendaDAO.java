package modelo.venda;

import modelo.usuario.Usuario;
import modelo.produto.Produto;
import modelo.venda_produto.VendaProduto;
import modelo.venda_produto.VendaProdutoDAO;
import util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class VendaDAO {
    public boolean inserir(Venda venda, List<VendaProduto> itens) {
        String sql = "INSERT INTO venda (data_hora, usuario_id, valor_total) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(venda.getData_hora()));
            ps.setInt(2, venda.getUsuario() != null ? venda.getUsuario().getId() : 0);
            ps.setBigDecimal(3, venda.getValor_total());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int vendaId = rs.getInt(1);
                        venda.setId(vendaId);
                        VendaProdutoDAO vendaProdutoDAO = new VendaProdutoDAO();
                        for (VendaProduto vp : itens) {
                            vp.setVenda(venda);
                            vendaProdutoDAO.inserir(vp);
                        }
                      }
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<Venda> listarPorUsuario(int usuarioId) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT id, data_hora, usuario_id, valor_total FROM venda WHERE usuario_id = ? ORDER BY data_hora DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venda venda = new Venda();
                    venda.setId(rs.getInt("id"));
                    venda.setData_hora(rs.getTimestamp("data_hora").toLocalDateTime());
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario_id"));
                    venda.setUsuario(usuario);
                    venda.setValor_total(rs.getBigDecimal("valor_total"));
                    VendaProdutoDAO vendaProdutoDAO = new VendaProdutoDAO();
                    venda.setItens(vendaProdutoDAO.listarPorVenda(venda.getId()));
                    vendas.add(venda);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vendas;
    }

    public List<Venda> listarTodas() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT v.id, v.data_hora, v.usuario_id, v.valor_total, u.nome, u.email " +
                     "FROM venda v INNER JOIN usuario u ON v.usuario_id = u.id ORDER BY v.data_hora DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getInt("id"));
                venda.setData_hora(rs.getTimestamp("data_hora").toLocalDateTime());
                venda.setValor_total(rs.getBigDecimal("valor_total"));
                modelo.usuario.Usuario usuario = new modelo.usuario.Usuario();
                usuario.setId(rs.getInt("usuario_id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                venda.setUsuario(usuario);
                // Busca os itens da venda
                VendaProdutoDAO vendaProdutoDAO = new VendaProdutoDAO();
                venda.setItens(vendaProdutoDAO.listarPorVenda(venda.getId()));
                vendas.add(venda);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vendas;
    }

    // Adicione este método para relatório dos mais vendidos
    public List<Produto> listarProdutosMaisVendidos(int limite) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.id, p.descricao, SUM(vp.quantidade) as total_vendido, SUM(vp.preco * vp.quantidade) as receita_total " +
                     "FROM venda_produto vp INNER JOIN produto p ON vp.produto_id = p.id " +
                     "GROUP BY p.id, p.descricao ORDER BY total_vendido DESC LIMIT ?";
        try (Connection conn = util.ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setQuantidade(rs.getInt("total_vendido"));
                    produto.setPreco(rs.getBigDecimal("receita_total"));
                    produtos.add(produto);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return produtos;
    }
}
