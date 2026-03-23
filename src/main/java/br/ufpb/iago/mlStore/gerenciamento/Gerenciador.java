package br.ufpb.iago.mlStore.gerenciamento;

import br.ufpb.iago.mlStore.excepcions.ProdutoNaoEncontradoException;
import br.ufpb.iago.mlStore.modelo.Produto;

import java.util.List;

public interface Gerenciador {
    public void cadastrarProduto(Produto produto);
    public Produto buscarProdutoPorId(int id) throws ProdutoNaoEncontradoException;
    public List<Produto> buscarProdutoPorNome(String nome);
    public List<Produto> listarProdutos();
    public void removerProduto(int id);
    public List<Produto> filtrarProdutosPorValor(double menorValor, double maiorValor);
    public boolean disponibilidadeEmEstoque(int id);
    public int quatidadeProdutos();
}
