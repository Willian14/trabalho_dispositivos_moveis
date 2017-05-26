package com.example.willian.agendabarbeariaapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.willian.agendabarbeariaapp.Model.Cliente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willian on 22/05/2017.
 */

public class ClienteDao extends SQLiteOpenHelper {
    final static String DATABASE = "CLIENTES";
    final static int VERSION = 1;
    final static String TABLE = "cliente";


    public ClienteDao(Context context) {
        super(context, DATABASE, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddlCliente = "create table cliente (" +
                "id integer not null primary key autoincrement," +
                "nome text not null ," +
                "telefone text not null," +
                "dataAgend text not null," +
                "horaAgend text not null, " +
                "tipoServico text );";
        db.execSQL(ddlCliente);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private ContentValues getValues(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put("nome", cliente.getNome());
        values.put("telefone", cliente.getTelefone());
        values.put("dataAgend", cliente.getData_agend());
        values.put("horaAgend", cliente.getHora_agend());
        values.put("tipoServico", cliente.getTipoServico());

        return values;

    }

    public void insert(Cliente cliente) {
        getWritableDatabase().insert(TABLE, null, getValues(cliente));
    }

    public void delete(Long id) {
        String[] args = {id.toString()};
        getWritableDatabase().delete(TABLE, "id=?", args);

    }

    public void upDate(Cliente cliente) {
        String[] args = {String.valueOf(cliente.getId())};
        getWritableDatabase().update(TABLE, getValues(cliente), "id=?", args);
    }

    public Cliente findById(String id) {
        String[] args = {id};
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE + " where id=?", args);
        Cliente cliente = null;
        if (c.moveToNext()) {
            cliente = fill(c);
        }
        c.close();
        return cliente;

    }

    public List<Cliente> listarClientes() {
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE + " order by nome", null);
        List<Cliente> lista = new ArrayList<>();
        while (c.moveToNext()) {
            lista.add(fill(c));
        }
        c.close();
        return lista;
    }

    private Cliente fill(Cursor c) {
        Cliente cliente = new Cliente();
        cliente.setId(c.getLong(c.getColumnIndex("id")));
        cliente.setNome(c.getString(c.getColumnIndex("nome")));
        cliente.setTelefone(c.getString(c.getColumnIndex("telefone")));
        cliente.setData_agend(c.getString(c.getColumnIndex("dataAgend")));
        cliente.setHora_agend(c.getString(c.getColumnIndex("horaAgend")));
        cliente.setTipoServico(c.getString(c.getColumnIndex("tipoServico")));
        return cliente;

    }

}