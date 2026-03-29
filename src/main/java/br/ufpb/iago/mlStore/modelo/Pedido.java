package br.ufpb.iago.mlStore.modelo;

import br.ufpb.iago.mlStore.excepcions.EstoqueInsuficienteException;

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


    public void addProdutos(Produto produto){
        if(produto != null){
            this.produtos.add(produto);
            this.valorTotal += produto.getPreco();

            System.out.println(produto.getNome() + " adicionado com sucesso");
        }
        else{
            System.out.println("Erro ao adicionar produto");
        }
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

    public void finalizarPedido() {
        if (!this.status.equals("ABERTO")) {
            System.out.println("Atenção: Este pedido já se encontra " + this.status + ".");
            return;
        }

        if (this.produtos.isEmpty()) {
            System.out.println("Não é possível finalizar um pedido sem itens no carrinho.");
            return;
        }

        boolean todosComStock = true;

        for (Produto produto : this.produtos) {
            try {
                produto.vender(1);
            } catch (EstoqueInsuficienteException e) {
                System.out.println("Erro ao processar pedido: " + e.getMessage());
                todosComStock = false;
            } catch (IllegalArgumentException e) {
                System.out.println("Erro de validação: " + e.getMessage());
                todosComStock = false;
            }
        }

        if (todosComStock) {
            this.status = "CONCLUIDO";
            System.out.println("Sucesso! Pedido " + this.idPedido + " finalizado. Valor total cobrado: R$ " + String.format("%.2f", this.valorTotal));
        } else {
            System.out.println("O pedido não pôde ser finalizado devido a erros nos itens.");
        }
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
