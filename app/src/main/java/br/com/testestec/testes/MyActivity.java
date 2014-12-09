package br.com.testestec.testes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.LinearLayout;
import br.com.testestec.moduloChecagem.Controle;


public class MyActivity extends ActionBarActivity {

    String json = "{\"checklists\":[{\"App\":\"X\",\"id\":\"1\",\"nome\":\"Entrega do Carro do José\",\"categorias\":[{\"nome\":\"Manuais e Serviços\",\"itens\":[{\"id\":\"1\",\"texto\":\"Entrega do Manual do Proprietário\",\"respostas\":[{\"id\":\"1\",\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":\"2\",\"tipo\":\"foto\"},{\"id\":\"3\",\"tipo\":\"texto-livre\",\"visivel-se\":{\"pergunta\":\"1\",\"resposta\":\"3\",\"valor\":\"V2\"}}]},{\"id\":\"2\",\"texto\":\"Entrega da Chave reserva\",\"respostas\":[{\"id\":\"1\",\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":\"2\",\"tipo\":\"texto-livre\"},{\"id\":\"3\",\"tipo\":\"foto\"},{\"id\":\"4\",\"tipo\":\"alternativas-lista\",\"opcoes\":[{\"texto\":\"V1+\",\"resposta\":\"1\"},{\"texto\":\"V1\",\"resposta\":\"2\"},{\"texto\":\"V2\",\"resposta\":\"3\"},{\"texto\":\"V3\",\"resposta\":\"4\"}],\"visivel-se\":{\"pergunta\":\"1\",\"resposta\":\"0\",\"valor\":\"N-A\"}}]}]},{\"nome\":\"Documentos\",\"itens\":[{\"id\":\"3\",\"texto\":\"Informação sobre a Autorização Provisória para circulação\",\"respostas\":[{\"id\":\"1\",\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":\"2\",\"tipo\":\"texto-livre\",\"opcional\":\"true\"},{\"id\":\"3\",\"tipo\":\"alternativas-lista\",\"opcoes\":[{\"texto\":\"V1+\",\"resposta\":\"1\"},{\"texto\":\"V1\",\"resposta\":\"2\"},{\"texto\":\"V2\",\"resposta\":\"3\"},{\"texto\":\"V3\",\"resposta\":\"4\"}],\"visivel-se\":{\"pergunta\":\"1\",\"resposta\":\"2\",\"valor\":\"N-OK\"}}]}]}]},{\"App\":\"Y\",\"id\":\"2\",\"nome\":\"Entrega do Carro do João\",\"categorias\":[{\"nome\":\"Manuais e Serviços\",\"itens\":[{\"id\":\"1\",\"texto\":\"Entrega do Manual do Proprietário \",\"respostas\":[{\"id\":\"1\",\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":\"2\",\"tipo\":\"texto-livre\"}]},{\"id\":\"2\",\"texto\":\"Entrega da Chave reserva\",\"respostas\":[{\"id\":\"1\",\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":\"2\",\"tipo\":\"texto-livre\"},{\"id\":\"3\",\"tipo\":\"foto\"}]}]},{\"nome\":\"Documentos\",\"itens\":[{\"id\":\"3\",\"texto\":\"Informação sobre a Autorização Provisória para circulaçãoY\",\"respostas\":[{\"id\":\"1\",\"tipo\":\"alternativas-radio\",\"opcional\":\"true\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":\"2\",\"tipo\":\"texto-livre\"},{\"id\":\"3\",\"tipo\":\"alternativas-lista\",\"opcoes\":[{\"texto\":\"V1+\",\"resposta\":\"1\"},{\"texto\":\"V1\",\"resposta\":\"2\"},{\"texto\":\"V2\",\"resposta\":\"3\"},{\"texto\":\"V3\",\"resposta\":\"4\"}],\"visivel-se\":{\"pergunta\":\"1\",\"resposta\":\"2\",\"valor\":\"V1\"}}]}]}]},{\"App\":\"Z\",\"id\":\"3\",\"nome\":\"Entrega do Carro do Luiz\",\"categorias\":[{\"nome\":\"Manuais e Serviços \",\"itens\":[{\"id\":\"1\",\"texto\":\"Entrega do Manual do Proprietário \",\"respostas\":[{\"id\":\"1\",\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":\"2\",\"tipo\":\"texto-livre\",\"visivel-se\":{\"pergunta\":\"1\",\"resposta\":\"2\",\"valor\":\"N-OK\"}}]},{\"id\":\"3\",\"texto\":\"Entrega da Chave reserva \",\"respostas\":[{\"id\":\"1\",\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":\"4\",\"tipo\":\"texto-livre\"},{\"id\":\"5\",\"tipo\":\"foto\"}]}]},{\"nome\":\"Documentos\",\"itens\":[{\"id\":\"3\",\"texto\":\"Informação sobre a Autorização Provisória para circulação Z\",\"respostas\":[{\"id\":\"1\",\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":\"2\",\"tipo\":\"texto-livre\",\"opcional\":\"true\"},{\"id\":\"3\",\"tipo\":\"alternativas-lista\",\"opcoes\":[{\"texto\":\"V1+\",\"resposta\":\"1\"},{\"texto\":\"V1\",\"resposta\":\"2\"},{\"texto\":\"V2\",\"resposta\":\"3\"},{\"texto\":\"V3\",\"resposta\":\"4\"}],\"visivel-se\":{\"pergunta\":\"1\",\"resposta\":\"0\",\"valor\":\"N-A\"}}]}]}]}]}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Controle controle = new Controle(getApplicationContext());
        boolean inseriu = controle.insereJson(json);//mudar e passar um object JSON ?

        if(inseriu) {
            Log.i(String.valueOf(getApplicationContext()), "json inserido com sucesso");
        }

            LinearLayout linearLayout = controle.montaLayout();
            setContentView(linearLayout);//mostra o layout com dados existentes

//        setContentView(R.layout.activity_my);
//        getApplicationContext().deleteDatabase("bancoModulo");//usado para deletar o banco em testes


//        String nomeApp = "X"; //ou Z com a string montada acima
//        JSONObject jsonFinal = controle.obterJsonRespostas(nomeApp);
//        Log.i("jsonFinal  ", jsonFinal.toString());
    }

}
