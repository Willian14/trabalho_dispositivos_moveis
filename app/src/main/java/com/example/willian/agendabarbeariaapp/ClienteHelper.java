package com.example.willian.agendabarbeariaapp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.willian.agendabarbeariaapp.Model.Cliente;
import com.example.willian.agendabarbeariaapp.dao.ClienteDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Willian on 16/05/2017.
 */

public class ClienteHelper {
    private CadastroActivity activity;
    private ImageView img1, img2;
    private EditText edNome, edTelefone, edData, edHora;
    private Button btData, btHora, btSalvar, btFoto;
    private Spinner spinnerTipoServico;
    private List<String> tipoServico = Arrays.asList(new String[]{
            "--Selecione--", "cabelo", "barba", "bigode", "cabelo,barba e bigode"});
    private ArrayAdapter<String> adapter;
    private String tipoServicoSelecionado;
    private Cliente cliente, clienteExistente;
    private List<Cliente> lista;


    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");


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

        btFoto = (Button) activity.findViewById(R.id.btn_foto);
        btSalvar = (Button) activity.findViewById(R.id.btn_salvarCliente);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliente = carregarDadosDaTela();
                if (validarHoraEdata(cliente)) {
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


    public void setImage(String localArquivoFoto) {
        if (localArquivoFoto != null) {
            Bitmap imagemFoto = BitmapFactory.decodeFile(localArquivoFoto);
            Bitmap imagemFotoReduzida = Bitmap
                    .createScaledBitmap(imagemFoto, imagemFoto.getWidth(),
                            200, true);
            img1.setImageBitmap(imagemFotoReduzida);
            img1.setTag(localArquivoFoto);
            img1.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    public boolean validarHoraEdata(Cliente cliente) {
        this.cliente = cliente;

        boolean validar = true;

        ClienteDao dao = new ClienteDao(activity);
        int quantPosterior = 0;
        lista = dao.listarClientes();
        String data = cliente.getData_agend();
        String horaCliente = cliente.getHora_agend();
        Calendar dataAgend = Calendar.getInstance();
        try {
            dataAgend.setTime(formatDate.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar horaAgend = Calendar.getInstance();
        try {
            horaAgend.setTime(format.parse(horaCliente));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < lista.size(); i++) {
            Calendar horaClienteExist = Calendar.getInstance();
            try {
                horaClienteExist.setTime(format.parse(lista.get(i).getHora_agend()));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar dataClienteExist = Calendar.getInstance();

            try {
                dataClienteExist.setTime(formatDate.parse(lista.get(i).getData_agend()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar horaAnterior = Calendar.getInstance();
            horaAnterior.setTime(horaClienteExist.getTime());
            horaAnterior.add(Calendar.MINUTE, -30);

            Calendar horaPosterior = Calendar.getInstance();
            horaPosterior.setTime(horaClienteExist.getTime());
            horaPosterior.add(Calendar.MINUTE, 30);


            if (horaAgend.getTime().equals(horaPosterior.getTime())) {
                quantPosterior = quantPosterior + 1;
            }
            if (quantPosterior > 0) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                dialogBuilder.setTitle("--->>>ERRO<<<---");
                dialogBuilder.setMessage(
                        String.format("Já exite um agendamento nesse horário ! Escolha um outro horário 30 Minutos após o que você Escolheu "));
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.cancel();
                    }
                });

                dialogBuilder.create().show();
                validar = false;
                break;
            }

        }

            /*
            int hora = horaAgend.get(Calendar.HOUR);
            int minuto = horaAgend.get(Calendar.MINUTE);

            for (int i = 0; i < lista.size(); i++) {
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(format.parse(lista.get(i).getHora_agend()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int h = c.get(Calendar.HOUR);
                int m = c.get(Calendar.MINUTE);
                if (hora == h || minuto == m || minuto == m + 29) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                    dialogBuilder.setTitle("--->>>ERRO<<<---");
                    dialogBuilder.setMessage(
                            String.format("Já exite um agendamento nesse horário ! Escolha um outro horário 30 Minutos após o que você Escolheu "));
                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.cancel();
                        }
                    });

                    dialogBuilder.create().show();
                    validar = false;

                    // edHora.setError("Já existe um agendamento nesse horário! ");
                    break;
                }

            }
        }
        */
        return validar;
    }

    public Button getBtFoto() {
        return btFoto;
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
        cliente.setCaminhoFoto((String) img1.getTag());
        cliente.setTipoServico(tipoServicoSelecionado);


        return cliente;
    }

    public void carregaDadosParaTela(Cliente cliente) {
        this.cliente = cliente;
        edNome.setText(cliente.getNome());
        edTelefone.setText(cliente.getTelefone());
        edData.setText(cliente.getData_agend());
        edHora.setText(cliente.getHora_agend());
        setImage(cliente.getCaminhoFoto());
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
        if (edHora.getText().toString().trim().isEmpty()) {
            edHora.setError("O campo hora é obrigatório!");
            validar = false;
        }
        if (edData.getText().toString().trim().isEmpty()) {
            edData.setError("O campo data é obrigatório!");
            validar = false;
        }

        if (spinnerTipoServico.getSelectedItem().toString().equals("--Selecione--")) {
            Toast.makeText(activity, "Selecione um tipo de serviço", Toast.LENGTH_SHORT).show();
            validar = false;
        }
        return validar;
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
