package br.ufpb.iago.mlStore.modelo;

import br.ufpb.iago.mlStore.excepcions.EstoqueInsuficienteException;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private Cliente cliente;
    private List<Produto> produtos;
    private double valorTotal;
    private String status;


    public Pedido(int idPedido, Cliente cliente) {
        this.idPedido = idPedido;
        this.cliente = cliente;

        this.produtos = new ArrayList<Produto>();

        this.valorTotal = 0.0;

        this.status = "ABERTO";
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


}
