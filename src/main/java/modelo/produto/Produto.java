package modelo.produto;

import java.math.BigDecimal;
import java.util.Objects;
import modelo.categoria.Categoria;

public class Produto {
	private int id;
	private String nome; // Alterado de 'descricao' para 'nome'
	private BigDecimal preco;
	private byte[] fotoBytes;
	private int quantidade;
	private Categoria categoria;

	// Getters e Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	// Getter e Setter para 'nome'
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	public byte[] getFotoBytes() { return fotoBytes; }
	public void setFotoBytes(byte[] fotoBytes) { this.fotoBytes = fotoBytes; }
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Produto produto = (Produto) o;
		return id == produto.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}