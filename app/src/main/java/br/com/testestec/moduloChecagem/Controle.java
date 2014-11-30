package br.com.testestec.moduloChecagem;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import br.com.testestec.testes.R;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Controle {

    public Context context;
    BancoDeDadosChecagem banco;
    private boolean salvaItem = true;

    public Controle(Context context) {
        this.context = context;
        this.banco = new BancoDeDadosChecagem(this.context);
    }

    JSONObject jsonCompleto = null;
    List<Integer> listaIdExterno = new ArrayList<Integer>();
    List<String> listaTituloItemChecagem = new ArrayList<String>();

    public boolean insereJson(String json) {
        boolean retorno = false;
        listaIdExterno = banco.obterListaIdExterno(); //lista somente pra ver se tem o id dentro do banco

        try {

            retorno = verificaSeItemExiste(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return retorno;
    }

    private boolean verificaSeItemExiste(String json) throws JSONException {
        boolean retorno = false;
        jsonCompleto = new JSONObject(json);
        JSONArray jsonPrimeiroNo = jsonCompleto.optJSONArray("checklists");//separa os itens de Checagem
        int tamanhoJsonArray = jsonPrimeiroNo.length();

        for (int i = 0; i < tamanhoJsonArray; i++) {

            JSONObject jsonObjetosUnicos = jsonPrimeiroNo.getJSONObject(i);
            String tituloChecagem = jsonObjetosUnicos.getString("tituloChecagem");
            String nomeApp = jsonObjetosUnicos.getString("App");
            int idExterno = jsonObjetosUnicos.getInt("id");

            if (listaIdExterno.contains(idExterno)) {
                salvaItem = false;
            }

            if (salvaItem) {
                SalvarItem(jsonObjetosUnicos, tituloChecagem, idExterno, nomeApp);
                retorno = true;
            }
        }
        return retorno;
    }

    private void SalvarItem(JSONObject jsonObjetosUnicos, String tituloChecagem, int idExterno, String nomeApp) throws JSONException {
        long idItemChecagem;
        long idCategoria;
        long idItemDaCategoria;
        long idPerguntaDoItem;
        idItemChecagem = banco.insereItemChecagem(idExterno, tituloChecagem, nomeApp);//status 0 ja no metodo no banco
        JSONArray jsonCategorias = jsonObjetosUnicos.optJSONArray("categorias");
        int qtdCategoriaNoObjeto = jsonCategorias.length();

        for (int a = 0; a < qtdCategoriaNoObjeto; a++) {
            JSONObject objetCategoria = jsonCategorias.getJSONObject(a);
            String nomeCategoria = objetCategoria.getString("nomeCategoria");

            JSONArray jsonItensChecagem = objetCategoria.optJSONArray("itensDaCategoria");
            idCategoria = banco.insereCatetoria(nomeCategoria, idItemChecagem);

            int qtdItensNaCategoria = jsonItensChecagem.length();

            for (int b = 0; b < qtdItensNaCategoria; b++) {
                JSONObject objectItemNaCategoria = jsonItensChecagem.getJSONObject(b);
                String tituloItemDaCategoria = objectItemNaCategoria.getString("tituloItem");
                int idExternoTituloItemDaCategoria = objectItemNaCategoria.getInt("id");

                idItemDaCategoria = banco.insereItemDaCategoria(tituloItemDaCategoria, idCategoria, idExternoTituloItemDaCategoria);

                JSONArray jsonPerguntasDoItemDaCategoria = objectItemNaCategoria.optJSONArray("perguntas");

                int qtdPerguntasNoItem = jsonPerguntasDoItemDaCategoria.length();
                for (int c = 0; c < qtdPerguntasNoItem; c++) {
                    JSONObject objectPerguntaDoItem = jsonPerguntasDoItemDaCategoria.getJSONObject(c);


                    int idExternoPergunta = objectPerguntaDoItem.getInt("id");
                    String tipo = objectPerguntaDoItem.getString("tipo");

                    String opcional = null;
                    opcional = objectPerguntaDoItem.optString("opcional");

                    int valorOpcional = 0;
                    if (opcional.equals("true")) {
                        valorOpcional = 1;
                    }

                    String condicional = "";
                    condicional = objectPerguntaDoItem.optString("visivel-se");

                    int valorCondicional = 0;
                    if (condicional.contains("pergunta")) {
                        valorCondicional = 1;
                    }
                    idPerguntaDoItem = banco.inserePerguntaDoItemDaCategoria(idItemDaCategoria, tipo, idExternoPergunta, valorOpcional, valorCondicional);

                    if (tipo.contains("alternativas")) {
                        JSONArray jsonOpcoes = objectPerguntaDoItem.optJSONArray("opcoes");
                        int qtdOpcoes = jsonOpcoes.length();
                        for (int d = 0; d < qtdOpcoes; d++) {
                            JSONObject objectOpcao = jsonOpcoes.getJSONObject(d);
                            String texto = objectOpcao.getString("texto");
                            String resposta = objectOpcao.getString("resposta");

                            banco.insereOpcao(idPerguntaDoItem, texto, resposta);
                        }
                    }
                    if (valorCondicional == 1) {
                        JSONObject objectCondicional = objectPerguntaDoItem.getJSONObject("visivel-se");
                        int idPergunta = objectCondicional.getInt("pergunta");
                        String valorResposta = objectCondicional.getString("resposta");
                        banco.insereCondicao(idPerguntaDoItem, idPergunta, valorResposta);
                    }
                }

            }
        }
    }


    public LinearLayout getLayout() {

        LinearLayout linearLayoutPrincipal = new LinearLayout(this.context);
        ListView listViewTitulos = new ListView(this.context);
        listaIdExterno = banco.obterListaIdExterno();
        listaTituloItemChecagem = banco.obterListaTituloItemChecagem();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context.getApplicationContext(), R.layout.simple_list_item_1, listaTituloItemChecagem);
        listViewTitulos.setAdapter(adapter);
        listViewTitulos.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent irParaChecagem = new Intent(Controle.this.context.getApplicationContext(), ActivityChecagem.class);
                irParaChecagem.putExtra("idExterno", listaIdExterno.get(position));
                irParaChecagem.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Controle.this.context.startActivity(irParaChecagem);
            }
        });
        linearLayoutPrincipal.addView(listViewTitulos, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        return linearLayoutPrincipal;
    }

    public JSONObject obterJsonRespostas(String nomeApp) {

        JSONObject jsonValoresCategorias = null;
        JSONObject jsonValoresItemDaCategoria = null;
        JSONObject jsonValoresPerguntasDoItemDaCategoria = null;
        JSONArray arrayPrincipal = new JSONArray();
        int idItemChecagem = banco.obterIdItemChecagem(nomeApp);
        List<Categoria> listaCategoria = banco.obterCategoriasRespondidasDoApp(idItemChecagem);
        JSONObject jsonPrincipal = new JSONObject();
        JSONObject jsonValoresItemChecagem = new JSONObject();

        try {
            jsonValoresItemChecagem.put("App Nome",nomeApp);
            jsonValoresItemChecagem.put("App id",String.valueOf(idItemChecagem));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayPrincipal.put(jsonValoresItemChecagem);

        for(Categoria categoria:listaCategoria){
            jsonValoresCategorias = new JSONObject();
            try {
                jsonValoresCategorias.put("id Categoria",categoria.getId());
                jsonValoresCategorias.put("nome Categoria",categoria.getNome());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arrayPrincipal.put(jsonValoresCategorias);

            List<ItemChecagemDaCategoria> listaItensDaCategoria = banco.obterListaItemDaCategoria(categoria.getId());
            for(ItemChecagemDaCategoria itemChecagemDaCategoria:listaItensDaCategoria){
                try {
                    jsonValoresItemDaCategoria = new JSONObject();
                    jsonValoresItemDaCategoria.put("Item id",itemChecagemDaCategoria.getIdExternoItemDaCategoria());
                    jsonValoresItemDaCategoria.put("Item titulo",itemChecagemDaCategoria.getTitulo());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arrayPrincipal.put(jsonValoresItemDaCategoria);

                List<Pergunta> listaPerguntasDoItem = banco.obterListaPerguntasDoItem(itemChecagemDaCategoria.getId());
                for(Pergunta pergunta: listaPerguntasDoItem){
                    if(pergunta.getResposta()!=null) {


                        try {
                            jsonValoresPerguntasDoItemDaCategoria = new JSONObject();
                            jsonValoresPerguntasDoItemDaCategoria.put("resposta id", String.valueOf(pergunta.getIdExterno()));
                            jsonValoresPerguntasDoItemDaCategoria.put("valores", pergunta.getResposta());
                            arrayPrincipal.put(jsonValoresPerguntasDoItemDaCategoria);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        try {
            jsonPrincipal.put("Checklists-respostas",arrayPrincipal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonPrincipal;
    }
}

