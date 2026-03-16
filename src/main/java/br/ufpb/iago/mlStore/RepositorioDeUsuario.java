package br.ufpb.iago.mlStore;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeUsuario {
    static List<User> users = new ArrayList<User>();
    private static int id = 0;
    public static List<User> acharTodos(){
        return users;
    }

    public static void salvar(User user){
        user.setId(++id);

        users.add(user);

    }
}
