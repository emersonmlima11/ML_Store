package br.ufpb.iago.mlStore.excepcions;

public class PedidoStatusInvalidoException extends RuntimeException {
    public PedidoStatusInvalidoException(String message) {
        super(message);
    }
}
