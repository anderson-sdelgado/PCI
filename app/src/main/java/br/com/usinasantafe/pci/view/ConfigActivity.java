package br.com.usinasantafe.pci.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import br.com.usinasantafe.pci.BuildConfig;
import br.com.usinasantafe.pci.PCIContext;
import br.com.usinasantafe.pci.R;
import br.com.usinasantafe.pci.util.ConexaoWeb;

public class ConfigActivity extends ActivityGeneric {

    private ProgressDialog progressBar;
    private EditText editTextNroAparelhoConfig;
    private PCIContext pciContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        pciContext = (PCIContext) getApplication();

        Button buttonAtualBDConfig = findViewById(R.id.buttonAtualBDConfig);
        Button buttonCancConfig = findViewById(R.id.buttonCancConfig);
        Button buttonSalvarConfig = findViewById(R.id.buttonSalvarConfig);
        editTextNroAparelhoConfig = findViewById(R.id.editTextNroAparelhoConfig);

        if(pciContext.getConfigCTR().hasElements()) {
            editTextNroAparelhoConfig.setText(String.valueOf(pciContext.getConfigCTR().getConfig().getNroAparelhoConfig()));
        }

        buttonAtualBDConfig.setOnClickListener(v -> {

            if(pciContext.getConfigCTR().hasElements()) {

                ConexaoWeb conexaoWeb = new ConexaoWeb();

                if(conexaoWeb.verificaConexao(ConfigActivity.this)){

                    progressBar = new ProgressDialog(v.getContext());
                    progressBar.setCancelable(true);
                    progressBar.setMessage("ATUALIZANDO...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();

                    pciContext.getConfigCTR().atualTodasTabelas(ConfigActivity.this, progressBar);

                } else {

                    AlertDialog.Builder alerta = new AlertDialog.Builder(ConfigActivity.this);
                    alerta.setTitle("ATENÇÃO");
                    alerta.setMessage("FALHA NA CONEXÃO DE DADOS. O CELULAR ESTA SEM SINAL. POR FAVOR, TENTE NOVAMENTE QUANDO O CELULAR ESTIVE COM SINAL.");

                    alerta.setPositiveButton("OK", (dialog, which) -> {});
                    alerta.show();

                }

            } else {

                AlertDialog.Builder alerta = new AlertDialog.Builder(ConfigActivity.this);
                alerta.setTitle("ATENÇÃO");
                alerta.setMessage("POR FAVOR, INSIRA O EQUIPAMENTO ANTES DE ATUALIZAR OS DADDOS.");
                alerta.setPositiveButton("OK", (dialog, which) -> {});
                alerta.show();
            }

        });

        buttonSalvarConfig.setOnClickListener(v -> {

            if (!editTextNroAparelhoConfig.getText().toString().equals("")) {

                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("Pequisando o Equipamento...");
                progressBar.show();
                pciContext.getConfigCTR().salvarToken(BuildConfig.VERSION_NAME, Long.valueOf(editTextNroAparelhoConfig.getText().toString()), ConfigActivity.this, TelaInicialActivity.class, progressBar);

            } else {

                AlertDialog.Builder alerta = new AlertDialog.Builder(ConfigActivity.this);
                alerta.setTitle("ATENÇÃO");
                alerta.setMessage("POR FAVOR! DIGITE O NUMERO DA LINHA.");
                alerta.setPositiveButton("OK", (dialog, which) -> {});
                alerta.show();

            }

        });

        buttonCancConfig.setOnClickListener(v -> {
            Intent it = new Intent(ConfigActivity.this, TelaInicialActivity.class);
            startActivity(it);
            finish();
        });

    }

    public void onBackPressed()  {
    }

}