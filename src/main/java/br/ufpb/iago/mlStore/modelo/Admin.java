package br.ufpb.iago.mlStore.modelo;

public class Admin extends User {
    private String codigoDeAcesso;

    public Admin(String nomeCompleto, String email, String password, Endereco endereco, String codigoDeAcesso){
        super(nomeCompleto, email, password, endereco);
        this.codigoDeAcesso = codigoDeAcesso;
    }

    public Admin(){
        this("", "", "", new Endereco(), "");
    }


    public String getCodigoDeAcesso() { return codigoDeAcesso; }
    public void setCodigoDeAcesso(String codigoDeAcesso) { this.codigoDeAcesso = codigoDeAcesso; }
}