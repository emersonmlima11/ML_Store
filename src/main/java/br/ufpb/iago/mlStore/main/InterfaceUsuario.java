package br.ufpb.iago.mlStore.main;

import br.ufpb.iago.mlStore.excepcions.OperacaoCanceladaException;
import javax.swing.JOptionPane;

public class InterfaceUsuario {

    // Mostra mensagens genéricas
    public void mostrarMensagem(String titulo, String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    // Mostra mensagens de erro
    public void mostrarErro(String titulo, String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
    }

    // Cria menus de forma limpa e devolve o index escolhido
    public int mostrarMenu(String titulo, String subtitulo, String[] opcoes) {
        int escolha = JOptionPane.showOptionDialog(null, subtitulo, titulo,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opcoes, opcoes[0]);

        if (escolha == JOptionPane.CLOSED_OPTION) {
            return -1; // Representa que fechou a janela
        }
        return escolha;
    }

    // Solicita texto garantindo que não seja vazio e que não quebre seu .txt
    public String pedirTexto(String mensagem) throws OperacaoCanceladaException {
        String input = JOptionPane.showInputDialog(mensagem);
        if (input == null) {
            throw new OperacaoCanceladaException();
        }
        if (input.trim().isEmpty()) {
            mostrarErro("Erro", "O campo não pode ficar vazio. Tente novamente.");
            return pedirTexto(mensagem); // Chama de novo até digitar certo (recursão simples)
        }
        return input.replace(";", ","); // Protege seu banco de dados
    }

    // Solicita Inteiro e já faz o Try-Catch internamente!
    public int pedirInteiro(String mensagem) throws OperacaoCanceladaException {
        while (true) {
            String input = pedirTexto(mensagem);
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                mostrarErro("Entrada Inválida", "Por favor, digite apenas números inteiros.");
            }
        }
    }

    // Solicita Double e já faz o Try-Catch internamente!
    public double pedirDouble(String mensagem) throws OperacaoCanceladaException {
        while (true) {
            String input = pedirTexto(mensagem);
            try {
                // Troca vírgula por ponto para não dar erro se o usuário digitar "15,50"
                return Double.parseDouble(input.replace(",", ".").trim());
            } catch (NumberFormatException e) {
                mostrarErro("Entrada Inválida", "Por favor, digite um valor numérico válido (Ex: 15.50).");
            }
        }
    }
}