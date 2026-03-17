package br.ufpb.iago.mlStore;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeProduto  implements Gerenciador{
    private List<Produto> produtos;

    public GerenciadorDeProduto() {
        this.produtos = new ArrayList<Produto>();
    }

    @Override
    public void cadastrarProduto(Produto produto) {
        if (produto != null) {
        this.produtos.add(produto);
        System.out.println("Produto Cadastrado com sucesso!");
        }
    }

    @Override
    public List<Produto> listarProdutos() {
        return this.produtos;
    }

    @Override
    public Produto buscarProdutoPorId(int id) {
        for (Produto produto : this.produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    @Override
    public void removerProduto(int id) {
        produtos.removeIf(produto -> produto.getId() == id);
    }

    @Override
    public List<Produto> filtrarProdutosPorValor(double menorValor, double maiorValor) {
        List<Produto> produtosValor = new ArrayList<>();
        for(Produto produto : this.produtos){
            if(produto.getPreco() >= menorValor && produto.getPreco() <= maiorValor){
                produtosValor.add(produto);
            }
        }

        return produtosValor;
    }

    @Override
    public boolean disponibilidadeEmEstoque(int id) {
        for(Produto produto : this.produtos){
            if(produto.getQuantidadeEstoque() > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public int quatidadeProdutos() {
        return this.produtos.size();
    }
}

