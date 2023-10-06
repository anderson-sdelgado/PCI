package br.com.usinasantafe.pci.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.usinasantafe.pci.control.CheckListCTR;
import br.com.usinasantafe.pci.model.dao.ItemDAO;
import br.com.usinasantafe.pci.view.MenuInicialActivity;
import br.com.usinasantafe.pci.control.ConfigCTR;
import br.com.usinasantafe.pci.model.dao.OSDAO;
import br.com.usinasantafe.pci.util.conHttp.PostVerGenerico;
import br.com.usinasantafe.pci.util.conHttp.UrlsConexaoHttp;
import br.com.usinasantafe.pci.model.pst.GenericRecordable;
import br.com.usinasantafe.pci.model.bean.AtualAplicBean;

/**
 * Created by anderson on 16/11/2015.
 */
public class VerifDadosServ {

    private static VerifDadosServ instance = null;
    private GenericRecordable genericRecordable;
    private UrlsConexaoHttp urlsConexaoHttp;
    private ProgressDialog progressDialog;
    private Context telaAtual;
    private Class telaProx;
    private String dados;
    private String classe;
    private PostVerGenerico postVerGenerico;
    private MenuInicialActivity menuInicialActivity;
    public static int status;

    public VerifDadosServ() {
        genericRecordable = new GenericRecordable();
    }

    public static VerifDadosServ getInstance() {
        if (instance == null)
            instance = new VerifDadosServ();
        return instance;
    }

    public void manipularDadosHttp(String result) {

        try {
            CheckListCTR checkListCTR = new CheckListCTR();
            ConfigCTR configCTR = new ConfigCTR();
            if(this.classe.equals("OS")) {
                checkListCTR.receberVerifOS(result);
            } else if(this.classe.equals("Item")) {
                checkListCTR.receberVerifItem(result);
            } else if(this.classe.equals("Token")) {
                configCTR.recToken(result.trim(), this.telaAtual, this.telaProx, this.progressDialog);
            }
        } catch (Exception e) {
            Log.i("PMM", "Erro Manip atualizar = " + e);
        }

    }


    public void verDados(String dados, String tipo, Context telaAtual, Class telaProx, ProgressDialog progressDialog) {

        urlsConexaoHttp = new UrlsConexaoHttp();
        this.telaAtual = telaAtual;
        this.telaProx = telaProx;
        this.progressDialog = progressDialog;
        this.dados = dados;
        this.classe = tipo;

        envioVerif();

    }


    public void salvarToken(String dados, Context telaAtual, Class telaProx, ProgressDialog progressDialog) {

        this.urlsConexaoHttp = new UrlsConexaoHttp();
        this.telaAtual = telaAtual;
        this.telaProx = telaProx;
        this.progressDialog = progressDialog;
        this.dados = dados;
        this.classe = "Token";

        envioVerif();

    }

    public void envioVerif() {

        status = 2;
        this.urlsConexaoHttp = new UrlsConexaoHttp();
        String[] url = {urlsConexaoHttp.urlVerifica(classe)};
        Map<String, Object> parametrosPost = new HashMap<>();
        parametrosPost.put("dado", this.dados);

        Log.i("PMM", "postVerGenerico.execute('" + urlsConexaoHttp.urlVerifica(classe) + "'); - Dados de Envio = " + this.dados);
       postVerGenerico = new PostVerGenerico();
        postVerGenerico.setParametrosPost(parametrosPost);
        postVerGenerico.execute(url);

    }

    public void cancel() {
        status = 3;
        if (postVerGenerico.getStatus() == AsyncTask.Status.RUNNING) {
            postVerGenerico.cancel(true);
        }
    }

    public void pulaTela(){
        if(status < 3){
            status = 3;
            this.progressDialog.dismiss();
            Intent it = new Intent(telaAtual, telaProx);
            telaAtual.startActivity(it);
        }
    }


    public void msg(String texto){
        if(status < 3){
            status = 3;
            this.progressDialog.dismiss();
            AlertDialog.Builder alerta = new AlertDialog.Builder(telaAtual);
            alerta.setTitle("ATENÇÃO");
            alerta.setMessage(texto);
            alerta.setPositiveButton("OK", (dialog, which) -> {
            });
            alerta.show();
        }
    }

}
