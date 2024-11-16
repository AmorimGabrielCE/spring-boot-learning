package com.example.springboot.enums;

public enum Messages {
    ERRO_PRODUTO_NAO_ENCONTRADO("Produto não encontrado"),
    MENSAGEM_PRODUTO_DELETADO("Produto deletado com sucesso!");

    private final String message;

    Messages(String s) {
        this.message=s;
    }

    public String getMessage() {
        return message;
    }
}
