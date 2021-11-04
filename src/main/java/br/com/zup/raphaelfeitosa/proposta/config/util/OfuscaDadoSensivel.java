package br.com.zup.raphaelfeitosa.proposta.config.util;

public interface OfuscaDadoSensivel {

    default String ofuscaEmail(String email) {
        String[] emailDividido = email.split("@");
        return emailDividido[0].substring(0, 3) + "***@" + emailDividido[1];
    }

    default String ofuscaNome(String nome) {
        String[] nomeDividido = nome.split(" ");
        StringBuilder nomeFinal = new StringBuilder();
        for (int i = 0; i < nomeDividido.length; i++) {
            nomeFinal.append(nomeDividido[i].substring(0, 1) + ". ");
        }
        return nomeFinal.toString();
    }

    default String ofuscaDocumento(String documento) {
        return documento.substring(0, 3) + "***" + documento.substring(documento.length() - 2);
    }

    default String ofuscaCartao(String cartao) {
        if (cartao == null) {
            return null;
        }
        return cartao.substring(0, 4) + "***" + cartao.substring(cartao.length() - 2);
    }
}
