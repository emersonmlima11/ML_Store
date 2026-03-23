package br.ufpb.iago.mlStore.modelo;

public class Endereco {
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;

    public Endereco(String logradouro, String numero,
                    String bairro, String cidade, String estado, String complemento) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.complemento = complemento;
    }
    public Endereco(String logradouro, String numero,
                    String bairro, String cidade, String estado) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;

    }
    public Endereco() {
        this("","","","","", "");
    }
    public String getLogradouro() {
        return logradouro;
    }
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    @Override
    public String toString() {
        return "Endereco [logradouro=" + logradouro +
                ", numero=" + numero + ", bairro=" + bairro +
                ", cidade=" + cidade
                + ", estado=" + estado + "]";
    }
    public String getBairro() {
        return bairro;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getComplemento() {
        return complemento;
    }
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String format() {
        return this.logradouro + ";" + this.numero + ";" + this.bairro + ";" + this.cidade + ";" + this.estado +  ";" + this.complemento;
    }
}
