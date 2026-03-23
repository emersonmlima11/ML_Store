package br.ufpb.iago.mlStore.gerenciamento;

import br.ufpb.iago.mlStore.excepcions.ProdutoNaoEncontradoException;
import br.ufpb.iago.mlStore.modelo.Produto;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeProduto  implements Gerenciador {
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
    public Produto buscarProdutoPorId(int id) throws ProdutoNaoEncontradoException {
        for (Produto produto : this.produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        throw new ProdutoNaoEncontradoException("Produto com ID " + id + " não foi encontrado no sistema.");
    }

    @Override
    public List<Produto> buscarProdutoPorNome(String nome) {
        List<Produto> produtos = new ArrayList<>();
        for(Produto produto : this.produtos) {
            if(produto.getNome().toLowerCase().contains(nome.toLowerCase())) {
                produtos.add(produto);
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

