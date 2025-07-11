package br.com.usinasantafe.pci.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.usinasantafe.pci.PCIContext;
import br.com.usinasantafe.pci.R;
import br.com.usinasantafe.pci.model.bean.estatica.FuncBean;
import br.com.usinasantafe.pci.model.bean.estatica.OSBean;
import br.com.usinasantafe.pci.model.bean.variavel.PlantaCabecBean;
import br.com.usinasantafe.pci.util.ConexaoWeb;
import br.com.usinasantafe.pci.util.EnvioDadosServ;
import br.com.usinasantafe.pci.util.VerifDadosServ;

public class ListaPlantaActivity extends ActivityGeneric {

    private PCIContext pciContext;
    private ArrayList<PlantaCabecBean> plantaCabecList;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_planta);

        TextView textViewAuditor = findViewById(R.id.textViewAuditor);
        TextView textViewOS = findViewById(R.id.textViewOS);
        ListView listPlantaCL = findViewById(R.id.listPlantaCL);
        Button buttonEnviarChecklist = findViewById(R.id.buttonEnviarChecklist);
        Button buttonExcluirChecklist = findViewById(R.id.buttonExcluirChecklist);
        Button buttonRetornarChecklist = findViewById(R.id.buttonRetornarPlanta);
        Button buttonAtualPlanta = findViewById(R.id.buttonAtualPlanta);

        pciContext = (PCIContext) getApplication();

        FuncBean funcBean = pciContext.getCheckListCTR().getFunc();
        textViewAuditor.setText(funcBean.getMatricFunc() + " - " + funcBean.getNomeFunc());

        OSBean osBean = pciContext.getCheckListCTR().getOS();
        textViewOS.setText("NRO OS: " + osBean.getNroOS());

        plantaCabecList = pciContext.getCheckListCTR().retSalvarPlantaCabec();

        AdapterListPlanta adapterListPlanta = new AdapterListPlanta(this, plantaCabecList);
        listPlantaCL.setAdapter(adapterListPlanta);

        listPlantaCL.setOnItemClickListener((l, v, position, id) -> {

            PlantaCabecBean plantaCabecBean = plantaCabecList.get(position);
            pciContext.getCheckListCTR().atualStatusApontPlanta(plantaCabecBean);

            Intent it = new Intent(ListaPlantaActivity.this, ListaQuestaoActivity.class);
            startActivity(it);
            finish();

        });

        buttonEnviarChecklist.setOnClickListener(v -> {

            if(pciContext.getCheckListCTR().verPlantaEnvio()){

                pciContext.getCheckListCTR().atualStatusEnvio();

                progressBar = new ProgressDialog(ListaPlantaActivity.this);
                progressBar.setCancelable(true);
                progressBar.setMessage("ENVIANDO DADOS...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();

                EnvioDadosServ.getInstance().envioDadosPrinc(ListaPlantaActivity.this, MenuInicialActivity.class, progressBar);

            }
            else{

                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaPlantaActivity.this);
                alerta.setTitle("ATENÇÃO");
                alerta.setMessage("RESPONDA TODAS AS QUESTÕES DE PELO MENOS UMA PLANTA PARA PODE REALIZAR O ENVIO DOS DADOS.");
                alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alerta.show();

            }

        });

        buttonExcluirChecklist.setOnClickListener(v -> {

            AlertDialog.Builder alerta = new AlertDialog.Builder(  ListaPlantaActivity.this);
            alerta.setTitle("ATENÇÃO");
            alerta.setMessage("DESEJA REALMENTE EXCLUIR TODO CHECKLIST?");
            alerta.setNegativeButton("SIM", (dialog, which) -> {

                pciContext.getCheckListCTR().delCheckListApont();

                Intent it = new Intent(ListaPlantaActivity.this, MenuInicialActivity.class);
                startActivity(it);
                finish();

            });

            alerta.setPositiveButton("NÃO", (dialog, which) -> {
            });

            alerta.show();

        });

        buttonAtualPlanta.setOnClickListener(v -> {

            AlertDialog.Builder alerta = new AlertDialog.Builder(ListaPlantaActivity.this);
            alerta.setTitle("ATENÇÃO");
            alerta.setMessage("DESEJA REALMENTE ATUALIZAR BASE DE DADOS?");
            alerta.setNegativeButton("SIM", (dialog, which) -> {

                ConexaoWeb conexaoWeb = new ConexaoWeb();

                if (conexaoWeb.verificaConexao(ListaPlantaActivity.this)) {

                    progressBar = new ProgressDialog(ListaPlantaActivity.this);
                    progressBar.setCancelable(true);
                    progressBar.setMessage("Atualizando Planta...");
                    progressBar.show();

                    pciContext.getCheckListCTR().atualDadosItem(ListaPlantaActivity.this, ListaOSActivity.class, progressBar);

                } else {

                    AlertDialog.Builder alerta1 = new AlertDialog.Builder( ListaPlantaActivity.this);
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

        });

        buttonRetornarChecklist.setOnClickListener(v -> {
            Intent it = new Intent(ListaPlantaActivity.this, ListaOSActivity.class);
            startActivity(it);
            finish();
        });

    }

    public void onBackPressed()  {
    }

}