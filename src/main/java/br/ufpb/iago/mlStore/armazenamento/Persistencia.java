package br.ufpb.iago.mlStore.armazenamento;

import br.ufpb.iago.mlStore.excepcions.ErroAoLerArquivoException;
import br.ufpb.iago.mlStore.excepcions.ErroAoSalvarArquivoException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Persistencia {
    protected void salvarLinhas(List<String> linhas, String caminho) throws IOException{
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(caminho)); //Acessar o caminho
            for(String linha : linhas){
                writer.write(linha);
                writer.newLine();
            }
            writer.close();
        } catch(IOException e){
            throw new ErroAoSalvarArquivoException("Erro Ao Salvar o arquivo");
        }
    }

    protected List<String> carregarLinhas(String caminho) throws IOException{
        File arquivo = new File(caminho);

        if (!arquivo.exists()) {
            arquivo.createNewFile(); // cria o arquivo vazio
            return new ArrayList<>(); // retorna lista vazia
        }

        try {
            List<String> linhas = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(caminho));
            String linha = reader.readLine();
            while (linha != null){
                linhas.add(linha);
                linha = reader.readLine();
            }
            reader.close();
            return linhas;
        } catch (IOException e) {
            throw new ErroAoLerArquivoException("Erro ao ler o arquivo "+caminho);
        }
    }
}
