package br.ufpb.iago.mlStore.repositorio;

import br.ufpb.iago.mlStore.modelo.Admin;
import br.ufpb.iago.mlStore.modelo.User;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeUsuario {
    static List<User> users = new ArrayList<User>();
    private static int id = 0;


    static {
        Admin adminRaiz = new Admin();
        adminRaiz.setId(++id);
        adminRaiz.setNomeCompleto("admin");
        adminRaiz.setPassword("admin123");
        users.add(adminRaiz);
    }

    public static List<User> acharTodos() {
        return users;
    }

    public static void salvar(User user) {
        user.setId(++id);
        users.add(user);
    }
}