package br.ufpb.iago.mlStore.excepcions;

public class ErroAoLerArquivoException extends RuntimeException {
    public ErroAoLerArquivoException(String message) {
        super(message);
    }


    public ErroAoLerArquivoException(String message, Throwable cause) {
        super(message, cause);
    }
}