package br.com.usinasantafe.pci.control;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.usinasantafe.pci.model.bean.AtualAplicBean;
import br.com.usinasantafe.pci.model.dao.AtualAplicDAO;
import br.com.usinasantafe.pci.view.MenuInicialActivity;
import br.com.usinasantafe.pci.model.bean.variavel.ConfigBean;
import br.com.usinasantafe.pci.model.dao.ConfigDAO;
import br.com.usinasantafe.pci.util.AtualDadosServ;
import br.com.usinasantafe.pci.util.VerifDadosServ;

public class ConfigCTR {

    public ConfigCTR() {
    }

    /////////////////////////////MANIPULAR CONFIG///////////////////////////////////////////

    public void salvarConfig(Long numLinha){
        ConfigDAO configDAO = new ConfigDAO();
        configDAO.salvarConfig(numLinha);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////// CONFIG //////////////////////////////////////////////

    public boolean hasElements(){
        ConfigDAO configDAO = new ConfigDAO();
        return configDAO.hasElements();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////// GET CONFIG, EQUIP E COLAB ////////////////////////////////////

    public ConfigBean getConfig(){
        ConfigDAO configDAO = new ConfigDAO();
        return configDAO.getConfig();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void salvarToken(String versao, Long nroAparelho, Context telaAtual, Class telaProx, ProgressDialog progressDialog){
        AtualAplicDAO atualAplicDAO = new AtualAplicDAO();
        VerifDadosServ.getInstance().salvarToken(atualAplicDAO.dadosAplic(nroAparelho, versao), telaAtual, telaProx, progressDialog);
    }

    public void atualTodasTabelas(Context tela, ProgressDialog progressDialog){
        AtualDadosServ.getInstance().atualTodasTabBD(tela, progressDialog);
    }

    public void recToken(String result, Context telaAtual, Class telaProx, ProgressDialog progressDialog) {

        AtualAplicBean atualAplicBean = new AtualAplicBean();

        try {

            progressDialog.dismiss();

            JSONObject jObj = new JSONObject(result);
            JSONArray jsonArray = jObj.getJSONArray("dados");

            if (jsonArray.length() > 0) {
                AtualAplicDAO atualAplicDAO = new AtualAplicDAO();
                atualAplicBean = atualAplicDAO.recAparelho(jsonArray);
            }

            salvarConfig(atualAplicBean.getNroAparelho());
            progressDialog = new ProgressDialog(telaAtual);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("ATUALIZANDO ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();

            AtualDadosServ.getInstance().atualTodasTabBD(telaAtual, telaProx, progressDialog);

        } catch (Exception e) {
            VerifDadosServ.status = 1;
        }
    }

}
