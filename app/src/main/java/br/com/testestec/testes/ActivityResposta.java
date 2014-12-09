package br.com.testestec.testes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import br.com.testestec.moduloChecagem.Constantes;
import br.com.testestec.moduloChecagem.Controle;
import br.com.testestec.moduloChecagem.DataBaseHelper;
import org.json.JSONObject;

public class ActivityResposta extends ActionBarActivity {
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
