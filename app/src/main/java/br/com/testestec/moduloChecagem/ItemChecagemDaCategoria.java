package br.com.testestec.moduloChecagem;

import java.util.ArrayList;
import java.util.List;


public class ItemChecagemDaCategoria {

    private String titulo;
    private int id;
    private int idExternoItemDaCategoria;
    private int idCategoria;
    private List<Pergunta> listaDePerguntas = new ArrayList<Pergunta>();

    public List<Pergunta> getListaDePerguntas() {
        return listaDePerguntas;
    }

    public void setListaDePerguntas(List<Pergunta> listaDePerguntas) {
        this.listaDePerguntas = listaDePerguntas;
    }

    public int getIdExternoItemDaCategoria() {
        return idExternoItemDaCategoria;
    }

    public void setIdExternoItemDaCategoria(int idExternoItemDaCategoria) {
        this.idExternoItemDaCategoria = idExternoItemDaCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
