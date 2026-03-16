package br.ufpb.iago.mlStore;

import java.util.Scanner;

public class RegistradorDeUsuario {
    Scanner sc = new Scanner(System.in);
    private String nomeCompleto;
    private String password;
    private int id;


    public User guardar(){

        User user = new User();

        System.out.println("Coloque Nome do Usuario: ");
        nomeCompleto = sc.nextLine();
        user.setNomeCompleto(nomeCompleto);


        System.out.println("Coloque Senha: ");
        password = sc.nextLine();
        user.setPassword(password);
        return user;

    }
}
