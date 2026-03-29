package br.ufpb.iago.mlStore.armazenamento;

import br.ufpb.iago.mlStore.excepcions.ErroAoLerArquivoException;
import br.ufpb.iago.mlStore.excepcions.ErroAoSalvarArquivoException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Persistencia {
    protected void salvarLinhas(List<String> linhas, String caminho) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) { //
            for(String linha : linhas){
                writer.write(linha);
                writer.newLine();
            }
        } catch(IOException e){
            throw new ErroAoSalvarArquivoException("Erro Ao Salvar o arquivo");
        }
    }


protected List<String> carregarLinhas(String caminho) throws IOException {
    File arquivo = new File(caminho);

    // Se o ficheiro não existir, cria um novo ficheiro vazio e retorna a lista vazia
    if (!arquivo.exists()) {
        arquivo.createNewFile();
        return new ArrayList<>();
    }


    try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
        List<String> linhas = new ArrayList<>();
        String linha;

        while ((linha = reader.readLine()) != null) {
            linhas.add(linha);
        }

        return linhas;

    }  catch(IOException e){
        throw new ErroAoSalvarArquivoException("Erro Ao Salvar o arquivo", e);

}
    }
}

