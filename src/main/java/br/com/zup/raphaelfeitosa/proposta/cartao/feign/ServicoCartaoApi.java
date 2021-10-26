package br.com.zup.raphaelfeitosa.proposta.cartao.feign;

import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoCartaoResponse;
import br.com.zup.raphaelfeitosa.proposta.cartao.SolicitaAnaliseCartao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "servico-cartao", url = "${feign.servico.cartao.api-solicitacao}")
public interface ServicoCartaoApi {

    @PostMapping(consumes = "application/json")
    RetornoCartaoResponse solicitaCartao(@RequestBody SolicitaAnaliseCartao solicitaAnaliseCartao);
}
