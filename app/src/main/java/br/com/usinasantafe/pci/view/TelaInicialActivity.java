package br.com.usinasantafe.pci.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.usinasantafe.pci.PCIContext;
import br.com.usinasantafe.pci.R;

public class TelaInicialActivity extends ActivityGeneric {

    private PCIContext pciContext;
    private Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        pciContext = (PCIContext) getApplication();
        customHandler.postDelayed(excluirBDThread, 0);

    }
    private Runnable excluirBDThread = () -> {

        clearBD();
        Intent it = new Intent(TelaInicialActivity.this, MenuInicialActivity.class);
        startActivity(it);
        finish();

    };

    public void clearBD() {
        pciContext.getCheckListCTR().deleteOS();
        pciContext.getCheckListCTR().deleteCabecResp();
        pciContext.getCheckListCTR().updStatusApont();
    }

}