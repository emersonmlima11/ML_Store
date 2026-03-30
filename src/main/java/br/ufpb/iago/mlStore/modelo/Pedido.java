package br.ufpb.iago.mlStore.modelo;

import br.ufpb.iago.mlStore.excepcions.EstoqueInsuficienteException;
import br.ufpb.iago.mlStore.excepcions.PedidoStatusInvalidoException;
import br.ufpb.iago.mlStore.excepcions.PedidoVazioException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // 1. CHECAGEM DE ESTOQUE (Evita o problema de Rollback)
        // Agrupa e conta quantos exemplares de cada produto estão no carrinho
        Map<Produto, Integer> contagemProdutos = new HashMap<>();
        for (Produto p : this.produtos) {
            contagemProdutos.put(p, contagemProdutos.getOrDefault(p, 0) + 1);
        }

        // Verifica se há estoque para TUDO antes de vender qualquer coisa
        for (Produto p : contagemProdutos.keySet()) {
            int quantidadeDesejada = contagemProdutos.get(p);
            if (p.getQuantidadeEstoque() < quantidadeDesejada) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para o produto: " + p.getNome()
                        + " (Desejado: " + quantidadeDesejada + ", Disponível: " + p.getQuantidadeEstoque() + ")");
            }
        }

        // 2. VENDA SEGURA
        // Como o loop acima passou sem lançar exceção, temos 100% de certeza que há estoque.
        // Agora sim, descontamos da memória.
        for (Produto produto : this.produtos) {
            produto.vender(1);
        }

        this.status = "CONCLUIDO";
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
