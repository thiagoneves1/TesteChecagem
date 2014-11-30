package br.com.testestec.moduloChecagem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;


public class BancoDeDadosChecagem extends SQLiteOpenHelper {

private static final int VERSAO_BANCO = 1;
private static final String NOME_BANCO = "bancoModulo";

    public BancoDeDadosChecagem(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sqlItem ="CREATE TABLE IF NOT EXISTS itemChecagem (id INTEGER PRIMARY KEY, idExterno INTEGER, tituloItem TEXT, app TEXT, status INTEGER)";
        String sqlCategoria = "CREATE TABLE IF NOT EXISTS categoria(id INTEGER PRIMARY KEY, idItem INTEGER, nomeCategoria TEXT, FOREIGN KEY(idItem) REFERENCES itemChecagem(id))";
        String sqlItemCategoria = "CREATE TABLE IF NOT EXISTS itemDaCategoria (id INTEGER PRIMARY KEY, idCategoria INTEGER, itemTitulo TEXT, idExternoItemDaCategoria INTEGER, FOREIGN KEY(idCategoria) REFERENCES categoria(id))";
        String sqlPergunta = "CREATE TABLE IF NOT EXISTS pergunta (id INTEGER PRIMARY KEY, idExterno INTEGER, idItemDaCategoria INTEGER, tipo TEXT, opcional INTEGER, condicional INTEGER, respondida INTEGER, resposta TEXT, FOREIGN KEY(idItemDaCategoria) REFERENCES itemDaCategoria(id))";
        String sqlOpcao = "CREATE TABLE IF NOT EXISTS opcao (id INTEGER PRIMARY KEY,idPergunta INTEGER, valorTexto TEXT, valorResposta TEXT,FOREIGN KEY(idPergunta)  REFERENCES pergunta(id))";
        String sqlCondicao = "CREATE TABLE IF NOT EXISTS condicao (id INTEGER PRIMARY KEY, idPergunta INTEGER, idPerguntaEmCondicional INTEGER, valorResposta TEXT, FOREIGN KEY(idPergunta) REFERENCES pergunta(id))";

        db.execSQL(sqlItem);
        db.execSQL(sqlCategoria);
        db.execSQL(sqlItemCategoria);
        db.execSQL(sqlPergunta);
        db.execSQL(sqlOpcao);
        db.execSQL(sqlCondicao);

        Log.i("BANCO", "BANCO CRIADO COM SUCESSO");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public long insereItemChecagem(int idExterno, String tituloChecagem, String app)
    {
        Log.i("BANCO", String.valueOf(idExterno) + " - " + tituloChecagem);
        long retorno;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idExterno", idExterno);
        values.put("tituloItem",tituloChecagem);
        values.put("app", app);
        values.put("status",0);//sempre com status 0
        retorno = db.insert("itemChecagem", null, values);
        db.close();
        return retorno;
    }

    public long insereCatetoria(String nomeCategoria, long idItemChecagem)
    {
        long retorno;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idItem",idItemChecagem);
        values.put("nomeCategoria",nomeCategoria);
        retorno = db.insert("categoria", null, values);
        db.close();
        return retorno;
    }

    public long insereItemDaCategoria(String tituloItemDaCategoria, long idCategoria, long idExternoItemDaCategoria) {
        long retorno;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idCategoria",idCategoria);
        values.put("itemTitulo",tituloItemDaCategoria);
        values.put("idExternoItemDaCategoria",idExternoItemDaCategoria);
        retorno = db.insert("itemDaCategoria", null, values);
        db.close();
        return retorno;
    }

    public List<Integer> obterListaIdExterno(){
        List<Integer> valores = new ArrayList<Integer>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT idExterno FROM itemChecagem";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
            {
                valores.add(cursor.getInt(0));
            }
        db.close();
        return valores;
        }

    public List<String> obterListaTituloItemChecagem()
    {
        List<String> valores = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT tituloItem FROM itemChecagem";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
            {
                valores.add(cursor.getString(0));
            }
        db.close();
        return valores;
    }

    public int obetStatus(int idExterno){
        int retorno=2;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT status FROM itemChecagem WHERE idExterno =" +idExterno;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
            {
                retorno = cursor.getInt(0);
            }
        db.close();
        return retorno;
    }

    public ItemChecagem obterItemChecagem(int idExterno){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM itemChecagem WHERE idExterno =" +idExterno;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();
        ItemChecagem itemChecagem = new ItemChecagem();
        itemChecagem.setId(cursor.getInt(0));
        itemChecagem.setIdExterno(cursor.getInt(1));
        itemChecagem.setTituloItem(cursor.getString(2));
        itemChecagem.setApp(cursor.getString(3));
        itemChecagem.setStatus(cursor.getInt(4));
        db.close();
        return itemChecagem;
    }

    public Condicao obterCondicao(int id) {
        Condicao condicao = new Condicao();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM condicao WHERE idPergunta =" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            condicao.setId(cursor.getInt(0));
            condicao.setIdPergunta(cursor.getInt(1));
            condicao.setIdPerguntaEmCondicional(cursor.getInt(2));
            condicao.setValorResposta(cursor.getString(3));
        }
       db.close();
        return  condicao;
    }

    public List<Categoria> obterListaCategorias(int idItemChecagem) {
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM categoria WHERE idItem =" + idItemChecagem;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
            {
                Categoria categoria = new Categoria();
                categoria.setId(cursor.getInt(0));
                categoria.setIdItem(cursor.getInt(1));
                categoria.setNome(cursor.getString(2));
                listaCategorias.add(categoria);
            }
        db.close();
        return  listaCategorias;
    }

    public List<ItemChecagemDaCategoria> obterListaItemDaCategoria(int idCategoria)  {
        List<ItemChecagemDaCategoria> listaChecagem = new ArrayList<ItemChecagemDaCategoria>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM itemDaCategoria WHERE idCategoria =" + idCategoria;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
            {
                ItemChecagemDaCategoria ic = new ItemChecagemDaCategoria();
                ic.setId(cursor.getInt(0));
                ic.setIdCategoria(cursor.getInt(1));
                ic.setTitulo(cursor.getString(2));
                ic.setIdExternoItemDaCategoria(cursor.getInt(3));
                listaChecagem.add(ic);
            }
        db.close();
        return  listaChecagem;
    }

    public List<Pergunta> obterListaPerguntasDoItem(int id) {
        List<Pergunta> listaPergunta = new ArrayList<Pergunta>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM pergunta WHERE idItemDaCategoria =" + id;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
        {
            Pergunta pergunta = new Pergunta();
            pergunta.setId(cursor.getInt(0));
            pergunta.setIdExterno(cursor.getInt(1));
            pergunta.setIdItemDaCategoria(cursor.getInt(2));
            pergunta.setTipo(cursor.getString(3));
            pergunta.setOpcional(cursor.getInt(4));
            pergunta.setCondicional(cursor.getInt(5));
            pergunta.setRespondida(cursor.getInt(6));
            pergunta.setResposta(cursor.getString(7));
            listaPergunta.add(pergunta);
        }
        db.close();
        return listaPergunta;
    }
    public List<Opcao> obterListaOpcoesDaPergunta(int id) {
        List<Opcao> listaOpcao = new ArrayList<Opcao>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM opcao WHERE idPergunta =" + id;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
        {
            Opcao opcao = new Opcao();
            opcao.setId(cursor.getInt(0));
            opcao.setIdPergunta(cursor.getInt(1));
            opcao.setValorTexto(cursor.getString(2));
            opcao.setValorResposta(cursor.getString(3));
            listaOpcao.add(opcao);
        }
        return listaOpcao;
    }

    public long inserePerguntaDoItemDaCategoria(long idItemDaCategoria, String tipo, int idExterno, int valorOpcional, int valorCondicional) {
        long retorno;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idItemDaCategoria",idItemDaCategoria);
        values.put("idExterno", idExterno);
        values.put("tipo", tipo);
        values.put("opcional",valorOpcional);
        values.put("condicional",valorCondicional);
        values.put("respondida",0);//default
        retorno =db.insert("pergunta",null,values);
        db.close();
        return retorno;
    }

    public void insereOpcao(long idPerguntaDoItem, String texto, String resposta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idPergunta",idPerguntaDoItem);
        values.put("valorTexto",texto);
        values.put("valorResposta",resposta);
        db.insert("opcao",null,values);
        db.close();
    }

    public void insereCondicao(long idPerguntaDoItem, int idPergunta, String valorResposta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idPergunta",idPerguntaDoItem);
        values.put("idPerguntaEmCondicional",idPergunta);
        values.put("valorResposta",valorResposta);
        db.insert("condicao", null,values);
        db.close();

    }

    public void atualizaPerguntas(Pergunta pergunta) {
        String filtro = "id=" + pergunta.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("respondida",pergunta.getRespondida());
        values.put("resposta",pergunta.getResposta());
        db.update("pergunta", values, filtro, null);
        db.close();
    }

    public void atualizaStatusDoitem(int idExterno) {
        String filtro = "idExterno=" + idExterno;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", 1);
        db.update("itemChecagem", values,filtro,null);
        db.close();
    }

    public List<Categoria> obterCategoriasRespondidasDoApp(int idItemChecagem) {
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM categoria WHERE idItem =" + idItemChecagem;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
        {
            Categoria categoria = new Categoria();
            categoria.setId(cursor.getInt(0));
            categoria.setIdItem(cursor.getInt(1));
            categoria.setNome(cursor.getString(2));
            listaCategorias.add(categoria);
        }
        db.close();
        return  listaCategorias;
    }

    public int obterIdItemChecagem(String nomeApp) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id FROM itemChecagem WHERE app LIKE '" + nomeApp + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        db.close();
        return 0;
    }
}