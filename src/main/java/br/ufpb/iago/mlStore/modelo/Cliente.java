package br.ufpb.iago.mlStore.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends User {
    private String cpf;
    private List<Pedido> historicoDeCompras;

    public Cliente(String nomeCompleto, String email, String password, Endereco endereco, String cpf){
        super(nomeCompleto, email, password, endereco);
        this.cpf = cpf;
        this.historicoDeCompras = new ArrayList<>();
    }

    public Cliente(){
        this("", "", "", new Endereco(), "");
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public List<Pedido> getHistoricoDeCompras() {
        return historicoDeCompras;
    }

    public void setHistoricoDeCompras(List<Pedido> historicoDeCompras) {
        this.historicoDeCompras = historicoDeCompras;
    }
}