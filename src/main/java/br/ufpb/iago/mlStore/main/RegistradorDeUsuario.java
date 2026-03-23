package br.ufpb.iago.mlStore.main;

import br.ufpb.iago.mlStore.modelo.Cliente;
import br.ufpb.iago.mlStore.modelo.User;

import java.util.Scanner;

public class RegistradorDeUsuario {
    Scanner sc = new Scanner(System.in);
    private String nomeCompleto;
    private String password;
    private int id;


    public User guardar() {
        User user = new Cliente();

        try {
            System.out.println("Coloque Nome do Usuario: ");
            nomeCompleto = sc.nextLine();
            if (nomeCompleto.trim().isEmpty()) {
                throw new IllegalArgumentException("O nome do usuário não pode ser vazio.");
            }
            user.setNomeCompleto(nomeCompleto);

            System.out.println("Coloque Senha: ");
            password = sc.nextLine();
            if (password.trim().isEmpty()) {
                throw new IllegalArgumentException("A senha não pode ser vazia.");
            }
            user.setPassword(password);

            System.out.println("Usuário registrado com sucesso!");
            return user;

        } catch (IllegalArgumentException e) {
            System.out.println("Erro de entrada: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado ao ler os dados do usuário.");
        }

        return null; // Retorna null se o cadastro falhar
    }
}
