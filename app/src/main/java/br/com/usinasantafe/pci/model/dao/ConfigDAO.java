package br.com.usinasantafe.pci.model.dao;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.usinasantafe.pci.model.bean.AtualAplicBean;
import br.com.usinasantafe.pci.model.bean.variavel.ConfigBean;

public class ConfigDAO {

    public ConfigDAO() {
    }

    public boolean hasElements(){
        ConfigBean configBean = new ConfigBean();
        return configBean.hasElements();
    }

    public ConfigBean getConfig(){
        ConfigBean configBean = new ConfigBean();
        List configList = configBean.all();
        configBean = (ConfigBean) configList.get(0);
        configList.clear();
        return configBean;
    }

    public void salvarConfig(Long numLinha){
        ConfigBean configBean = new ConfigBean();
        configBean.deleteAll();
        configBean.setNroAparelhoConfig(numLinha);
        configBean.insert();
    }

    public AtualAplicBean recAparelho(JSONArray jsonArray) throws JSONException {

        JSONObject objeto = jsonArray.getJSONObject(0);
        Gson gson = new Gson();
        AtualAplicBean atualAplicBean = gson.fromJson(objeto.toString(), AtualAplicBean.class);

        return atualAplicBean;

    }

}
