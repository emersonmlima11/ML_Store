package br.ufpb.iago.mlStore.armazenamento;

import br.ufpb.iago.mlStore.excepcions.ArquivoCorrompidoException;
import br.ufpb.iago.mlStore.modelo.Produto;
import br.ufpb.iago.mlStore.modelo.TipoProduto;
import br.ufpb.iago.mlStore.repositorio.RepositorioDeTipos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PersistenciaDeProdutos extends Persistencia {
    private static final String CAMINHO = "produtos.txt";

    public void salvar(List<Produto> produtos) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Produto p : produtos){
            String linha = p.getId() + ";" + p.getNome() + ";" + p.getPreco() + ";" + p.getQuantidadeEstoque() + ";" + p.getTipo().getNome();
            linhas.add(linha);
        }

        salvarLinhas(linhas, CAMINHO);

    }

    public List<Produto> carregar(List<TipoProduto> tipos) throws IOException {
        List<Produto> produtos = new ArrayList<>();
        for (String linha : carregarLinhas(CAMINHO)){
            String [] partes = linha.split(";");
            if(partes.length != 5){
                throw new ArquivoCorrompidoException("Arquivo " + CAMINHO + " Não Possui Todos os Parametros");
            }

            TipoProduto tipo = buscarTipo(partes[4], tipos);
            produtos.add(new Produto(Integer.parseInt(partes[0]), partes[1],
                    Double.parseDouble(partes[2]), Integer.parseInt(partes[3]), tipo));
        }

        return produtos;
    }

    private TipoProduto buscarTipo(String nome, List<TipoProduto> tipos) {
        for (TipoProduto t : tipos) {
            if (t.getNome().equalsIgnoreCase(nome)) {
                return t;
            }
        }
        return null;
    }

}
