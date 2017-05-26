package com.example.willian.agendabarbeariaapp;

import android.app.DialogFragment;
import android.content.Intent;
;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.willian.agendabarbeariaapp.Model.Cliente;
import com.example.willian.agendabarbeariaapp.dao.ClienteDao;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import java.util.List;

/**
 * Created by Willian on 16/05/2017.
 */

public class ClienteHelper {
    private CadastroActivity activity;
    private ImageView img1, img2;
    private EditText edNome, edTelefone, edData, edHora;
    private Button btData, btHora, btSalvar;
    private Spinner spinnerTipoServico;
    private List<String> tipoServico = Arrays.asList(new String[]{
            "--Selecione--", "cabelo", "barba", "bigode", "cabelo,barba e bigode"});
    private ArrayAdapter<String> adapter;
    private String tipoServicoSelecionado;
    private Cliente cliente;
    private boolean intecao = true;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");


    public ClienteHelper(final CadastroActivity activity) {
        this.activity = activity;

        adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, tipoServico);
        spinnerTipoServico = (Spinner) activity.findViewById(R.id.spn_tipo_servico_cad);
        spinnerTipoServico.setAdapter(adapter);
        img1 = (ImageView) activity.findViewById(R.id.imv_cad);
        edNome = (EditText) activity.findViewById(R.id.edt_nome_cad);
        edTelefone = (EditText) activity.findViewById(R.id.edt_telefone_cad);
        edData = (EditText) activity.findViewById(R.id.edtData_cad);
        edHora = (EditText) activity.findViewById(R.id.edtHora_cad);
        spinnerTipoServico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoServicoSelecionado = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btSalvar = (Button) activity.findViewById(R.id.btn_salvarCliente);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cliente = carregarDadosDaTela();
                System.out.println(cliente);
                if (validarCampos()) {
                    ClienteDao dao = new ClienteDao(activity);
                    if (cliente.getId() == 0) {
                        dao.insert(cliente);
                    } else {
                        dao.upDate(cliente);
                    }
                    dao.close();
                    Intent intent = new Intent(activity, ListaActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                }
            }


        });
        btData = (Button) activity.findViewById(R.id.btn_data);
        btData.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                showDatePicker(v);

            }
        });

        btHora = (Button) activity.findViewById(R.id.btn_hora);
        btHora.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                showTimePicker(v);
            }
        });
        Intent i = activity.getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            cliente = (Cliente) b.getSerializable("clienteSelecionado");
            carregaDadosParaTela(cliente);
        } else {
            cliente = retornaClientevazio();
            carregaDadosParaTela(cliente);
        }


    }

    public Cliente retornaClientevazio() {
        cliente = new Cliente();
        cliente.setId((long) 0);
        cliente.setNome("");
        cliente.setTelefone("");
        cliente.setData_agend("");
        cliente.setHora_agend("");
        cliente.setTipoServico("");

        System.out.println(cliente);
        return cliente;
    }



    public Cliente carregarDadosDaTela() {
        cliente.setNome(edNome.getText().toString());
        cliente.setTelefone(edTelefone.getText().toString());
        cliente.setData_agend(edData.getText().toString());
        cliente.setHora_agend(edHora.getText().toString());
        cliente.setTipoServico(tipoServicoSelecionado);


        return cliente;
    }

    public void carregaDadosParaTela(Cliente cliente) {
        this.cliente = cliente;
        edNome.setText(cliente.getNome());
        edTelefone.setText(cliente.getTelefone());
        edData.setText(cliente.getData_agend());
        edHora.setText(cliente.getHora_agend());
        spinnerTipoServico.setSelection(tipoServico.indexOf(cliente.getTipoServico()));

    }

    public boolean validarCampos() {
        boolean validar = true;

        if (edNome.getText().toString().trim().isEmpty()) {
            edNome.setError(" O Campo nome é obrigatório!");
            validar = false;
        }
        if (edTelefone.getText().toString().trim().isEmpty()) {
            edTelefone.setError("O Campo telefone é obrigatório!");
            validar = false;
        }
        //if(edHora.getText().toString().trim().isEmpty()){
        //  edHora.setError("O campo hora é obrigatório!");
        //validar = false;
        //}
        return validar;
    }

    public Cliente salvarDadosCliente() {
        cliente.setNome(edNome.getText().toString());
        cliente.setTelefone(edTelefone.getText().toString());
        cliente.setData_agend(edData.getText().toString());
        cliente.setHora_agend(edHora.getText().toString());

        return cliente;

    }

    public Button getBtSalvar() {
        return btSalvar;
    }


    public void showDatePicker(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(activity.getFragmentManager(), "datePicker");
    }

    public void showTimePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(activity.getFragmentManager(), "timePicker");
    }


}
