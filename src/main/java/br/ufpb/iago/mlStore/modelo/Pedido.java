package br.ufpb.iago.mlStore.modelo;

import br.ufpb.iago.mlStore.excepcions.EstoqueInsuficienteException;
import br.ufpb.iago.mlStore.excepcions.PedidoStatusInvalidoException;
import br.ufpb.iago.mlStore.excepcions.PedidoVazioException;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private String idPedido;
    private Cliente cliente;
    private List<Produto> produtos;
    private double valorTotal;
    private String status;


    public Pedido(String idPedido, Cliente cliente, List<Produto> produtosDoPedido, double v, String parte) {
        this.idPedido = idPedido;
        this.cliente = cliente;

        this.produtos = produtosDoPedido;

        this.valorTotal = v;

        this.status = parte;
    }

    public Pedido(String idPedido, Cliente cliente, List<Produto> produtosDoPedido) {
        this.idPedido = idPedido;
        this.cliente = cliente;

        this.produtos = produtosDoPedido;

        this.valorTotal = 0.0;

        this.status = "ABERTO";
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void addProdutos(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Erro: O produto não pode ser nulo.");
        }
        this.produtos.add(produto);
        this.valorTotal += produto.getPreco();
    }

    public void exibirResumo() {
        System.out.println("\n=== Resumo do Pedido ===");
        System.out.println("Pedido ID: " + this.idPedido);
        System.out.println("Estado: " + this.status);

        if (this.cliente != null) {
            System.out.println("Cliente: " + this.cliente.getNomeCompleto());
        }

        System.out.println("Itens no carrinho:");

        if (this.produtos.isEmpty()) {
            System.out.println(" - O carrinho está vazio.");
        } else {
            for (Produto p : this.produtos) {
                System.out.println(" - " + p.getNome() + " | R$ " + String.format("%.2f", p.getPreco()));
            }
        }

        System.out.println("Valor Total: R$ " + String.format("%.2f", this.valorTotal));
        System.out.println("========================\n");
    }

    public void finalizarPedido() throws PedidoStatusInvalidoException, PedidoVazioException, EstoqueInsuficienteException {

        if (!this.status.equals("ABERTO")) {
            throw new PedidoStatusInvalidoException("Não é possível finalizar: Este pedido já se encontra " + this.status + ".");
        }

        if (this.produtos.isEmpty()) {
            throw new PedidoVazioException("Não é possível finalizar um pedido sem itens no carrinho.");
        }

        // Tenta vender os itens. Se der EstoqueInsuficienteException,
        // a exceção VAI SUBIR direto, não faça try/catch aqui!
        for (Produto produto : this.produtos) {
            produto.vender(1); // Se falhar aqui, o método para imediatamente e joga a exceção pra cima
        }

        // Se passou do loop, é porque tinha estoque de tudo
        this.status = "CONCLUIDO";

        // REMOVIDO: System.out.println("Sucesso! Pedido...");
        // O Sucesso não deve ser impresso pelo modelo. A interface grafica vai ver que a exceção não foi jogada e exibirá o sucesso.
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
