package com.example.willian.agendabarbeariaapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.willian.agendabarbeariaapp.Model.Cliente;


/**
 * Created by Willian on 11/05/2017.
 */

public class CadastroActivity extends AppCompatActivity {


    private ClienteHelper helper;
    private Cliente cliente;
    private EditText edData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_cliente);
        final ClienteHelper helper = new ClienteHelper(this);



    }

}



