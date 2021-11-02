package br.com.zup.raphaelfeitosa.proposta.cartao.feign;

import br.com.zup.raphaelfeitosa.proposta.aviso.AvisoViagemRequest;
import br.com.zup.raphaelfeitosa.proposta.aviso.RetornoAvisoViagemServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.bloqueio.RetornoBloqueioServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.bloqueio.SolicitaBloqueioServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoCartaoCriadoServicoCartaoApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "servico-cartao", url = "${feign.servico.cartao.api-solicitacao}")
public interface ServicoCartaoApi {

    @GetMapping(consumes = "application/json")
    RetornoCartaoCriadoServicoCartaoApi solicitaCartao(@RequestParam Long idProposta);

    @PostMapping(value = "/{id}/bloqueios", consumes = "application/json")
    RetornoBloqueioServicoCartaoApi notificacaoBloqueio(@PathVariable(name = "id") String id, @RequestBody SolicitaBloqueioServicoCartaoApi request);

    @PostMapping(value = "/{id}/avisos", consumes = "application/json")
    RetornoAvisoViagemServicoCartaoApi avisoViagem(@PathVariable(name = "id") String id, @RequestBody AvisoViagemRequest request);

}
