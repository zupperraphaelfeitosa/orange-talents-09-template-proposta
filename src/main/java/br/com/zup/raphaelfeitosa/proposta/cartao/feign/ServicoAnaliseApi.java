package br.com.zup.raphaelfeitosa.proposta.cartao.feign;

import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoAnaliseCartaoServicoAnaliseApi;
import br.com.zup.raphaelfeitosa.proposta.cartao.SolicitaAnaliseCartaoServicoAnaliseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "servico-analise", url = "${feign.servico.analise.api-solicitacao}")
public interface ServicoAnaliseApi {

    @PostMapping(consumes = "application/json")
    RetornoAnaliseCartaoServicoAnaliseApi solicitaVerificacao(@RequestBody SolicitaAnaliseCartaoServicoAnaliseApi solicitaAnaliseCartaoServicoAnaliseApi);
}
