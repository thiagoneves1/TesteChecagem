package br.com.testestec.moduloChecagem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.com.testestec.testes.ActivityResposta;
import br.com.testestec.testes.MyActivity;
import br.com.testestec.testes.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityChecagem extends ActionBarActivity {

    DataBaseHelper banco;
    private Bundle extras;
    ItemChecagem itemChecagem;
    List<Categoria> listaCategoria;
    List<ItemChecagemDaCategoria> listaItemDaCategoria;
    List<Resposta> listaRespostasDoItem;
    final List<Resposta> listaRespostasTotais = new ArrayList<Resposta>();
    final Map<Integer,Integer> mapaPerguntasCondicionais = new HashMap<Integer,Integer>();
    int idExterno;
    private Uri fileUri;
    Button btnVerResposta;
    private int idRespostaParaFoto=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        banco = new DataBaseHelper(getApplicationContext());
        idExterno =0;

        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if(extras == null) {
                idExterno= 0;
            } else {
                idExterno= extras.getInt(Constantes.ID_EXTERNO);
            }
        }

        int statusItemJaVerificado = banco.obetStatus(idExterno);

        if(statusItemJaVerificado== Constantes.valorItemJaVerificadoTrue){
            setContentView(R.layout.activity_activity_checagem);
            btnVerResposta=(Button)findViewById(R.id.btn_ver_resposta);

            btnVerResposta.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent irParaResposta = new Intent(ActivityChecagem.this,ActivityResposta.class);
                    irParaResposta.putExtra(Constantes.ID_EXTERNO,idExterno);
                    startActivity(irParaResposta);
                }
            });

        }
        else{
            carregaListas();
            ScrollView sv = montaLayout();
            this.setContentView(sv);
        }
    }


private ScrollView montaLayout() {

        ScrollView scrollView = new ScrollView(this);

        LayoutParams parametrosLayout1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        LinearLayout linearLayoutBase = new LinearLayout(this);
        linearLayoutBase.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayoutBase);

        LinearLayout layoutDoTitulo = new LinearLayout(this);
        layoutDoTitulo.setOrientation(LinearLayout.HORIZONTAL);
        layoutDoTitulo.setGravity(Gravity.CENTER);
        layoutDoTitulo.setBackgroundResource(R.color.azul_claro);
        layoutDoTitulo.setPadding(0,15,0,15);
        linearLayoutBase.addView(layoutDoTitulo);

        TextView textViewTituloItem = new TextView(this);
        textViewTituloItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constantes.tamanhoTextoTitulo);
        textViewTituloItem.setText(itemChecagem.getTituloItem());
        textViewTituloItem.setTextColor(getResources().getColor(Constantes.corPreta));
        layoutDoTitulo.addView(textViewTituloItem);

        montaLabelsCategorias(parametrosLayout1, linearLayoutBase);

        Button buttonOkPerguntasRespondidas = new Button(this);
        buttonOkPerguntasRespondidas.setText(Constantes.OK);
        buttonOkPerguntasRespondidas.setLayoutParams(parametrosLayout1);
        buttonOkPerguntasRespondidas.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constantes.tamanhoTextoTitulo);
        buttonOkPerguntasRespondidas.setBackgroundResource(Constantes.corAzulClaro);
        linearLayoutBase.addView(buttonOkPerguntasRespondidas);

        buttonOkPerguntasRespondidas.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean perguntaSemResposta = false;

                for (Resposta resposta : listaRespostasTotais) {
                    if (resposta.getOpcional() == Constantes.valorOpcionalFalse) {
                        if (resposta.getRespondida() == Constantes.valorRespondidaFalse) {
                            perguntaSemResposta = true;
                        }
                    }
                }

                if (perguntaSemResposta) {
                    Toast.makeText(getApplicationContext(), Constantes.RESPONDA_TODOS_ITENS, Toast.LENGTH_SHORT).show();
                } else {
                    salvaPerguntas();
                    Intent irParaLista = new Intent(getApplicationContext(), MyActivity.class);
                    startActivity(irParaLista);
                }
            }
        });
        return scrollView;
    }

    private void salvaPerguntas() {
        Toast.makeText(getApplicationContext(), Constantes.ITENS_RESPONDIDOS_SALVANDO, Toast.LENGTH_SHORT).show();
        for (Resposta resposta : listaRespostasTotais) {
            banco.atualizaRespostas(resposta);
        }
        atalizaStatuDoItem(idExterno);
    }

    private void montaLabelsCategorias(LayoutParams parametrosLayout1, LinearLayout linearLayout) {

        for(final Categoria categoria:itemChecagem.getListaCategorias()) {
            LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout2.setGravity(Gravity.CENTER);
            linearLayout2.setPadding(0,10,0,10);

            TextView textViewCategorias = new TextView(this);
            textViewCategorias.setGravity(Gravity.CENTER);
            textViewCategorias.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constantes.tamanhoTextoCategoria);
            textViewCategorias.setText(categoria.getNome());
            textViewCategorias.setLayoutParams(parametrosLayout1);
            textViewCategorias.setBackgroundResource(Constantes.corCiano);
            linearLayout2.addView(textViewCategorias);
            linearLayout.addView(linearLayout2);
            montaItensDaCategoria(linearLayout, categoria);
        }
    }

    private void montaItensDaCategoria(LinearLayout linearLayout, Categoria categoria) {

        for(final ItemChecagemDaCategoria itemChecagemDaCategoria :categoria.getListaItemChecagemDaCategoria()) {

            LinearLayout linearLayoutPergunta = new LinearLayout(this);
            linearLayoutPergunta.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutPergunta.setGravity(Gravity.CENTER);
            linearLayoutPergunta.setPadding(10,10,10,10);

            TextView textViewItemDaCategoria = new TextView(this);
            textViewItemDaCategoria.setText(itemChecagemDaCategoria.getTitulo());
            textViewItemDaCategoria.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constantes.tamanhoTextoSubtitulo);
            linearLayoutPergunta.addView(textViewItemDaCategoria);
            linearLayout.addView(linearLayoutPergunta);

            HorizontalScrollView horizontalScrollViewItens = new HorizontalScrollView(this);

            LinearLayout linearLayoutItens = new LinearLayout(this);
            linearLayoutItens.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutItens.setGravity(Gravity.CENTER_VERTICAL);

            if(itemChecagemDaCategoria.getId()%2==0) {//alterna cores
                linearLayoutPergunta.setBackgroundResource(Constantes.corGhostWhite);
                linearLayoutItens.setBackgroundResource(Constantes.corGhostWhite);
                horizontalScrollViewItens.setBackgroundResource(Constantes.corGhostWhite);
                horizontalScrollViewItens.setBackgroundResource(Constantes.corGhostWhite);
            }

            horizontalScrollViewItens.addView(linearLayoutItens);
            linearLayout.addView(horizontalScrollViewItens);
            listaRespostasDoItem = banco.obterListaRespostasDoItem(itemChecagemDaCategoria.getId());
            montaViewsDasRespostas(itemChecagemDaCategoria, linearLayoutItens);
        }
    }

    private void montaViewsDasRespostas(final ItemChecagemDaCategoria itemChecagemDaCategoria, LinearLayout linearLayoutItens) {

        List<Opcao> listaOpcoes = null;
        LayoutParams parametrosLayoutBackground = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        for(final Resposta resposta : listaRespostasDoItem){

            LinearLayout linearLayoutConjuntoItensStatus = new LinearLayout(this);
            MarginLayoutParams paramsConjuto = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            linearLayoutConjuntoItensStatus.setOrientation(LinearLayout.VERTICAL);
            linearLayoutConjuntoItensStatus.setLayoutParams(paramsConjuto);
            linearLayoutConjuntoItensStatus.setPadding(10,0,0,10);
            linearLayoutConjuntoItensStatus.setGravity(Gravity.CENTER_HORIZONTAL);

            if(resposta.getCondicional()==Constantes.valorCondicionalTrue){
                resposta.setCondicao(banco.obterCondicao(resposta.getId()));
                mapaPerguntasCondicionais.put(resposta.getCondicao().getIdRespostaEmCondicional(), resposta.getId());
            }

            listaRespostasTotais.add(resposta);

            if(resposta.getTipo().contains(Constantes.ALTERNATIVAS)){
                listaOpcoes = banco.obterListaOpcoesDaResposta(resposta.getId());
                resposta.setListaOpcoes(listaOpcoes);
            }

            if(resposta.getTipo().equals(Constantes.RESPOSTA_TIPO_ALTERNATIVAS_RADIO)) {

                LinearLayout layoutStatus = montarLayoutStatus(resposta);

                final RadioGroup rg1 = new RadioGroup(this);
                rg1.setOrientation(LinearLayout.HORIZONTAL);
                rg1.setLayoutParams(parametrosLayoutBackground);
                rg1.setId(resposta.getId());

                    for(Opcao opcao:listaOpcoes){
                        RadioButton rb1 = new RadioButton(this);
                        rb1.setText(opcao.getValorTexto());
                        rb1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                        rg1.addView(rb1);
                    }

                rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup rg, int checkedId) {
                        for (int i = 0; i < rg.getChildCount(); i++) {
                            RadioButton btn = (RadioButton) rg.getChildAt(i);
                            if (btn.getId() == checkedId) {
                                String text = (String) btn.getText();//valor
                                if(rg1.isShown()) {
                                    marcaResposta(rg1.getId(),text);
                                    verificaCondicionais(rg1.getId(),text);
                                    foiRespondida(rg1.getId());
                                }
                                else{
                                    desmarcaRespostas(rg1.getId());
                                }
                                return;
                            }
                        }
                    }
                });
                if(resposta.getCondicional()==1){
                    rg1.setVisibility(View.INVISIBLE);
                    layoutStatus.setVisibility(View.INVISIBLE);
                }
                linearLayoutConjuntoItensStatus.addView(layoutStatus);
                linearLayoutConjuntoItensStatus.addView(rg1);
                linearLayoutItens.addView(linearLayoutConjuntoItensStatus);
            }

            else if(resposta.getTipo().equals(Constantes.RESPOSTA_TIPO_ALTERNATIVAS_LISTA)){
                LinearLayout layoutStatus = montarLayoutStatus(resposta);
                List<String> listaParaAdapter = new ArrayList<String>();
                for(Opcao opcao:listaOpcoes) {
                    listaParaAdapter.add(opcao.getValorTexto());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, listaParaAdapter);
                final Spinner sp = new Spinner(this);
                sp.setId(resposta.getId());
                sp.setAdapter(dataAdapter);
                sp.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String text = sp.getSelectedItem().toString(); //valor
                        if (sp.isShown()) {
                            marcaResposta(sp.getId(), text);
                            verificaCondicionais(sp.getId(), text);
                            foiRespondida(sp.getId());
                        } else {
                            desmarcaRespostas(sp.getId());
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                if(resposta.getCondicional()== Constantes.valorCondicionalTrue){
                    sp.setVisibility(View.INVISIBLE);
                    layoutStatus.setVisibility(View.INVISIBLE);
                }
                linearLayoutConjuntoItensStatus.addView(layoutStatus);
                linearLayoutConjuntoItensStatus.addView(sp);
                linearLayoutItens.addView(linearLayoutConjuntoItensStatus);
            }

            else if(resposta.getTipo().equals(Constantes.RESPOSTA_TIPO_TEXTO_LIVRE)){

                LinearLayout layoutStatus = montarLayoutStatus(resposta);
                final Button bt1 = new Button(this);
                bt1.setId(resposta.getId());
                bt1.setBackgroundResource(R.drawable.ic_action_edit);
                bt1.setLayoutParams(parametrosLayoutBackground);
                bt1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(bt1.isShown()) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ActivityChecagem.this);
                            alert.setTitle(itemChecagemDaCategoria.getTitulo());
                            final EditText input = new EditText(ActivityChecagem.this);
                            String valorJaRespondido = verificaValorJaRespondido(bt1.getId());
                            input.setText(valorJaRespondido);
                            alert.setView(input);
                            alert.setPositiveButton(Constantes.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String text = input.getEditableText().toString();
                                    if (text.length() > 1) {
                                        marcaResposta(bt1.getId(), text);
                                        foiRespondida(bt1.getId());
                                    }
                                    else{
                                        desmarcaRespostas(bt1.getId());
                                    }
                                }
                            });
                            alert.setNegativeButton(Constantes.CANCELA, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = alert.create();
                            alertDialog.show();
                        }
                    }
                });
                if(resposta.getCondicional()==Constantes.valorCondicionalTrue){
                    bt1.setVisibility(View.INVISIBLE);
                    layoutStatus.setVisibility(View.INVISIBLE);
                }
                linearLayoutConjuntoItensStatus.addView(layoutStatus);
                linearLayoutConjuntoItensStatus.addView(bt1);
                linearLayoutItens.addView(linearLayoutConjuntoItensStatus);
            }
            else if(resposta.getTipo().equals(Constantes.RESPOSTA_TIPO_FOTO)){
                LinearLayout layoutStatus = montarLayoutStatus(resposta);
                final  Button bt1 = new Button(this);
                bt1.setId(resposta.getId());
                bt1.setBackgroundResource(R.drawable.ic_action_camera);
                bt1.setLayoutParams(parametrosLayoutBackground);
                bt1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        idRespostaParaFoto = bt1.getId();
                        String valorJaRespondido = verificaValorJaRespondido(bt1.getId());
                        if(valorJaRespondido!=null){
                            mostraFoto(valorJaRespondido, bt1.getId(),itemChecagemDaCategoria.getTitulo());
                        }else {
                            String text = tirarFoto(bt1.getId());
                            marcaResposta(bt1.getId(), text);
                            foiRespondida(bt1.getId());
                        }
                    }
                });
                if(resposta.getCondicional()==Constantes.valorCondicionalTrue){
                    bt1.setVisibility(View.INVISIBLE);
                    layoutStatus.setVisibility(View.INVISIBLE);
                }
                linearLayoutConjuntoItensStatus.addView(layoutStatus);
                linearLayoutConjuntoItensStatus.addView(bt1);
                linearLayoutItens.addView(linearLayoutConjuntoItensStatus);
            }
        }
    }

    private String tirarFoto(int id) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(caminhoArquivoDaFoto(id));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, Constantes.CODIGO_IMAGEM_CAPTURA_FOTO);
        return String.valueOf(fileUri);
    }

    private void mostraFoto(final String caminhoFoto, int id, String titulo) {
    final int idInterno = id;

        ImageView imageViewFoto;
        Button buttonManterFoto, buttonTrocarFoto;
        TextView textViewTituloFoto;

        final Dialog dialog = new Dialog(ActivityChecagem.this);
        dialog.setContentView(R.layout.dialog_foto);
        imageViewFoto=(ImageView)dialog.findViewById(R.id.image_view_foto);
        imageViewFoto.setImageURI(Uri.parse(caminhoFoto));

        textViewTituloFoto=(TextView)dialog.findViewById(R.id.text_view_titulo_foto);
        textViewTituloFoto.setText(titulo);

        buttonManterFoto=(Button)dialog.findViewById(R.id.button_manter_foto);
        buttonManterFoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        buttonTrocarFoto=(Button)dialog.findViewById(R.id.button_trocar_foto);
        buttonTrocarFoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = tirarFoto(idInterno);
                marcaResposta(idInterno, text);
                foiRespondida(idInterno);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private String verificaValorJaRespondido(int id) {
        String retorno = null;
        for (Resposta resposta : listaRespostasTotais) {
            if (resposta.getId() == id) {
                if (resposta.getRespondida() == 1) {
                    retorno = resposta.getValorResposta();
                }
            }

        }
        return retorno;
    }

    private LinearLayout montarLayoutStatus(Resposta resposta) {

        MarginLayoutParams paramsStatus = new MarginLayoutParams(LayoutParams.MATCH_PARENT, Constantes.valorAlturaLayoutDeStatus);
        LinearLayout layoutStatus = new LinearLayout(this);
        layoutStatus.setLayoutParams(paramsStatus);
        layoutStatus.setId(resposta.getId()+ Constantes.diferencaIdItemELayout);
        layoutStatus.setBackgroundResource(Constantes.corVermelha);
            if(resposta.getOpcional()== Constantes.valorOpcionalTrue){
                layoutStatus.setBackgroundResource(Constantes.corAmarela);
            }
        return layoutStatus;
    }

    private void atalizaStatuDoItem(int idExterno) {

        banco.atualizaStatusDoitem(idExterno);
    }

    private void desmarcaRespostas(int id){
        for(Resposta resposta : listaRespostasTotais) {
            if (resposta.getId() == id) {
                resposta.setValorResposta(null);
                resposta.setRespondida(0);

                if(resposta.getOpcional()==Constantes.valorOpcionalTrue){
                    View view = findViewById(id+ Constantes.diferencaIdItemELayout);
                    view.setBackgroundResource(Constantes.corAmarela);
                }
                else{
                    View view = findViewById(id+ Constantes.diferencaIdItemELayout);
                    view.setBackgroundResource(Constantes.corVermelha);
                }

            }
        }
    }

    private void marcaResposta(int id, String text) {

        for(Resposta resposta : listaRespostasTotais){
            if(resposta.getId()==id){
                if(resposta.getTipo().equals(Constantes.RESPOSTA_TIPO_ALTERNATIVAS_LISTA)||resposta.getTipo().equals(Constantes.RESPOSTA_TIPO_ALTERNATIVAS_RADIO)){//pega valor resposta, quando tem opções
                        for(Opcao opcao : resposta.getListaOpcoes()){
                                if(opcao.getValorTexto().equals(text)){
                                    resposta.setValorResposta(opcao.getValorResposta());
                                    resposta.setRespondida(1);
                                }
                        }
                }
            else {
                 resposta.setValorResposta(text);
                 resposta.setRespondida(1);
                 }
            }
        }
    }

    private void foiRespondida(int id) {
        View view = findViewById(id+ Constantes.diferencaIdItemELayout);
        view.setBackgroundResource(Constantes.corVerde);
    }

    private void verificaCondicionais(int id, String text) {

        if(mapaPerguntasCondicionais.containsKey(id)){
            int idDependente = mapaPerguntasCondicionais.get(id);
            percorrePerguntas:for (Resposta resposta : listaRespostasTotais){
                if(idDependente== resposta.getId()){
                    if(resposta.getCondicao().getValorResposta().equals(text)){

                        View view = findViewById(idDependente);
                        view.setVisibility(View.VISIBLE);
                        View viewStatus = findViewById(idDependente+ Constantes.diferencaIdItemELayout);
                        viewStatus.setVisibility(View.VISIBLE);
                        break percorrePerguntas;
                    }
                    else{
                        View view = findViewById(idDependente);
                        view.setVisibility(View.INVISIBLE);
                        View viewStatus = findViewById(idDependente+ Constantes.diferencaIdItemELayout);
                        viewStatus.setVisibility(View.INVISIBLE);
                        desmarcaRespostas(idDependente);
                    }
                }
            }
        }
    }

    private File caminhoArquivoDaFoto(int id) {
        File diretorio = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getPackageName());
        if (!diretorio.exists()) {
            if (!diretorio.mkdirs()) {
                return null;
            }
        }
        String montaString = Constantes.FOTOGRAFIA_PERGUNTA_ID + String.valueOf(id)  ;
        return new File(diretorio.getPath() + File.separator
                + montaString.trim() + ".jpg");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constantes.CODIGO_IMAGEM_CAPTURA_FOTO) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(this, Constantes.IMAGEM_SALVA_COM_SUCESSO,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, Constantes.IMAGEM_SALVA_COM_SUCESSO_EM + data.getData(),
                            Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, Constantes.CANCELADO, Toast.LENGTH_SHORT).show();
                desmarcaRespostas(idRespostaParaFoto);
            } else {
                Toast.makeText(this, Constantes.ERRO_AO_SALVAR_IMAGEM,
                        Toast.LENGTH_LONG).show();
                desmarcaRespostas(idRespostaParaFoto);
            }
        }
    }

    private void carregaListas() {
        itemChecagem = banco.obterItemChecagem(idExterno);
        listaCategoria = banco.obterListaCategorias(itemChecagem.getId());
        itemChecagem.setListaCategorias(listaCategoria);
        for(Categoria categoria:listaCategoria){
           listaItemDaCategoria = banco.obterListaItemDaCategoria(categoria.getId());
            categoria.setListaItemChecagemDaCategoria(listaItemDaCategoria);
        }
    }
}