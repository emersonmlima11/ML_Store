package br.ufpb.iago.mlStore.armazenamento;

import br.ufpb.iago.mlStore.modelo.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDeUsuarios extends Persistencia{
    private static final String CAMINHO = "usuarios.txt";

    public void salvar(List<User> usuarios) throws IOException{
        List<String> linhas = new ArrayList<>();

        for(User u : usuarios){
            if(u instanceof Admin a){
                String linha = "ADMIN;" + a.getNomeCompleto() + ";" + a.getEmail() + ";"
                        + a.getPassword() + ";" + a.getEndereco().format() + ";"
                        + a.getCodigoDeAcesso();
                linhas.add(linha);

            } else if(u instanceof Cliente c){
                String linha = "CLIENTE;" + c.getNomeCompleto() + ";" + c.getEmail() + ";"
                        + c.getPassword() + ";" + c.getEndereco().format() + ";"
                        + c.getCpf();
                linhas.add(linha);
            }
        }
        salvarLinhas(linhas, CAMINHO);
    }

    public List<User> carregarUsuarios(List<Pedido> pedidos) throws IOException{
        List<User> usuarios = new ArrayList<>();

        for (String linha : carregarLinhas(CAMINHO)) {
            String[] partes = linha.split(";");

            if (partes[0].equals("ADMIN")) {
                Endereco endereco = new Endereco(partes[4], partes[5], partes[6],
                        partes[7], partes[8], partes[9]);
                usuarios.add(new Admin(partes[1], partes[2], partes[3], endereco, partes[10]));

            } else if (partes[0].equals("CLIENTE")) {
                Endereco endereco = new Endereco(partes[4], partes[5], partes[6],
                        partes[7], partes[8], partes[9]);
                Cliente c = new Cliente(partes[1], partes[2], partes[3], endereco, partes[10]);

                // reconstrói o histórico de compras
                for(Pedido p : pedidos){
                    if(p.getCliente().getCpf().equals(c.getCpf())){
                        c.getHistoricoDeCompras().add(p);
                    }
                }

                usuarios.add(c);
            }
        }

        return usuarios;
    }

}
