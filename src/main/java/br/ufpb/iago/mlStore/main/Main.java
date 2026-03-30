package br.ufpb.iago.mlStore.main;

import br.ufpb.iago.mlStore.armazenamento.*;
import br.ufpb.iago.mlStore.gerenciamento.*;
import br.ufpb.iago.mlStore.modelo.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Instâncias Globais de Persistência e Gerenciamento
    private static PersistenciaDeUsuarios persistenciaUsuarios = new PersistenciaDeUsuarios();
    private static PersistenciaDeTipos persistenciaTipos = new PersistenciaDeTipos();

    private static GerenciadorDeProduto gerenciadorProduto;
    private static GerenciadorDePedido gerenciadorPedido;

    private static List<User> listaUsuarios = new ArrayList<>();
    private static List<TipoProduto> listaTipos = new ArrayList<>();

    public static void main(String[] args) {
        // Inicializa os dados carregando dos arquivos (txt)
        inicializarSistema();

        boolean rodando = true;

        while (rodando) {
            String[] opcoes = {"Entrar como Admin", "Entrar como Cliente", "Cadastrar Cliente", "Sair"};
            int escolha = JOptionPane.showOptionDialog(null,
                    "Bem-vindo ao mlStore!\nEscolha uma opção:",
                    "Menu Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (escolha) {
                case 0: loginAdmin(); break;
                case 1: loginCliente(); break;
                case 2: cadastrarCliente(); break;
                case 3:
                case JOptionPane.CLOSED_OPTION:
                    rodando = false;
                    JOptionPane.showMessageDialog(null, "Saindo do sistema... Até logo!");
                    break;
            }
        }
    }

    private static void inicializarSistema() {
        try {
            // Tenta carregar os tipos
            try {
                listaTipos = persistenciaTipos.carregar();
            } catch (Exception e) {
                listaTipos = new ArrayList<>(); // Se falhar/não existir, inicia vazio
            }

            // Inicia o Gerenciador de Produtos passando os tipos
            gerenciadorProduto = new GerenciadorDeProduto(listaTipos);

            // Inicia os usuários passando uma lista vazia de pedidos inicialmente para evitar erro de loop infinito
            try {
                listaUsuarios = persistenciaUsuarios.carregarUsuarios(new ArrayList<>());
            } catch (Exception e) {
                listaUsuarios = new ArrayList<>();
            }

            // Inicia o Gerenciador de Pedidos
            gerenciadorPedido = new GerenciadorDePedido(listaUsuarios, gerenciadorProduto.listarProdutos());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inicializar os arquivos do sistema: " + e.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================== LOGINS ==================

    private static void loginAdmin() {
        try {
            String codigoDeAcesso = solicitarDado("Digite o Código de Acesso do Admin:");
            String senha = solicitarDado("Digite a Senha:");

            Admin adminLogado = null;

            for (User u : listaUsuarios) {
                if (u instanceof Admin) {
                    Admin a = (Admin) u;
                    if (a.getCodigoDeAcesso().equals(codigoDeAcesso) && a.getPassword().equals(senha)) {
                        adminLogado = a;
                        break;
                    }
                }
            }

            // Fallback: Se não houver nenhum admin cadastrado no sistema, entra com admin/admin
            if (codigoDeAcesso.equals("admin") && senha.equals("admin")) {
                adminLogado = new Admin("Administrador Padrão", "admin@mlstore.com", "admin", new Endereco(), "admin");
                // Adiciona e salva para usos futuros
                if(listaUsuarios.isEmpty()) {
                    listaUsuarios.add(adminLogado);
                    persistenciaUsuarios.salvar(listaUsuarios);
                }
            }

            if (adminLogado != null) {
                JOptionPane.showMessageDialog(null, "Acesso concedido!\nBem-vindo, " + adminLogado.getNomeCompleto() + ".");
                menuAdmin();
            } else {
                JOptionPane.showMessageDialog(null, "Código de Acesso ou Senha incorretos!", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Silencioso se cancelar
        }
    }

    private static void loginCliente() {
        try {
            String cpf = solicitarDado("Digite seu CPF:");
            String senha = solicitarDado("Digite sua Senha:");

            Cliente clienteLogado = null;

            for (User u : listaUsuarios) {
                if (u instanceof Cliente) {
                    Cliente c = (Cliente) u;
                    if (c.getCpf().equals(cpf) && c.getPassword().equals(senha)) {
                        clienteLogado = c;
                        break;
                    }
                }
            }

            if (clienteLogado != null) {
                JOptionPane.showMessageDialog(null, "Acesso concedido!\nBem-vindo, " + clienteLogado.getNomeCompleto() + ".");
                menuCliente(clienteLogado);
            } else {
                JOptionPane.showMessageDialog(null, "CPF ou Senha incorretos!", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Silencioso se cancelar
        }
    }

    // ================== CADASTROS ==================

    private static void cadastrarCliente() {
        try {
            String nomeCompleto = solicitarDado("Nome Completo:");
            String email = solicitarDado("Email:");
            String password = solicitarDado("Senha:");
            String cpf = solicitarDado("CPF:");

            String logradouro = solicitarDado("Endereço - Logradouro (Rua/Av):");
            String numero = solicitarDado("Endereço - Número:");
            String bairro = solicitarDado("Endereço - Bairro:");
            String cidade = solicitarDado("Endereço - Cidade:");
            String estado = solicitarDado("Endereço - Estado (UF):");

            String complemento = JOptionPane.showInputDialog("Endereço - Complemento (Opcional):");
            if (complemento == null) complemento = "";

            Endereco endereco = new Endereco(logradouro, numero, bairro, cidade, estado, complemento);
            Cliente novoCliente = new Cliente(nomeCompleto, email, password, endereco, cpf);

            listaUsuarios.add(novoCliente);
            persistenciaUsuarios.salvar(listaUsuarios);

            JOptionPane.showMessageDialog(null, "Usuário registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Operação cancelada
        }
    }

    // ================== MENUS ==================

    private static void menuAdmin() {
        boolean rodando = true;
        while (rodando) {
            String[] opcoes = {"Cadastrar Produto", "Listar Produtos", "Cadastrar Admin", "Voltar"};
            int escolha = JOptionPane.showOptionDialog(null,
                    "Painel de Administração", "Menu Admin",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, opcoes, opcoes[0]);

            try {
                switch (escolha) {
                    case 0:
                        // Cadastrar Produto
                        if (listaTipos.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Não há Tipos de Produto cadastrados. Cadastrando um tipo padrão 'Geral'...");
                            TipoProduto tipoPadrao = new TipoProduto("Geral", 10.0);
                            listaTipos.add(tipoPadrao);
                            persistenciaTipos.salvar(listaTipos);
                        }

                        int id = Integer.parseInt(solicitarDado("ID do Produto (Numérico):"));
                        String nome = solicitarDado("Nome do Produto:");
                        double preco = Double.parseDouble(solicitarDado("Preço do Produto (Ex: 15.50):"));
                        int estoque = Integer.parseInt(solicitarDado("Quantidade em Estoque:"));

                        // Mostra opções de tipos
                        String[] nomesTipos = new String[listaTipos.size()];
                        for (int i = 0; i < listaTipos.size(); i++) {
                            nomesTipos[i] = listaTipos.get(i).getNome();
                        }

                        String tipoEscolhido = (String) JOptionPane.showInputDialog(null, "Escolha o Tipo:",
                                "Tipo de Produto", JOptionPane.QUESTION_MESSAGE, null, nomesTipos, nomesTipos[0]);

                        if (tipoEscolhido == null) break;

                        TipoProduto tipoSelecionado = null;
                        for (TipoProduto tp : listaTipos) {
                            if (tp.getNome().equals(tipoEscolhido)) {
                                tipoSelecionado = tp;
                                break;
                            }
                        }

                        Produto novoProduto = new Produto(id, nome, preco, estoque, tipoSelecionado);
                        gerenciadorProduto.cadastrarProduto(novoProduto);
                        JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
                        break;

                    case 1:
                        // Listar Produtos
                        List<Produto> produtos = gerenciadorProduto.listarProdutos();
                        StringBuilder txtProdutos = new StringBuilder("Produtos no Sistema:\n\n");
                        for (Produto p : produtos) {
                            txtProdutos.append(p.toString()).append(" | Tipo: ").append(p.getTipo().getNome()).append("\n");
                        }
                        mostrarLista("Produtos", txtProdutos.toString());
                        break;

                    case 2:
                        // Cadastrar Admin
                        String nomeAdmin = solicitarDado("Nome do Admin:");
                        String emailAdmin = solicitarDado("Email:");
                        String senhaAdmin = solicitarDado("Senha:");
                        String codigoAcesso = solicitarDado("Código de Acesso (Login):");

                        Admin novoAdmin = new Admin(nomeAdmin, emailAdmin, senhaAdmin, new Endereco(), codigoAcesso);
                        listaUsuarios.add(novoAdmin);
                        persistenciaUsuarios.salvar(listaUsuarios);
                        JOptionPane.showMessageDialog(null, "Admin cadastrado com sucesso!");
                        break;

                    case 3:
                    case JOptionPane.CLOSED_OPTION:
                        rodando = false;
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro: Você deve digitar números válidos para ID, Preço e Estoque.", "Erro de Digitação", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Operação cancelada ou erro ocorrido: " + e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private static void menuCliente(Cliente cliente) {
        boolean rodando = true;
        while (rodando) {
            String[] opcoes = {"Ver Catálogo", "Novo Pedido", "Meus Pedidos", "Voltar"};
            int escolha = JOptionPane.showOptionDialog(null,
                    "Área do Cliente", "Menu Cliente",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, opcoes, opcoes[0]);

            try {
                switch (escolha) {
                    case 0:
                        // Catálogo
                        List<Produto> produtos = gerenciadorProduto.listarProdutos();
                        StringBuilder txtProdutos = new StringBuilder("Catálogo Disponível:\n\n");
                        for (Produto p : produtos) {
                            txtProdutos.append(p.toString()).append("\n");
                        }
                        mostrarLista("Catálogo", txtProdutos.toString());
                        break;

                    case 1:
                        // Novo Pedido
                        gerenciadorPedido.criarPedido(cliente);

                        // O último pedido criado pertence a este cliente
                        List<Pedido> pedidosDoCliente = gerenciadorPedido.listarPedidosPorCliente(cliente.getCpf());
                        Pedido pedidoAtual = pedidosDoCliente.get(pedidosDoCliente.size() - 1);

                        boolean adicionando = true;
                        while(adicionando) {
                            String idBusca = JOptionPane.showInputDialog("Digite o ID do produto para adicionar ao carrinho (ou deixe vazio para finalizar):");
                            if (idBusca == null || idBusca.trim().isEmpty()) {
                                adicionando = false;
                                break;
                            }

                            try {
                                Produto produtoEncontrado = gerenciadorProduto.buscarProdutoPorId(Integer.parseInt(idBusca));
                                gerenciadorPedido.adicionarProduto(pedidoAtual.getIdPedido(), produtoEncontrado);
                                JOptionPane.showMessageDialog(null, produtoEncontrado.getNome() + " adicionado ao pedido!");
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Produto não encontrado ou erro: " + e.getMessage());
                            }
                        }

                        // Finalizar o pedido se tiver itens
                        if (!pedidoAtual.getProdutos().isEmpty()) {
                            gerenciadorPedido.finalizarPedido(pedidoAtual.getIdPedido());
                            JOptionPane.showMessageDialog(null, "Pedido " + pedidoAtual.getIdPedido() + " finalizado com sucesso!");
                        } else {
                            gerenciadorPedido.cancelarPedido(pedidoAtual.getIdPedido());
                            JOptionPane.showMessageDialog(null, "Pedido cancelado (Carrinho vazio).");
                        }
                        break;

                    case 2:
                        // Histórico de Pedidos
                        List<Pedido> meusPedidos = gerenciadorPedido.listarPedidosPorCliente(cliente.getCpf());
                        if (meusPedidos.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Você ainda não possui pedidos.");
                        } else {
                            StringBuilder txtPedidos = new StringBuilder("Seu Histórico:\n\n");
                            for (Pedido p : meusPedidos) {
                                txtPedidos.append("ID: ").append(p.getIdPedido())
                                        .append(" | Status: ").append(p.getStatus())
                                        .append(" | R$ ").append(String.format("%.2f", p.getValorTotal())).append("\n");
                            }
                            mostrarLista("Meus Pedidos", txtPedidos.toString());
                        }
                        break;

                    case 3:
                    case JOptionPane.CLOSED_OPTION:
                        rodando = false;
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na operação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ================== UTILITÁRIOS ==================

    private static String solicitarDado(String mensagem) {
        String dado = JOptionPane.showInputDialog(mensagem);
        if (dado == null) {
            throw new RuntimeException("Cancelado");
        }
        if (dado.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo não pode ficar vazio.");
        }
        return dado;
    }

    private static void mostrarLista(String titulo, String conteudo) {
        if (conteudo.trim().endsWith(":\n")) {
            conteudo += "Nenhum item encontrado.";
        }
        JOptionPane.showMessageDialog(null, conteudo, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}
