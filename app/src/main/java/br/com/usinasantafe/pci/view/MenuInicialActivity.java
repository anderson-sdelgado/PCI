package br.com.usinasantafe.pci.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.usinasantafe.pci.BuildConfig;
import br.com.usinasantafe.pci.PCIContext;
import br.com.usinasantafe.pci.R;

public class MenuInicialActivity extends ActivityGeneric {

    private PCIContext pciContext;
    private TextView textViewProcesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        pciContext = (PCIContext) getApplication();
        textViewProcesso = findViewById(R.id.textViewProcesso);
        TextView textViewPrincipal = findViewById(R.id.textViewPrincipal);

        textViewPrincipal.setText("PRINCIPAL - V " + BuildConfig.VERSION_NAME);

        verifEnvio();

        ArrayList<String> itens = new ArrayList<>();
        itens.add("CHECKLIST");
        itens.add("REENVIO DE DADOS");
        itens.add("CONFIGURAÇÃO");
        itens.add("RELAÇÃO DE OS");
        itens.add("SAIR");

        AdapterList adapterList = new AdapterList(this, itens);
        ListView listView = findViewById(R.id.listaMenuInicial);
        listView.setAdapter(adapterList);

        listView.setOnItemClickListener((l, v, position, id) -> {

            TextView textView = v.findViewById(R.id.textViewItemList);
            String text = textView.getText().toString();

            Intent it;
            switch (text) {
                case "CHECKLIST": {
                    if (pciContext.getCheckListCTR().hasElementFunc()) {
                        it = new Intent(MenuInicialActivity.this, FuncionarioActivity.class);
                        startActivity(it);
                        finish();
                    }
                    break;
                }
                case "REENVIO DE DADOS": {
                    it = new Intent(MenuInicialActivity.this, EnvioDadosActivity.class);
                    startActivity(it);
                    finish();
                    break;
                }
                case "CONFIGURAÇÃO": {
                    it = new Intent(MenuInicialActivity.this, ConfigActivity.class);
                    startActivity(it);
                    finish();
                    break;
                }
                case "RELAÇÃO DE OS": {
                    it = new Intent(MenuInicialActivity.this, FuncVerOSActivity.class);
                    startActivity(it);
                    finish();
                    break;
                }
                case "SAIR": {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                }
            }

        });

    }

    public void onBackPressed()  {
    }

    public void verifEnvio(){
        if (pciContext.getCheckListCTR().verDadosEnvio()) {
            textViewProcesso.setTextColor(Color.RED);
            textViewProcesso.setText("Existem Dados para serem Enviados");
        } else {
            textViewProcesso.setTextColor(Color.GREEN);
            textViewProcesso.setText("Todos os Dados já foram Enviados");
        }
    }

}