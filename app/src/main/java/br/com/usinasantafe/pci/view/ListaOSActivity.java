package br.com.usinasantafe.pci.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.usinasantafe.pci.PCIContext;
import br.com.usinasantafe.pci.R;
import br.com.usinasantafe.pci.model.bean.estatica.OSBean;
import br.com.usinasantafe.pci.util.VerifDadosServ;

public class ListaOSActivity extends ActivityGeneric {

    private PCIContext pciContext;
    private ProgressDialog progressBar;
    private ArrayList<OSBean> osCabList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_os);

        ListView listViewOS = findViewById(R.id.listViewOS);
        Button buttonRetOS = findViewById(R.id.buttonRetOS);
        Button buttonAtualOS = findViewById(R.id.buttonAtualOS);

        pciContext = (PCIContext) getApplication();
        osCabList = pciContext.getCheckListCTR().osList();

        if(osCabList.size() == 0){
            atualizarOS();
        }

        AdapterListOS adapterListOS = new AdapterListOS(this, osCabList);
        listViewOS.setAdapter(adapterListOS);

        listViewOS.setOnItemClickListener((l, v, position, id) -> {

            OSBean osBean = osCabList.get(position);

            pciContext.getCheckListCTR().salvarAtualCabec(osBean);

            progressBar = new ProgressDialog(v.getContext());
            progressBar.setCancelable(true);
            progressBar.setMessage("Pequisando Item...");
            progressBar.show();

            pciContext.getCheckListCTR().verItem(osBean.getIdOS(), ListaOSActivity.this, ListaPlantaActivity.class, progressBar);

        });

        buttonAtualOS.setOnClickListener(v -> atualizarOS());


        buttonRetOS.setOnClickListener(v -> {
            Intent it = new Intent(ListaOSActivity.this, FuncionarioActivity.class);
            startActivity(it);
            finish();
            osCabList.clear();
        });

    }

    private void atualizarOS() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Pequisando a OS...");
        progressBar.show();

        pciContext.getCheckListCTR().verOS(ListaOSActivity.this, ListaOSActivity.class, progressBar);
    }

    public void onBackPressed()  {
    }

}