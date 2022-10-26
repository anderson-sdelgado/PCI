package br.com.usinasantafe.pci;

import android.app.Application;

import java.util.ArrayList;

import br.com.usinasantafe.pci.control.CheckListCTR;
import br.com.usinasantafe.pci.control.ConfigCTR;
import br.com.usinasantafe.pci.model.bean.estatica.ItemBean;
import br.com.usinasantafe.pci.model.bean.variavel.CabecBean;
import br.com.usinasantafe.pci.model.bean.variavel.RespItemBean;

public class PCIContext extends Application {

    private ConfigCTR configCTR;
    private CheckListCTR checkListCTR;
    private Long idFunc;

    public static String versaoApp = "2.02";
    public static String versaoWS = "2.00";

    public PCIContext() {
    }

    public ConfigCTR getConfigCTR() {
        if(configCTR == null)
            configCTR = new ConfigCTR();
        return configCTR;
    }

    public CheckListCTR getCheckListCTR() {
        if(checkListCTR == null)
            checkListCTR = new CheckListCTR();
        return checkListCTR;
    }

    public Long getIdFunc() {
        return idFunc;
    }

    public void setIdFunc(Long idFunc) {
        this.idFunc = idFunc;
    }
}
