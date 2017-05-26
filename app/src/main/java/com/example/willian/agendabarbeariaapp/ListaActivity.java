package com.example.willian.agendabarbeariaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.willian.agendabarbeariaapp.Model.Cliente;
import com.example.willian.agendabarbeariaapp.dao.ClienteDao;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Willian on 13/05/2017.
 */

public class ListaActivity extends AppCompatActivity {
    private ListView listaCliente;
    Cliente cliente;
    private ClienteHelper helper;
    private List<Cliente> lista;

    private ClienteAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listaCliente = (ListView) findViewById(R.id.ltw);
        listaCliente.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        listaCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cliente clienteSelecionado = adapter.getItem(position);
                System.out.println(clienteSelecionado);
                Intent cadCliente = new Intent(ListaActivity.this, CadastroActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("clienteSelecionado", clienteSelecionado);
                System.out.println(clienteSelecionado);
                cadCliente.putExtras(bundle);

                startActivity(cadCliente);

            }
        });
        registerForContextMenu(listaCliente);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Cliente clienteSelecionado = adapter.getItem(info.position);
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                confirmaRemocao(clienteSelecionado);
                return false;
            }
        });
    }

    private void confirmaRemocao(final Cliente clienteSelecionado) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Remoção");
        dialogBuilder.setMessage(
                String.format("Confirma a remoção do cliente %s?",
                        clienteSelecionado.getNome()));
        dialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ClienteDao dao = new ClienteDao(ListaActivity.this);
                dao.delete(clienteSelecionado.getId());
                dao.close();
                carregarLista();
                dialog.cancel();
            }
        });
        dialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        dialogBuilder.create().show();
    }

    public void carregarLista() {


        ClienteDao dao = new ClienteDao(this);
        lista = dao.listarClientes();
        dao.close();
        adapter = new ClienteAdapter(this, lista);
        listaCliente.setAdapter(adapter);
        for (Cliente c : lista) {
            System.out.println(c);
        }
    }

    @OnClick(R.id.floatbt)
    public void irParaActivityCadatro() {
        Intent i2 = new Intent(ListaActivity.this, CadastroActivity.class);
        startActivity(i2);

    }
}
