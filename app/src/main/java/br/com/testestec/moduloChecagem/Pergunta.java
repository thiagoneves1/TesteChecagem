package br.com.testestec.moduloChecagem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandre2013 on 27/11/14.
 */
public class Pergunta {
    int id;
    int idItemDaCategoria;
    String tipo;
    int opcional;
    int respondida;
    int idExterno;
    Condicao condicao;
    String resposta;
    int condicional;

    public int getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(int idExterno) {
        this.idExterno = idExterno;
    }

    public Condicao getCondicao() {
        return condicao;
    }

    public void setCondicao(Condicao condicao) {
        this.condicao = condicao;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public int getRespondida() {
        return respondida;
    }

    public void setRespondida(int respondida) {
        this.respondida = respondida;
    }

    public List<Opcao> getListaDeOpcoes() {
        return listaDeOpcoes;
    }

    public void setListaDeOpcoes(List<Opcao> listaDeOpcoes) {
        this.listaDeOpcoes = listaDeOpcoes;
    }

    List<Opcao> listaDeOpcoes = new ArrayList<Opcao>();

    public int getCondicional() {
        return condicional;
    }

    public void setCondicional(int condicional) {
        this.condicional = condicional;
    }

    public int getOpcional() {
        return opcional;
    }

    public void setOpcional(int opcional) {
        this.opcional = opcional;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdItemDaCategoria() {
        return idItemDaCategoria;
    }

    public void setIdItemDaCategoria(int idItemDaCategoria) {
        this.idItemDaCategoria = idItemDaCategoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
