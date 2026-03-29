package br.ufpb.iago.mlStore.gerenciamento;

import br.ufpb.iago.mlStore.excepcions.ProdutoNaoEncontradoException;
import br.ufpb.iago.mlStore.modelo.Produto;

import java.io.IOException;
import java.util.List;

public interface Gerenciador {
    public void cadastrarProduto(Produto produto) throws IOException;
    public Produto buscarProdutoPorId(int id) throws ProdutoNaoEncontradoException;
    public List<Produto> buscarProdutoPorNome(String nome);
    public List<Produto> listarProdutos();
    public void removerProduto(int id) throws IOException;
    public List<Produto> filtrarProdutosPorValor(double menorValor, double maiorValor);
    public boolean disponibilidadeEmEstoque(int id);
    public int quatidadeProdutos();
}
