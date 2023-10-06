package br.com.usinasantafe.pci.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.usinasantafe.pci.model.dao.AtualAplicDAO;
import br.com.usinasantafe.pci.model.pst.GenericRecordable;
import br.com.usinasantafe.pci.util.conHttp.PostBDGenerico;
import br.com.usinasantafe.pci.util.conHttp.UrlsConexaoHttp;

public class AtualDadosServ {

	private ArrayList tabAtualArrayList;
	private static AtualDadosServ instance = null;
	private int contAtualBD = 0;
	private String classe = "";
	private ProgressDialog progressDialog;
	private int qtdeBD = 0;
	private GenericRecordable genericRecordable;
	private Context telaAtual;
	private Class telaProx;
	private int tipoReceb;
	private UrlsConexaoHttp urlsConexaoHttp;

	public AtualDadosServ() {
		genericRecordable = new GenericRecordable();
	}
	
    public static AtualDadosServ getInstance() {
        if (instance == null)
        instance = new AtualDadosServ();
        return instance;
    }
	
	@SuppressWarnings("unchecked")
	public void manipularDadosHttp(String tipo, String result){
		
		if(!result.equals("")){

			try{

				Log.i("PCI", "RESULT -> " + result);

				JSONObject jObj = new JSONObject(result);
				JSONArray jsonArray = jObj.getJSONArray("dados");
				Class classe = Class.forName(manipLocalClasse(tipo));
				genericRecordable.deleteAll(classe);

				for(int i = 0; i < jsonArray.length(); i++){
					JSONObject objeto = jsonArray.getJSONObject(i);
					Gson gson = new Gson();
					Log.i("PCI", objeto.toString());
					genericRecordable.insert(gson.fromJson(objeto.toString(), classe), classe);
				}

				if(contAtualBD > 0) {
					atualizandoBD();
				}

			} catch (Exception e) {
				Log.i("PCI", "Erro Manip = " + e);
			}

		}
		else{
			encerrar();
		}
		
	}

	public void startAtualizacao(){

		classe = (String) tabAtualArrayList.get(contAtualBD);
		String[] url = {classe};
		contAtualBD++;

		AtualAplicDAO atualAplicDAO = new AtualAplicDAO();
		Map<String, Object> parametrosPost = new HashMap<>();
		parametrosPost.put("dado", atualAplicDAO.getAtualBDToken());

		PostBDGenerico postBDGenerico = new PostBDGenerico();
		postBDGenerico.setParametrosPost(parametrosPost);
		postBDGenerico.execute(url);

	}

	public void atualTodasTabBD(Context telaAtual, ProgressDialog progressDialog){
		
		try {
			
			this.tipoReceb = 1;
			this.telaAtual = telaAtual;
			this.progressDialog = progressDialog;
			tabAtualArrayList = new ArrayList();
	        Class<?> retClasse = Class.forName(urlsConexaoHttp.localUrl); 

	        for (Field field : retClasse.getDeclaredFields()) {
	            String campo = field.getName();
	            Log.i("PCI", "Campo = " + campo);
	            if(campo.contains("Bean")){
	            	tabAtualArrayList.add(campo);
	            }
	            
	        }

			startAtualizacao();
	        
	        
		} catch (Exception e) {
			Log.i("PCI", "Erro Manip2 = " + e);
		}
        
	}

	public void atualTodasTabBD(Context telaAtual, Class telaProx, ProgressDialog progressDialog){

		try {

			this.tipoReceb = 3;
			this.telaAtual = telaAtual;
			this.telaProx = telaProx;
			this.progressDialog = progressDialog;
			tabAtualArrayList = new ArrayList();
			Class<?> retClasse = Class.forName(urlsConexaoHttp.localUrl);

			for (Field field : retClasse.getDeclaredFields()) {
				String campo = field.getName();
				Log.i("PCI", "Campo = " + campo);
				if(campo.contains("Bean")){
					tabAtualArrayList.add(campo);
				}

			}

			startAtualizacao();

		} catch (Exception e) {
			Log.i("PCI", "Erro Manip2 = " + e);
		}

	}

	public void atualGenericoBD(Context telaAtual, Class telaProx, ProgressDialog progressDialog, ArrayList classeArrayList){

		try {

			this.tipoReceb = 1;
			this.telaAtual = telaAtual;
			this.telaProx = telaProx;
			this.progressDialog = progressDialog;
			tabAtualArrayList = new ArrayList();

			Class<?> retClasse = Class.forName(urlsConexaoHttp.localUrl);

			for (Field field : retClasse.getDeclaredFields()) {
				String campo = field.getName();
				Log.i("PCI", "Campo = " + campo);
				for (int i = 0; i < classeArrayList.size(); i++) {
					String classe = (String) classeArrayList.get(i);
					if(campo.equals(classe)){
						tabAtualArrayList.add(campo);
					}
				}
			}

			startAtualizacao();

		} catch (Exception e) {
			Log.i("PCI", "ERRO = " + e);
		}

	}
	
	public void atualizandoBD(){

		if((this.tipoReceb == 1) || (this.tipoReceb == 3)) {
		
			qtdeBD = tabAtualArrayList.size();
			
			if(contAtualBD < tabAtualArrayList.size()){
				this.progressDialog.setProgress((contAtualBD * 100) / qtdeBD);

				startAtualizacao();
		        
			} else {
				this.progressDialog.dismiss();
				contAtualBD = 0;
				AlertDialog.Builder alerta = new AlertDialog.Builder(this.telaAtual);
				alerta.setTitle("ATENCAO");
				alerta.setMessage("FOI ATUALIZADO COM SUCESSO OS DADOS.");
				alerta.setPositiveButton("OK", (dialog, which) -> {
					if(tipoReceb == 3){
						Intent it = new Intent(telaAtual, telaProx);
						telaAtual.startActivity(it);
					}
				});
				
				alerta.show();
			}
		
		} else if(this.tipoReceb == 2) {
			
			qtdeBD = tabAtualArrayList.size();
			
			if(contAtualBD < tabAtualArrayList.size()){

				startAtualizacao();
		        
			} else {
				contAtualBD = 0;
			}
			
		}

	}
	
	
	public void encerrar(){
		
		if(this.tipoReceb == 1){
			
			this.progressDialog.dismiss();
			AlertDialog.Builder alerta = new AlertDialog.Builder(this.telaAtual);
			alerta.setTitle("ATENCAO");
			alerta.setMessage("FALHA NA CONEXAO DE DADOS. O CELULAR ESTA SEM SINAL. POR FAVOR, TENTE NOVAMENTE QUANDO O CELULAR ESTIVE COM SINAL.");
			alerta.setPositiveButton("OK", (dialog, which) -> {
			});
			
			alerta.show();
			
		}
	}

	public String manipLocalClasse(String classe){
	    classe = urlsConexaoHttp.localPSTEstatica + classe;
		return classe;
	}

}
