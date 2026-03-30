package br.ufpb.iago.mlStore.gerenciamento;

import br.ufpb.iago.mlStore.armazenamento.PersistenciaDeProdutos;
import br.ufpb.iago.mlStore.armazenamento.PersistenciaDeTipos;
import br.ufpb.iago.mlStore.excepcions.ProdutoNaoEncontradoException;
import br.ufpb.iago.mlStore.modelo.Produto;
import br.ufpb.iago.mlStore.modelo.TipoProduto;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeProduto  implements Gerenciador {
    private List<Produto> produtos;
    private PersistenciaDeProdutos pdp = new PersistenciaDeProdutos();

    public GerenciadorDeProduto(List<TipoProduto> tipos) throws IOException {
        this.produtos = pdp.carregar(tipos);
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public int gerarProximoId() {
        if (this.produtos.isEmpty()) {
            return 1; // Se não houver produtos, o primeiro ID será 1
        }

        int maiorId = 0;
        for (Produto p : this.produtos) {
            if (p.getId() > maiorId) {
                maiorId = p.getId();
            }
        }

        return maiorId + 1; // Devolve o maior ID encontrado + 1
    }

    @Override
    public void cadastrarProduto(Produto produto) throws IOException{
        if (produto != null) {
        this.produtos.add(produto);
        pdp.salvar(this.produtos);
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
        List<Produto> produtosBusca = new ArrayList<>();
        for(Produto produto : this.produtos) {
            if(produto.getNome().toLowerCase().contains(nome.toLowerCase())) {
                produtosBusca.add(produto);
            }
        }
        return produtosBusca;
    }

    @Override
    public void removerProduto(int id) throws IOException{
        produtos.removeIf(produto -> produto.getId() == id);
        pdp.salvar(this.produtos);
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
    public int quantidadeProdutos() {
        return this.produtos.size();
    }
}

