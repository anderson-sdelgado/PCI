package br.com.usinasantafe.pci.util.conHttp;

import br.com.usinasantafe.pci.PCIContext;

public class UrlsConexaoHttp {

	public static String versao = "versao_" + PCIContext.versaoWS.replace(".", "_");

//	public static String url = "https://www.usinasantafe.com.br/pcidev/view/";
//	public static String url = "https://www.usinasantafe.com.br/pciqa/view/";
	public static String url = "https://www.usinasantafe.com.br/pciprod/" + versao + "/view/";

	public static String localPSTEstatica = "br.com.usinasantafe.pci.model.bean.estatica.";
	public static String localUrl = "br.com.usinasantafe.pci.util.conHttp.UrlsConexaoHttp";

	public static String ComponenteBean = url + "componente.php";
	public static String FuncBean = url + "funcionario.php";
	public static String ServicoBean = url + "servico.php";
	public static String PlantaBean = url + "planta.php";

	public UrlsConexaoHttp() {
	}

	public String getsInsertChecklist() {
		return url + "inserirchecklist.php";
	}

	public String urlVerifica(String classe) {
		String retorno = "";
		if (classe.equals("OS")) {
			retorno = url + "os.php";
		} else if (classe.equals("Item")) {
			retorno = url + "itemos.php";
		} else if (classe.equals("Atualiza")) {
			retorno = url + "atualaplic.php";
		} else if (classe.equals("Token")) {
			retorno = url + "aparelho.php";
		}
		return retorno;
	}

}