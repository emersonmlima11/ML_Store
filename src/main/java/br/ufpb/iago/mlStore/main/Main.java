package br.ufpb.iago.mlStore.main;

import br.ufpb.iago.mlStore.armazenamento.*;
import br.ufpb.iago.mlStore.excepcions.*;
import br.ufpb.iago.mlStore.gerenciamento.*;
import br.ufpb.iago.mlStore.modelo.*;
import br.ufpb.iago.mlStore.repositorio.RepositorioDeTipos;
import br.ufpb.iago.mlStore.repositorio.RepositorioDeUsuario;

import java.io.IOException;
import java.util.List;

public class Main {

    // Instâncias Globais de Persistência, Gerenciamento e UI
    private static RepositorioDeUsuario repositorioUsuario;
    private static RepositorioDeTipos repositorioTipos;
    private static GerenciadorDeProduto gerenciadorProduto;
    private static GerenciadorDePedido gerenciadorPedido;

    // Nossa nova classe de visualização (Fim do acoplamento com o Swing!)
    private static InterfaceUsuario ui = new InterfaceUsuario();



    public static void main(String[] args) {
        inicializarSistema();
        exibirMenuPrincipal();
    }

    private static void inicializarSistema() {
        try {
            repositorioTipos = new RepositorioDeTipos();
            repositorioUsuario = new RepositorioDeUsuario();

            // Passamos as listas dos repositórios para os gerenciadores
            gerenciadorProduto = new GerenciadorDeProduto(repositorioTipos.getTiposDeProdutos());
            gerenciadorPedido = new GerenciadorDePedido(repositorioUsuario.acharTodos(), gerenciadorProduto.listarProdutos());

        } catch (IOException e) {
            ui.mostrarErro("Erro Crítico", "Erro ao inicializar os arquivos do sistema: " + e.getMessage());
            System.exit(1);
        }
    }

    // ================== MENUS PRINCIPAIS ==================

    private static void exibirMenuPrincipal() {
        boolean rodando = true;
        while (rodando) {
            String[] opcoes = {"Entrar como Admin", "Entrar como Cliente", "Cadastrar Cliente", "Sair"};
            int escolha = ui.mostrarMenu("Menu Principal", "Bem-vindo ao mlStore!\nEscolha uma opção:", opcoes);

            switch (escolha) {
                case 0 -> loginAdmin();
                case 1 -> loginCliente();
                case 2 -> cadastrarClienteUI();
                case 3, -1 -> {
                    rodando = false;
                    ui.mostrarMensagem("Despedida", "Saindo do sistema... Até logo!");
                }
            }
        }
    }

    private static void menuAdmin() {
        boolean rodando = true;
        while (rodando) {
            String[] opcoes = {"Cadastrar Produto", "Listar Produtos", "Cadastrar Admin", "Voltar"};
            int escolha = ui.mostrarMenu("Menu Admin", "Painel de Administração", opcoes);

            switch (escolha) {
                case 0 -> cadastrarProdutoUI();
                case 1 -> listarProdutosUI();
                case 2 -> cadastrarAdminUI();
                case 3, -1 -> rodando = false;
            }
        }
    }

    private static void menuCliente(Cliente cliente) {
        boolean rodando = true;
        while (rodando) {
            String[] opcoes = {"Ver Catálogo", "Novo Pedido", "Meus Pedidos", "Voltar"};
            int escolha = ui.mostrarMenu("Menu Cliente", "Área do Cliente: " + cliente.getNomeCompleto(), opcoes);

            switch (escolha) {
                case 0 -> listarProdutosUI();
                case 1 -> realizarPedidoUI(cliente);
                case 2 -> listarPedidosClienteUI(cliente);
                case 3, -1 -> rodando = false;
            }
        }
    }

    // ================== AÇÕES DO CLIENTE ==================

    private static void realizarPedidoUI(Cliente cliente) {
        try {
            gerenciadorPedido.criarPedido(cliente);
            List<Pedido> pedidosDoCliente = gerenciadorPedido.listarPedidosPorCliente(cliente.getCpf());
            Pedido pedidoAtual = pedidosDoCliente.get(pedidosDoCliente.size() - 1);

            boolean adicionando = true;
            while (adicionando) {
                try {
                    // Usamos o 0 como flag de parada, o que é muito mais seguro que deixar texto vazio
                    int idBusca = ui.pedirInteiro("Digite o ID do produto para adicionar ao carrinho\n(Ou digite 0 para finalizar o carrinho):");

                    if (idBusca == 0) {
                        adicionando = false;
                        break;
                    }

                    Produto produtoEncontrado = gerenciadorProduto.buscarProdutoPorId(idBusca);
                    gerenciadorPedido.adicionarProduto(pedidoAtual.getIdPedido(), produtoEncontrado);
                    ui.mostrarMensagem("Sucesso", produtoEncontrado.getNome() + " adicionado ao carrinho!");

                } catch (ProdutoNaoEncontradoException e) {
                    ui.mostrarMensagem("Não Encontrado", e.getMessage());
                }
            }

            try {
                gerenciadorPedido.finalizarPedido(pedidoAtual.getIdPedido());
                ui.mostrarMensagem("Sucesso", "Pedido " + pedidoAtual.getIdPedido() + " finalizado com sucesso!");

            } catch (PedidoVazioException e) {
                gerenciadorPedido.cancelarPedido(pedidoAtual.getIdPedido());
                ui.mostrarMensagem("Carrinho Vazio", "Pedido cancelado: " + e.getMessage());

            } catch (PedidoStatusInvalidoException | EstoqueInsuficienteException e) {
                ui.mostrarErro("Erro no Pedido", "Não foi possível finalizar a compra:\n" + e.getMessage());
            }

        } catch (OperacaoCanceladaException e) {
            // Se o usuário clicou em cancelar em algum input, a compra é abortada silenciosamente
        } catch (Exception e) {
            ui.mostrarErro("Erro Interno", "Erro inesperado ao processar pedido: " + e.getMessage());
        }
    }

    private static void listarPedidosClienteUI(Cliente cliente) {
        List<Pedido> meusPedidos = gerenciadorPedido.listarPedidosPorCliente(cliente.getCpf());
        if (meusPedidos.isEmpty()) {
            ui.mostrarMensagem("Histórico", "Você ainda não possui pedidos.");
            return;
        }

        StringBuilder txtPedidos = new StringBuilder("Seu Histórico:\n\n");
        for (Pedido p : meusPedidos) {
            txtPedidos.append("ID: ").append(p.getIdPedido())
                    .append(" | Status: ").append(p.getStatus())
                    .append(" | Total: R$ ").append(String.format("%.2f", p.getValorTotal()))
                    .append("\n");
        }
        ui.mostrarMensagem("Meus Pedidos", txtPedidos.toString());
    }

    // ================== AÇÕES DO ADMIN ==================

    private static void cadastrarProdutoUI() {
        try {
            if (repositorioTipos.getTiposDeProdutos().isEmpty()) {
                ui.mostrarMensagem("Aviso", "Não há Tipos de Produto cadastrados. Cadastrando 'Geral' padrão...");
                TipoProduto tipoPadrao = new TipoProduto("Geral", 10.0);
                repositorioTipos.addTipo(tipoPadrao); // Salva usando o repositório
            }

            int id = gerenciadorProduto.gerarProximoId();
            ui.mostrarMensagem("ID Automático", "O sistema gerou o ID " + id + " para este novo produto.");
            String nome = ui.pedirTexto("Nome do Produto:");
            double preco = ui.pedirDouble("Preço do Produto (Ex: 15.50):");
            int estoque = ui.pedirInteiro("Quantidade em Estoque:");

            String[] nomesTipos = repositorioTipos.getTiposDeProdutos().stream().map(TipoProduto::getNome).toArray(String[]::new);
            int indexTipo = ui.mostrarMenu("Tipo de Produto", "Escolha o Tipo:", nomesTipos);
            if (indexTipo == -1) return;
            TipoProduto tipoSelecionado = repositorioTipos.getTiposDeProdutos().get(indexTipo);

            Produto novoProduto = new Produto(id, nome, preco, estoque, tipoSelecionado);
            gerenciadorProduto.cadastrarProduto(novoProduto);

            ui.mostrarMensagem("Sucesso", "Produto cadastrado com sucesso!");

        } catch (OperacaoCanceladaException e) {
            // Sai silenciosamente e volta pro menu
        } catch (Exception e) {
            ui.mostrarErro("Erro", "Falha ao cadastrar: " + e.getMessage());
        }
    }

    private static void listarProdutosUI() {
        List<Produto> produtos = gerenciadorProduto.listarProdutos();
        if (produtos.isEmpty()) {
            ui.mostrarMensagem("Catálogo", "Nenhum produto cadastrado no momento.");
            return;
        }

        StringBuilder txtProdutos = new StringBuilder("Catálogo Disponível:\n\n");
        for (Produto p : produtos) {
            txtProdutos.append("ID: ").append(p.getId()).append(" | ").append(p.toString())
                    .append(" | Tipo: ").append(p.getTipo().getNome()).append("\n");
        }
        ui.mostrarMensagem("Produtos", txtProdutos.toString());
    }

    // ================== LOGINS E CADASTROS DE USUÁRIOS ==================

    private static void loginAdmin() {
        try {
            String codigo = ui.pedirTexto("Digite o Código de Acesso do Admin:");
            String senha = ui.pedirTexto("Digite a Senha:");

            for (User u : repositorioUsuario.acharTodos()) {
                if (u instanceof Admin a && a.getCodigoDeAcesso().equals(codigo) && a.getPassword().equals(senha)) {
                    ui.mostrarMensagem("Login", "Bem-vindo, " + a.getNomeCompleto() + ".");
                    menuAdmin();
                    return;
                }
            }

            // Fallback de admin padrão se não achar nenhum
            if (codigo.equals("admin") && senha.equals("admin")) {
                ui.mostrarMensagem("Login", "Bem-vindo, Administrador Padrão.");
                menuAdmin();
                return;
            }

            ui.mostrarErro("Erro", "Credenciais inválidas!");
        } catch (OperacaoCanceladaException e) { /* Volta pro menu */ }
    }

    private static void loginCliente() {
        try {
            String cpf = ui.pedirTexto("Digite seu CPF:");
            String senha = ui.pedirTexto("Digite sua Senha:");

            for (User u : repositorioUsuario.acharTodos()) {
                if (u instanceof Cliente c && c.getCpf().equals(cpf) && c.getPassword().equals(senha)) {
                    ui.mostrarMensagem("Login", "Bem-vindo, " + c.getNomeCompleto() + ".");
                    menuCliente(c);
                    return;
                }
            }
            ui.mostrarErro("Erro", "Credenciais inválidas!");
        } catch (OperacaoCanceladaException e) { /* Volta pro menu */ }
    }

    private static void cadastrarClienteUI() {
        try {
            String nomeCompleto = ui.pedirTexto("Nome Completo:");
            String email = ui.pedirTexto("Email:");
            String password = ui.pedirTexto("Senha:");
            String cpf = ui.pedirTexto("CPF:");
            String logradouro = ui.pedirTexto("Endereço - Logradouro (Rua/Av):");
            String numero = ui.pedirTexto("Endereço - Número:");
            String bairro = ui.pedirTexto("Endereço - Bairro:");
            String cidade = ui.pedirTexto("Endereço - Cidade:");
            String estado = ui.pedirTexto("Endereço - Estado (UF):");

            // Opcional pode ficar com um traço se a pessoa digitar vazio
            String complemento = ui.pedirTexto("Endereço - Complemento (Digite '-' se não houver):");

            // 1. Cria o Endereço APENAS UMA VEZ
            Endereco endereco = new Endereco(logradouro, numero, bairro, cidade, estado, complemento);

            // 2. O repositório cria o cliente e salva no arquivo automaticamente!
            repositorioUsuario.cadastrarCliente(nomeCompleto, email, password, endereco, cpf);

            ui.mostrarMensagem("Sucesso", "Cliente registrado com sucesso!");
        } catch (OperacaoCanceladaException e) {
            // Cadastro cancelado no meio
        } catch (Exception e) {
            ui.mostrarErro("Erro", "Falha ao registrar cliente: " + e.getMessage());
        }
    }

    private static void cadastrarAdminUI() {
        try {
            String nome = ui.pedirTexto("Nome do Admin:");
            String email = ui.pedirTexto("Email:");
            String senha = ui.pedirTexto("Senha:");

            // O Repositório já gera o código "ADMIN00X" e guarda no ficheiro
            repositorioUsuario.cadastrarAdmin(nome, email, senha, new Endereco());

            ui.mostrarMensagem("Sucesso", "Admin cadastrado com sucesso!");
        } catch (OperacaoCanceladaException e) { /* Volta pro menu */ }
        catch (Exception e) {
            ui.mostrarErro("Erro", "Falha ao registrar admin: " + e.getMessage());
        }
    }
}