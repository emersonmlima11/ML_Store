package br.ufpb.iago.mlStore.excepcions;

public class OperacaoCanceladaException extends Exception {
    public OperacaoCanceladaException() {
        super("Operação cancelada pelo usuário.");
    }
}