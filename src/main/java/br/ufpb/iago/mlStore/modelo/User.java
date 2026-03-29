package br.ufpb.iago.mlStore.modelo;

public abstract class User {
    //private int id;
    private String nomeCompleto;
    private String password;
    private Endereco endereco;
    private String email;

    public User(String nomeCompleto, String email, String password, Endereco endereco){
        this.nomeCompleto = nomeCompleto;
        this.password = password;
        this.endereco = endereco;
        this.email = email;
    }

    //public int getId() { return id; }
    //public void setId(int id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}