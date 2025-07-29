package modelo.usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.ConnectionFactory;

/**
 *
 * @author Leonardo Oliveira Moreira
 *
 * Classe que implementa o padrão DAO para a entidade usuário
 */
public class UsuarioDAO {

    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, login, senha, administrador, endereco) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getLogin());
            ps.setString(4, usuario.getSenha());
            ps.setBoolean(5, usuario.isAdministrador());
            ps.setString(6, usuario.getEndereco());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir usuário no banco de dados.", ex);
        }
    }

    public Usuario obter(String login, String senha) {
        Usuario usuario = null;
        String sql = "SELECT id, nome, endereco, email, login, senha, administrador FROM usuario WHERE login = ? AND senha = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEndereco(rs.getString("endereco"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setAdministrador(rs.getBoolean("administrador"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter usuário por login e senha.", e);
        }
        return usuario;
    }

    public Usuario obterPorId(int id) {
        Usuario usuario = null;
        String sql = "SELECT id, nome, endereco, email, login, administrador FROM usuario WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEndereco(rs.getString("endereco"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setAdministrador(rs.getBoolean("administrador"));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao obter usuário por ID.", ex);
        }
        return usuario;
    }

    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, endereco = ?, email = ?, login = ?, administrador = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEndereco());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getLogin());
            ps.setBoolean(5, usuario.isAdministrador());
            ps.setInt(6, usuario.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao atualizar o usuário.", ex);
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM usuario WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover o usuário.", e);
        }
    }

    public List<Usuario> obterTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nome, endereco, email, login, administrador FROM usuario";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEndereco(rs.getString("endereco"));
                usuario.setEmail(rs.getString("email"));
                usuario.setLogin(rs.getString("login"));
                usuario.setAdministrador(rs.getBoolean("administrador"));
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao obter todos os usuários.", ex);
        }
        return usuarios;
    }

    /**
     * Lista os 20 clientes que mais compraram, trazendo também a soma da coluna valor_total das vendas.
     */
    public List<Usuario> listarTop20ClientesMaisCompraram() {
        List<Usuario> clientes = new ArrayList<>();
        String sql = "SELECT u.id, u.nome, u.email, u.login, u.endereco, COUNT(v.id) as pedidos_realizados, SUM(v.valor_total) as valor_total " +
                     "FROM usuario u INNER JOIN venda v ON u.id = v.usuario_id " +
                     "GROUP BY u.id, u.nome, u.email, u.login, u.endereco " +
                     "ORDER BY pedidos_realizados DESC, valor_total DESC " +
                     "LIMIT 20";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setLogin(rs.getString("login"));
                usuario.setEndereco(rs.getString("endereco"));
                usuario.setQuantidadePedidos(rs.getInt("pedidos_realizados")); // Adicione getter/setter
                usuario.setValorTotalComprado(rs.getBigDecimal("valor_total")); // Adicione getter/setter
                clientes.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return clientes;
    }
}