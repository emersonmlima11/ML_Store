package br.ufpb.iago.mlStore.main;

import br.ufpb.iago.mlStore.armazenamento.PersistenciaDePedidos;
import br.ufpb.iago.mlStore.armazenamento.PersistenciaDeTipos;
import br.ufpb.iago.mlStore.modelo.Pedido;
import br.ufpb.iago.mlStore.modelo.TipoProduto;

import java.io.IOException;
import java.util.List;

public class Teste2 {
    public static void main(String[] args) throws IOException {
        PersistenciaDeTipos pdt = new PersistenciaDeTipos();
        List<TipoProduto> listapdt = pdt.carregar();
        for(TipoProduto tp : listapdt){
            System.out.println(tp.getNome() + tp.getImposto());
        }
    }
}
