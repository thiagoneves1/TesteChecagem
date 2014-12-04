package br.com.testestec.testes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import br.com.testestec.moduloChecagem.Controle;


public class MyActivity extends ActionBarActivity {


    String json="{\"checklists\":[{\"App\":\"X\",\"id\":1,\"tituloChecagem\":\"Entrega do Carro do " +
            "José\",\"categorias\":[{\"nomeCategoria\":\"Manuais e Serviços\",\"itensDaCategoria\"" +
            ":[{\"id\":1,\"tituloItem\":\"Entrega do Manual do Proprietário\",\"perguntas\"" +
            ":[{\"id\":1,\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"" +
            "resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"" +
            "resposta\":\"0\"}]},{\"id\":2,\"tipo\":\"texto-livre\"}]},{\"id\":2,\"tituloItem\":\"" +
            "Entrega da Chave reserva\",\"perguntas\":[{\"id\":3,\"tipo\":\"alternativas-radio\",\"" +
            "opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"}," +
            "{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":4,\"tipo\":\"texto-livre\"}]}]}," +
            "{\"nomeCategoria\":\"Documentos\",\"itensDaCategoria\":[{\"id\":3,\"tituloItem\":\"" +
            "Informação sobre a Autorização Provisória para circulação\",\"perguntas\":" +
            "[{\"id\":5,\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"" +
            "resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"" +
            "resposta\":\"0\"}]},{\"id\":6,\"tipo\":\"texto-livre\"}]},{\"id\":4,\"tituloItem\":\"" +
            "Avaliação das condições externas do veículo (lataria)\",\"perguntas\":" +
            "[{\"id\":7,\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"" +
            "resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"" +
            "resposta\":\"0\"}]},{\"id\":8,\"tipo\":\"texto-livre\",\"opcional\":\"true\"},{\"id\":9,\"" +
            "tipo\":\"alternativas-lista\",\"opcional\":\"true\",\"opcoes\":[{\"texto\":\"V1+\",\"" +
            "resposta\":\"1\"},{\"texto\":\"V1\",\"resposta\":\"2\"},{\"texto\":\"V2\",\"" +
            "resposta\":\"3\"},{\"texto\":\"V3\",\"resposta\":\"4\"}],\"visivel-se\":{\"pergunta\":5,\"" +
            "resposta\":\"N-A\",\"valor\":\"0\"}},{\"id\":10,\"tipo\":\"foto\",\"" +
            "opcional\":\"true\"}]}]}]},{\"App\":\"Z\",\"id\":2,\"tituloChecagem\":\"" +
            "Entrega do Carro do João\",\"categorias\":[{\"nomeCategoria\":\"Manuais e Serviços do João\",\"" +
            "itensDaCategoria\":[{\"id\":1,\"tituloItem\":\"Entrega do Manual do Proprietário do João\",\"" +
            "perguntas\":[{\"id\":1,\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"" +
            "resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"" +
            "resposta\":\"0\"}]},{\"id\":2,\"tipo\":\"texto-livre\"}]},{\"id\":2,\"tituloItem\":\"" +
            "Entrega da Chave reserva do João\",\"perguntas\":[{\"id\":3,\"tipo\":\"alternativas-radio\",\"" +
            "opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"" +
            "texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":4,\"tipo\":\"texto-livre\"}]}]},{\"" +
            "nomeCategoria\":\"Documentos do João\",\"itensDaCategoria\":[{\"id\":3,\"tituloItem\":\"" +
            "Informação sobre a Autorização Provisória para circulação do João\",\"" +
            "perguntas\":[{\"id\":5,\"tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"" +
            "resposta\":\"1\"},{\"texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"" +
            "resposta\":\"0\"}]},{\"id\":6,\"tipo\":\"texto-livre\"}]},{\"id\":4,\"tituloItem\":\"" +
            "Avaliação das condições externas do veículo (lataria) do João\",\"perguntas\":[{\"id\":7,\"" +
            "tipo\":\"alternativas-radio\",\"opcoes\":[{\"texto\":\"OK\",\"resposta\":\"1\"},{\"" +
            "texto\":\"N-OK\",\"resposta\":\"2\"},{\"texto\":\"N-A\",\"resposta\":\"0\"}]},{\"id\":8,\"tipo\":\"" +
            "texto-livre\",\"opcional\":\"true\"},{\"id\":9,\"tipo\":\"alternativas-lista\",\"opcional\":\"true\",\"" +
            "opcoes\":[{\"texto\":\"V1+\",\"resposta\":\"1\"},{\"texto\":\"V1\",\"resposta\":\"2\"},{\"" +
            "texto\":\"V2\",\"resposta\":\"3\"},{\"texto\":\"V3\",\"resposta\":\"4\"}],\"visivel-se\":{\"" +
            "pergunta\":5,\"resposta\":\"N-A\",\"valor\":\"0\"}},{\"id\":10,\"tipo\":\"foto\",\"opcional\":\"true\"}]}]}]}]}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Controle controle = new Controle(getApplicationContext());
        boolean inseriu = controle.insereJson(json);//mudar e passar um object JSON ?

        if(inseriu) {
            Log.i(String.valueOf(getApplicationContext()),"json inserido com sucesso");
        }

            LinearLayout linearLayout = controle.montaLayout();
            setContentView(linearLayout);//mostra o layout com dados existentes

//        setContentView(R.layout.activity_my);
//        getApplicationContext().deleteDatabase("bancoModulo");//usado para deletar o banco em testes


//        String nomeApp = "X"; //ou Z com a string montada acima
//        JSONObject jsonFinal = controle.obterJsonRespostas(nomeApp);
//        Log.i("jsonFinal  ", jsonFinal.toString());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
