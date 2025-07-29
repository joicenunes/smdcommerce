package modelo.produto;

import static config.Config.JDBC_DRIVER;
import static config.Config.JDBC_SENHA;
import static config.Config.JDBC_URL;
import static config.Config.JDBC_USUARIO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.categoria.Categoria;

public class ProdutoDAO {

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar o driver JDBC", e);
        }
    }

    /**
     * Método auxiliar para extrair um objeto Produto do ResultSet.
     * Isso evita duplicação de código nos métodos de listagem.
     */
    private static Produto extrairProdutoDoResultSet(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setPreco(rs.getBigDecimal("preco"));
        produto.setFotoBytes(rs.getBytes("foto"));
        produto.setQuantidade(rs.getInt("quantidade"));

        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("categoria_id"));
        categoria.setDescricao(rs.getString("categoria_descricao"));
        produto.setCategoria(categoria);

        return produto;
    }

    public List<Produto> obterTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.id, p.descricao, p.preco, p.foto, p.quantidade, c.id as categoria_id, c.descricao as categoria_descricao " +
                "FROM Produto p INNER JOIN Categoria c ON p.categoria_id = c.id ORDER BY p.id";

        try (Connection c = DriverManager.getConnection(JDBC_URL, JDBC_USUARIO, JDBC_SENHA);
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                produtos.add(extrairProdutoDoResultSet(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao listar produtos.", ex);
        }
        return produtos;
    }

    public static List<Produto> listarDisponiveis() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.id, p.descricao, p.preco, p.foto, p.quantidade, c.id as categoria_id, c.descricao as categoria_descricao " +
                "FROM Produto p INNER JOIN Categoria c ON p.categoria_id = c.id WHERE p.quantidade > 0 ORDER BY p.id";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USUARIO, JDBC_SENHA);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(extrairProdutoDoResultSet(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao listar produtos disponíveis.", ex);
        }
        return produtos;
    }

    public Produto obterPorId(int id) {
        String sql = "SELECT p.id, p.descricao, p.preco, p.foto, p.quantidade, c.id as categoria_id, c.descricao as categoria_descricao " +
                "FROM Produto p INNER JOIN Categoria c ON p.categoria_id = c.id WHERE p.id = ?";
        try (Connection c = DriverManager.getConnection(JDBC_URL, JDBC_USUARIO, JDBC_SENHA);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extrairProdutoDoResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao obter produto por ID.", ex);
        }
        return null;
    }

    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produto (descricao, preco, quantidade, foto, categoria_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DriverManager.getConnection(JDBC_URL, JDBC_USUARIO, JDBC_SENHA);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, produto.getDescricao());
            ps.setBigDecimal(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidade());
            ps.setBytes(4, produto.getFotoBytes());
            ps.setInt(5, produto.getCategoria().getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produto SET descricao = ?, preco = ?, quantidade = ?, categoria_id = ? " +
                (produto.getFotoBytes() != null && produto.getFotoBytes().length > 0 ? ", foto = ? " : "") +
                "WHERE id = ?";
        try (Connection c = DriverManager.getConnection(JDBC_URL, JDBC_USUARIO, JDBC_SENHA);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, produto.getDescricao());
            ps.setBigDecimal(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidade());
            ps.setInt(4, produto.getCategoria().getId());

            int proximoParametro = 5;
            if (produto.getFotoBytes() != null && produto.getFotoBytes().length > 0) {
                ps.setBytes(proximoParametro++, produto.getFotoBytes());
            }
            ps.setInt(proximoParametro, produto.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (Connection c = DriverManager.getConnection(JDBC_URL, JDBC_USUARIO, JDBC_SENHA);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Adicione este método para relatório de estoque
    public List<Produto> listarPorEstoqueDesc() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.id, p.descricao, p.preco, p.foto, p.quantidade, c.id as categoria_id, c.descricao as categoria_descricao " +
                "FROM Produto p INNER JOIN Categoria c ON p.categoria_id = c.id ORDER BY p.quantidade, p.descricao ASC";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USUARIO, JDBC_SENHA);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(extrairProdutoDoResultSet(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao listar produtos por estoque.", ex);
        }
        return produtos;
    }
}