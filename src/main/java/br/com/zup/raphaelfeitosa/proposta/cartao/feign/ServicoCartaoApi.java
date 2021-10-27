package br.com.zup.raphaelfeitosa.proposta.cartao.feign;

import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoCartaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "servico-cartao", url = "${feign.servico.cartao.api-solicitacao}")
public interface ServicoCartaoApi {

    @GetMapping(consumes = "application/json")
    RetornoCartaoResponse solicitaCartao(@RequestParam Long idProposta);
}
