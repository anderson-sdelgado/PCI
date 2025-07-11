package br.com.usinasantafe.pci.view;

import androidx.appcompat.app.AppCompatActivity;

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
import br.com.usinasantafe.pci.model.bean.estatica.ItemBean;
import br.com.usinasantafe.pci.model.bean.estatica.OSBean;
import br.com.usinasantafe.pci.util.ConexaoWeb;

public class ListaQuestaoActivity extends ActivityGeneric {

    private PCIContext pciContext;
    private ArrayList<ItemBean> itemArrayList;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_questao);

        pciContext = (PCIContext) getApplication();

        TextView textViewDadosAuditor = findViewById(R.id.textViewDadosAuditor);
        TextView textViewDadosOS = findViewById(R.id.textViewDadosOS);
        ListView listViewQuestao = findViewById(R.id.listViewQuestao);
        Button buttonRetQuestao = findViewById(R.id.buttonRetQuestao);
        Button buttonAtualQuestao = findViewById(R.id.buttonAtualQuestao);

        FuncBean funcBean = pciContext.getCheckListCTR().getFunc();
        textViewDadosAuditor.setText(funcBean.getMatricFunc() + " - " + funcBean.getNomeFunc());

        OSBean osBean = pciContext.getCheckListCTR().getOS();
        textViewDadosOS.setText("NRO OS: " + osBean.getNroOS());

        itemArrayList = pciContext.getCheckListCTR().getItemArrayList();

        AdapterListQuestao adapterListQuestao = new AdapterListQuestao(this, itemArrayList);
        listViewQuestao.setAdapter(adapterListQuestao);

        listViewQuestao.setOnItemClickListener((l, v, position, id) -> {

            pciContext.getCheckListCTR().setItemBean(itemArrayList.get(position));

            if(pciContext.getCheckListCTR().verRespItem()){
                Intent it = new Intent(ListaQuestaoActivity.this, QuestaoActivity.class);
                startActivity(it);
                finish();
            }
            else{
                Intent it = new Intent(ListaQuestaoActivity.this, DescricaoQuestaoActivity.class);
                startActivity(it);
                finish();
            }


        });

        buttonRetQuestao.setOnClickListener(v -> {

            Intent it = new Intent(ListaQuestaoActivity.this, ListaPlantaActivity.class);
            startActivity(it);
            finish();

        });

        buttonAtualQuestao.setOnClickListener(v -> {


            AlertDialog.Builder alerta = new AlertDialog.Builder(ListaQuestaoActivity.this);
            alerta.setTitle("ATENÇÃO");
            alerta.setMessage("DESEJA REALMENTE ATUALIZAR BASE DE DADOS?");
            alerta.setNegativeButton("SIM", (dialog, which) -> {

                ConexaoWeb conexaoWeb = new ConexaoWeb();

                if (conexaoWeb.verificaConexao(ListaQuestaoActivity.this)) {

                    progressBar = new ProgressDialog(ListaQuestaoActivity.this);
                    progressBar.setCancelable(true);
                    progressBar.setMessage("Atualizando Questão e Serviços...");
                    progressBar.show();

                    pciContext.getCheckListCTR().atualDadosPlantaServico(ListaQuestaoActivity.this, ListaOSActivity.class, progressBar);

                } else {

                    AlertDialog.Builder alerta1 = new AlertDialog.Builder( ListaQuestaoActivity.this);
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

    }

    public void onBackPressed()  {
    }

}