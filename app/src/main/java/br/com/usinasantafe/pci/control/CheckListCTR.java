package br.com.usinasantafe.pci.control;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.usinasantafe.pci.model.bean.estatica.ComponenteBean;
import br.com.usinasantafe.pci.model.bean.estatica.FuncBean;
import br.com.usinasantafe.pci.model.bean.estatica.ItemBean;
import br.com.usinasantafe.pci.model.bean.estatica.OSBean;
import br.com.usinasantafe.pci.model.bean.estatica.ServicoBean;
import br.com.usinasantafe.pci.model.bean.variavel.CabecBean;
import br.com.usinasantafe.pci.model.bean.variavel.PlantaCabecBean;
import br.com.usinasantafe.pci.model.bean.variavel.RespItemBean;
import br.com.usinasantafe.pci.model.dao.AtualAplicDAO;
import br.com.usinasantafe.pci.model.dao.CabecDAO;
import br.com.usinasantafe.pci.model.dao.ComponenteDAO;
import br.com.usinasantafe.pci.model.dao.FuncDAO;
import br.com.usinasantafe.pci.model.dao.ItemDAO;
import br.com.usinasantafe.pci.model.dao.OSDAO;
import br.com.usinasantafe.pci.model.dao.PlantaCabecDAO;
import br.com.usinasantafe.pci.model.dao.PlantaDAO;
import br.com.usinasantafe.pci.model.dao.RespItemDAO;
import br.com.usinasantafe.pci.model.dao.ServicoDAO;
import br.com.usinasantafe.pci.util.AtualDadosServ;
import br.com.usinasantafe.pci.util.EnvioDadosServ;
import br.com.usinasantafe.pci.util.Json;
import br.com.usinasantafe.pci.util.VerifDadosServ;

public class CheckListCTR {

    private CabecBean cabecBean;
    private ItemBean itemBean;

    public CheckListCTR() {
    }

    ////////////////////////////// SALVAR OU ATUALIZAR CABEC /////////////////////////////////////

    public void salvarAtualCabec(OSBean osBean) {

        CabecDAO cabecDAO = new CabecDAO();
        if(!cabecDAO.verCabecAbertoOS(osBean)){
            cabecDAO.salvarCabecAberto(cabecBean, osBean);
        }

        cabecDAO.updStatusApont(osBean);

    }

    public ArrayList<PlantaCabecBean> retSalvarPlantaCabec(){

        ArrayList<PlantaCabecBean> plantaCabecArrayList = new ArrayList();
        ItemDAO itemDAO = new ItemDAO();
        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        CabecDAO cabecDAO = new CabecDAO();

        if(plantaCabecDAO.verPlantaCabec(cabecDAO.getCabecApont().getIdCabec())){
            plantaCabecArrayList = plantaCabecDAO.plantaCabecSemEnvioArrayList(cabecDAO.getCabecApont().getIdCabec());
            ArrayList<Long> idPlantaOSArrayList = itemDAO.idPlantaArrayList(cabecDAO.getCabecApont().getIdOSCabec());
            for(Long idPlantaOS : idPlantaOSArrayList){
                boolean verPlanta = true;
                for(PlantaCabecBean plantaCabecBean : plantaCabecArrayList){
                    if(idPlantaOS.equals(plantaCabecBean.getIdPlanta())){
                        verPlanta = false;
                    }
                }
                if(verPlanta){
                    plantaCabecArrayList.add(plantaCabecDAO.salvarPlantaCabec(idPlantaOS, cabecDAO.getCabecApont().getIdCabec()));
                }
            }
        }
        else{
            ArrayList<Long> idPlantaOSArrayList = itemDAO.idPlantaArrayList(cabecDAO.getCabecApont().getIdOSCabec());
            for(Long idPlantaOS : idPlantaOSArrayList){
                plantaCabecArrayList.add(plantaCabecDAO.salvarPlantaCabec(idPlantaOS, cabecDAO.getCabecApont().getIdCabec()));
            }
        }

        return plantaCabecArrayList;

    }

    public void salvarAtualRespItem(Long opcao, String obs) {

        RespItemDAO respItemDAO = new RespItemDAO();
        CabecDAO cabecDAO = new CabecDAO();
        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        if(verRespItem()){
            respItemDAO.salvarRespItem(cabecDAO.getCabecApont().getIdCabec(), itemBean, plantaCabecDAO.getPlantaCabecApont(cabecDAO.getCabecApont().getIdCabec()).getIdPlantaCabec(), opcao, obs);
        }
        else{
            respItemDAO.updRespItem(cabecDAO.getCabecApont().getIdCabec(), itemBean, opcao, obs);
        }

    }

    public void atualStatusApontPlanta(PlantaCabecBean plantaCabecBean){

        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        CabecDAO cabecDAO = new CabecDAO();
        plantaCabecDAO.updStatusApontPlantaCabec(plantaCabecBean, cabecDAO.getCabecApont().getIdCabec());

    }

    public void atualStatusEnvio(){

        CabecDAO cabecDAO = new CabecDAO();

        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        plantaCabecDAO.updStatusPlantaFechadoEnvio(cabecDAO.getCabecApont().getIdCabec());

        if(!plantaCabecDAO.verPlantaAberto(cabecDAO.getCabecApont().getIdCabec())){
            cabecDAO.updStatusFechado(cabecDAO.getCabecApont());
        }

    }

    public void updStatusApont(){

        CabecDAO cabecDAO = new CabecDAO();
        cabecDAO.updStatusApont();

        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        plantaCabecDAO.updStatusApontPlanta();

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////// DELETAR DADOS  ////////////////////////////////////////////

    public void deleteCabecResp(){

        CabecDAO cabecDAO = new CabecDAO();
        ArrayList<Long> idCabecList = cabecDAO.cabecDiaAnterior();

        if(idCabecList.size() > 0){

            for(Long idCabec : idCabecList){

                PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
                ArrayList<Long> idPlantaList = plantaCabecDAO.idPlantaCabecNaoEnvioList(idCabec);

                if(idPlantaList.size() > 0){

                    RespItemDAO respItemDAO = new RespItemDAO();
                    respItemDAO.deleteItemPlantaCabec(idPlantaList);

                    plantaCabecDAO.deletePlanta(idPlantaList);

                }

                if(plantaCabecDAO.verPlantaEnvio(idCabec)){
                    cabecDAO.updStatusFechado(idCabec);
                }
                else{
                    cabecDAO.deleteCabec(idCabec);
                }

            }

        }

    }

    public void delCheckListApont(){

        CabecDAO cabecDAO = new CabecDAO();
        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();

        ArrayList<Long> idPlantaList = plantaCabecDAO.idPlantaCabecSemEnvioArrayList(cabecDAO.getCabecApont().getIdCabec());

        if(idPlantaList.size() > 0){

            RespItemDAO respItemDAO = new RespItemDAO();
            respItemDAO.deleteItemPlantaCabec(idPlantaList);

            plantaCabecDAO.deletePlanta(idPlantaList);

        }

        if(!plantaCabecDAO.verPlantaEnvio(cabecDAO.getCabecApont().getIdCabec())){
            cabecDAO.deleteCabec(cabecDAO.getCabecApont().getIdCabec());
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////// VERIFICAR DADOS /////////////////////////////////////

    public boolean hasElementFunc(){
        FuncDAO funcDAO = new FuncDAO();
        return funcDAO.hasElements();
    }

    public boolean verFunc(Long matricFunc){
        FuncDAO funcDAO = new FuncDAO();
        return funcDAO.verMatricFunc(matricFunc);
    }

    public boolean verPlantaEnvio(){
        CabecDAO cabecDAO = new CabecDAO();
        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        return plantaCabecDAO.verPlantaTerminada(cabecDAO.getCabecApont().getIdCabec());
    }

    public boolean verPlanta(ArrayList<ItemBean> itemList){
        boolean verPlanta = false;
        PlantaDAO plantaDAO = new PlantaDAO();
        for(ItemBean itemBean : itemList){
            verPlanta = plantaDAO.verPlanta(itemBean.getIdPlantaItem());
        }
        return verPlanta;
    }

    public boolean verServico(ArrayList<ItemBean> itemList){
        boolean verServico = false;
        ServicoDAO servicoDAO = new ServicoDAO();
        for(int i = 0; i < itemList.size(); i++){
            ItemBean itemBean = itemList.get(i);
            verServico = servicoDAO.verServico(itemBean.getIdServicoItem());
        }
        return verServico;
    }

    public boolean verRespItem() {
        RespItemDAO respItemDAO = new RespItemDAO();
        CabecDAO cabecDAO = new CabecDAO();
        return respItemDAO.verRespItem(cabecDAO.getCabecApont().getIdCabec(), itemBean.getIdItem());
    }

    public boolean verComponente(Long idComponente){
        ComponenteDAO componenteDAO = new ComponenteDAO();
        return componenteDAO.verComponente(idComponente);
    }

    public boolean verDadosEnvio(){
        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        return plantaCabecDAO.verPlantaEnvio();
    }

    public boolean verCabecFechado(Long idFunc){
        CabecDAO cabecDAO = new CabecDAO();
        return cabecDAO.verCabecFechado(idFunc);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////// GET CAMPOS /////////////////////////////////////

    public ItemBean getItemBean() {
        return itemBean;
    }

    public ServicoBean getServico(Long idServico){
        ServicoDAO servicoDAO = new ServicoDAO();
        return servicoDAO.getServico(idServico);
    }

    public ComponenteBean getComponente(Long idComponente){
        ComponenteDAO componenteDAO = new ComponenteDAO();
        return componenteDAO.getComponente(idComponente);
    }

    public RespItemBean getRespItem(){
        RespItemDAO respItemDAO = new RespItemDAO();
        CabecDAO cabecDAO = new CabecDAO();
        return respItemDAO.getRespItem(cabecDAO.getCabecApont().getIdCabec(), itemBean.getIdItem());
    }

    public FuncBean getFunc(Long matricFunc){
        FuncDAO funcDAO = new FuncDAO();
        return funcDAO.getMatricFunc(matricFunc);
    }

    public FuncBean getFunc(){
        CabecDAO cabecDAO = new CabecDAO();
        FuncDAO funcDAO = new FuncDAO();
        return funcDAO.getIdFunc(cabecDAO.getCabecApont().getIdFuncCabec());
    }

    public OSBean getOS(){
        CabecDAO cabecDAO = new CabecDAO();
        OSDAO osDAO = new OSDAO();
        return osDAO.getOS(cabecDAO.getCabecApont().getIdOSCabec());
    }

    public ArrayList<OSBean> osList(){
        CabecDAO cabecDAO = new CabecDAO();
        OSDAO osDAO = new OSDAO();
        List<CabecBean> cabecAbertoList = cabecDAO.cabecAbertoList();
        List<CabecBean> cabecFechadoList = cabecDAO.cabecFechadoList();
        List<OSBean> osList = osDAO.osList();
        ArrayList retOSList = new ArrayList();
        ArrayList<Long> qtdeDiasList = new ArrayList<Long>();
        if(cabecAbertoList.size() > 0){
            for(CabecBean cabecBean : cabecAbertoList){
                for(OSBean osBean : osList){
                    if(cabecBean.getIdOSCabec().equals(osBean.getIdOS())){
                        retOSList.add(osBean);
                        qtdeDiasList.add(osBean.getQtdeDiaOS());
                    }
                }
            }
        }
        cabecAbertoList.clear();
        for(OSBean osBean : osList){
            boolean verOS = true;
            for (Long qtdeDias : qtdeDiasList) {
                if (Objects.equals(osBean.getQtdeDiaOS(), qtdeDias)) {
                    verOS = false;
                    break;
                }
            }
            for (CabecBean cabecBean : cabecFechadoList) {
                if (Objects.equals(osBean.getIdOS(), cabecBean.getIdOSCabec())) {
                    verOS = false;
                    break;
                }
            }
            if(verOS){
                retOSList.add(osBean);
            }
        }
        return retOSList;
    }

    public ArrayList<String> osFeitasList(Long idFunc){
        CabecDAO cabecDAO = new CabecDAO();
        List<CabecBean> cabecList = cabecDAO.cabecFechadoList(idFunc);
        ArrayList<String> osList = new ArrayList();
        for(CabecBean cabecBean : cabecList){
            OSDAO osDAO = new OSDAO();
            osList.add(osDAO.getOS(cabecBean.getIdOSCabec()).getNroOS().toString());
        }
        return osList;
    }

    public ArrayList<ItemBean> getItemArrayList(){

        ArrayList<ItemBean> itemArrayList = new ArrayList<>();

        CabecDAO cabecDAO = new CabecDAO();
        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        ItemDAO itemDAO = new ItemDAO();
        RespItemDAO respItemDAO = new RespItemDAO();

        PlantaCabecBean plantaCabecBean = plantaCabecDAO.getPlantaCabecApont(cabecDAO.getCabecApont().getIdCabec());
        List<ItemBean> itemList = itemDAO.itemList(plantaCabecBean.getIdPlanta());
        List<RespItemBean> respItemList = respItemDAO.respItemIdCabecList(cabecDAO.getCabecApont().getIdCabec());

        for(ItemBean itemBean : itemList){
            boolean ver = true;
            for (RespItemBean respItemBean : respItemList) {
                if(itemBean.getIdItem().equals(respItemBean.getIdItOsMecanRespItem())){
                    ver = false;
                }
            }
            if(ver) {
                itemBean.setOpcaoSelItem(0L);
                itemArrayList.add(itemBean);
            }
        }

        if(itemArrayList.size() == 0){
            plantaCabecDAO.updStatusPlantaFinalizar(plantaCabecBean);
        }

        for(ItemBean itemBean : itemList){
            boolean ver = false;
            for (RespItemBean respItemBean : respItemList) {
                if(itemBean.getIdItem().equals(respItemBean.getIdItOsMecanRespItem())){
                    ver = true;
                    if(respItemBean.getOpcaoRespItem() == 2L){
                        itemBean.setOpcaoSelItem(2L);
                    }
                    else if(respItemBean.getOpcaoRespItem() == 1L){
                        itemBean.setOpcaoSelItem(1L);
                    }
                }
            }
            if(ver) {
                itemArrayList.add(itemBean);
            }
        }

        return itemArrayList;

    }

    public void deleteOS(){
        OSDAO osDAO = new OSDAO();
        osDAO.deleteOS();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////// SETAR CAMPOS //////////////////////////////////////////////////

    public void setIniciarCabec(Long matricFunc){
        this.cabecBean = new CabecBean();
        this.cabecBean.setIdFuncCabec(getFunc(matricFunc).getIdFunc());
        this.cabecBean.setIdOficSecaoCabec(getFunc(matricFunc).getIdOficSecaoFunc());
    }

    public void setItemBean(ItemBean itemBean) {
        this.itemBean = itemBean;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////// VERIFICAÇÃO E ATUALIZAÇÃO DE DADOS POR SERVIDOR ///////////////////////

    public void verOS(Context telaAtual, Class telaProx, ProgressDialog progressDialog){
        OSDAO osDAO = new OSDAO();
        AtualAplicDAO atualAplicDAO = new AtualAplicDAO();
        osDAO.verOS(atualAplicDAO.getAtualIdOficSecao(this.cabecBean.getIdOficSecaoCabec()), telaAtual, telaProx, progressDialog);
    }

    public void verItem(Long idOS, Context telaAtual, Class telaProx, ProgressDialog progressDialog){
        ItemDAO itemDAO = new ItemDAO();
        AtualAplicDAO atualAplicDAO = new AtualAplicDAO();
        itemDAO.verItem(atualAplicDAO.getAtualIdOS(idOS), telaAtual, telaProx, progressDialog);
    }

    public void atualDadosFunc(Context telaAtual, Class telaProx, ProgressDialog progressDialog){
        ArrayList operadorArrayList = new ArrayList();
        operadorArrayList.add("FuncBean");
        AtualDadosServ.getInstance().atualGenericoBD(telaAtual, telaProx, progressDialog, operadorArrayList);
    }

    public void atualDadosPlanta(Context telaAtual, Class telaProx, ProgressDialog progressDialog){
        ArrayList operadorArrayList = new ArrayList();
        operadorArrayList.add("PlantaBean");
        AtualDadosServ.getInstance().atualGenericoBD(telaAtual, telaProx, progressDialog, operadorArrayList);
    }

    public void atualDadosServico(Context telaAtual, Class telaProx, ProgressDialog progressDialog){
        ArrayList operadorArrayList = new ArrayList();
        operadorArrayList.add("ServicoBean");
        AtualDadosServ.getInstance().atualGenericoBD(telaAtual, telaProx, progressDialog, operadorArrayList);
    }

    public void receberVerifItem(String result){

        try {

            if (!result.contains("exceeded")) {

                Json json = new Json();

                ItemDAO itemDAO = new ItemDAO();
                itemDAO.recDadosItem(json.jsonArray(result.trim()));

                VerifDadosServ.getInstance().pulaTela();

            } else {
                VerifDadosServ.getInstance().msg("EXCEDEU TEMPO LIMITE DE PESQUISA! POR FAVOR, PROCURE UM PONTO MELHOR DE CONEXÃO DOS DADOS.");
            }
        } catch (Exception e) {
            VerifDadosServ.getInstance().msg("FALHA DE PESQUISA DE ATIVIDADE! POR FAVOR, TENTAR NOVAMENTE COM UM SINAL MELHOR.");
        }
    }

    public void receberVerifOS(String result){

        try {

            if (!result.contains("exceeded")) {

                JSONObject jObj = new JSONObject(result);
                JSONArray jsonArray = jObj.getJSONArray("dados");

                if (jsonArray.length() > 0) {

                    OSDAO osDAO = new OSDAO();
                    osDAO.recDadosOS(jsonArray);

                    VerifDadosServ.getInstance().pulaTela();

                } else {
                    VerifDadosServ.getInstance().msg("NÃO EXISTE O.S. PARA ESSE COLABORADOR! POR FAVOR, ENTRE EM CONTATO COM A AREA QUE CRIA O.S. PARA APONTAMENTO.");
                }

            }
            else{
                VerifDadosServ.getInstance().msg("EXCEDEU TEMPO LIMITE DE PESQUISA! POR FAVOR, PROCURE UM PONTO MELHOR DE CONEXÃO DOS DADOS.");
            }

        } catch (Exception e) {
            VerifDadosServ.getInstance().msg("FALHA DE PESQUISA DE OS! POR FAVOR, TENTAR NOVAMENTE COM UM SINAL MELHOR.");
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////ENVIO DE DADOS////////////////////////////////////////////

    public int qtdeCabecEnvio(){

        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        CabecDAO cabecDAO = new CabecDAO();

        ArrayList<Long> idCabecList = plantaCabecDAO.idCabecPlantaEnvioList();
        return cabecDAO.cabecList(idCabecList).size() + 1;

    }

    public String dadosEnvio() {

        PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
        CabecDAO cabecDAO = new CabecDAO();
        RespItemDAO respItemDAO = new RespItemDAO();

        ArrayList<Long> idCabecList = plantaCabecDAO.idCabecPlantaEnvioList();
        String cabec = cabecDAO.dadosEnvioCabec(cabecDAO.cabecList(idCabecList));

        ArrayList<Long> idPlantaCabecList = plantaCabecDAO.idPlantaCabecEnvioList();
        String item = respItemDAO.dadosEnvioRespItem(respItemDAO.respItemIdPlantaCabecEnvio(idPlantaCabecList));

        String planta = plantaCabecDAO.dadosEnvioPlantaCabec(plantaCabecDAO.plantaCabecEnvioList());

        return cabec + "_" + planta + "#" + item;

    }

    public void recDados(String retorno){

        try {

            int pos2 = retorno.indexOf("#") + 1;

            String objSeg = retorno.substring(pos2);

            PlantaCabecDAO plantaCabecDAO = new PlantaCabecDAO();
            plantaCabecDAO.updPlantaEnviada(objSeg);

        } catch(Exception e){
            EnvioDadosServ.getInstance().falhaEnvio();
        }

    }

}
