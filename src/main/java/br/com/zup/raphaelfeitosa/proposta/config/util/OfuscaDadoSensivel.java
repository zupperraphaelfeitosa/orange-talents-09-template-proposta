package br.com.zup.raphaelfeitosa.proposta.config.util;

public class OfuscaDadoSensivel {

    public static String ofuscaEmail(String email) {
        String[] emailDividido = email.split("@");
        return emailDividido[0].substring(0, 3) + "***@" + emailDividido[1];
    }

    public static String ofuscaNome(String nome) {
        String[] nomeDividido = nome.split(" ");
        StringBuilder nomeFinal = new StringBuilder();
        for (String s : nomeDividido) {
            nomeFinal.append(s.charAt(0)).append(". ");
        }
        return nomeFinal.toString();
    }

    public static String ofuscaDocumento(String documento) {
        return documento.substring(0, 3) + "***" + documento.substring(documento.length() - 2);
    }

    public static String ofuscaCartao(String cartao) {
        if (cartao == null) {
            return null;
        }
        return cartao.substring(0, 4) + "***" + cartao.substring(cartao.length() - 2);
    }
}
