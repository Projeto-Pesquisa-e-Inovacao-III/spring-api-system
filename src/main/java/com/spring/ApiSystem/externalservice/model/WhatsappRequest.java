package com.spring.ApiSystem.externalservice.model;

public class WhatsappRequest {

    private String to; // Número do destinatário (meu)
    private String code; // Código


    public WhatsappRequest() {

    }

    public String getTo() {
            return to;
        }
    public void setTo(String to) {
            this.to = to;
    }
    public String getCode() {
            return code;
    }
    public void setCode(String code) {
            this.code = code;
    }
}
