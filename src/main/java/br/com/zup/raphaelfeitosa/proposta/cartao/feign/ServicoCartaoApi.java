package br.com.zup.raphaelfeitosa.proposta.cartao.feign;

import br.com.zup.raphaelfeitosa.proposta.bloqueio.RetornoBloqueio;
import br.com.zup.raphaelfeitosa.proposta.bloqueio.SolicitaBloqueio;
import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoCartaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "servico-cartao", url = "${feign.servico.cartao.api-solicitacao}")
public interface ServicoCartaoApi {

    @GetMapping(consumes = "application/json")
    RetornoCartaoResponse solicitaCartao(@RequestParam Long idProposta);

    @PostMapping(value = "/{id}/bloqueios", consumes = "application/json")
    RetornoBloqueio notificacaoBloqueio(@PathVariable(name = "id") String id, @RequestBody SolicitaBloqueio request);

}
