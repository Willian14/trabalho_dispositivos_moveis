package com.example.willian.agendabarbeariaapp.Model;


import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Willian on 10/05/2017.
 */

public class Cliente implements Serializable {
    private String nome;
    private String telefone;
    private String data_agend;
    private String hora_agend;
    private String tipoServico;
    private Long id;
    private String caminhoFoto;

    public String getData_agend() {
        return data_agend;
    }

    public void setData_agend(String data_agend) {
        this.data_agend = data_agend;
    }

    public String getHora_agend() {
        return hora_agend;
    }

    public void setHora_agend(String hora_agend) {
        this.hora_agend = hora_agend;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", data_agend='" + data_agend + '\'' +
                ", hora_agend='" + hora_agend + '\'' +
                ", tipoServico='" + tipoServico + '\'' +
                ", id=" + id +
                ", caminhoFoto='" + caminhoFoto + '\'' +
                '}';
    }
}
