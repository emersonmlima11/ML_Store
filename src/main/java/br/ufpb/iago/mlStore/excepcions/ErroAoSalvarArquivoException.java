package br.ufpb.iago.mlStore.excepcions;

public class ErroAoSalvarArquivoException extends RuntimeException {
    public ErroAoSalvarArquivoException(String message) {
        super(message);
    }
}
