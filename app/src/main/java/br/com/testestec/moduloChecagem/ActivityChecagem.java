package br.com.testestec.moduloChecagem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.com.testestec.testes.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityChecagem extends ActionBarActivity {

    BancoDeDadosChecagem banco;
    private Bundle extras;
    ItemChecagem itemChecagem;
    List<Categoria> listaCategoria;
    List<ItemChecagemDaCategoria> listaItemDaCategoria;
    List<Pergunta> listaPerguntasDoItem;
    final List<Pergunta> listaPerguntasTotais = new ArrayList<Pergunta>();
    final Map<Integer,Integer> mapaPerguntasCondicionais = new HashMap<Integer,Integer>();
    int idExterno;
    private static final int CODIGO_IMAGEM_CAPTURA_FOTO = 100;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        banco = new BancoDeDadosChecagem(getApplicationContext());
        idExterno =0;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if(extras == null) {
                idExterno= 0;
            } else {
                idExterno= extras.getInt("idExterno");
            }
        }

        int status = banco.obetStatus(idExterno);
        if(status==1){
            setContentView(R.layout.activity_activity_checagem);
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

        LinearLayout linearLayoutHorizontal = new LinearLayout(this);
        linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHorizontal.setGravity(Gravity.CENTER);
        linearLayoutBase.addView(linearLayoutHorizontal);

        TextView textViewTituloItem = new TextView(this);
        textViewTituloItem.setText(itemChecagem.getTituloItem());
        linearLayoutHorizontal.addView(textViewTituloItem);

        montaLabelsCategorias(parametrosLayout1, linearLayoutBase);

        Button buttonOkPerguntasRespondidas = new Button(this);
        buttonOkPerguntasRespondidas.setText("OK");
        buttonOkPerguntasRespondidas.setLayoutParams(parametrosLayout1);
        buttonOkPerguntasRespondidas.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        linearLayoutBase.addView(buttonOkPerguntasRespondidas);

        buttonOkPerguntasRespondidas.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean perguntaSemResposta = false;

                for (Pergunta pergunta : listaPerguntasTotais) {
                    if (pergunta.getOpcional() == 0) {
                        if (pergunta.getRespondida() == 0) {
                            perguntaSemResposta = true;
                        }
                    }
                }

                if (perguntaSemResposta) {
                    Toast.makeText(getApplicationContext(), "Por Favor, Responda Todos os Itens", Toast.LENGTH_SHORT).show();
                } else {
                    salvaPerguntas();
                }
                ActivityChecagem.this.finishAffinity();
            }
        });
        return scrollView;
    }

    private void salvaPerguntas() {
        Toast.makeText(getApplicationContext(), "Todos os Itens Respondidos, Salvando...", Toast.LENGTH_SHORT).show();
        for (Pergunta pergunta : listaPerguntasTotais) {
            banco.atualizaPerguntas(pergunta);
        }
        atalizaStatuDoItem(idExterno);
    }


    private void montaLabelsCategorias(LayoutParams parametrosLayout1, LinearLayout linearLayout) {
        for(final Categoria categoria:itemChecagem.getListaCategorias()) {

            LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout2.setGravity(Gravity.CENTER);

            TextView tvc = new TextView(this);
            tvc.setGravity(Gravity.CENTER);
            tvc.setText(categoria.getNome());
            tvc.setLayoutParams(parametrosLayout1);
            tvc.setBackgroundColor(Color.BLUE);
            linearLayout2.addView(tvc);

            linearLayout.addView(linearLayout2);
            montaItensDaCategoria(linearLayout, categoria);
        }
    }

    private void montaItensDaCategoria(LinearLayout linearLayout, Categoria categoria) {
        for(final ItemChecagemDaCategoria itemChecagemDaCategoria :categoria.getListaItemChecagemDaCategoria()) {

            LinearLayout linearLayoutPergunta = new LinearLayout(this);
            linearLayoutPergunta.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutPergunta.setGravity(Gravity.CENTER);

            TextView tvic = new TextView(this);
            tvic.setText(itemChecagemDaCategoria.getTitulo());
            tvic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            linearLayoutPergunta.addView(tvic);
            linearLayout.addView(linearLayoutPergunta);

            LinearLayout linearLayoutItens = new LinearLayout(this);
            linearLayoutItens.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutItens.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.addView(linearLayoutItens);

            if(itemChecagemDaCategoria.getId()%2==0) {
                linearLayoutPergunta.setBackgroundColor(Color.parseColor("#F8F8FF"));
                linearLayoutItens.setBackgroundColor(Color.parseColor("#F8F8FF"));
            }


            listaPerguntasDoItem = banco.obterListaPerguntasDoItem(itemChecagemDaCategoria.getId());
            List<Opcao> listaOpcoes = null;

            montaViewsDasPerguntas( itemChecagemDaCategoria, linearLayoutItens, listaOpcoes);
        }
    }

    private void montaViewsDasPerguntas(final ItemChecagemDaCategoria itemChecagemDaCategoria, LinearLayout linearLayoutItens, List<Opcao> listaOpcoes) {

        LayoutParams parametrosLayoutBackground = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        for(Pergunta pergunta : listaPerguntasDoItem){

            if(pergunta.getCondicional()==1){
                pergunta.setCondicao(banco.obterCondicao(pergunta.getId()));
                mapaPerguntasCondicionais.put(pergunta.getCondicao().getIdPerguntaEmCondicional(), pergunta.getIdExterno());
            }

            listaPerguntasTotais.add(pergunta);

            if(pergunta.getTipo().contains("alternativa")){
                listaOpcoes = banco.obterListaOpcoesDaPergunta(pergunta.getId());
            }

            if(pergunta.getTipo().equals("alternativas-radio")) {
                final RadioGroup rg1 = new RadioGroup(this);
                rg1.setOrientation(LinearLayout.HORIZONTAL);
                rg1.setId(pergunta.getIdExterno());
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
                                String text = (String) btn.getText();

                                if(rg1.isShown()) {
                                    marcaResposta(rg1.getId(),text);
                                    verificaCondicionais(rg1.getId(),text);
                                }
                                else{
                                    desmarcaRespostas(rg1.getId());
                                }
                                return;
                            }
                        }
                    }
                });
                if(pergunta.getCondicional()==1){
                    rg1.setVisibility(View.INVISIBLE);
                }
                linearLayoutItens.addView(rg1);
            }

            else if(pergunta.getTipo().equals("alternativas-lista")){
                List<String> list = new ArrayList<String>();
                for(Opcao opcao:listaOpcoes) {
                    list.add(opcao.getValorTexto());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, list);
                final Spinner sp = new Spinner(this);
                sp.setId(pergunta.getIdExterno());
                sp.setAdapter(dataAdapter);
                sp.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String text = sp.getSelectedItem().toString();

                        if(sp.isShown()) {
                            marcaResposta(sp.getId(), text);
                            verificaCondicionais(sp.getId(), text);
                        }
                        else{
                            desmarcaRespostas(sp.getId());
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                if(pergunta.getCondicional()==1){
                    sp.setVisibility(View.INVISIBLE);
                }
                linearLayoutItens.addView(sp);
            }

            else if(pergunta.getTipo().equals("texto-livre")){
                final Button bt1 = new Button(this);
                bt1.setId(pergunta.getIdExterno());
                bt1.setBackgroundResource(R.drawable.ic_action_edit);
                bt1.setLayoutParams(parametrosLayoutBackground);

                bt1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityChecagem.this);
                        alert.setTitle(itemChecagemDaCategoria.getTitulo());
                        final EditText input = new EditText(ActivityChecagem.this);
                        alert.setView(input);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String text = input.getEditableText().toString();
                                if(text.length()>1){
                                    marcaResposta(bt1.getId(),text);
                                }
                            }
                        });
                        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                });
                linearLayoutItens.addView(bt1);
            }
            else if(pergunta.getTipo().equals("foto")){
                final  Button bt1 = new Button(this);
                bt1.setId(pergunta.getIdExterno());
                bt1.setBackgroundResource(R.drawable.ic_action_camera);
                bt1.setLayoutParams(parametrosLayoutBackground);
                bt1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = Uri.fromFile(caminhoArquivoDeFoto(bt1.getId()));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(intent, CODIGO_IMAGEM_CAPTURA_FOTO);

                        String text = String.valueOf(fileUri);
                        marcaResposta(bt1.getId(),text);

                    }
                });

                linearLayoutItens.addView(bt1);
            }
        }
    }

    private void atalizaStatuDoItem(int idExterno) {
        banco.atualizaStatusDoitem(idExterno);
    }

    private void desmarcaRespostas(int idExterno){
        for(Pergunta pergunta :listaPerguntasTotais) {
            if (pergunta.getIdExterno() == idExterno) {
                pergunta.setResposta(null);
                pergunta.setRespondida(0);
            }
        }
    }
    private void marcaResposta(int idExterno, String text) {

        for(Pergunta pergunta :listaPerguntasTotais){
            if(pergunta.getIdExterno()==idExterno){
                pergunta.setResposta(text);
                pergunta.setRespondida(1);
            }
        }
    }

    private void verificaCondicionais(int id, String text) {

        if(mapaPerguntasCondicionais.containsKey(id)){
            int idDependente = mapaPerguntasCondicionais.get(id);

            for (Pergunta pergunta : listaPerguntasTotais){
                if(idDependente==pergunta.getIdExterno()){
                    if(pergunta.getCondicao().getValorResposta().equals(text)){

                        View visiver = findViewById(idDependente);
                        visiver.setVisibility(View.VISIBLE);
                    }
                    else{
                        View visiver = findViewById(idDependente);
                        visiver.setVisibility(View.INVISIBLE);
                        desmarcaRespostas(idDependente);
                    }
                }

            }
        }
    }

    private File caminhoArquivoDeFoto(int id) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getPackageName());
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e("ERRO", "Criando diretorio");
                return null;
            }
        }
        String montaString = "Fotografia_pergunta_id_" + String.valueOf(id)  ;
        return new File(directory.getPath() + File.separator
                + montaString.trim() + ".jpg");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODIGO_IMAGEM_CAPTURA_FOTO) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = null;
                if (data == null) {
                    Toast.makeText(this, "Imagem Salva com Sucesso",
                            Toast.LENGTH_LONG).show();
                    photoUri = fileUri;
                } else {
                    photoUri = data.getData();
                    Toast.makeText(this, "Imagem Salva com Sucesso em: " + data.getData(),
                            Toast.LENGTH_LONG).show();
                    Log.i("imagem salva em " , String.valueOf(data.getData()));
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao Salvar Imagem",
                        Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_checagem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}