package br.com.zup.raphaelfeitosa.proposta.carteira;

public class SolicitaInclusaoCartaieraServicoCartaoApi {

    private String email;
    private String carteira;

    public SolicitaInclusaoCartaieraServicoCartaoApi(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }
}
