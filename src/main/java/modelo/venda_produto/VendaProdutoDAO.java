package modelo.venda_produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.ConnectionFactory;

public class VendaProdutoDAO {

    public void inserir(VendaProduto vendaProduto) throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO venda_produto (venda_id, produto_id, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, vendaProduto.getVendaId());
        ps.setInt(2, vendaProduto.getProdutoId());
        ps.setInt(3, vendaProduto.getQuantidade());
        ps.setDouble(4, vendaProduto.getValorUnitario());

        ps.executeUpdate();

        ps.close();
        conn.close();
    }
}