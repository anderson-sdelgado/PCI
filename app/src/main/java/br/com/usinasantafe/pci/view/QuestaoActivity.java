package br.com.usinasantafe.pci.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.usinasantafe.pci.PCIContext;
import br.com.usinasantafe.pci.R;
import br.com.usinasantafe.pci.model.bean.estatica.ComponenteBean;

public class QuestaoActivity extends ActivityGeneric {

    private PCIContext pciContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questao);

        pciContext = (PCIContext) getApplication();

        TextView textViewItemQuestao = findViewById(R.id.textViewItemQuestao);
        Button buttonConforme = findViewById(R.id.buttonConforme);
        Button buttonNaoConforme = findViewById(R.id.buttonNaoConforme);
        Button buttonRetQuestao = findViewById(R.id.buttonRetQuestao);

        String texto = pciContext.getCheckListCTR().getServico(pciContext.getCheckListCTR().getItemBean().getIdServicoItem()).getDescrServico();

        if(pciContext.getCheckListCTR().verComponente(pciContext.getCheckListCTR().getItemBean().getIdServicoItem())){
            ComponenteBean componenteBean = pciContext.getCheckListCTR().getComponente(pciContext.getCheckListCTR().getItemBean().getIdServicoItem());
            texto = texto + "\n" + componenteBean.getCodComponente() + " - " +componenteBean.getDescrComponente();
        }

        textViewItemQuestao.setText(texto);

        buttonRetQuestao.setOnClickListener(v -> {
            Intent it = new Intent(QuestaoActivity.this, ListaQuestaoActivity.class);
            startActivity(it);
            finish();
        });

        buttonConforme.setOnClickListener(v -> {

            pciContext.getCheckListCTR().salvarAtualRespItem(2L, "null");

            Intent it = new Intent(QuestaoActivity.this, ListaQuestaoActivity.class);
            startActivity(it);
            finish();

        });

        buttonNaoConforme.setOnClickListener(v -> {
            Intent it = new Intent(QuestaoActivity.this, ObsQuestaoActivity.class);
            startActivity(it);
            finish();
        });

    }

    public void onBackPressed()  {
    }

}