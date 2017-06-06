package com.example.willian.agendabarbeariaapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;

import butterknife.ButterKnife;


/**
 * Created by Willian on 11/05/2017.
 */

public class CadastroActivity extends AppCompatActivity {


    private ClienteHelper helper;
    private static final int TIRAR_FOTO = 1;
    private static final int ESCOLHER_FOTO = 2;
    private String localArquivoFoto;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_cliente);
        ButterKnife.bind(this);
        helper = new ClienteHelper(this);
        Button btnfoto = helper.getBtFoto();
        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
        registerForContextMenu(btnfoto);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem biblioteca = menu.add("Procurar na biblioteca");
        biblioteca.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "selecionar imagem"), ESCOLHER_FOTO);
                return true;
            }
        });
        MenuItem camera = menu.add("Tirar Foto");
        camera.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                localArquivoFoto = getExternalFilesDir(null) + "/" +
                        System.currentTimeMillis() + ".jpg";
                Intent intentCamera = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(localArquivoFoto)));
                startActivityForResult(intentCamera, TIRAR_FOTO);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ESCOLHER_FOTO) {
                localArquivoFoto = ImageFilePath.getPath(getApplicationContext(), data.getData());
                helper.setImage(localArquivoFoto);
            } else if (requestCode == TIRAR_FOTO) {
                helper.setImage(localArquivoFoto);
            } else {
                localArquivoFoto = null;
            }
        }
    }

}



