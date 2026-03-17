package br.ufpb.iago.mlStore;

import java.util.List;

public interface Gerenciador {
    public void cadastrarProduto(Produto produto);
    public Produto buscarProdutoPorId(int id);
    public List<Produto> listarProdutos();
    public void removerProduto(int id);
    public List<Produto> filtrarProdutosPorValor(double menorValor, double maiorValor);
    public boolean disponibilidadeEmEstoque(int id);
    public int quatidadeProdutos();
}
