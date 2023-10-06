package br.com.usinasantafe.pci.model.bean;

/**
 * Created by anderson on 24/07/2017.
 */

public class AtualAplicBean {

    private Long nroAparelho;
    private String versao;
    private String dthr;
    private String token;
    private Long idOficSecao;
    private Long idOS;

    public AtualAplicBean() {
    }

    public Long getNroAparelho() {
        return nroAparelho;
    }

    public void setNroAparelho(Long nroAparelho) {
        this.nroAparelho = nroAparelho;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getDthr() {
        return dthr;
    }

    public void setDthr(String dthr) {
        this.dthr = dthr;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getIdOficSecao() {
        return idOficSecao;
    }

    public void setIdOficSecao(Long idOficSecao) {
        this.idOficSecao = idOficSecao;
    }

    public Long getIdOS() {
        return idOS;
    }

    public void setIdOS(Long idOS) {
        this.idOS = idOS;
    }
}
