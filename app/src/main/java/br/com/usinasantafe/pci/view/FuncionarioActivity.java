package br.com.usinasantafe.pci.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.usinasantafe.pci.PCIContext;
import br.com.usinasantafe.pci.R;
import br.com.usinasantafe.pci.model.bean.estatica.FuncBean;
import br.com.usinasantafe.pci.model.bean.variavel.CabecBean;
import br.com.usinasantafe.pci.util.ConexaoWeb;

public class FuncionarioActivity extends ActivityGeneric {

    private PCIContext pciContext;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionario);

        pciContext = (PCIContext) getApplication();

        Button buttonOkFuncionario = findViewById(R.id.buttonOkPadrao);
        Button buttonCancFuncionario = findViewById(R.id.buttonCancPadrao);
        Button buttonAtualPadrao = findViewById(R.id.buttonAtualPadrao);

        buttonAtualPadrao.setOnClickListener(v -> {

            AlertDialog.Builder alerta = new AlertDialog.Builder(FuncionarioActivity.this);
            alerta.setTitle("ATENÇÃO");
            alerta.setMessage("DESEJA REALMENTE ATUALIZAR BASE DE DADOS?");
            alerta.setNegativeButton("SIM", (dialog, which) -> {

                ConexaoWeb conexaoWeb = new ConexaoWeb();

                if (conexaoWeb.verificaConexao(FuncionarioActivity.this)) {

                    progressBar = new ProgressDialog(FuncionarioActivity.this);
                    progressBar.setCancelable(true);
                    progressBar.setMessage("Atualizando Colaboradores...");
                    progressBar.show();

                    pciContext.getCheckListCTR().atualDadosFunc(FuncionarioActivity.this, FuncionarioActivity.class, progressBar);

                } else {

                    AlertDialog.Builder alerta1 = new AlertDialog.Builder( FuncionarioActivity.this);
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

        buttonOkFuncionario.setOnClickListener(v -> {

            if(!editTextPadrao.getText().toString().equals("")) {

                Long matricFunc = Long.parseLong(editTextPadrao.getText().toString());

                if(pciContext.getCheckListCTR().verFunc(matricFunc)){

                    pciContext.getCheckListCTR().setIniciarCabec(matricFunc);
                    Intent it = new Intent(FuncionarioActivity.this, ListaOSActivity.class);
                    startActivity(it);
                    finish();

                }
                else{

                    AlertDialog.Builder alerta = new AlertDialog.Builder(FuncionarioActivity.this);
                    alerta.setTitle("ATENÇÃO");
                    alerta.setMessage("FUNCIONÁRIO INEXISTENTE!");

                    alerta.setPositiveButton("OK", (dialog, which) -> editTextPadrao.setText(""));
                    alerta.show();

                }

            }

        });


        buttonCancFuncionario.setOnClickListener(v -> {
            if (editTextPadrao.getText().toString().length() > 0) {
                editTextPadrao.setText(editTextPadrao.getText().toString().substring(0, editTextPadrao.getText().toString().length() - 1));
            }
        });

    }

    public void onBackPressed()  {
        Intent it = new Intent(FuncionarioActivity.this, MenuInicialActivity.class);
        startActivity(it);
        finish();
    }

}