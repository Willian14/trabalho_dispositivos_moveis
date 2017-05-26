package com.example.willian.agendabarbeariaapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.willian.agendabarbeariaapp.Model.Cliente;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Willian on 13/05/2017.
 */

public class ClienteAdapter extends ArrayAdapter<Cliente> {
    public ClienteAdapter( Context context, List<Cliente> clienteList) {
        super(context, R.layout.item_lista_cliente, clienteList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

      final ViewHolder holder;

      if(convertView == null){
         convertView = View.inflate(getContext(),R.layout.item_lista_cliente,null);

          holder = new ViewHolder();
          ButterKnife.bind(holder,convertView);
          convertView.setTag(holder);

      }else{
          holder = (ViewHolder) convertView.getTag();
      }
        Cliente cliente = getItem(position);

        if(cliente != null) {
            holder.nome.setText(cliente.getNome());
            holder.telefone.setText(cliente.getTelefone());
          //  holder.imagem.setImageResource();
        }
        return convertView;
    }
    class ViewHolder{
       // @Bind(R.id.imv_item_list)
       // ImageView imagem;
        @Bind(R.id.txtNome_item_list)
        TextView nome;
        @Bind(R.id.txtTelefone_item_list)
        TextView telefone;
    }
}
