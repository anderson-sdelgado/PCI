package br.com.usinasantafe.pci.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

        FuncBean funcBean = pciContext.getCheckListCTR().getFunc();
        textViewDadosAuditor.setText(funcBean.getMatricFunc() + " - " + funcBean.getNomeFunc());

        OSBean osBean = pciContext.getCheckListCTR().getOS();
        textViewDadosOS.setText("NRO OS: " + osBean.getNroOS());

        itemArrayList = pciContext.getCheckListCTR().getItemArrayList();

        if(pciContext.getCheckListCTR().verPlanta(itemArrayList)){

            progressBar = new ProgressDialog( ListaQuestaoActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Atualizando Plantas...");
            progressBar.show();

            pciContext.getCheckListCTR().atualDadosPlanta(ListaQuestaoActivity.this, ListaQuestaoActivity.class, progressBar);

        }

        if(pciContext.getCheckListCTR().verServico(itemArrayList)){

            progressBar = new ProgressDialog( ListaQuestaoActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Atualizando Itens...");
            progressBar.show();

            pciContext.getCheckListCTR().atualDadosServico(ListaQuestaoActivity.this, ListaQuestaoActivity.class, progressBar);

        }

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

    }

    public void onBackPressed()  {
    }

}