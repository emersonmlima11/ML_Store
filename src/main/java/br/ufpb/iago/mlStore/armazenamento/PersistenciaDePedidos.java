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
    public List<Pedido> carregar(List<User> usuarios, List<Produto> produtos) throws IOException {
        List<Pedido> pedidos = new ArrayList<>();

        for (String linha : carregarLinhas(CAMINHO)) {
            String[] partes = linha.split(";");

            // Se o arquivo tiver uma linha vazia, ele pula pra evitar erro
            if (linha.trim().isEmpty()) {
                continue;
            }

            if (partes.length != 5) {
                throw new ArquivoCorrompidoException("Erro ao ler pedidos: " + CAMINHO);
            }

            // 1. Busca o cliente pelo CPF
            Cliente cliente = null;
            for (User u : usuarios) {
                if (u instanceof Cliente c && c.getCpf().equals(partes[1])) {
                    cliente = c;
                    break;
                }
            }

            // 2. Reconstrói a lista de produtos do pedido
            List<Produto> produtosDoPedido = new ArrayList<>();


            // Só tenta quebrar a string e converter para número se a coluna NÃO estiver vazia
            if (!partes[2].trim().isEmpty()) {
                String[] idsProdutos = partes[2].split(",");
                for (String id : idsProdutos) {
                    int idInt = Integer.parseInt(id.trim()); // .trim() protege contra espaços acidentais como " 1"

                    for (Produto p : produtos) {
                        if (p.getId() == idInt) {
                            produtosDoPedido.add(p);
                            break; // Achou o produto, para a busca interna
                        }
                    }
                }
            }
            // =================================

            // 3. Adiciona o pedido na lista
            pedidos.add(new Pedido(
                    partes[0],
                    cliente,
                    produtosDoPedido,
                    Double.parseDouble(partes[3]),
                    partes[4]
            ));
        }

        return pedidos;
    }
}
