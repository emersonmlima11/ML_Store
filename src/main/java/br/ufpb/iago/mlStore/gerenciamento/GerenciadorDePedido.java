package br.ufpb.iago.mlStore.gerenciamento;

import br.ufpb.iago.mlStore.armazenamento.PersistenciaDePedidos;
import br.ufpb.iago.mlStore.armazenamento.PersistenciaDeUsuarios;
import br.ufpb.iago.mlStore.excepcions.EstoqueInsuficienteException;
import br.ufpb.iago.mlStore.excepcions.PedidoStatusInvalidoException;
import br.ufpb.iago.mlStore.excepcions.PedidoVazioException;
import br.ufpb.iago.mlStore.modelo.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDePedido {
    private List<Pedido> pedidos;
    private  int contadorPedidos = 0;
    private static PersistenciaDePedidos pdp = new PersistenciaDePedidos();

    public GerenciadorDePedido(List<User> usuarios, List<Produto> produtos) throws IOException {
        pedidos = pdp.carregar(usuarios, produtos);
        this.contadorPedidos = contarPedidos();
    }

    public GerenciadorDePedido(List<User> usuarios) throws IOException {
        pedidos = pdp.carregar(usuarios, new ArrayList<>());
        this.contadorPedidos = contarPedidos();
    }

    public Pedido buscarPedidoPorId(String idPedido) {
        for (Pedido p : pedidos) {
            if (p.getIdPedido().equals(idPedido)) {
                return p;
            }
        }
        return null;
    }

    public void criarPedido(Cliente cliente) throws IOException{
        String codigo = "PDD";
        codigo += String.format("%03d", contadorPedidos);
        Pedido p = new Pedido(codigo, cliente, new ArrayList<>());
        pedidos.add(p);
        contadorPedidos++;
        pdp.salvar(pedidos);
    }

    public void adicionarProduto(String idPedido, Produto produto) throws IOException{
        Pedido pedido = buscarPedidoPorId(idPedido);
        if(!pedido.getStatus().equals("ABERTO")){
            throw new IllegalStateException("Só é possível adicionar produtos em pedidos ABERTOS.");
        }
        pedido.addProdutos(produto);
        pdp.salvar(pedidos);
    }

    public void removerProduto(String idPedido, Produto produto) throws IOException{
        Pedido pedido = buscarPedidoPorId(idPedido);
        if(!pedido.getStatus().equals("ABERTO")){
            throw new IllegalStateException("Só é possível remover produtos em pedidos ABERTOS.");
        }
        pedido.getProdutos().remove(produto);
        pdp.salvar(pedidos);
    }

    public void finalizarPedido(String idPedido) throws IOException, PedidoStatusInvalidoException, PedidoVazioException, EstoqueInsuficienteException {
        Pedido pedido = buscarPedidoPorId(idPedido);


        pedido.finalizarPedido();

        pdp.salvar(pedidos);
    }

    public void cancelarPedido(String idPedido) throws IOException{
        Pedido pedido = buscarPedidoPorId(idPedido);
        if (pedido.getStatus().equals("CONCLUIDO")) {
            throw new IllegalStateException("Não é possível cancelar um pedido já concluído.");
        }
        pedido.setStatus("CANCELADO");
        pdp.salvar(pedidos);
    }

    public List<Pedido> listarPedidosPorCliente(String cpf) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getCliente().getCpf().equals(cpf)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Pedido> listarPedidosPorStatus(String status) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getStatus().equalsIgnoreCase(status)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    private int contarPedidos(){
        int contador = 0;
        for(Pedido p : this.pedidos){
            contador++;
        }
        return contador;
    }

}
