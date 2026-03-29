package br.ufpb.iago.mlStore.main;

import br.ufpb.iago.mlStore.armazenamento.PersistenciaDePedidos;
import br.ufpb.iago.mlStore.armazenamento.PersistenciaDeProdutos;
import br.ufpb.iago.mlStore.armazenamento.PersistenciaDeTipos;
import br.ufpb.iago.mlStore.armazenamento.PersistenciaDeUsuarios;
import br.ufpb.iago.mlStore.modelo.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Teste {
    public static void main(String[] args) throws IOException {
        // ========== CRIANDO TIPOS ==========
        System.out.println("===== TESTANDO PERSISTENCIA DE TIPOS =====");

        TipoProduto eletronico = new TipoProduto("Eletrônico", 15.0);
        TipoProduto roupa = new TipoProduto("Roupa", 10.0);
        TipoProduto alimento = new TipoProduto("Alimento", 5.0);

        List<TipoProduto> tipos = new ArrayList<>();
        tipos.add(eletronico);
        tipos.add(roupa);
        tipos.add(alimento);

        PersistenciaDeTipos persistenciaDeTipo = new PersistenciaDeTipos();
        persistenciaDeTipo.salvar(tipos);
        System.out.println("Tipos salvos com sucesso!");

        List<TipoProduto> tiposCarregados = persistenciaDeTipo.carregar();
        System.out.println("Tipos carregados:");
        for (TipoProduto t : tiposCarregados) {
            System.out.println(" - " + t.getNome() + " | Imposto: " + t.getImposto() + "%");
        }

        // ========== CRIANDO PRODUTOS ==========
        System.out.println("\n===== TESTANDO PERSISTENCIA DE PRODUTOS =====");

        Produto notebook = new Produto(1, "Notebook", 2500.00, 10, eletronico);
        Produto camiseta = new Produto(2, "Camiseta", 49.90, 50, roupa);
        Produto arroz = new Produto(3, "Arroz", 8.50, 100, alimento);

        List<Produto> produtos = new ArrayList<>();
        produtos.add(notebook);
        produtos.add(camiseta);
        produtos.add(arroz);

        PersistenciaDeProdutos persistenciaDeProduto = new PersistenciaDeProdutos();
        persistenciaDeProduto.salvar(produtos);
        System.out.println("Produtos salvos com sucesso!");

        List<Produto> produtosCarregados = persistenciaDeProduto.carregar(tiposCarregados);
        System.out.println("Produtos carregados:");
        for (Produto p : produtosCarregados) {
            System.out.println(" - " + p);
        }

        // ========== CRIANDO USUARIOS ==========
        System.out.println("\n===== TESTANDO PERSISTENCIA DE USUARIOS =====");

        Endereco endAdmin = new Endereco("Rua A", "123", "Bairro B", "Cidade C", "PB", "Apto 1");
        Endereco endCliente = new Endereco("Rua B", "456", "Bairro C", "Cidade D", "PB", "Casa");

        Admin admin = new Admin("João Silva", "joao@email.com", "senha123", endAdmin, "ADMIN001");
        admin.setId(1);

        Cliente cliente = new Cliente("Maria Souza", "maria@email.com", "senha456", endCliente, "123.456.789-00");
        cliente.setId(2);

        List<User> usuarios = new ArrayList<>();
        usuarios.add(admin);
        usuarios.add(cliente);


        PersistenciaDeUsuarios persistenciaDeUsuarios = new PersistenciaDeUsuarios();
        persistenciaDeUsuarios.salvar(usuarios);
        System.out.println("Usuarios salvos com sucesso!");

        // ========== CRIANDO PEDIDOS ==========
        System.out.println("\n===== TESTANDO PERSISTENCIA DE PEDIDOS =====");

        Pedido pedido = new Pedido("PED001", cliente, new ArrayList<>(), 0.0, "ABERTO");
        pedido.addProdutos(notebook);
        pedido.addProdutos(camiseta);

        cliente.getHistoricoDeCompras().add(pedido);

        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(pedido);

        PersistenciaDePedidos persistenciaDePedido = new PersistenciaDePedidos();
        persistenciaDePedido.salvar(pedidos);
        System.out.println("Pedidos salvos com sucesso!");
        pedido.exibirResumo();

        // ========== CARREGANDO PEDIDOS ==========
        List<Pedido> pedidosCarregados = persistenciaDePedido.carregar(usuarios, produtosCarregados);
        System.out.println("Pedidos carregados:");
        for (Pedido p : pedidosCarregados) {
            p.exibirResumo();
        }

        // ========== CARREGANDO USUARIOS COM HISTORICO ==========
        System.out.println("\n===== CARREGANDO USUARIOS COM HISTORICO =====");
        List<User> usuariosCarregados = persistenciaDeUsuarios.carregarUsuarios(pedidosCarregados);
        for (User u : usuariosCarregados) {
            System.out.println("Usuario: " + u.getNomeCompleto());
            if (u instanceof Cliente c) {
                System.out.println("Historico de compras: " + c.getHistoricoDeCompras().size() + " pedido(s)");
            }
        }
    }
}
