package br.com.usinasantafe.pci.util.conHttp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.com.usinasantafe.pci.util.AtualDadosServ;


public class PostBDGenerico extends AsyncTask<String, Void, String> {

	private static PostBDGenerico instance = null;
	private Map<String, Object> parametrosPost = null;
	private String tipo = null;
	
	private UrlsConexaoHttp urlsConexaoHttp;


	public PostBDGenerico() {
		urlsConexaoHttp = new UrlsConexaoHttp();
	}
	
    public static PostBDGenerico getInstance() {
        if (instance == null)
        instance = new PostBDGenerico();
        return instance;
    }


	@Override
	protected String doInBackground(String... arg) {
		
		String resultado = "";
		BufferedReader bufferedReader = null;
		
		tipo = arg[0];
		String url = "";
		
		try {
			
			Object o = new Object();
            Class<?> retClasse = Class.forName(urlsConexaoHttp.localUrl); 
			
            for (Field field : retClasse.getDeclaredFields()) {
                String campo = field.getName();
                if(campo.equals(tipo)){
                	url = "" + retClasse.getField(campo).get(o);
               }
                
            }
            
            Log.i("PCI", "URL = " + url);

			String parametros = getQueryString(parametrosPost);
			URL urlCon = new URL(url);
			HttpsURLConnection connection = (HttpsURLConnection) urlCon.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts(), new java.security.SecureRandom());
			connection.setSSLSocketFactory(sc.getSocketFactory());
			connection.setHostnameVerifier((s, sslSession) -> true);
			connection.connect();

			OutputStream out = connection.getOutputStream();
			byte[] bytes = parametros.getBytes("UTF8");
			out.write(bytes);
			out.flush();
			out.close();

			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String LS = System.getProperty("line.separator");
			while((line = bufferedReader.readLine()) != null){
				stringBuffer.append(line + LS);
			}
			bufferedReader.close();
			resultado = stringBuffer.toString();
            
		} catch (Exception e) {
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (Exception erro) {
					
				}
			}
		}
		finally{
			
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (Exception e) {
					
				}
			}
			
		}
		
		return resultado;
	}
	
	protected void onPostExecute(String result) {

		try {
			
			AtualDadosServ.getInstance().manipularDadosHttp(tipo, result);
			
		} catch (Exception e) {
			Log.i("ECM", "Erro2 = " + e);
			AtualDadosServ.getInstance().encerrar();
		}

    }

	public void setParametrosPost(Map<String, Object> parametrosPost) {
		this.parametrosPost = parametrosPost;
	}

	private String getQueryString(Map<String, Object> params){
		if (params == null || params.size() == 0) {
			return null;
		}
		String urlParams = null;
		Iterator<String> e = params.keySet().iterator();
		while (e.hasNext()) {
			String chave = e.next();
			Object objValor = params.get(chave);
			String valor = objValor.toString();
			urlParams = urlParams == null ? "" : urlParams + "&";
			urlParams += chave + "=" + valor;
		}
		return urlParams;
	}

	public TrustManager[] trustAllCerts(){
		return new TrustManager[]{
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers()
					{
						return null;
					}
					public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
					public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
				}
		};
	}

}
