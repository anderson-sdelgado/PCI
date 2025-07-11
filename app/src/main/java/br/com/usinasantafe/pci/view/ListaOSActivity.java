package br.com.usinasantafe.pci.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import br.com.usinasantafe.pci.util.ConexaoWeb;
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

        AdapterListOS adapterListOS = new AdapterListOS(this, osCabList);
        listViewOS.setAdapter(adapterListOS);

        listViewOS.setOnItemClickListener((l, v, position, id) -> {

            OSBean osBean = osCabList.get(position);

            pciContext.getCheckListCTR().salvarAtualCabec(osBean);
            Intent it = new Intent(ListaOSActivity.this, ListaPlantaActivity.class);
            startActivity(it);
            finish();

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

        AlertDialog.Builder alerta = new AlertDialog.Builder(ListaOSActivity.this);
        alerta.setTitle("ATENÇÃO");
        alerta.setMessage("DESEJA REALMENTE ATUALIZAR BASE DE DADOS?");
        alerta.setNegativeButton("SIM", (dialog, which) -> {

            ConexaoWeb conexaoWeb = new ConexaoWeb();

            if (conexaoWeb.verificaConexao(ListaOSActivity.this)) {

                progressBar = new ProgressDialog(ListaOSActivity.this);
                progressBar.setCancelable(true);
                progressBar.setMessage("Atualizando OS...");
                progressBar.show();

                pciContext.getCheckListCTR().atualDadosOS(ListaOSActivity.this, ListaOSActivity.class, progressBar);

            } else {

                AlertDialog.Builder alerta1 = new AlertDialog.Builder( ListaOSActivity.this);
                alerta1.setTitle("ATENÇÃO");
                alerta1.setMessage("FALHA NA CONEXÃO DE DADOS. O CELULAR ESTA SEM SINAL. POR FAVOR, TENTE NOVAMENTE QUANDO O CELULAR ESTIVE COM SINAL.");
                alerta1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alerta1.show();

            }


        });

        alerta.setPositiveButton("NÃO", (dialog, which) -> {
        });

        alerta.show();

    }

    public void onBackPressed()  {
    }

}