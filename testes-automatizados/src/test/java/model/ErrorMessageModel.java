package model;

import com.google.gson.annotations.Expose;

public class ErrorMessageModel {
    @Expose
    private String campo;

    @Expose
    private String mensagem;

    @Expose
    private String message;

    public ErrorMessageModel() {}

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
