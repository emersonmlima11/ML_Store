package br.ufpb.iago.mlStore.armazenamento;

import br.ufpb.iago.mlStore.excepcions.ArquivoCorrompidoException;
import br.ufpb.iago.mlStore.modelo.Cliente;
import br.ufpb.iago.mlStore.modelo.Pedido;
import br.ufpb.iago.mlStore.modelo.Produto;
import br.ufpb.iago.mlStore.modelo.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDePedidos extends Persistencia {
    private static final String CAMINHO = "pedidos.txt";

    public void salvar(List<Pedido> pedidos) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Pedido p : pedidos) {

            // 1. Criamos uma lista simples só com os IDs (em formato de String)
            List<String> idsProdutos = new ArrayList<>();
            for (Produto prod : p.getProdutos()) {
                idsProdutos.add(String.valueOf(prod.getId()));
            }



            String nomesProdutos = String.join(",", idsProdutos);

            // 3. Montamos a linha final
            linhas.add(p.getIdPedido() + ";" + p.getCliente().getCpf() + ";"
                    + nomesProdutos + ";" + p.getValorTotal() + ";" + p.getStatus());
        }
        salvarLinhas(linhas, CAMINHO);
    }
    public List<Pedido> carregar(List<User> usuarios, List<Produto> produtos) throws IOException{
        List<Pedido> pedidos = new ArrayList<>();

        for (String linha : carregarLinhas(CAMINHO)) {
            String[] partes = linha.split(";");
            if (partes.length != 5) {
                throw new ArquivoCorrompidoException(CAMINHO);
            }

            // busca o cliente pelo ID
            Cliente cliente = null;
            for (User u : usuarios) {
                if (u instanceof Cliente c && c.getCpf().equals(partes[1])) {
                    cliente = c;
                    break;
                }
            }

            // reconstrói a lista de produtos do pedido
            List<Produto> produtosDoPedido = new ArrayList<>();
            String[] idsProdutos = partes[2].split(",");
            for (String id : idsProdutos) {
                int idInt = Integer.parseInt(id);
                for (Produto p : produtos) {
                    if (p.getId() == idInt) {
                        produtosDoPedido.add(p);
                        break;
                    }
                }
            }

            pedidos.add(new Pedido(partes[0], cliente, produtosDoPedido,
                    Double.parseDouble(partes[3]), partes[4]));
        }

        return pedidos;
    }
}
