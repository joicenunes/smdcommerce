package modelo.usuario;

import java.math.BigDecimal;

/**
 *
 * @author Leonardo Oliveira Moreira
 * 
 * Classe que representa a entidade usuário
 */
public class Usuario {
 
    private int id;
    private String nome;
    private String endereco;
    private String email;
    private String login;
    private String senha;
    private String image;
    private boolean administrador;
    private int quantidadePedidos;
    private BigDecimal valorTotalComprado;
    
    public Usuario() {}
    
    public Usuario(String nome, String endereco, String email, String login, String senha) {
    	this.nome = nome;
    	this.endereco = endereco;
    	this.email = email;
    	this.login = login;
    	this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public int getQuantidadePedidos() {
        return quantidadePedidos;
    }

    public void setQuantidadePedidos(int quantidadePedidos) {
        this.quantidadePedidos = quantidadePedidos;
    }

    public java.math.BigDecimal getValorTotalComprado() {
        return valorTotalComprado;
    }

    public void setValorTotalComprado(java.math.BigDecimal valorTotalComprado) {
        this.valorTotalComprado = valorTotalComprado;
    }
    
}
