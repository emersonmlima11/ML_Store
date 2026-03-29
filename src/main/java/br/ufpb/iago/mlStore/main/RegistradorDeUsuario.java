package br.ufpb.iago.mlStore.main;

import br.ufpb.iago.mlStore.modelo.Cliente;
import br.ufpb.iago.mlStore.modelo.Endereco;
import br.ufpb.iago.mlStore.modelo.User;

import java.util.Scanner;

public class RegistradorDeUsuario {
    private Scanner sc = new Scanner(System.in);

    public User guardar() {
        try {


            System.out.print("Coloque o Nome Completo: ");
            String nomeCompleto = sc.nextLine();
            if (nomeCompleto.trim().isEmpty()) {
                throw new IllegalArgumentException("O nome do utilizador não pode ser vazio.");
            }

            System.out.print("Coloque o Email: ");
            String email = sc.nextLine();
            if (email.trim().isEmpty()) {
                throw new IllegalArgumentException("O email não pode ser vazio.");
            }

            System.out.print("Coloque a Senha: ");
            String password = sc.nextLine();
            if (password.trim().isEmpty()) {
                throw new IllegalArgumentException("A senha não pode ser vazia.");
            }

            System.out.print("Coloque o CPF: ");
            String cpf = sc.nextLine();
            if (cpf.trim().isEmpty()) {
                throw new IllegalArgumentException("O CPF não pode ser vazio.");
            }

            System.out.println("\n--- Dados de Endereço ---");
            System.out.print("Logradouro (Rua/Av): ");
            String logradouro = sc.nextLine();

            System.out.print("Número: ");
            String numero = sc.nextLine();

            System.out.print("Bairro: ");
            String bairro = sc.nextLine();

            System.out.print("Cidade: ");
            String cidade = sc.nextLine();

            System.out.print("Estado (UF): ");
            String estado = sc.nextLine();

            System.out.print("Complemento: ");
            String complemento = sc.nextLine();

            // 1. Cria o objeto Endereco com os dados recolhidos
            Endereco endereco = new Endereco(logradouro, numero, bairro, cidade, estado, complemento);

            // 2. Cria o Cliente utilizando o construtor completo
            User novoCliente = new Cliente(nomeCompleto, email, password, endereco, cpf);

            System.out.println("\nUtilizador registado com sucesso!");
            return novoCliente;

        } catch (IllegalArgumentException e) {
            System.out.println("Erro de entrada: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado ao ler os dados do utilizador.");
        }

        return null; // Retorna null se o registo falhar
    }
}