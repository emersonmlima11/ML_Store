package br.ufpb.iago.mlStore.excepcions;

public class ArquivoCorrompidoException extends RuntimeException {
    public ArquivoCorrompidoException(String message) {
        super(message);
    }
}
