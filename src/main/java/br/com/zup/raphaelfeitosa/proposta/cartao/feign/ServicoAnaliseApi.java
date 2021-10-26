package br.com.zup.raphaelfeitosa.proposta.cartao.feign;

import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoAnaliseCartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.SolicitaAnaliseCartao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "servico-analise", url = "${feign.servico.analise.api-solicitacao}")
public interface ServicoAnaliseApi {

    @PostMapping(consumes = "application/json")
    RetornoAnaliseCartao solicitaVerificacao(@RequestBody SolicitaAnaliseCartao solicitaAnaliseCartao);
}
