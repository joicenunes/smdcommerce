package modelo.venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import modelo.venda_produto.VendaProduto;
import util.ConnectionFactory; // Importa a sua classe de conexão

public class VendaDAO {

    /**
     * Insere uma venda no banco de dados e retorna o objeto Venda com o ID gerado.
     */
    public Venda inserir(Venda venda) throws SQLException {
        // Verifique se o nome da coluna 'valor' está correto de acordo com sua tabela
        String sql = "INSERT INTO venda (data_hora, usuario_id, valor) VALUES (?, ?, ?)";

        // Usa a sua ConnectionFactory para obter a conexão
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(venda.getData_hora()));
            stmt.setInt(2, venda.getUsuario().getId());
            stmt.setBigDecimal(3, venda.getValorTotal()); // O valor a ser inserido

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    venda.setId(rs.getInt(1));
                }
            }
        }
        return venda;
    }

    /**
     * Insere um item (produto) de uma venda na tabela de associação.
     */
    public void inserirVendaProduto(VendaProduto vendaProduto) throws SQLException {
        String sql = "INSERT INTO venda_produto (venda_id, produto_id, quantidade, preco) VALUES (?, ?, ?, ?)";

        // Usa a sua ConnectionFactory para obter a conexão
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendaProduto.getVenda().getId());
            stmt.setInt(2, vendaProduto.getProduto().getId());
            stmt.setInt(3, vendaProduto.getQuantidade());
            stmt.setBigDecimal(4, vendaProduto.getPreco());

            stmt.executeUpdate();
        }
    }
}