package br.com.usinasantafe.pci.model.dao;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.usinasantafe.pci.model.bean.estatica.ItemBean;
import br.com.usinasantafe.pci.util.Json;
import br.com.usinasantafe.pci.util.VerifDadosServ;

public class ItemDAO {

    public ItemDAO() {
    }

    public List<ItemBean> itemList(Long idPlantaItem) {
        ItemBean itemBean = new ItemBean();
        return itemBean.getAndOrderBy("idPlantaItem", idPlantaItem, "idComponenteItem", true);
    }

    public ArrayList<Long> idPlantaArrayList(Long idOS){
        ItemBean itemBean = new ItemBean();
        List<ItemBean> itemList = itemBean.getAndOrderBy("idOsItem", idOS, "idPlantaItem", true);
        Long idPlanta = 0L;
        ArrayList<Long> idPlantaList = new ArrayList<>();
        for (ItemBean itemBeanBD : itemList) {
            if(!idPlanta.equals(itemBeanBD.getIdPlantaItem())){
                idPlantaList.add(itemBeanBD.getIdPlantaItem());
                idPlanta = itemBeanBD.getIdPlantaItem();
            }
        }
        return idPlantaList;
    }

    public void verItem(String dado, Context telaAtual, Class telaProx, ProgressDialog progressDialog){
        VerifDadosServ.getInstance().verDados(dado, "Item", telaAtual, telaProx, progressDialog);
    }

    public void recDadosItem(JSONArray jsonArray) throws JSONException {

        ItemBean itemBean = new ItemBean();
        itemBean.deleteAll();

        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject objeto = jsonArray.getJSONObject(j);
            Gson gson = new Gson();
            ItemBean itemBeanBD = gson.fromJson(objeto.toString(), ItemBean.class);
            itemBeanBD.insert();
        }

    }

}
