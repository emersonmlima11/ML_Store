package br.ufpb.iago.mlStore;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeProduto  {
    private List<Produto> produtos;
    public GerenciadorDeProduto() {
        this.produtos = new ArrayList<Produto>();
    }
    public void cadastrarProduto(Produto produto) {
        if (produto != null) {
        this.produtos.add(produto);
        System.out.println("Produto Cadastrado com sucesso!");
    }}

    public List<Produto> listarProdutos() {
        return produtos;
    }
    public Produto buscarProdutoPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }
    public void removerProduto(int id) {
        produtos.removeIf(produto -> produto.getId() == id);
        }
}

