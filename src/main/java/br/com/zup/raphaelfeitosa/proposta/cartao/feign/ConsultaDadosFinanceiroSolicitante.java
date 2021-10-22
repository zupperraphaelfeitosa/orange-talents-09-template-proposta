package br.com.zup.raphaelfeitosa.proposta.cartao.feign;

import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoAnaliseCartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.SolicitaAnaliseCartao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "servico-analise-financeira", url = "${servico.analise.dados.solicitante}")
public interface ConsultaDadosFinanceiroSolicitante {

    @PostMapping(consumes = "application/json")
    RetornoAnaliseCartao solicitaVerificacao(@RequestBody SolicitaAnaliseCartao solicitaAnaliseCartao);
}
