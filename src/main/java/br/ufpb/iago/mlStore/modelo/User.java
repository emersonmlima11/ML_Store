package br.ufpb.iago.mlStore.modelo;

public abstract class User {
    private int id;
    private String nomeCompleto;
    private String password;
    private Endereco endereco;


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
}