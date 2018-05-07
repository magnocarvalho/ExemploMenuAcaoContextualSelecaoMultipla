package br.edu.utfpr.alexandrefeitosa.menuacaocontextualselecaomultipla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNome;

    private int    modo;
    private String nomeOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextNome = (EditText) findViewById(R.id.editTextNome);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        modo = bundle.getInt(PrincipalActivity.MODO);

        if (modo == PrincipalActivity.ALTERAR){

            nomeOriginal = bundle.getString(PrincipalActivity.NOME);

            editTextNome.setText(nomeOriginal);

            setTitle(getString(R.string.alterar_nome));
        }else{
            setTitle(getString(R.string.novo_nome));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cadastro_opcoes, menu);
        return true;
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void salvar(){

        String nome = editTextNome.getText().toString();

        if (nome == null || nome.trim().length() == 0){
            Toast.makeText(this, getString(R.string.erro_nome_vazio), Toast.LENGTH_SHORT).show();
            editTextNome.requestFocus();
            return;
        }

        if (modo == PrincipalActivity.ALTERAR){

            if (nome.equals(nomeOriginal)){
                // Não houve alteração //
                cancelar();
                return;
            }
        }

        Intent intent = new Intent();

        intent.putExtra(PrincipalActivity.NOME, nome);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;

            case R.id.menuItemCancelar:
            case android.R.id.home:
                cancelar();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
