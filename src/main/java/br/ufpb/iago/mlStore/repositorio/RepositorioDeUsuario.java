package br.ufpb.iago.mlStore.repositorio;

import br.ufpb.iago.mlStore.armazenamento.PersistenciaDeUsuarios;
import br.ufpb.iago.mlStore.modelo.Admin;
import br.ufpb.iago.mlStore.modelo.Cliente;
import br.ufpb.iago.mlStore.modelo.Endereco;
import br.ufpb.iago.mlStore.modelo.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioDeUsuario {
    private List<User> users;
    private static int contadorAdmin;
    private static PersistenciaDeUsuarios pdu = new PersistenciaDeUsuarios();

    public RepositorioDeUsuario() throws IOException {
        users = pdu.carregarUsuarios(new ArrayList<>());
        contadorAdmin = contarAdmin();
        if(!verificarAdmin()){
            this.criarAdminPadrao();
        }
    }

    private void criarAdminPadrao() throws IOException{
        User adminRaiz = new Admin("admin", "admin@email.com", "admin123", new Endereco(), "ADMIN000");
        users.add(adminRaiz);
        pdu.salvar(users);

    }

    private boolean verificarAdmin(){
        for(User u : this.users){
            if(u instanceof Admin){
                return true;
            }
        }
        return false;
    }

    private int contarAdmin(){
        int contador = 0;
        for(User u : this.users){
            if(u instanceof Admin){
                contador++;
            }
        }
        return contador;
    }

    public  List<User> acharTodos() {
        return users;
    }

    public void cadastrarAdmin(String nome, String email, String password, Endereco end) throws IOException{
        String codigo = "ADMIN";
        codigo += String.format("%03d", contadorAdmin);
        User adm = new Admin(nome, email, password, end, codigo);
        users.add(adm);
        contadorAdmin++;
        pdu.salvar(users);
    }

    public void cadastrarCliente(String nome, String email, String password, Endereco end, String cpf) throws IOException{
        User cliente = new Cliente(nome, email, password, end, cpf);
        users.add(cliente);
        pdu.salvar(users);
    }

    public void removerCliente(String cpf) throws IOException{
        users.removeIf(u -> u instanceof Cliente c && c.getCpf().equals(cpf));
        pdu.salvar(users);
    }

    public void removerAdmin(String codigo) throws IOException{
        users.removeIf(u -> u instanceof Admin a && a.getCodigoDeAcesso().equals(codigo));
        pdu.salvar(users);
    }
}