package br.ufpb.iago.mlStore.armazenamento;

import br.ufpb.iago.mlStore.excepcions.ArquivoCorrompidoException;
import br.ufpb.iago.mlStore.modelo.TipoProduto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDeTipos extends Persistencia{
    private static final String CAMINHO = "tipos.txt";

    public void salvar(List<TipoProduto> tipos) throws IOException{
        List<String> linhas = new ArrayList<>();
        for(TipoProduto t : tipos){
            linhas.add(t.getNome() + ";" + t.getImposto());
        }

        salvarLinhas(linhas, CAMINHO);
    }

    public List<TipoProduto> carregar() throws IOException {
        List<TipoProduto> tipos = new ArrayList<>();
        for (String linha : carregarLinhas(CAMINHO)){
            String[] partes = linha.split(";");
            if (partes.length != 2) {
                throw new ArquivoCorrompidoException("Arquivo " + CAMINHO + " Diferente do Esperado");
            }
            tipos.add(new TipoProduto(partes[0], Double.parseDouble(partes[1])));
        }
        return tipos;
    }
}
