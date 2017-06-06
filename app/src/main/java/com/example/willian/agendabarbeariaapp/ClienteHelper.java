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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

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


    String[] sampleImages = {};
    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatadorDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");


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
                        System.out.println(cliente.toString());
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


        boolean validar = true;
        boolean horaAnterior = true;
        String datDig = edData.getText().toString();
        String horDig = edHora.getText().toString();
        String dataValid = datDig + " " + horDig;
        Calendar dataDig = Calendar.getInstance();
        try {
            dataDig.setTime(formatadorDataHora.parse(dataValid));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar dataAtual = Calendar.getInstance();
        dataAtual.add(Calendar.SECOND, -59);
        if (dataDig.before(dataAtual)) {
            horaAnterior = false;
        }
        ClienteDao dao = new ClienteDao(activity);

        if (horaAnterior == true) {
            lista = dao.listarClientes();
            String data = cliente.getData_agend();
            String hora = cliente.getHora_agend();
            String dataDigitada = data + " " + hora;
            String dataClienteAlterando = null;
            String acao = "cadastrar";
            String id = String.valueOf(cliente.getId());
            Cliente clienteEncontrado = dao.findById(id);

            if (clienteEncontrado != null) {
                acao = "alterar";
            }
            if (acao.equals("alterar")) {
                String dataE = clienteEncontrado.getData_agend().toString();
                String horaE = clienteEncontrado.getHora_agend();
                dataClienteAlterando = dataE + " " + horaE;
                if (dataDigitada.equals(dataClienteAlterando)) {
                    validar = true;
                } else {
                    Calendar dataCliente = Calendar.getInstance();
                    try {
                        dataCliente.setTime(formatadorDataHora.parse(dataDigitada));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < lista.size(); i++) {
                        String dataP = lista.get(i).getData_agend().toString();
                        String horaP = lista.get(i).getHora_agend().toString();
                        String dataPosition = dataP + " " + horaP;
                        Calendar dataPosicao = Calendar.getInstance();

                        try {
                            dataPosicao.setTime(formatadorDataHora.parse(dataPosition));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar dataPosterior = Calendar.getInstance();
                        dataPosterior.setTime(dataPosicao.getTime());
                        dataPosterior.add(Calendar.MINUTE, 30);
                        if (dataCliente.compareTo(dataPosicao) == 0 || dataCliente.after(dataPosicao)
                                && dataCliente.before(dataPosterior) || dataCliente.compareTo(dataPosterior) == 0) {
                            String dialog = formatadorDataHora.format(dataPosicao.getTime());

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                            dialogBuilder.setTitle("Erro");
                            dialogBuilder.setMessage(
                                    String.format("Já existe agendamento nesse horário! selecione 30 min. a partir de -->> %s", dialog));
                            dialogBuilder.setPositiveButton("Retornar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.cancel();
                                }
                            });
                            dialogBuilder.create().show();
                            validar = false;


                        }
                    }
                }
            }

            if (acao.equals("cadastrar")) {
                String dataE = edData.getText().toString();
                String horaE = edHora.getText().toString();
                String dataClienteCadastrando = dataE + " " + horaE;

                Calendar dataCliente = Calendar.getInstance();
                try {
                    dataCliente.setTime(formatadorDataHora.parse(dataClienteCadastrando));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < lista.size(); i++) {
                    String dataP = lista.get(i).getData_agend().toString();
                    String horaP = lista.get(i).getHora_agend().toString();
                    String dataPosition = dataP + " " + horaP;
                    Calendar dataPosicao = Calendar.getInstance();

                    try {
                        dataPosicao.setTime(formatadorDataHora.parse(dataPosition));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar dataPosterior = Calendar.getInstance();
                    dataPosterior.setTime(dataPosicao.getTime());
                    dataPosterior.add(Calendar.MINUTE, 30);

                    if (dataCliente.compareTo(dataPosicao) == 0 || dataCliente.after(dataPosicao)
                            && dataCliente.before(dataPosterior) || dataCliente.compareTo(dataPosterior) == 0) {
                        String dialog = formatadorDataHora.format(dataPosicao.getTime());

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                        dialogBuilder.setTitle("Erro");
                        dialogBuilder.setMessage(
                                String.format("Já existe agendamento nesse horário! selecione 30 min. a partir de -->> %s", dialog));
                        dialogBuilder.setPositiveButton("Retornar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.cancel();
                            }
                        });
                        dialogBuilder.create().show();
                        validar = false;

                        break;
                    }
                }
            }
        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            dialogBuilder.setTitle("Erro");
            dialogBuilder.setMessage(
                    String.format("Impossível selecionar Hora ou data anteriores a atual!"));
            dialogBuilder.setPositiveButton("Retornar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                }
            });
            dialogBuilder.create().show();
            horaAnterior = false;
            validar = false;
        }

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
