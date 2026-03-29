package br.ufpb.iago.mlStore.modelo;

import java.util.Objects;

public class TipoProduto {
    private String nome;
    private double imposto;

    public TipoProduto(String nome, Double imposto){
        this.nome = nome;
        this.imposto = imposto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoProduto that = (TipoProduto) o;
        return nome.equalsIgnoreCase(that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }

    public String getNome() {
        return nome;
    }

    public double getImposto() {
        return imposto;
    }

    public void setImposto(double imposto) {
        this.imposto = imposto;
    }
}
