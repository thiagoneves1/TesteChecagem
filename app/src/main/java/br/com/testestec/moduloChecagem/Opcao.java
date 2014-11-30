package br.com.testestec.moduloChecagem;


public class Opcao {

    int id;
    int idPergunta;
    String valorTexto;
    String valorResposta;

    public String getValorResposta() {
        return valorResposta;
    }

    public void setValorResposta(String valorResposta) {
        this.valorResposta = valorResposta;
    }

    public String getValorTexto() {
        return valorTexto;
    }

    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
