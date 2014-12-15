package br.com.coletor.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import br.com.coletor.ColetorMovel.Constantes;
import br.com.coletor.ColetorMovel.Controle;
import br.com.coletor.ColetorMovel.DataBaseHelper;
import br.com.coletor.teste.R;
import org.json.JSONObject;

public class JsonResposta extends ActionBarActivity {
    private Bundle extras;
    EditText editTextResposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_resposta);
        DataBaseHelper banco = new DataBaseHelper(getApplicationContext());

        int idExterno =0;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if(extras == null) {
                idExterno= 0;
            } else {
                idExterno= extras.getInt(Constantes.ID_EXTERNO);
            }
        }
        editTextResposta = (EditText)findViewById(R.id.txt_json_resposta);
        String nomeApp = banco.obterNomeApp(idExterno);
        Controle controle = new Controle(getApplicationContext());
        JSONObject jsonResposta = controle.obterJsonRespostas(nomeApp);
        editTextResposta.setText(jsonResposta.toString());
    }



}
