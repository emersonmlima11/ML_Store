package br.ufpb.iago.mlStore.excepcions;

public class ErroAoSalvarArquivoException extends RuntimeException {
    public ErroAoSalvarArquivoException(String message) {
        super(message);

    }
    public ErroAoSalvarArquivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
