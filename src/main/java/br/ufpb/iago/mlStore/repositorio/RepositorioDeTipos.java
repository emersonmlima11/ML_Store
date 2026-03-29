package br.ufpb.iago.mlStore.repositorio;

import br.ufpb.iago.mlStore.armazenamento.PersistenciaDeTipos;
import br.ufpb.iago.mlStore.gerenciamento.GerenciadorDeProduto;
import br.ufpb.iago.mlStore.modelo.Produto;
import br.ufpb.iago.mlStore.modelo.TipoProduto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioDeTipos {
    private List<TipoProduto> tiposDeProdutos;
    private PersistenciaDeTipos pdt = new PersistenciaDeTipos();

    public RepositorioDeTipos() throws IOException {
        tiposDeProdutos = pdt.carregar();
    }

    public void addTipo(TipoProduto tipo) throws IOException{
        if (!this.tiposDeProdutos.contains(tipo)){
            this.tiposDeProdutos.add(tipo);
        }
        pdt.salvar(this.tiposDeProdutos);
    }

    public void removerTipo(String nome, List<Produto> produtos) throws IOException{
        TipoProduto encontrado = null;

        for (TipoProduto t : this.tiposDeProdutos) {
            if (t.getNome().equalsIgnoreCase(nome)) {
                encontrado = t;
                break;
            }
        }

        for (Produto p : produtos) {
            if (p.getTipo().equals(encontrado)) {
                throw new IllegalStateException(
                        "Não é possível remover o tipo '" + nome +
                                "' pois existem produtos cadastrados com ele."
                );
            }
        }

        this.tiposDeProdutos.remove(encontrado);
        pdt.salvar(this.tiposDeProdutos);
    }

    public void modificarImposto(String nome, double novoImposto) throws IOException{
        for(TipoProduto tipo : tiposDeProdutos){
            if(tipo.getNome().equals(nome)){
                tipo.setImposto(novoImposto);
            }
        }
        pdt.salvar(this.tiposDeProdutos);
    }

    public List<TipoProduto> getTiposDeProdutos() {
        return tiposDeProdutos;
    }

    public TipoProduto buscarPorNome(String nome) {
        for (TipoProduto t : tiposDeProdutos) {
            if (t.getNome().equalsIgnoreCase(nome)) {
                return t;
            }
        }
        return null;
    }

}
