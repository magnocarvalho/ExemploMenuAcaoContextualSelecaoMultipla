package br.edu.utfpr.alexandrefeitosa.menuacaocontextualselecaomultipla;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {

    private ListView             listViewNomes;
    private ArrayAdapter<String> listaAdapter;

    private ArrayList<String>    listaNomes;

    public static final String MODO     = "MODO";
    public static final String NOME     = "NOME";
    public static final int    NOVO     = 1;
    public static final int    ALTERAR  = 2;

    private int posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        listViewNomes = (ListView) findViewById(R.id.listViewNomes);

        listViewNomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                alterarNome(position);
            }
        });

        listViewNomes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listViewNomes.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {

                boolean selecionado = listViewNomes.isItemChecked(position);

                View view = listViewNomes.getChildAt(position);

                if (selecionado){
                    view.setBackgroundColor(Color.LTGRAY);
                }else{
                    view.setBackgroundColor(Color.TRANSPARENT);
                }

                int totalSelecionados = listViewNomes.getCheckedItemCount();

                if (totalSelecionados > 0){

                    mode.setTitle(getResources().getQuantityString(R.plurals.selecionado,
                                                                   totalSelecionados,
                                                                   totalSelecionados));
                }

                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.principal_item_selecionado, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {

                if (listViewNomes.getCheckedItemCount() > 1){
                    menu.getItem(0).setVisible(false);
                }else{
                    menu.getItem(0).setVisible(true);
                }

                return true;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {

                switch(item.getItemId()){
                    case R.id.menuItemAlterar:

                        for (int posicao = listViewNomes.getChildCount(); posicao >= 0; posicao--){

                            if (listViewNomes.isItemChecked(posicao)){
                                alterarNome(posicao);
                            }
                        }

                        mode.finish();
                        return true;

                    case R.id.menuItemExcluir:
                        excluirNomes();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {

                for (int posicao = 0; posicao < listViewNomes.getChildCount(); posicao++){

                    View view = listViewNomes.getChildAt(posicao);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        popularLista();
    }

    private void popularLista(){

        listaNomes  = new ArrayList<String>();

        listaAdapter = new ArrayAdapter<String>(this,
                                                android.R.layout.simple_list_item_1,
                                                listaNomes);

        listViewNomes.setAdapter(listaAdapter);
    }

    private void novoNome(){

        Intent intent = new Intent(this, CadastroActivity.class);

        intent.putExtra(MODO, NOVO);

        startActivityForResult(intent, NOVO);
    }

    private void alterarNome(int posicao){

        posicaoSelecionada = posicao;

        String nome = listaNomes.get(posicaoSelecionada);

        Intent intent = new Intent(this, CadastroActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(NOME, nome);

        startActivityForResult(intent, ALTERAR);
    }

    private void excluirNomes(){

        for (int posicao = listViewNomes.getChildCount(); posicao >= 0; posicao--){

            if (listViewNomes.isItemChecked(posicao)){
                listaNomes.remove(posicao);
            }
        }

        listaAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK){

            Bundle bundle = data.getExtras();

            String nome = bundle.getString(NOME);

            if (requestCode == ALTERAR) {

                listaNomes.remove(posicaoSelecionada);
                listaNomes.add(posicaoSelecionada, nome);
                posicaoSelecionada = -1;

            }else{
                listaNomes.add(nome);
            }

            listaAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemNovo:
                novoNome();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
