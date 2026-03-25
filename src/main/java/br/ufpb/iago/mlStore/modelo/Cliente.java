package br.ufpb.iago.mlStore.modelo;

import java.util.List;

public class Cliente extends User {
    private String cpf;

    private List<Pedido> historicoDeCompras;

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}