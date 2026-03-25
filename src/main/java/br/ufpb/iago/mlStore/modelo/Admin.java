package br.ufpb.iago.mlStore.modelo;

public class Admin extends User {
    private String codigoDeAcesso;

    public String getCodigoDeAcesso() { return codigoDeAcesso; }
    public void setCodigoDeAcesso(String codigoDeAcesso) { this.codigoDeAcesso = codigoDeAcesso; }
}